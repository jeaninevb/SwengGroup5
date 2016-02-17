package soft.swenggroup5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileOutputStream;

import java.util.ArrayList;
import java.util.List;


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
    private final static String STR = "Software Engineering Group 5 - SOFT";
    private final IntentIntegrator INTEGRATOR = new IntentIntegrator(this);
    private final ContactSelectActivity CONTACT_SCREEN = new ContactSelectActivity();
    private File TEST_FILE;


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
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the scan button referencing the button in res/activity_main.xml
<<<<<<< HEAD
        Button scanButton = (Button)findViewById(R.id.generate);
        Button contactButton= (Button)findViewById(R.id.contactButton);


//        // Create a space that will be used to present the demo generated qr code
//        ImageView imageView = (ImageView) findViewById(R.id.qrCode);
//
//        // Attempt to generate the qr code and put it into the ImageView
//        try {
//            Bitmap bitmap = encodeAsBitmap(STR);
//            imageView.setImageBitmap(bitmap);
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//        unnecessary at the moment

//        /** Called when the user clicks the open contacts button */
//        public void openContacts(View view) {
//            // Do something in response to button
//
//                Intent intent = new Intent(this, ContactSelectActivity.class);
//
//        }
//
=======
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
            Log.d("Write_to_temp", e.toString());
        }

        // TODO Attempt to generate the qr code and put it into the ImageView

>>>>>>> BackEnd
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
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View w) {
                startActivity(new Intent(MainActivity.this, ContactSelectActivity.class));
            }
        });
    }
    /**
     * encodeFile
     *
     * Takes a file and returns a List of bytes representing the data
     *
     * @param file: The data to be converted to QR Code
     * @return a List of Bytes
     */
    List<Byte> encodeFile(File file) {
        if(file!=null || file.length()!=0) {
            List<Byte> b = new ArrayList<Byte>(EncoderUtils.encodeHeader(file));
            b.addAll(EncoderUtils.getFileBytes(file));
            return b;
        }
        return null;
    }
    /**
     * onActivityResult
     *
     * Defines what happens when a successful read of a qr code occurs. Right now (at base), when
     * a qr code is successfully scanned, the scanner is exited and the contents of the scan
     * are briefly shown on the device screen TODO update when changes
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
                Log.d("Scan_Button", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("Scan_Button", "Scanned");
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
