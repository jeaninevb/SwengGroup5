package soft.swenggroup5;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileFilter;

/**FileBrowserActivity
 *
 * A basic File Browser Activity. Use with StartActivityForResult to get the path to a user
 * selected file or directory.
 */
public class FileBrowserActivity extends AppCompatActivity {

    //key to put/get displaying info from the intent that started the Activity
    public static final String DISPLAYING_KEY = "soft.swenggroup5.filebrowser.displaying.key";
    //key to put/get the directory that this activity should display from the intent that started the Activity
    public static final String ROOT_KEY = "soft.swenggroup5.filebrowser.root.key";
    //key that the returning intent will store the wanted result at
    public static final String RESULT_KEY = "soft.swenggroup5.filebrowser.result.key";
    //request code passed with calls of startActivityForResult within this activity
    private static final int RECURSE_RETURN_KEY = 345;
    //valid values that can be mapped to DISPLAYING_KEY
    public static final int DIRECTORY_ONLY = 87; //only display directories
    public static final int FILES_AND_DIRECTORYS = 88; //display files and directories
    public static final int FILE_SELECT_ONLY = 89; //display files and directories but only allow files
                                                   //+ to be selected

    private File[] listedFiles; //list of files on display
    private String[] listedFileNames; //list of filenames in listedFiles
    private ListView list; //the listView the user interacts with in this activity
    private int displayMode; //the current display mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);
        //get the directory this activity should represent
        Intent startingIntent = getIntent();
        String root = startingIntent.getStringExtra(ROOT_KEY);
        //if no directory given, set to root external storage folder
        if(root==null) root = getDefaultRootFolder();
        getSupportActionBar().setTitle(root);
        //get an array of all the files that should be displayed
        displayMode = startingIntent.getIntExtra(DISPLAYING_KEY, DIRECTORY_ONLY);
        File myDirectory = new File(root);
        if(displayMode==FILES_AND_DIRECTORYS || displayMode==FILE_SELECT_ONLY){
            listedFiles = myDirectory.listFiles(); //lists all files that are at this directory level
        }else {
            listedFiles = myDirectory.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isDirectory();
                }
            });
        }
        listedFileNames = new String[listedFiles.length];
        for(int i = 0; i < listedFiles.length; i++){
            listedFileNames[i] = listedFiles[i].getName();
        }
        //get the listView and give it an adapter that will make it display our files
        list = (ListView)findViewById(R.id.fileBrowserListID);
        list.setAdapter(
                new ArrayAdapter<String>(
                        this,
                        R.layout.layout_file_browser_list_row, //use this layout as a ListView row
                        R.id.filenametext, //place the String data into this Textview
                        listedFileNames) //get info from this array
        );

        //set up ListView OnClick Listener, this is for the row, not the button on the list row
        list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                        File selectedFile = listedFiles[myItemInt];
                        if(selectedFile.isDirectory()){
                            recurseToNextDirectory(selectedFile);
                        }else{
                            returnFile(selectedFile);
                        }
                    }
                }
        );
    }

    /**fileSelectOnClick
     *
     * The onClick listener method for the buttons on the ListView, but not the listView rows themselves.
     * If button at a valid list location return the selected file to the calling activity
     *
     * @param view
     */
    public void fileSelectOnClick(View view){
        final int position = list.getPositionForView(view);
        if (position != ListView.INVALID_POSITION && position < listedFiles.length) {
            File selectedFile = listedFiles[position];
            if(!(selectedFile.isDirectory() && displayMode==FILE_SELECT_ONLY)) {
                returnFile(selectedFile);
            }
        }
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
        if(resultCode==RESULT_OK && requestCode==RECURSE_RETURN_KEY){
            String filePath = data.getStringExtra(RESULT_KEY);
            returnFile(new File(filePath));
        }
    }

    /**returnFile
     *
     *  Returns selectedFile to the calling activity if this activity was started with startActivityForResult
     *
     * @param selectedFile : the file whose path will be returned
     */
    private void returnFile(File selectedFile){
        Intent data = new Intent();
        data.putExtra(RESULT_KEY, selectedFile.getAbsolutePath());
        setResult(RESULT_OK, data);
        finish();
    }

    /**recurseToNextDirectory
     *
     * Creates an FileBrowserActivity to display the passed directory. Used recursively to traverse
     * the file tree and return a file.
     *
     * @param selectedDirectory : the directory to have the next activity display
     */
    private void recurseToNextDirectory(File selectedDirectory){
        Intent startingIntent = new Intent(FileBrowserActivity.this, FileBrowserActivity.class);
        startingIntent.putExtra(DISPLAYING_KEY, displayMode);
        startingIntent.putExtra(ROOT_KEY, selectedDirectory.getAbsolutePath());
        startActivityForResult(startingIntent, RECURSE_RETURN_KEY);
    }

    // returns a string to the default root folder
    private String getDefaultRootFolder(){
        if(isExternalStorageWritable())
            //return root of external storage
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        return "";
    }

    // Checks if external storage is available for read and write
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}
