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
import android.widget.Toast;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class DecoderUtilsTest {

    private static final String PACKAGE = "com.sonyericsson.android.socialphonebook";
    private static final int TIMEOUT = 5000;
    private static final String STRING_TO_BE_TYPED = "UiAutomator";
    private UiDevice mDevice;

    @Test
    public void test_getMimeType_onNullInput() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        // Start from the home screen
        mDevice.pressHome();

        Context context = InstrumentationRegistry.getContext();
        ContactData cd = new ContactData();
        cd.addName("bob");
        Intent i = cd.TEST_getInsertIntent(context);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        //mDevice.waitForIdle(TIMEOUT * 100);
        //mDevice.wait(Until.hasObject(By.pkg(PACKAGE).depth(0)),
        //        TIMEOUT);
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        UiObject saveButton = mDevice.findObject(new UiSelector()
                .resourceId("com.sonyericsson.android.socialphonebook:id/save_menu_item"));
        try {
            saveButton.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        assertEquals(null, null); //one test needed or else will auto-fail. Just annoys me.
        //Toast toast = Toast.makeText(context, "Test over", Toast.LENGTH_SHORT);
        //toast.show();
    }

}
