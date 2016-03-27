package soft.swenggroup5;

import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * DecoderUtils
 *
 * Class of static utility methods to assist in the decoding of a File object
 *
 */
public class DecoderUtils {

    private final static boolean DEBUG = false;

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
            headerValues.put("QR code index", headerContents[0]); // add each value to the hashtable
            headerValues.put("Number of QR Codes", headerContents[1]);
            headerValues.put("Mime Type", headerContents[2]);
            headerValues.put("Hash Code", headerContents[3]);
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
    public static ReceivedData decodeFile(String data) throws Exception{
        String header = getHeader(data);
        String fileData = getFileData(data);
        Hashtable<String, String> details = decodeHeader(header);
        //if(!validateFile(
        //        fileData,
        //        Integer.getInteger(details.get("Hash Code")))
        //        )
        //  return null;
        return decodeFileData(fileData, details.get("Mime Type"));

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
    public static ReceivedData decodeFileData(String data, String fileExtension) throws Exception{
        switch(fileExtension){
            case ContactData.FILE_EXTENSION:
                return new ContactData(data);
            default:
                return new FileData(data, fileExtension);
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
     * @param fileString: the compiled file from the data scanned as a string
     * @param givenHash: the hash contained in the qr code
     * @return boolean based on whether the compiled files hash matches that in the qr code header
     */
    public static boolean validateFile(String fileString, int givenHash) {
        if(fileString != null) {
            if (DEBUG) Log.d("validateFile",
                    "File " + fileString + "not null. Return " + (fileString.hashCode() == givenHash));
            return fileString.hashCode() == givenHash;
        }
        if (DEBUG) Log.d("validateFile", "FileString was null, return false.");
        return false;
    }

    //returns a String containing just the Header from data, which should contain the entire
    //+ encoded data.
    public static String getHeader(String data){
        if (DEBUG) Log.d("getHeader", "Header: " + data.split(EncoderUtils.END_DLIMITER)[0].trim());
        return data.split(EncoderUtils.END_DLIMITER)[0].trim();
    }

    //returns a String containing just the actual file data from data, which should contain
    //+ the entire encoded data.
    public static String getFileData(String data){
        return data.split(EncoderUtils.END_DLIMITER)[1];
    }

    //returns the index stored in the the given QRCode
    public static int getQRCodeIndex(String code){
        return Integer.parseInt(code.split(EncoderUtils.DELIMITER)[0]);
    }

    //returns the total no of codes needed as stored in the the given QRCode
    public static int getTotalQRCodeNumber(String code){
        return Integer.parseInt(code.split(EncoderUtils.DELIMITER)[1]);
    }

    /** combineQRCodes
     *
     *  Given an arrayList of Strings (that are expected to be formatted like the strings generated
     *  by EncoderUtils) combines them into a single string without the middle headers but
     *  with the top header( which should be with the string in index 0 of the passed arraylist)
     *
     * @param codes : the qr codes to combine, in ascending index order
     * @return : the codes appended, without the middle headers but with the top header
     */
    public static String combineQRCodes(ArrayList<String> codes){
        StringBuilder combined = new StringBuilder();
        combined.append( codes.get(0) );
        for(String code : codes){
            combined.append( getFileData(code) );
        }
        return combined.toString();
    }

}
