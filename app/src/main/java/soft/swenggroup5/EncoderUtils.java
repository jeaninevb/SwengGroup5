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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * EncoderUtils
 * <p/>
 * Class of static utility methods to assist in the encoding of a File object
 */

public class EncoderUtils {

    private final static int MAX_FILE_SIZE = 2000;
    private final static int WHITE = 0xFFFFFFFF;
    private final static int BLACK = 0xFF000000;
    private final static int WIDTH = 400;
    private final static int HEIGHT = 400;
    private final static boolean DEBUG = true;

    public final static String DELIMITER = "|";

    /**
     * encodeFile
     * <p/>
     * Takes a file and returns a List of bytes representing the header and contents
     * Note: Android Studio complains that the b.addAll(EncoderUtils(...)) lines may have
     * a null input. However we are guaranteed not to based on the file != null check at the
     * very start of the method
     *
     * @param file: The data to be converted to QR Code
     * @return a List of Bytes
     */
    public static List<Byte> encodeFile(File file) {
        if (file != null) {
            List<Byte> b = new ArrayList<>();
            b.addAll(EncoderUtils.encodeHeader(file));
            b.addAll(EncoderUtils.getFileBytes(file));
            if (DEBUG) Log.d("encodeFile", "File " + file + ". Size of file is " + b.size());
            return b;
        }
        if (DEBUG) Log.d("encodeFile", "File is null.");
        return null;
    }

    /**
     * getMimeType
     * <p/>
     * Get the MIME type of a file.
     * List of MIME types: http://www.freeformatter.com/mime-types-list.html
     *
     * @param file: the file to get the MIME type of
     * @return the MIME type of the passed file or else null if not possible to get the type
     */
    public static String getMimeType(File file) {
        if (file != null) {
            String extension = MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
            //special case needed for contact data, or else MIME will be stored as "null"
            if (extension.equals(ContactData.FILE_EXTENSION)) {
                if(DEBUG) Log.d("getMimeType",
                        "File " + file + " has the Mime Type " + ContactData.FILE_EXTENSION);
                return ContactData.FILE_EXTENSION;
            } else {
                if (DEBUG) Log.d("getMimeType",
                        "File " + file + " has the Mime Type "
                                + MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension));
                return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            }
        }
        if (DEBUG) Log.d("encodeFile", "File is null.");
        return null;
    }

    /**
     * splitFileSize
     * <p/>
     * Takes in a file size and calculates the number of QR codes needed to transfer it.
     *
     * @param size: the size of the file to be transferred.
     * @return the number of QR Codes required.
     */
    public static int numberOfQRCodes(int size) {
        if (size <= 0) {
            if (DEBUG) Log.d("numberOfQRCodes", "Size was " + size + ". Returning 0");
            return 0;
        } else {
            if (size % MAX_FILE_SIZE > 0 || size < MAX_FILE_SIZE) {
                if (DEBUG) Log.d("numberOfQRCodes",
                        "Size was " + size + ". Returning " + size / MAX_FILE_SIZE + 1);
                return size / MAX_FILE_SIZE + 1;
            } else {
                if (DEBUG) Log.d("numberOfQRCodes",
                        "Size was " + size + ". Returning 0 " + size / MAX_FILE_SIZE);
                return size / MAX_FILE_SIZE;
            }
        }
    }

    /**
     * getFileBytes
     * <p/>
     * get a file, convert it's contents to a list of bytes.
     * Note: it is ok to ignore the warning regarding fileInputStream.read(array) as this
     * is due to the fact that fileInputStream.read returns an integer. Since we do not require
     * this integer it is ok not to make use of it but Android Studio will still complain
     * about it not being used.
     *
     * @param file: the file read in
     * @return an array of bytes
     */
    public static List<Byte> getFileBytes(File file) {
        if (file != null) {
            List<Byte> byteList = new ArrayList<>();
            byte[] array = new byte[(int) file.length()];
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                fileInputStream.read(array);
                fileInputStream.close();
                for (byte b : array) {
                    byteList.add(b);
                }
                if (DEBUG) Log.d("getFileBytes",
                        "File " + file + ". Returning byte list of size" + byteList.size());
                return byteList;
            } catch (IOException e) {
                Log.e("getFileBytes", e.toString());
            }
        }
        if (DEBUG) Log.d("getFileBytes", "File was null. Returning null");
        return null;
    }

    /**
     * encodeHeader
     * <p/>
     * Takes a file and its position and returns an ArrayList of bytes representing
     * 1. File name
     * 2. File size
     * 3. File Type
     * 4. Hash value
     * 5. Number of qr codes
     *
     * Note: NUMBER OF QR CODE + 5 is due to 4 bytes for the size, and 1 more for the end delimiter
     *
     * @param file: the data to be used
     * @return A List of bytes to be used as the QR code header
     */
    public static List<Byte> encodeHeader(File file) {
        if (file != null) {
            StringBuilder headerString = new StringBuilder();
            headerString.append(file.getName())
                    .append(DELIMITER)
                    .append(String.valueOf(file.length()))
                    .append(DELIMITER)
                    .append(EncoderUtils.getMimeType(file))
                    .append(DELIMITER)
                    .append(String.valueOf(file.hashCode()))
                    .append(DELIMITER)
                    .append(String.valueOf(EncoderUtils.numberOfQRCodes(headerString.length() + 5)))
                    .append(String.valueOf("\0"));

            List<Byte> listOfBytes = new ArrayList<>();
            for (byte b : headerString.toString().getBytes()) {
                listOfBytes.add((b));
            }
            if (DEBUG) Log.d("encodeHeader", "Header string is " + headerString.toString() + ".");
            return listOfBytes;
        }
        if (DEBUG) Log.d("encodeHeader", "File was null, returning null");
        return null;
    }

    /**
     * byteListToString
     * <p/>
     * Convert a List<Byte> to a String
     *
     * @param list: the list to convert
     * @return a string representing the passed list
     */
    public static String byteListToString(List<Byte> list) {
        if (list != null) {
            byte[] data = new byte[list.size()];
            int i = 0;
            for (Byte b : list) {
                data[i++] = b;
            }
            if (DEBUG) Log.d("byteListToString", "Returning " + new String(data));
            return new String(data);
        }
        if (DEBUG) Log.d("byteListToString", "List was null, returning null");
        return null;
    }

    /**
     * encodeAsBitmap
     * <p/>
     * Takes a string and returns a bitmap representation of the string as a qr code
     *
     * @param dataBytes: bytes to generate a qr code for
     * @return a bitmap representing the qr code generated for the passed string
     * @throws WriterException
     */
    public static Bitmap generateQRCodeBitmap(List<Byte> dataBytes) throws WriterException {
        if (dataBytes != null) {
            String stringToConvert = byteListToString(dataBytes);
            BitMatrix result;
            try {
                result = new MultiFormatWriter()
                        .encode(stringToConvert, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);
            } catch (IllegalArgumentException e) {
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
            if (DEBUG) Log.d("generateQRCodeBitmap", "Returning " + bitmap.toString());
            return bitmap;
        }
        if (DEBUG) Log.d("generateQRCodeBitmap", "List was null, returning null");
        return null;
    }
}