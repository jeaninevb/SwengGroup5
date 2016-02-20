package soft.swenggroup5;

import java.util.Scanner;

/**
 * DecoderUtils
 *
 * Class of static utility methods to assist in the decoding of a File object
 *
 */
public class DecoderUtils {


    /**
     * decodeFile
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
    public static ReceivedData decodeFile(String data, String fileExtension){

        switch(fileExtension){
        case ContactData.FILE_EXTENSION:
            return new ContactData( new Scanner(data) );
        default://will return generic "FileData" object in later versions.
            return null;
        }
    }

}
