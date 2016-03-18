package soft.swenggroup5;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * FileData
 *
 * Class for holding File Data and saving it to a user-specified arbitary location.
 * Implements ReceivedData and is meant be used as part of decoding and saving generic files
 * from QR codes
 *
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

    /**saveData
     *
     * Used to save the held file data to a user specified arbitary location on the device. First
     * saves the file to a temp location then uses FileSaveActivity to have the user specify where
     * they want the file actually saved. The temp file is moved there then.
     *
     * @param context the context ("the calling Activity") where this method
     *                is called. Needed to interact with much of the Android
     */
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

    /**
     * makeTempFile
     *
     * saves the data stored in this FileData to a temporary location. The temp file will automatically
     * destroy itself at the end of the applications life cycle if not already deleted beforehand.
     *
     * @param context : to get the location of this apps private memory space
     * @return outputFile : the Temp File
     * @throws IOException : from java.io method calls
     */
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
