package soft.swenggroup5;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

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
    private final static int WHITE = 0xFFFFFFFF;
    private final static int BLACK = 0xFF000000;
    private final static int WIDTH = 400;
    private final static int HEIGHT = 400;

    /**
     * encodeFile
     *
     * Takes a file and returns a List of bytes representing the header and contents
     *
     * @param file: The data to be converted to QR Code
     * @return a List of Bytes
     */
    public static List<Byte> encodeFile(File file) {
        try {
            if (file != null || file.length() != 0) {
                List<Byte> b = new ArrayList<Byte>(EncoderUtils.encodeHeader(file));
                b.addAll(EncoderUtils.getFileBytes(file));
                return b;
            }
        }
        catch (NullPointerException e) {
            Log.e("encodeFile", e.toString());
        }
        return null;
    }

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
            String type = null;
            String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
            //special case needed for contact data, or else MIME will be stored as "null"
            if(extension.equals(ContactData.FILE_EXTENSION)){
                type = ContactData.FILE_EXTENSION;
            }else if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            }
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
        if(size <= 0) {
            return 0;
        }
        else {
            if(size % MAX_FILE_SIZE > 0 || size < MAX_FILE_SIZE) {
                return size / MAX_FILE_SIZE + 1;
            }
            else {
                return size / MAX_FILE_SIZE;
            }
        }
    }

    /**
     * getFileBytes
     *
     * get a file, convert it's contents to a list of bytes
     *
     * @param file: the file read in
     * @return an array of bytes
     */
    public static List<Byte> getFileBytes(File file) {
        if(file != null){
            byte[] array = new byte[(int) file.length()];
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                fileInputStream.read(array);
                for (int i = 0; i < array.length; i++) {
                    System.out.print((char)array[i]);
                }
            } catch (FileNotFoundException e) {
                Log.e("getFileBytes", e.toString());
            }
            catch (IOException e1) {
                Log.e("getFileBytes", e1.toString());
            }
            List<Byte> byteList = new ArrayList<Byte>();
            for (int index = 0; index < array.length; index++) {
                byteList.add(array[index]);
            }
            return byteList;
        }
            return null;
    }

    /**
     * encodeHeader
     *
     * Takes a file and its position and returns an ArrayList of bytes representing
     * 1. File size
     * 2. File Type
     * 3. Hash value
     * 4. Number of qr codes
     *
     * @param file: the data to be used
     * @return A List of bytes to be used as the QR code header
     */
   public static List<Byte> encodeHeader(File file) {

        if(file==null || file.length()==0) {
            return null;
        }

        ArrayList<String> headerString = new ArrayList<String>();
        List<Byte> listOfBytes = new ArrayList<Byte>();

        //FILE SIZE
        headerString.add(String.valueOf(file.length()+"|"));

        // FILE TYPE
        String filename = EncoderUtils.getMimeType(file);
        headerString.add(filename+"|");

        // HASH VALUE
        int h = file.hashCode();
        headerString.add(String.valueOf(h) + "|");

        //NUMBER OF QR CODE
        int qrCodes = EncoderUtils.numberOfQRCodes(headerString.size() + 5);
        headerString.add(String.valueOf(qrCodes));
        headerString.add(String.valueOf("\0"));                 //End tag for header

        for(int i = 0; i < headerString.size(); i++) {
            byte[] b =  headerString.get(i).getBytes();
            for(int j=0; j < b.length;j++) {
                listOfBytes.add(b[j]);
            }
        }
        return listOfBytes;
    }

    /**
     * byteListToString
     *
     * Convert a List<Byte> to a String
     *
     * @param list: the list to convert
     * @return a string representing the passed list
     */
    public static String byteListToString(List<Byte> list) {
        if(list != null) {
            byte[] data = new byte[list.size()];
            int i = 0;
            for (Byte b : list) {
                data[i++] = b;
            }
            return new String(data);
        }
        return null;
    }

    /**
     * encodeAsBitmap
     *
     * Takes a string and returns a bitmap representation of the string as a qr code
     *
     * @param dataBytes: bytes to generate a qr code for
     * @return a bitmap representing the qr code generated for the passed string
     * @throws WriterException
     */
    public static Bitmap generateQRCodeBitmap(List<Byte> dataBytes) throws WriterException {
        String stringToConvert = byteListToString(dataBytes);
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(stringToConvert, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);
        } catch (IllegalArgumentException e) {
            // Unsupported format
            Log.e("generateQRCodeBitmap", e.toString());
            return null;
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}