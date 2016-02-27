package soft.swenggroup5;

import android.util.Log;

import java.io.File;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Scanner;

/**
 * DecoderUtils
 *
 * Class of static utility methods to assist in the decoding of a File object
 *
 */
public class DecoderUtils {

    private final static boolean DEBUG = true;

    /**
     * decodeHeader
     *
     * the data forming the header is decoded and is displayed in a hashtable
     *
     * @param header
     * @return hashtable object, which contains the details of the header
     */

    public static Hashtable<String, String> decodeHeader(String header) {
        if (header != null) {
            Hashtable<String, String> headerValues = new Hashtable<String, String>();
            String[] headerContents = header.split("|"); // split by the delimeter
            headerValues.add("File Name", headerContents[0]); // add each value to the hashtable
            headerValues.add("File Length", headerContents[1]);
            headerValues.add("Mime Type", headerContents[2]);
            headerValues.add("Hash Code", headerContents[3]);
            headerValues.add("Number of QR Codes", headerContents[4]);
            return headerValues;
        } else {
            return null;
        }
    }

    /**
     * decodeFile
     *
     * a String that should contain all the data gotten from decoded
     * QR codes (i.e. contains a Encoded Header and file data), this method will extract
     * the header and file data. If the passed hash in the header is correct, then this method
     * will return a ReceivedData object which can be used to save the given file to a device.
     *
     * @param data a String that should contain all the data gotten from decoded
     *             QR codes (i.e. contains an Encoded Header and file data)
     * @return ReceivedData object that can be used to save the data to a Android
     *         device and view info on the file
     */
    public static ReceivedData decodeFile(String data){
        return null;
    }

    /**
     * decodeFileData
     *
     * given a string (that should contain the data gotten from decoded QR codes
     * without the encoding Header) returns a ReceivedData object that can be used
     * to save the data to a Android device and view info on the file
     *
     * @param data  a string (that should contain the data gotten from decoded QR codes
     *              without the encoding Header)
     * @param fileExtension the file extension saved in the MIME section of the
     *                      encoded header
     * @return  ReceivedData object that can be used to save the data to a Android
     *          device and view info on the file
     */
    public static ReceivedData decodeFileData(String data, String fileExtension ){
        switch(fileExtension){
            case ContactData.FILE_EXTENSION:
                return new ContactData( new Scanner(data) );
            default://will return generic "FileData" object in later versions.
                return null;
        }
    }

    /**
     * validateFile
     *
     * When the user scans in the first QR code (when there are multiple or the only QR code when
     * there is just one), the QR code contains some header information. One piece in the header
     * is the hash code of the file represented by the qr code.
     * To ensure that the user is about to save the information that they scanned, we must first
     * compare the hash of the file created by the data in the QR code against the hash contained
     * in the header. This becomes especially important when we deal with larger files as it could
     * be possible to start scanning a sequence of QR codes and attempt to manipulate the data
     * by scanning in QR codes from a different source. This function protects against that.
     *
     * @param file: the compiled file from the data scanned
     * @param givenHash: the hash contained in the qr code
     * @return boolean based on whether the compiled files hash matches that in the qr code header
     */
    public static boolean validateFile(File file, int givenHash) {
        if(file != null) {
            if (DEBUG) Log.d("validateFile",
                    "File " + file + "not null. Return " + (file.hashCode() == givenHash));
            return file.hashCode() == givenHash;
        }
        if (DEBUG) Log.d("validateFile", "File was null, return false.");
        return false;
    }

}
