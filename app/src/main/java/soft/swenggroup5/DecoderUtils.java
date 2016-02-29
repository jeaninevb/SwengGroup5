package soft.swenggroup5;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
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
            String[] headerContents = header.split(EncoderUtils.DELIMITER); // split by the delimeter
            headerValues.put("File Name", headerContents[0]); // add each value to the hashtable
            headerValues.put("File Length", headerContents[1]);
            headerValues.put("Mime Type", headerContents[2]);
            headerValues.put("Hash Code", headerContents[3]);
            headerValues.put("Number of QR Codes", headerContents[4]);
            return headerValues;
        }
        return null;
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
    public static ReceivedData decodeFile(String data) throws IOException{
        String header = getHeader(data);
        String fileData = getFileData(data);
        Hashtable<String, String> details = decodeHeader(header);
        //if(!validateFile(
        //        fileDataToFile(fileData, details.get("File Name")),
        //       Integer.getInteger(details.get("Hash Code")))
        //        )
        //   return null;
        return decodeFileData(fileData, details.get("Mime Type"));

    }

    /**
     * stringToByteList
     *
     * Given a String returns a Byte List containing the same "characters"
     *
     * @param s : The String to convert
     * @return : a Byte List containing the same "characters" as s
     */
    public static List<Byte> stringToByteList(String s){
        if (s != null) {
            byte[] primitives = s.getBytes();
            Byte[] data = new Byte[primitives.length];
            for(int i = 0; i < data.length; i++)
                data[i] = primitives[i];
            if (DEBUG) Log.d("stringToByteList", "Returning " + data.toString());
            return Arrays.asList(data);
        }
        if (DEBUG) Log.d("stringToByteList", "List was null, returning null");
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

    /**
     * fileDataToFile
     *
     * Creates a file containing the data in the passed String, needed to use ValidateFile().
     *
     * @param s : the given file data
     * @param name : the name of the file we will be matching hashes with
     * @return : a temporary file containing the data in s
     * @throws IOException : from File creation + filling
     */
    private static File fileDataToFile(String s, String name) throws IOException{
        File f = new File(name);
        f.deleteOnExit();
        FileWriter writer = new FileWriter(f);
        writer.write(s);
        return f;
    }

    //returns a String containing just the Header from data, which should contain the entire
    //+ encoded data.
    public static String getHeader(String data){
        return data.split(EncoderUtils.END_DLIMITER)[0];
    }

    //returns a String containing just the actual file data from data, which should contain
    //+ the entire encoded data.
    public static String getFileData(String data){
        return data.split(EncoderUtils.END_DLIMITER)[1];
    }

}
