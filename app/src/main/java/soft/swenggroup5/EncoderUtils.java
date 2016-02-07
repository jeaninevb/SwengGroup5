package soft.swenggroup5;

import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;

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
    public static int splitFileSize(int size) {
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

    //test pull first
/*    private List<Bytes> getFileBytes(File file) {
        // your code here
    }*/
}