package soft.swenggroup5;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
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

import static junit.framework.TestCase.assertEquals;

/**
 *  DecoderUtilAutomatorTest
 *
 *  Contains tests for DecoderUtils that uses auto-input code. Tests are only compatible with the
 *  Nexus 4 at the moment.
 */
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class DecoderUtilAutomatorTest {

    //As ContactData opens the device's default Contact App to insert contacts, different
    //+ "resource_ids" must be used to identify the "save contact button" on the opened app.
    //+ These are needed to automate pushing the save button to write Contacts to the device
    private static final String[] CONTACT_SAVE_BUTTON_ID = {
            "com.sonyericsson.android.socialphonebook:id/save_menu_item",            //Sony Experia m2
            "com.android.contacts:id/menu_save"                                      //Nexus 5
    };

    private static final int WAIT_TIME = 5000; //5 seconds
    final static private int REQUEST_CODE_ASK_PERMISSIONS = 8888;
    
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
     *
     */
    @Test
    public void test_saveData_contact_valid() throws Exception{
        // Initialize UiDevice instance, the object which will look at the current screen
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        // Start from the home screen
        mDevice.pressHome();
        //context needed to start Activities
        Context context = InstrumentationRegistry.getContext();

        //Create a ContactData which will be encoded, for us to then decode.
        ContactData contactToDecode = DecoderUtilsTest.getExpectedValidContactData();
        List<Byte> dataAsList = null;
        File f = contactToDecode.toFile(context);
        dataAsList = EncoderUtils.getFileBytes(f);

        String data = EncoderUtils.byteListToString(dataAsList);
        ReceivedData decodedContact = DecoderUtils.decodeFileData(data, ContactData.FILE_EXTENSION);
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

        ContactData expected = DecoderUtilsTest.getExpectedValidContactData();
        expected.printData();
        ContactData result = DecoderUtilsTest.getResultValidContactData(context);
        result.printData();

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
        Log.d("XXX","EXP");
        expected.printData();
        Log.d("XXX","RES");
        result.printData();
        Thread.sleep(WAIT_TIME);
        // TODO remove this redundant test when following test is solved
        assertEquals(true, true);
        assertEquals(true, expected.equals(result));
    }
}
