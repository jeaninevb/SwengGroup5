package soft.swenggroup5;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    /**
     * Constants
     *
     * WHITE: Hex value to colour generated qr code pixels white
     * BLACK: Hex value to colour generated qr code pixels black
     * WIDTH: The width of the generated qr code TODO include in layout to scale correctly?
     * HEIGHT: The height of the generated qr code TODO include in layout to scale correctly?
     * DEFAULT_STR: Test string to show generation of qr code TODO remove
     * INTEGRATOR: Object that is used to access the integrated scanner
     * TEST_FILE: test file to show encoding of header TODO remove
     */
    private final static int WHITE = 0xFFFFFFFF;
    private final static int BLACK = 0xFF000000;
    private final static int WIDTH = 400;
    private final static int HEIGHT = 400;
    private final static String STR = "Software Engineering Group 5 - SOFT";
    private final IntentIntegrator INTEGRATOR = new IntentIntegrator(this);
<<<<<<< HEAD
    private final File TEST_FILE = new File("testFile.txt");
    private final static int MAX_FILE_SIZE = 2000;
=======
    private File TEST_FILE;
>>>>>>> origin/BackEnd

    /**
     * onCreate
     *
     * TODO note that this is only for the base state of V1
     *
     * Entry point for app when started. Initializes the generated qr code, the scan button,
     * and attaches a listener to the scan button.
     *
     * @param savedInstanceState: required param to instantiate the super class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the scan button referencing the button in res/activity_main.xml
        Button scanButton = (Button)findViewById(R.id.button);

        // Create a space that will be used to present the demo generated qr code
        ImageView imageView = (ImageView) findViewById(R.id.qrCode);

        try {
            // cannot initialize as a constant as an error must be handled.
            // TODO move this to unit tests
            TEST_FILE = File.createTempFile("testing", ".txt");
            // explicitly specify that the temp file is to be deleted on exit of the app
            TEST_FILE.deleteOnExit();
            // write hello to the temp file
            FileOutputStream s = new FileOutputStream(TEST_FILE);
            s.write('h');
            s.write('e');
            s.write('l');
            s.write('l');
            s.write('o');
            // close  the stream
            s.close();
        }
        catch (Exception e) {
            Log.d("Write to temp", e.toString());
        }

        // Attempt to generate the qr code and put it into the ImageView
        try {
            ArrayList<Byte> Header = encodeHeader(TEST_FILE);
            Bitmap bitmap = encodeAsBitmap(STR);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        // Create the event listener for when scanButton is clicked
        scanButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /**
                 * setDesiredBarcodeFormats: setting the scanner to scan qr codes, this can be
                 * changed to scan other codes (bar codes) and may become useful if we want to
                 * implement extended functionality beyond V1
                 * setPrompt: a string shown to the user when scanning. May make this dynamic
                 * to show how many qr codes have been scanned so far (probably V1)
                 * setCameraId: use a specific camera of the device (front or back)
                 * setBeepEnabled: when true, audible beep occurs when scan occurs
                 * setBarcodeImageEnabled: TODO investigate
                 * initiateScan: open the scanner (after it has been configured)
                 */
                INTEGRATOR.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                INTEGRATOR.setPrompt("Scan the files qr code");
                INTEGRATOR.setCameraId(0);
                INTEGRATOR.setBeepEnabled(false);
                INTEGRATOR.setBarcodeImageEnabled(true);
                INTEGRATOR.initiateScan();
            }
        });
    }


    /**
     * encodeAsBitmap
     *
     * Takes a string and returns a bitmap representation of the string as a qr code
     *
     * @param stringToConvert: the string to generate a qr code for
     * @return a bitmap representing the qr code generated for the passed string
     * @throws WriterException
     */
    Bitmap encodeAsBitmap(String stringToConvert) throws WriterException {
        // TODO investigate
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(stringToConvert, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
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

    /**
     * encodeHeader
     *
     * Takes a file and its position and returns an ArrayList of bytes representing
     * 1. File size
     * 2. File Type
     * 3. Hash value
     * 4. Position
     *
     * @param file: the data to be used
     * @return An ArrayList of buytes to be used as the QR code header
     */
    ArrayList encodeHeader(File file) {
        ArrayList<Byte> header = new ArrayList<Byte>();

        //FILE SIZE
        ByteBuffer buffer;
        buffer = ByteBuffer.allocate(8);
        buffer.putLong(file.length());
        Log.d("file length ", String.valueOf(file.length()));
        buffer.flip();
        int length = buffer.remaining();
        for(int i=0;i<length;i++)  {         //Passes individual bytes from byte array into the header list
            header.add(buffer.get(i));
        }
        buffer.clear();

        // FILE TYPE
        // String ext = FilenameUtils.getExtension(file);
        String filename = file.getName();
        String ext = filename.substring(filename.lastIndexOf(".") + 1, filename.length()); // gets the file extension
        byte[] fileType = ext.getBytes();
        for(int i=0;i<fileType.length;i++) {     //Passes individual bytes from byte array into the header list
            header.add(fileType[i]);
        }

        // HASH VALUE
        int h = file.hashCode();
        buffer = ByteBuffer.allocate(4);
        buffer.putInt(h);
        buffer.flip();
        for(int i=0;i<buffer.remaining();i++) {      //Passes individual bytes from byte array into the header list
            header.add(buffer.get(i));
        }
        buffer.clear();

        //NUMBER OF QR CODE
        int qrCodes = splitFileSize(file.length());
        buffer = ByteBuffer.allocate(4);
        buffer.putInt(qrCodes);
        buffer.flip();
        for(int i=0;i<buffer.remaining();i++) {     //Passes individual bytes from byte array into the header list
            header.add(buffer.get(i));
        }
        buffer.clear();

        for(int i=0; i<header.size();i++) {
            Log.d("Header val " + i, String.valueOf(header.get(i)));
        }

        return header;
    }
    /**
     * splitFileSize
     *
     * Takes in a file size and calculates the number of QR codes needed to transfer it.
     *
     * @param size: the size of the file to be transferred.
     * @return the number of QR Codes required.
     */
    int splitFileSize(long size) {
        int qrCodes= 0;
        while(size > MAX_FILE_SIZE) {
            qrCodes++;
            size -= MAX_FILE_SIZE;
        }
        return qrCodes;

    }

    /**
     * onActivityResult
     *
     * Defines what happens when a successful read of a qr code occurs. Right now (at base), when
     * a qr code is successfully scanned, the scanner is exited and the contents of the scan
     * are breifly shown on the device screen TODO update when changes
     *
     * @param requestCode: TODO investigate params
     * @param resultCode: TODO investigate params
     * @param data: TODO investigate params
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("Scan Button", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("Scan Button", "Scanned");
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}