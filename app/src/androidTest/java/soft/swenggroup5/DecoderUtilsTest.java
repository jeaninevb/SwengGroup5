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

    //Contact Data encoded by EncoderUtils, will be replaced by actual Encoder calls soon
    private static final Byte[] VALID_CONTACT_DATA_ENCODED_WITHOUT_HEADER =
            {35, 84, 101, 115, 116, 32, 67, 111, 110, 116, 97, 99, 116, 35, 78, 35, 63, 35, 116, 101,
                    115, 116, 64, 116, 101, 115, 116, 46, 99, 111, 109, 35, 69, 35, 72, 35, 49, 50, 51,
                    52, 53, 54, 55, 56, 57, 35, 80, 35, 72, 35, 84, 101, 115, 116, 32, 83, 116, 114,
                    101, 101, 116, 44, 32, 85, 83, 65, 35, 65, 35, 63};
    //The phone number of the contact encoded above
    private static final String VALID_CONTACT_DATA_ENCODED_PHONE_NUMBER = "123 456 789";

    private static final int WAIT_TIME = 8000; //8 seconds

    @Test
    public void test_addContact_normalInput() {
        // Initialize UiDevice instance, the object which will look at the current screen
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        // Start from the home screen
        mDevice.pressHome();
        //context needed to start Activities
        Context context = InstrumentationRegistry.getContext();

        List<Byte> dataAsList = new ArrayList<Byte>(Arrays.asList(VALID_CONTACT_DATA_ENCODED_WITHOUT_HEADER));
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
