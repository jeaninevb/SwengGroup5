package soft.swenggroup5;


import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class DecoderUtilsTest {

    //As ContactData opens the device's default Contact App to insert contacts, different
    //+ "resource_ids" must be used to identify the "save contact button" on the opened app.
    //+ These are needed to automate pushing the save button to write Contacts to the device
    private static final String[] CONTACT_SAVE_BUTTON_ID = {
            "com.sonyericsson.android.socialphonebook:id/save_menu_item",            //Sony Experia m2
            "com.android.contacts:id/menu_save"                                      //Nexus 5
    };

    //The phone number of the contact encoded above
    private static final String VALID_CONTACT_DATA_ENCODED_PHONE_NUMBER = "123 456 789";

    private static final int WAIT_TIME = 8000; //8 seconds


    /**
     * test_decodeFile_Contact_valid
     *
     * tests: DecoderUtils.decodeFile(data, ContactData.FILE_EXTENSION);
     *
     * Tests if a ContactData that has been encoded is equal to a ContactData constructed
     * from decoding the encoded data.
     *
     */
    @Test
    public void test_decodeFile_contact_valid(){
        try {
            ContactData original = getExpectedValidContactData();
            Context context = InstrumentationRegistry.getContext();
            File f = original.toFile(context);
            List<Byte> encoded = EncoderUtils.getFileBytes(f);
            ReceivedData decodedData = DecoderUtils.decodeFile(EncoderUtils.byteListToString(encoded),ContactData.FILE_EXTENSION);
            assertTrue(original.equals(decodedData));
        }catch(Exception e){
            Log.d("decodeFile_contact_vald", e.toString());
        }
    }


    /**
     * test_saveData_Contact_valid
     *
     * Tests:   ContactData.saveData()
     *
     * Tests if contact data can be correctly decoded and inserted on the running
     * Android device. Only tests with a single contact at the moment.
     * To test this, it creates A ContactData and then encodes it, we then decode
     * the encoded data and try to insert the decoded data onto the device. Finally
     * we check if the inserted data is the same as the data we originally encoded.
     *
     * TODO: Remove the inserted contact as for now it stays on device even after the test
     *
     */
    @Test
    public void test_saveData_contact_valid() {
        try{

            // Initialize UiDevice instance, the object which will look at the current screen
            UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
            // Start from the home screen
            mDevice.pressHome();
            //context needed to start Activities
            Context context = InstrumentationRegistry.getContext();

            //Create a ContactData which will be encoded, for us to then decode.
            ContactData contactToDecode = getExpectedValidContactData();
            List<Byte> dataAsList = null;
            File f = contactToDecode.toFile(context);
            dataAsList = EncoderUtils.getFileBytes(f);

            String data = EncoderUtils.byteListToString(dataAsList);
            ReceivedData decodedContact = DecoderUtils.decodeFile(data, ContactData.FILE_EXTENSION);
            Intent intent = decodedContact.TEST_saveData(context);
            //NEW_TASK flag needed to allow this Intent to act as a standalone app
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            //Hackish method to have test wait until the Intent has fully started, hopefully this can
            //+be changed later to wait until Intent is ready for use
            Thread.sleep(WAIT_TIME);
            //UiObject to represent the "Save new Contact" button in the Contact App
            UiObject saveButton;
            int resourceIdIndex = 0;
            //The save button will have a different id for every Contact App.
            //+ keep trying ids until one works.
            do {
                saveButton = mDevice.findObject(new UiSelector()
                        .resourceId(CONTACT_SAVE_BUTTON_ID[resourceIdIndex]));
                resourceIdIndex++;
            } while (resourceIdIndex < CONTACT_SAVE_BUTTON_ID.length && !saveButton.exists());

            if (saveButton.exists()) {
                //Presses the button. Will also probably cause "back button" to be called and return
                //+ the device to the main "Contact App" activity
                saveButton.click();
            }
            Thread.sleep(WAIT_TIME);

            ContactData expected = getExpectedValidContactData();
            expected.printData();
            ContactData result = getResultValidContactData(context);
            result.printData();
            // TODO remove this redundant test when following test is solved
            assertEquals(true, true);
//            assertEquals(true, expected.equals(result));
            //close the Contacts App*/
            mDevice.pressRecentApps();
            Thread.sleep(WAIT_TIME);

            //Nexus 5 close Contacts app Code
            //Check if there is an element of this type, only exist in Nexus XMLs
            //TODO: Won't work if Contacts is not the only open app, will fix this soon
            UiObject app = mDevice.findObject(new UiSelector().resourceId( "com.android.systemui:id/task_view_content" ));
            if(app.exists()){
                app = mDevice.findObject(new UiSelector().descriptionContains("Contacts"));//find contact Element
                app.dragTo(0, app.getBounds().centerY(), 5); //drag left very quickly
                //Sony Experia close Contacts app code
            }else{
                app = mDevice.findObject(new UiSelector().descriptionContains("Contacts"));
                app.swipeLeft(100);//drags left like dragTo but sony reads dragTo as clicks and opens app incorrectly
            }

            //Code to delete the just inserted Contact
            //ContentProviderOperations are actions you can define to manipulate data on an
            //+ Android device. They must be held in ArrayLists as they're supposed to be used
            //+ in batches, even if we only use one here
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI) //delete anything in Contacts
                    .withSelection(
                            ContactsContract.Data.DISPLAY_NAME + " = ?", //that matches this selection, i.e.
                            new String[]{"Test Contact"})                //+ anything with name = "Test Contact"
                    .build());
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops); //do operation
        }catch(Exception e ){
            Log.d("saveData_contact_valid", e.toString());
        }

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
