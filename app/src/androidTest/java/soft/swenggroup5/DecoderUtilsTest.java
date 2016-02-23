package soft.swenggroup5;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;

import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class DecoderUtilsTest {

    //As ContactData opens the device's default Contact App to insert contacts, different
    //+ "resource_ids" must be used to identify the "save contact button" on the opened
    private static final String[] CONTACT_SAVE_BUTTON_ID = {
            "com.sonyericsson.android.socialphonebook:id/save_menu_item",            //Sony Experia m2
            "com.android.contacts:id/menu_save"                                      //Nexus 5
    };

    //The phone number of the contact encoded above
    private static final String VALID_CONTACT_DATA_ENCODED_PHONE_NUMBER = "123 456 789";

    private static final int WAIT_TIME = 8000; //8 seconds

    /**
     * test_addContact_normalInput
     *
     * Tests:   DecodeUtils.decodeFile( String data )
     *          ContactData.saveData()
     *
     * Tests if contact data can be correctly decoded and inserted on the running
     * Android device. Only tests with a single contact at the moment.
     * To test this, it creates A ContactData and then encodes it, we then decode
     * the encoded data and try to insert the decoded data onto the device. Finally
     * we check if the inserted data is the same as the data we originally encoded.
     *
     * TODO: Remove the inserted contact as for now it stays on device even after the test
     * TODO: Test decodeFile() and saveData() in seperate test functions
     *
     */
    @Test
    public void test_addContact_normalInput() {
        // Initialize UiDevice instance, the object which will look at the current screen
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        // Start from the home screen
        mDevice.pressHome();
        //context needed to start Activities
        Context context = InstrumentationRegistry.getContext();

        //Create a ContactData which will be encoded, for us to then decode.
        ContactData contactToDecode = getExpectedValidContactData();
        List<Byte> dataAsList = null;
        try {
            File f = contactToDecode.toFile(context);
            dataAsList = EncoderUtils.getFileBytes(f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String data = EncoderUtils.byteListToString(dataAsList);
        ReceivedData decodedContact = DecoderUtils.decodeFile(data, ContactData.FILE_EXTENSION);
        Intent intent = decodedContact.TEST_saveData(context);
        //NEW_TASK flag needed to allow this Intent to act as a standalone app
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        //Hackish method to have test wait until the Intent has fully started, hopefully this can
        //+be changed later to wait until Intent is ready for use
        try {
            Thread.sleep(WAIT_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //UiObject to represent the "Save new Contact" button in the Contact App
        UiObject saveButton;
        int resourceIdIndex = 0;
        //The save button will have a different id for every Contact App.
        //+ keep trying ids until one works.
        do{
            saveButton = mDevice.findObject(new UiSelector()
                    .resourceId(CONTACT_SAVE_BUTTON_ID[resourceIdIndex]));
            resourceIdIndex++;
        }while(resourceIdIndex < CONTACT_SAVE_BUTTON_ID.length && !saveButton.exists());

        //Might wrap entire test in one big try-catch instead
        try {
            if(saveButton.exists()) {
                //Presses the button. Will also probably cause "back button" to be called and return
                //+ the device to the main "Contact App" activity
                saveButton.click();
            }
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(WAIT_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ContactData expected = getExpectedValidContactData();
        ContactData result = getResultValidContactData(context);
        assertEquals(true, expected.equals(result));
    }

    //Return a ContactData that should be .equals() to the just decoded and stored Contact Data
    private ContactData getExpectedValidContactData(){
        ContactData con = new ContactData();
        con.addName("Test Contact");
        con.addEmail("test@test.com", ContactsContract.CommonDataKinds.Email.TYPE_HOME);
        con.addPhoneNumber("123456789", ContactsContract.CommonDataKinds.Phone.TYPE_HOME);
        con.addPostalAddress("Test Street, USA");
        return con;
    }

    //Get the contact data that has just been stored on the device
    private ContactData getResultValidContactData(Context context){
        //Gets a URI pointing to the beginning of the Contact data that contains the phone
        //+ number VALID_CONTACT_DATA_ENCODED_PHONE_NUMBER, this should be the data just
        //+ inserted into the phone.
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(VALID_CONTACT_DATA_ENCODED_PHONE_NUMBER));
        ContactData con = new ContactData(uri, context);
        return con;
    }

}
