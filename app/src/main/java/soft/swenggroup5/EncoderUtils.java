package soft.swenggroup5;

import android.util.Log;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * EncoderUtils
 *
 * Class of static utility methods to assist in the encoding of a File object
 *
 */

public class EncoderUtils {

    private final static int MAX_FILE_SIZE = 2000;
    /**
     * getMimeType
     *
     * Get the MIME type of a file.
     * List of MIME types: http://www.freeformatter.com/mime-types-list.html
     *
     * @param file: the file to get the MIME type of
     * @return the MIME type of the passed file or else null if not possible to get the type
     */
    public static String getMimeType(File file) {
        if(file != null) {
            String filePath = file.getAbsolutePath();
            Log.d("File_path", filePath);
            String type = null;
            String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            }
            Log.d("File_MIME_type", "" + type);
            return type;
        }
        return null;
    }
    /**
     * splitFileSize
     *
     * Takes in a file size and calculates the number of QR codes needed to transfer it.
     *
     * @param size: the size of the file to be transferred.
     * @return the number of QR Codes required.
     */
    public static int numberOfQRCodes(int size) {
        if(size<=0) {
            return 0;
        }
        else {
            if(size%MAX_FILE_SIZE>0 || size<MAX_FILE_SIZE) {
                return size / MAX_FILE_SIZE + 1;
            }
            else {
                return size / MAX_FILE_SIZE;
            }
        }
    }

    //convert a file object into a list of bytes
    private List<Byte> getFileBytes(File file) {

        byte[] array = new byte[(int) file.length()];  //create an array for bytes
        try {
            FileInputStream fileInputStream = new FileInputStream(file);  //read through the file and put into the array in Bytes
            fileInputStream.read(array);
            for (int i = 0; i < array.length; i++) {
                System.out.print((char)array[i]);
            }
        } catch (FileNotFoundException e) {                 //through "File Not Found" if can't find the file
            System.out.println("File Not Found.");
            e.printStackTrace();
        }
        catch (IOException e1) {
            System.out.println("Error Reading The File.");      //through "Error Reading The File" if there is an error of reading file
            e1.printStackTrace();
        }

        List<Byte> byteList = new ArrayList<Byte>();    // create a new array list
        for (int index = 0; index < array.length; index++) {
            byteList.add(array[index]);                     //put each index of array into array list
        }
        return byteList;                //return the array list containing all bytes
    }

}