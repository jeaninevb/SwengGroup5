package soft.swenggroup5;

import java.io.ByteArrayInputStream;
import java.util.List;
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
     * @return  ReceivedData object that can be used to save the data to a Android
     *          device and view info on the file
     */
    //There will need to be some extra data passed to identify what type of data is in
    //+data. This data will needed to be probably added during Encoding.
    public ReceivedData decodeFile(String data/*, dataType*/){

        /*switch(dataType){
        case CONTACT_DATA:
            return new ContactData( new Scanner(data) );
        default:
            return null;
        }
        */
        return new ContactData( new Scanner(data) );
    }

}
