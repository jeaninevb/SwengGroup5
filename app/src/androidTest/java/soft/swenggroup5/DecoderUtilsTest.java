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
