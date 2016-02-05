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

    //test pull first
/*    private List<Bytes> getFileBytes(File file) {
        // your code here
    }*/
}