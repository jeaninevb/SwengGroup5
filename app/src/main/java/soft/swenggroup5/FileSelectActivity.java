package soft.swenggroup5;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;

public class FileSelectActivity extends AppCompatActivity {

    // our ID for the Intent called in doLaunchContectPicker(), unneeded for now
    private static final int FILE_PICKER_RESULT = 1002;

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
                    FILE_PICKER_RESULT);
            //It's possible that the device has no valid file browsers, ActivityNotFound is thrown
            //+ if this is the case.
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
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
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) { //if File Intent ended with the user selecting a File
            Uri fileUri = data.getData(); //Get the URI that represents the chosen file
            File file = new File(fileUri.getPath()); //get the choosen file
            Intent intent = new Intent(FileSelectActivity.this, ContactEncodeActivity.class);
            //save the file name and file path to the passing Intent so EncodeActivity can use them
            intent.putExtra(ContactEncodeActivity.FILE_NAME_KEY, file.getName());
            intent.putExtra(ContactEncodeActivity.FILE_PATH_KEY, file.getAbsolutePath());
            startActivity(intent);
            finish(); //this activity is now longer needed so just close it
        } else { //if !RESULT_OK, i.e. user cancelled file app
            finish(); //end Activity, will return to previous activity (i.e. The Home Activity in Version 1)
        }
    }
}
