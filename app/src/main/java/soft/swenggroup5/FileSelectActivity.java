package soft.swenggroup5;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;

/**
 * FileSelectActivity
 *
 * An activity that opens a user choosen File Browser where they can pick a file to be encoded
 * as a QR code. If no File browser is installed on the device then a toast is displayed instead.
 *
 */
public class FileSelectActivity extends AppCompatActivity {

    // our ID for the Intent called in doLaunchContectPicker(), unneeded for now
    private static final int THIRD_PARTY_FILE_PICKER_RESULT = 1002;
    private static final int ACTIVITY_FILE_PICKER_RESULT = 1003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_select);
        startFileChooserIntent();
    }

    /**
     * startFileChooserIntent
     *
     * Creates an intent that tells a file browser to let the user select a file and return it.
     * This intent is wrapped in a Chooser Intent so the user can choose their preferred file
     * browser. The choosen file will be returned in the OnActivityResult method with code
     * FILE_PICKER_RESULT
     */
    private void startFileChooserIntent(){
        //create intent that tells the Activity that fulfils to list all openable files
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //return choose
        intent.setType("*/*"); //match all files and directories
        intent.addCategory(Intent.CATEGORY_OPENABLE); //
        try {
            startActivityForResult(
                    //createChooser is an Intent that wraps our intent. In the chooser Intent, the
                    //+ user selects their preferred file browser
                    Intent.createChooser(intent, "Select a File to Upload"),
                    THIRD_PARTY_FILE_PICKER_RESULT);
            //It's possible that the device has no valid file browsers, ActivityNotFound is thrown
            //+ if this is the case.
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            getFileWithBuiltinBrowser();
        }
    }

    /**
     * onActivityResult
     *
     * The callback function to any startActivityForResult() activities when they complete, although
     * here the only possible intent it could handle is the one in startFileChooserIntent.
     * Gets the URI pointing to the file choosen by the user. Stores the name of the file and
     * it's path into an Intent and passes the info onto the EncodeActivity. Then calls finish() as
     * this activity has no more functionality and we don't want the user returning here.
     *
     * @param requestCode : the code  passed with startActivityForResult so the correct code can be run
     * @param resultCode : a code representing how the returning activity went
     * @param data : an intent containing the data the Activity has collected
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == THIRD_PARTY_FILE_PICKER_RESULT) {
            Uri fileUri = data.getData(); //Get the URI that represents the chosen file
            File file = new File(fileUri.getPath()); //get the choosen file
            sendFileToBeEncoded(file);
            finish(); //this activity is now longer needed so just close it
        }else if(resultCode == RESULT_OK && requestCode == ACTIVITY_FILE_PICKER_RESULT){
            String filePath = data.getStringExtra(FileBrowserActivity.RESULT_KEY);
            File file = new File(filePath);
            sendFileToBeEncoded(file);
            finish();
        } else { //if !RESULT_OK, i.e. user cancelled file app
            finish(); //end Activity, will return to previous activity (i.e. The Home Activity in Version 1)
        }
    }

    //sends the selected file to the encoding activity to be displayed as qr codes(s)
    private void sendFileToBeEncoded(File file){
        //TODO: Check if the file isn't too large for QR codes to manage
        Intent intent = new Intent(FileSelectActivity.this, ContactEncodeActivity.class);
        //save the file name and file path to the passing Intent so EncodeActivity can use them
        intent.putExtra(ContactEncodeActivity.FILE_NAME_KEY, file.getName());
        intent.putExtra(ContactEncodeActivity.FILE_PATH_KEY, file.getAbsolutePath());
        startActivity(intent);
    }

    //creates an Activity of the builtin FileBrowserActivity for the user to select a file
    private void getFileWithBuiltinBrowser(){
        Intent startingIntent = new Intent(FileSelectActivity.this, FileBrowserActivity.class);
        startingIntent.putExtra(FileBrowserActivity.DISPLAYING_KEY, FileBrowserActivity.FILE_SELECT_ONLY);
        startActivityForResult(startingIntent, ACTIVITY_FILE_PICKER_RESULT);
    }
}
