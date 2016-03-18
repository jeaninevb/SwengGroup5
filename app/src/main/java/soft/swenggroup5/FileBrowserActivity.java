package soft.swenggroup5;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;

public class FileBrowserActivity extends AppCompatActivity {

    public static final String DISPLAYING_KEY = "soft.swenggroup5.filebrowser.displaying.key";
    public static final String ROOT_KEY = "soft.swenggroup5.filebrowser.root.key";
    public static final String RESULT_KEY = "soft.swenggroup5.filebrowser.result.key";
    private static final int RECURSE_RETURN_KEY = 345;

    public static final int DIRECTORY_ONLY = 87;
    public static final int FILES_AND_DIRECTORYS = 88;

    private File[] listedFiles;
    private String[] listedFileNames;
    private ListView list;
    private int displayMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);
        //get the directory this activity should represent
        Intent startingIntent = getIntent();
        String root = startingIntent.getStringExtra(ROOT_KEY);
        //if no directory given, set to root external storage folder
        if(root==null) root = Environment.getExternalStorageDirectory().getAbsolutePath();
        //get an array of all the files that should be displayed
        displayMode = startingIntent.getIntExtra(DISPLAYING_KEY, DIRECTORY_ONLY);
        File myDirectory = new File(root);
        if(displayMode==FILES_AND_DIRECTORYS){
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

    //set up Select Button OnClick Listener, this is the buttons on the rows in the ListView, not the ListView
    //rows themselves
    public void fileSelectOnClick(View view){
        final int position = list.getPositionForView(view);
        if (position != ListView.INVALID_POSITION && position < listedFiles.length) {
            File selectedFile = listedFiles[position];
            returnFile(selectedFile);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK && requestCode==RECURSE_RETURN_KEY){
            String filePath = data.getStringExtra(RESULT_KEY);
            returnFile(new File(filePath));
        }
    }

    private void returnFile(File selectedFile){
        Intent data = new Intent();
        data.putExtra(RESULT_KEY, selectedFile.getAbsolutePath());
        setResult(RESULT_OK, data);
        finish();
    }

    private void recurseToNextDirectory(File selectedDirectory){
        Intent startingIntent = new Intent(FileBrowserActivity.this, FileBrowserActivity.class);
        startingIntent.putExtra(DISPLAYING_KEY, displayMode);
        startingIntent.putExtra(ROOT_KEY, selectedDirectory.getAbsolutePath());
        startActivityForResult(startingIntent, RECURSE_RETURN_KEY);
    }
}
