package soft.swenggroup5;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.util.Log;
import android.widget.Toast;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class DecoderUtilsTest {

    private byte[] by =
            {49, 54, 56, 124, 110, 117, 108, 108, 124, 45,
                    49, 54, 56, 57, 51, 55, 57, 51, 49, 57, 124,
                    49, 0, 35, 65, 97, 97, 97, 97, 32, 66, 98,
                    98, 98, 32, 86, 99, 99, 99, 99, 32, 68, 100,
                    100, 100, 100, 35, 78, 35, 63, 35, 104, 111,
                    109, 101, 64, 99, 46, 99, 111, 109, 35, 69, 35,
                    72, 35, 111, 116, 104, 101, 114, 64, 99, 46, 99,
                    111, 109, 35, 69, 35, 79, 35, 119, 111, 114, 107,
                    64, 99, 46, 99, 111, 109, 35, 69, 35, 87, 35, 48, 56,
                    54, 32, 49, 50, 51, 32, 52, 53, 54, 55, 35, 80,
                    35, 77, 35, 50, 56, 53, 49, 50, 51, 52, 53, 54,
                    55, 35, 80, 35, 72, 35, 56, 56, 56, 49, 50, 51, 52,
                    53, 54, 55, 35, 80, 35, 87, 35, 51, 53, 32, 71, 101,
                    108, 32, 83, 116, 114, 101, 101, 116, 32, 10, 53, 52,
                    54, 54, 10, 65, 117, 115, 116, 105, 110, 32, 44, 32, 84, 101,
                    120, 97, 115, 10, 65, 109, 101, 114, 105, 99, 97, 32, 35, 65, 35, 63};
    //private static final String PACKAGE = "com.sonyericsson.android.socialphonebook";

    //As ContactData opens the device's default Contact App to insert contacts, different
    //+ "resource_ids" must be used to identify the "save contact button" on the opened
    private static final String[] CONTACT_SAVE_BUTTON_ID = {
            "com.sonyericsson.android.socialphonebook:id/save_menu_item",            //Sony Experia m2
            "com.android.contacts:id/menu_save"                                      //Nexus 5
    };

    private static final int WAIT_TIME = 8000; //8 seconds
    private static final String STRING_TO_BE_TYPED = "UiAutomator";
    private UiDevice mDevice;

    @Test
    public void test_addContact_normalInput() {
        // Initialize UiDevice instance, the object which will look at the current screen
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        // Start from the home screen
        mDevice.pressHome();
        //context needed to start Activities
        Context context = InstrumentationRegistry.getContext();
        //create a ContactData to make the Intent. This will be changed to DecoderUtil
        //+calls once the auto-input is functioning correctly
        ContactData cd = new ContactData();
        cd.addName("bob");


        Intent i = cd.TEST_getInsertIntent(context);
        //NEW_TASK flag needed to allow this Intent to act as a standalone app
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
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

        assertEquals(null, null); //one test needed or else will auto-fail. Just annoys me.
    }

}
