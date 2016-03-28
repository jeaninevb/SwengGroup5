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
 *
 * Class of static utility methods to assist in the encoding of a File object
 */

public class EncoderUtils {

    /**
     * public constants
     * Used to create stable testing and decoding
     */
    public final static String DELIMITER = "~";
    public final static String END_DLIMITER = "\0";

    /**
     * private constants
     * TODO WIDTH and HEIGHT will need to be modified to increase the size of the QR code
     */
    private final static int MAX_QR_CODE_DATA_SIZE = 1000;
    private final static int WIDTH = 400;
    private final static int HEIGHT = 400;

    private final static int WHITE = 0xFFFFFFFF;
    private final static int BLACK = 0xFF000000;

    /**
     * debugging constants
     * Set to false for release version
     */
    private final static boolean DEBUG = true;

    /**
     * encodeFile
     *
     * Takes a file and returns a String representing the header and contents
     *
     * @param file: The file that will be encoded into a String
     * @return a string representing the generated header and file contents
     */
    public static String encodeFile(File file) {
        if (file != null) {
            StringBuilder b = new StringBuilder();
            b.append(EncoderUtils.generateHeader(file));
            b.append(EncoderUtils.getFileContents(file));
            if (DEBUG) Log.d("encodeFile", "File " + file + ". Size of file is " + b.length());
            return b.toString();
        }
        if (DEBUG) Log.d("encodeFile", "File is null.");
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
        if (file != null) {
            String extension = MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
            //special case needed for contact data, or else MIME will be stored as "null"
            if (extension.equals(ContactData.FILE_EXTENSION)) {
                if(DEBUG) Log.d("getMimeType",
                        "File " + file + " has the Mime Type " + ContactData.FILE_EXTENSION);
                return ContactData.FILE_EXTENSION;
            }
            else if(extension.equals("customtxtentry")) {
                return "customtxtentry";
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
     * numberOfQRCodes
     *
     * Takes in a file size and calculates the number of QR codes needed to represent it. The
     * number of QR codes needed is based upon the MAX_QR_CODE_DATA_SIZE constant.
     *
     * @param size: the size of the file to be transferred
     * @return the number of QR Codes required to represent the file
     */
    public static int numberOfQRCodes(int size) {
        if (size <= 0) {
            if (DEBUG) Log.d("numberOfQRCodes", "Size was " + size + ". Returning 0");
            return 0;
        } else {
            if (size % MAX_QR_CODE_DATA_SIZE > 0 || size < MAX_QR_CODE_DATA_SIZE) {
                if (DEBUG) Log.d("numberOfQRCodes1",
                        "Size was " + size + ". Returning " + ((size / MAX_QR_CODE_DATA_SIZE) + 1));
                Log.d("XXX_max", " "+((size / MAX_QR_CODE_DATA_SIZE) + 1));
                return (size / MAX_QR_CODE_DATA_SIZE) + 1;
            } else {
                if (DEBUG) Log.d("numberOfQRCodes2",
                        "Size was " + size + ". Returning 0 " + (size / MAX_QR_CODE_DATA_SIZE));
                return size / MAX_QR_CODE_DATA_SIZE;
            }
        }
    }

    /**
     * getFileContents
     *
     * Takes a file, convert it's contents to a String
     * Note: it is ok to ignore the warning regarding fileInputStream.read(array) as this
     * is due to the fact that fileInputStream.read returns an integer. Since we do not require
     * this integer it is ok not to make use of it but Android Studio will still complain
     * about it not being used.
     *
     * @param file: the file read in
     * @return a String containing the file contents
     */
    public static String getFileContents(File file) {
        if (file != null) {
            try {
                byte [] fileContentsArray = new byte [(int) file.length()];
                FileInputStream fileInputStream = new FileInputStream(file);
                fileInputStream.read(fileContentsArray);
                fileInputStream.close();

                StringBuilder fileContents = new StringBuilder();
                for(byte b : fileContentsArray) {
                    fileContents.append((char) b);
                }

                if (DEBUG) Log.d("getFileBytes",
                        "File " + file + ". Returning byte list of size" + fileContents.length());
                return fileContents.toString();
            } catch (IOException e) {
                if (DEBUG) Log.e("getFileBytes", e.toString());
            }
        }
        if (DEBUG) Log.d("getFileBytes", "File was null. Returning null");
        return null;
    }

    /**
     * generateHeader
     *
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
    public static String generateHeader(File file) {
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
                    .append(END_DLIMITER);

            if (DEBUG) Log.d("generateHeader", "Header string is " + headerString.toString());
            return headerString.toString();
        }
        if (DEBUG) Log.d("generateHeader", "File was null, returning null");
        return null;
    }


    /**
     * generateQRCodeBitmap
     *
     * Takes a string and returns a bitmap representation of the string as a qr code
     *
     * @param data: String to generate a qr code for
     * @return a bitmap representing the qr code generated for the passed string
     * @throws WriterException
     */
    public static Bitmap generateQRCodeBitmap(String data) throws WriterException {
        if (data != null) {
            BitMatrix result;
            try {
                result = new MultiFormatWriter()
                        .encode(data, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);
            } catch (IllegalArgumentException e) {
                if (DEBUG) Log.e("generateQRCodeBitmap", e.toString());
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

    /**
     * generateQRCodeBitmaps
     *
     * Takes a string and returns bitmaps representational of the string as a qr code
     *
     * @param data: String to generate a qr code for
     * @return bitmaps representing the qr code generated for the passed string
     * @throws WriterException
     */
    public static List<Bitmap> generateQRCodeBitmaps(String data) throws WriterException {
        String strings;
        List<Bitmap> bitmaps = new ArrayList<>();
        int index = 0;
        while (index < data.length()) {
           // strings.add(data.substring(index, Math.min(index + 2000, data.length())));
            strings = data.substring(index, Math.min(index + 2000, data.length()));
            bitmaps.add(generateQRCodeBitmap(strings));
            index += 2000;
        }
        return bitmaps;
    }

    /**********************************************************************************************/
    /*      Multi QR code version of EncoderUtils. I have not replaced the old code with this until
    *       all references to the old code have been removed. I don't want to suddenly break everything
    *       on everyone.
    */
    /**********************************************************************************************/

    /** encodeFileToQRStrings
     *
     * Given a file returns a List<String> containing the file broken up into a number of strings
     * that can be converted into QR codes using generateQRCodeBitmap. The Strings already contain
     * the QR code headers. The first QR code (i.e. the one with the full header) is placed in the
     * first index of the returned list
     *
     * @param file : the file to break up into Strings that can become QR codes
     * @return : the given file broken up into Strings that can become QR codes
     */
    public static List<String> encodeFileToQRStrings(File file){
        if(file == null){
            if(DEBUG) Log.d("encodeFileToQRStrings", "file given was null");
            return null;
        }
        String fileString = getFileContents(file);
        Log.d("encodeFileToQRStrings", "File contents: " + fileString);
        String header = generateTopHeader(file, fileString);
        Log.d("encodeFileToQRStrings", "Header: " + header);
        Log.d("encodeFileToQRStrings", "File length: " + file.length());
        final int max = numberOfQRCodes((int) file.length());
        Log.d("encodeFileToQRStrings", "Max = " + max);
        ArrayList<String> qrChunks = getQRChunks(fileString, max);
        ArrayList<String> qrStrings = new ArrayList<String>(qrChunks.size());
        //Log.d("encodeFileToQRStrings", "Adding: " + header + qrChunks.get(0));
        //Log.d("encodeFileToQRStrings", "Before qrString(0): " + qrChunks.get(0));
        qrStrings.add(header + qrChunks.get(0));
        //Log.d("encodeFileToQRStrings", "After qrString(0): " + qrStrings.get(0));
        for(int i = 1; i < max; i++){
            //Log.d("encodeFileToQRStrings", "Adding: " + generateMiddleHeader(i, max) + qrChunks.get(i));
            qrStrings.add( generateMiddleHeader(i + 1, max) + qrChunks.get(i));
        }

        for(int i = 0; i < max; i++){
            Log.d("XXX_QRS","no: "+i+" "+ qrStrings.get(i).substring(0, 25));
        }
        Log.d("done","XXXXXXXXXXXXXXXXXX");
        return qrStrings;
    }

    /**generateTopHeader
     *
     * Generates the header that will be prefixed up the data in the first QR code. It is in the
     * format:
     *      index~no of total qr codes~file extension~hash code'/0'
     *
     * @param file : the file this header will represent
     * @param fileString : the first parameter stored in a string.
     *                   passed in so that it can be calced once elsewhere and just passed around
     * @return : the header that represents the passed in file
     */
    private static String generateTopHeader(File file, String fileString){
        StringBuilder header = new StringBuilder();
        header.append("1"); //header for first file, so will always have index 0
        header.append(DELIMITER);
        header.append(numberOfQRCodes((int)file.length()));
        header.append(DELIMITER);
        header.append(getTopMimeType(file));
        header.append(DELIMITER);
        header.append(fileString.hashCode());
        header.append(END_DLIMITER);
        return header.toString();
    }


    /**generateMiddleHeader
     *
     * Creates a string in the format of a 'middle' header, which are the headers that are prefixed
     * to all QR codes for a file bar the first qr code (which gets the 'Top' header).
     *
     * @param index : the index of the qr code this header will be prefixed to
     * @param max : the total number of qr codes for this file
     * @return : the header containing the passed in information
     */
    private static String generateMiddleHeader(int index, int max){
        StringBuilder header = new StringBuilder();
        header.append(index);
        header.append(DELIMITER);
        header.append(max);
        header.append(DELIMITER);    //this delimiter is added so index and max can be gotten from the top
                                     //+ and middle headers with the same code.
        header.append(END_DLIMITER);
        return header.toString();
    }


    /**getQRChunks
     *
     * Given a String (that should represent the data in a file), breaks up the string into a list
     * of substrings that are all at most MAX_QR_CODE_DATA_SIZE length.
     *
     * @param fileString : the string to break up
     * @param noOfCodes : the number of chunks the string should break up into.
     * @return : an arrayList holding all the substrings
     */
    private static ArrayList<String> getQRChunks(String fileString, int noOfCodes){
        ArrayList<String> chunks = new ArrayList<String>(noOfCodes);
        for(int i = 0; i < (noOfCodes-1); i++){ //get all but the last chunk
            chunks.add(i, fileString.substring(i*MAX_QR_CODE_DATA_SIZE, (i+1)*MAX_QR_CODE_DATA_SIZE));
        }
        //the last chunk is gotten seperately so that .substring() doesnt try to index off the
        //+ end of the passed in string
        chunks.add((noOfCodes-1), fileString.substring((noOfCodes - 1) * MAX_QR_CODE_DATA_SIZE));
        return chunks;
    }


    /**getTopMimeType
     *
     * Returns the extension of a given file.
     *
     * @param file
     * @return
     */
    private static String getTopMimeType(File file){
        if (file != null) {
           return MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
        }
        if (DEBUG) Log.d("encodeFile", "File is null.");
        return null;
    }



}