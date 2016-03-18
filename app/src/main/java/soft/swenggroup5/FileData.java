package soft.swenggroup5;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by CrusaderCrab on 15/03/2016.
 */
public class FileData implements ReceivedData {

    private final String fileData;
    private final String fileExtension;

    public FileData(String data, String fileExtension){
        this.fileData = data;
        this.fileExtension = fileExtension;
        if( fileExtension == null){
            throw new java.lang.NullPointerException("EncodeUtil probably encoded a null mime");
        }
    }

    @Override
    public void saveData(Context context){
        try {
            File temp = makeTempFile(context);
            Intent startingIntent = new Intent(context, FileSaveActivity.class);
            startingIntent.putExtra(FileSaveActivity.TEMP_FILE_PATH_KEY, temp.getAbsolutePath());
            context.startActivity(startingIntent);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private File makeTempFile(Context context) throws IOException{
        //create the Contact file
        File outputDir = context.getCacheDir(); // get folder path for this app
        //createTempFile makes a file with a random filename, but we must still delete the file later ourselves
        File outputFile = File.createTempFile("TempFile", "."+fileExtension, outputDir);
        outputFile.deleteOnExit();
        FileWriter wr = new FileWriter(outputFile);
        wr.write(fileData);
        wr.close();
        return outputFile;
    }

    @Override
    public Intent TEST_saveData(Context context) {
        return null;
    }

    @Override
    public void printData() {
        Log.d("FileData.printData()", " First 10 chars of data " + fileData.substring(0, 10) + " File Extension: " + fileExtension);
    }

    @Override
    public String toString(){
        return "File of type "+fileExtension;
    }
}
