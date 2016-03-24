package soft.swenggroup5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * FileSaveActivity
 *
 * Takes in a temp file from the starting intent. The user then chooses a directory and filename
 * to move the temp file to. Used as part of FileData to save a decoded File to the device.
 *
 */
public class FileSaveActivity extends AppCompatActivity {

    //Key that the path to the passed in temp file should be stored with
    public static final String TEMP_FILE_PATH_KEY = "com.swenggroup5.filesave.temppath.key";
    private String tempFilePath = null;
    private String savingdirectory = null; //the directory path chosen by the user
    private static final int DIRECTORY_RESULT_KEY = 999;
    private boolean DEBUG = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_save);
        //get the path to the temp file
        Intent startingIntent = getIntent();
        tempFilePath = startingIntent.getStringExtra(TEMP_FILE_PATH_KEY);
    }

    /** directorySelectOnClick
     *
     * Callback method for the "choose Directory (@+id/choosedirectorybutton) button. Starts a
     * FileBrowserActivity where the user can choose a directory to store the file at.
     *
     * @param view : the View that caused this callback. So here can be cast to a Button
     */
    public void directorySelectOnClick(View view){
        Intent startingIntent = new Intent(FileSaveActivity.this, FileBrowserActivity.class);
        //only display directories
        startingIntent.putExtra(FileBrowserActivity.DISPLAYING_KEY, FileBrowserActivity.DIRECTORY_ONLY);
        startActivityForResult(startingIntent, DIRECTORY_RESULT_KEY);
    }

    /** saveFileOnClick
     *
     * callback method for the "save file" (@+id/savefilebutton) button. Checks if the user has choosen
     * a valid directory and file name. If so moves the temp file to the new location and ends this activity.
     * If not does nothing.
     *
     * @param view : the View that caused this callback. So here can be cast to a Button
     */
    public void saveFileOnClick(View view){
        if(savingdirectory!=null){
            String fileName = getFileName();
            if(fileName != null){
                moveFile(savingdirectory+fileName, tempFilePath);
                finish();
            }
        }
    }

    /**moveFile
     *
     * Moves a file from srcpath to destpath. If an excpetion occurs, catches it and does nothing.
     * This method will destroy any file that exists before it that has the same path as srcpath.
     *
     * @param destPath : the destination path (and file name) of the temp file
     * @param srcPath : the source path (and file name) of the temp file
     */
    private void moveFile(String destPath, String srcPath){
        InputStream in;
        OutputStream out;
        try {
            in = new FileInputStream(srcPath);
            out = new FileOutputStream(destPath);
            //read in from srcPath and output it into destPath
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            // write the output file
            out.flush();
            out.close();
            // delete the original file
            new File(srcPath).delete();
        }
        catch (FileNotFoundException e) {
            if(DEBUG)Log.d("FileSaveActiv.moveFile", e.getMessage());
        }
        catch (Exception e) {
            if(DEBUG)Log.d("FileSaveActiv.moveFile", e.getMessage());
        }
    }

    //gets the filename stored in this activitys EditText (@+id/filenametextedit)
    private String getFileName(){
        EditText editText = (EditText) findViewById(R.id.filenametextedit);
        return editText.getText().toString();
    }

    /**onActivityResult
     *
     * The callback method for when a method called by startActivityForResult returns. For us if the returning
     * activity is in a good state and is the expected activity (i.e. another FileBrowserActivity) then just now
     * return self with the data gotten from the activity that caused this callback
     *
     * @param requestCode : the code passed when the returning activity started with StartActivityForResult
     * @param resultCode : a code representing how the data collection went
     * @param data : the returned data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK && requestCode==DIRECTORY_RESULT_KEY){
            String filePath = data.getStringExtra(FileBrowserActivity.RESULT_KEY);
            savingdirectory = filePath;
            TextView dirText = (TextView)findViewById(R.id.choosendirectorytext);
            dirText.setText(filePath);
        }
    }
}
