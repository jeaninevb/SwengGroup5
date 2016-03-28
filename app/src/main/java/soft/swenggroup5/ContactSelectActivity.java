package soft.swenggroup5;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
/**
 * Contact Select Activity:
 * In Short: Activity that allows a user to select a contact, if cancelled returns to the previous
 * screen, if contact selected does nothing for the moment
 *
 */
public class ContactSelectActivity extends AppCompatActivity {
    // our ID for the Intent called in doLaunchContectPicker(), unneeded for now
    private static final int CONTACT_PICKER_RESULT = 1001;
    /**
     * onCreate
     *
     * Just calls doLaunchContactPicker()
     *
     * @param savedInstanceState: unused
     */
    protected  void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_select);
        Log.d("onCreate_CSA", "On create");
        doLaunchContactPicker();
    }
    /**
     * doLaunchContactPicker
     *
     * Creates an intent that will open the default "Contacts App" where the user
     * can select a contact in which case the intent will end. The return values
     * of the intent are given as parameters to onActivityResult
     */
    public void doLaunchContactPicker() {
        Log.d("doLaunchContactPicker", "Started");
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }
    /**
     * onActivityResult
     *
     * Called when an Intent started by startActivityForResult finishes, its parameters
     * contain the data collected by the finished Intent
     *
     * The returned data will contain a URI pointing to the chosen contact, using that
     * we can access the data on the chosen contact
     *
     * If no contact is chosen the entire Activity ends (with finish()) and the app
     * will return to the previous activity (the Home Activity in Version 1)
     *
     * TODO: Save the collected Contact in some location
     *
     * @param requestCode : the second parameter passed to the startActivityForResult call
     *                    that created the intent tht returned here
     * @param resultCode : Describes how the intent ended, e.g. cancelled, select made ...etc
     * @param data : The data the intent collects as it ran
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) { //if Contact Intent ended with the user selecting a Contact
            Uri contactUri = data.getData(); //Get the URI that represents the chosen contact
            ContactData contactData = new ContactData(contactUri, ContactSelectActivity.this);
            Intent intent = new Intent(ContactSelectActivity.this, ContactEncodeActivity.class);
            //save the file name to the passing Intent so EncodeActivity can use it
            intent.putExtra(ContactEncodeActivity.FILE_NAME_KEY, contactData.getName());

            try {
                File file = contactData.toFile(this);
                //save the file path to the passing Intent so EncodeActivity can reach the file in it's intent
                intent.putExtra(ContactEncodeActivity.FILE_PATH_KEY, file.getAbsolutePath());
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }

            finish(); //this activity is now longer needed so just close it
        } else { //if !RESULT_OK, i.e. user cancelled contact app
            finish(); //end Activity, will return to previous activity (i.e. The Home Activity in Version 1)
        }
    }
}