package soft.swenggroup5;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import java.io.File;
import java.io.IOException;
/**
 * Contact Select Activity:
 * In Short: Activity that allows a user to select a contact, if cancelled returns to the previous
 * screen, if contact selected does nothing for the moment
 *
 */
public class ContactSelectActivity extends AppCompatActivity {
    public static String CONTACT_NAME;
    public static File DATA_FILE;
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
            ContactData contactData = new ContactData(); //used to store collected Contact data
            // handle contact results
            String name;    // the chosen contacts name
            String mime;    // the id of the piece of data in the contact we are looking at
            int dataIdx;    // Index of DATA1 column
            int data2Idx;   // Index of DATA2 column
            int mimeIdx;    // Index of MIMETYPE column
            int nameIdx;    // Index of DISPLAY_NAME column
            Uri contactUri = data.getData(); //Get the URI that represents the chosen contact
                /*
                Contacts are stored in a "database" that we query and then traverse using Cursors
                First we get the name of the chosen contact
                 */
            Cursor cursor = getContentResolver().query(contactUri, //start query here
                    new String[]{ContactsContract.Contacts.DISPLAY_NAME}, //return table with one column, the name
                    null, null, null); //no filter, sorting
            //move Cursor to first element in returned table, will be name of chosen contact
            if (cursor.moveToFirst()) {
                //Get Column index that holds names, will be zero but this is less fragile code
                nameIdx = cursor.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME);
                name = cursor.getString(nameIdx); //get the string at row 0, DISPLAY_NAME column
                contactData.addName(name);
                CONTACT_NAME = name;
                    /*
                    Now that we have the name we query the contact "database" again, filtering results
                    to only return results on the same row as our chosen contacts name
                     */
                // Set up the projection, the projection describes what data to return and how to order it
                // in the the created "table"
                String[] projection = {
                        ContactsContract.Data.DISPLAY_NAME, //Name
                        ContactsContract.Contacts.Data.DATA1, //Generic data piece (will be phone or email)
                        ContactsContract.Contacts.Data.DATA2, //Meta info on DATA1 (e.g. is home, work)
                        ContactsContract.Contacts.Data.MIMETYPE}; //Describes what is stored in DATA1 (i.e. PHONE or EMAIL)
                // Query ContactsContract.Data
                cursor = getContentResolver().query(
                        ContactsContract.Data.CONTENT_URI, //start query here
                        projection,
                        ContactsContract.Data.DISPLAY_NAME + " = ?",
                        new String[]{name},
                        null);
                //move to start of created result table
                if (cursor.moveToFirst()) {
                    // Get the indexes of the MIME type and data1 and data2
                    mimeIdx = cursor.getColumnIndex(
                            ContactsContract.Contacts.Data.MIMETYPE);
                    dataIdx = cursor.getColumnIndex(
                            ContactsContract.Contacts.Data.DATA1);
                    data2Idx = cursor.getColumnIndex(
                            ContactsContract.Contacts.Data.DATA2);
                    // Match the data to the MIME type, store in variables
                    do {
                        mime = cursor.getString(mimeIdx);
                        if (ContactsContract.CommonDataKinds.Email
                                .CONTENT_ITEM_TYPE.equalsIgnoreCase(mime)) {
                            String email = cursor.getString(dataIdx);
                            int emailType = cursor.getInt(data2Idx);
                            contactData.addEmail(email, emailType);
                        }
                        if (ContactsContract.CommonDataKinds.Phone
                                .CONTENT_ITEM_TYPE.equalsIgnoreCase(mime)) {
                            String phone = cursor.getString(dataIdx);
                            int phoneType = cursor.getInt(data2Idx);
                            contactData.addPhoneNumber(phone, phoneType);
                        }
                        if(ContactsContract.CommonDataKinds.StructuredPostal.
                                CONTENT_ITEM_TYPE.equalsIgnoreCase(mime)){
                            String postal = cursor.getString(dataIdx);
                            contactData.addPostalAddress(postal);
                        }
                    } while (cursor.moveToNext());
                    try {
                        DATA_FILE = contactData.toFile(this);
                        startActivity(new Intent(ContactSelectActivity.this, ContactEncodeActivity.class));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else { //if !RESULT_OK, i.e. user cancelled contact app
            finish(); //end Activity, will return to previous activity (i.e. The Home Activity in Version 1)
        }
    }
}