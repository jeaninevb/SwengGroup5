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

public class FileSaveActivity extends AppCompatActivity {

    public static final String TEMP_FILE_PATH_KEY = "com.swenggroup5.filesave.temppath.key";
    private String tempFilePath = null;
    private String savingdirectory = null;
    private static final int DIRECTORY_RESULT_KEY = 999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_save);
        Intent startingIntent = getIntent();
        tempFilePath = startingIntent.getStringExtra(TEMP_FILE_PATH_KEY);
    }

    public void directorySelectOnClick(View view){
        Intent startingIntent = new Intent(FileSaveActivity.this, FileBrowserActivity.class);
        startingIntent.putExtra(FileBrowserActivity.DISPLAYING_KEY, FileBrowserActivity.DIRECTORY_ONLY);
        startActivityForResult(startingIntent, DIRECTORY_RESULT_KEY);
    }

    public void saveFileOnClick(View view){
        if(savingdirectory!=null){
            String fileName = getFileName();
            if(fileName != null){
                moveFile(savingdirectory+fileName, tempFilePath);
                finish();
            }
        }
    }

    private void moveFile(String destPath, String srcPath){
        InputStream in;
        OutputStream out;
        try {
            in = new FileInputStream(srcPath);
            out = new FileOutputStream(destPath);
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
        catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

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
