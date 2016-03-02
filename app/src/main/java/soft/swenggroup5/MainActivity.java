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

import java.util.Hashtable;


public class MainActivity extends AppCompatActivity {

    /**
     * Constants
     *
     * INTEGRATOR: Object that is used to access the integrated scanner
     */
    private final IntentIntegrator INTEGRATOR = new IntentIntegrator(this);

    /**
     * onCreate
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
        /**
         * Create buttons and their event listeners
         */
        Button scanButton = (Button)findViewById(R.id.generate);
        Button contactButton = (Button)findViewById(R.id.contactButton);

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
                 * setBarcodeImageEnabled: Set to true to enable saving the barcode image and
                 * sending its path in the result Intent.
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
                // switch to the contact select activity
                startActivity(new Intent(MainActivity.this, ContactSelectActivity.class));
            }
        });
    }

    /**
     * onActivityResult
     *
     * Defines what happens when a successful read of a qr code occurs. Right now (at base), when
     * a qr code is successfully scanned, the scanner is exited and the contents of the scan
     * are briefly shown on the device screen
     *
     * TODO the body of this function will need to be moved to DecodeUtils
     *
     * @param requestCode: Needed to pass to the intent integrator and super class
     * @param resultCode: Needed to pass to the intent integrator and super class
     * @param data: Needed to pass to the intent integrator and super class
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
                try {
                    Hashtable<String, String> ht = DecoderUtils.decodeHeader(result.getContents());
                    for(String k : ht.keySet()) {
                        Toast.makeText(this, k + ":"+ ht.get(k), Toast.LENGTH_LONG).show();
                        Thread.sleep(1000);
                    }

                }
                catch (Exception e) {}
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
