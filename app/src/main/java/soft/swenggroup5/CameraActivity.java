package soft.swenggroup5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

/**
 * CameraActivity
 *
 * Uses ZXING library to be able to scan an arbitary number of QR codes.
 *
 */
public class CameraActivity extends AppCompatActivity {

    private ArrayList<String> scannedStrings;
    private final IntentIntegrator INTEGRATOR = new IntentIntegrator(this);
    private boolean firstCodeReceived = false;
    private int currentIndex = 1;
    private int totalCodes = 0;
    private boolean DEBUG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        scannedStrings = new ArrayList<String>();
        if(DEBUG) Log.d("onCreate_CA", "Starting camera scan activity");
        startCameraScan();
    }

    /**
     * startCameraScan()
     *
     * Starts up a ZXING QR scan activity which will attempt to scan a QR code and return it
     * through the onActivityResult() callback.
     *
     */
    private void startCameraScan(){
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
        INTEGRATOR.setPrompt("Scan the file's QR code.");
        INTEGRATOR.setCameraId(0);
        INTEGRATOR.setBeepEnabled(false);
        INTEGRATOR.setBarcodeImageEnabled(true);
        INTEGRATOR.initiateScan();
    }

    /**
     * onActivityResult
     *
     * The callback method that is called when the ZXING scanning activity returns. If the activity
     * was cancelled then this activity ends. If the scanning process got a QR code, it is added to
     * the list of qr codes (if it is not already there). If the qr code was the last code needed A
     * DecodeActivity is started and all the scanned QR codes are passed to it.
     *
     * @param requestCode: Needed to pass to the intent integrator and super class
     * @param resultCode: Reports how the scan went, was it successful, was it cancelled
     * @param data: Needed to pass to the intent integrator and super class
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("onActivityResult_CA", "Begun result handling");
        //if cancelled go back to home screen
        if(resultCode == RESULT_CANCELED ){
            Log.d("onActivityResult_CA", "resultCode is RESULT_CANCELED");
            finish();
            return;
        }
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String qrString = result.getContents();
            Log.d("TAG", "qrString: " + qrString);
            if (qrString == null) {
                if (DEBUG) Log.d("onActivityResult", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else if( newQRCode(qrString) ){ //if a new QR code add to list of codes
                if (DEBUG) Log.d("onActivityResult", "Scanned new String: " + qrString);
                addQRtoList(qrString);
                if(finalQRReceived(qrString)) { //if this was the final QR code
                    Intent showScannedContact = new Intent(CameraActivity.this, ContactDecodeActivity.class);
                    showScannedContact.putExtra("scanned_data", getTotalData());
                    startActivity(showScannedContact);
                }else{ //if not final qr, keep scanning
                    startCameraScan();
                }
            } else{ //if just scanned in old QR, scan again.
                startCameraScan();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //adds the just scanned in QR code to the list of codes. May possibly remove headers of codes
    //TODO: Actual Implementation
    private void addQRtoList(String input){
            currentIndex++;
            scannedStrings.add(input);
    }

    //checks if the passed qr code has not already been scanned and stored
    //TODO: Actual Implementation
    private boolean newQRCode(String input){
        int index = DecoderUtils.getQRCodeIndex(input);
        if(!firstCodeReceived && index==1){
            firstCodeReceived = true;
            totalCodes = DecoderUtils.getTotalQRCodeNumber(input);
            return true;
        }
        return (firstCodeReceived && index==currentIndex);

    }

    //checks if the passed qr code is the last code in a file transfer
    //TODO: Actual Implementation
    private boolean finalQRReceived(String input){
        return (firstCodeReceived && (currentIndex-1)==totalCodes);
    }

    //returns all the codes as a single string
    //TODO: Actual Implementation
    private String getTotalData(){
        return DecoderUtils.combineQRCodes(scannedStrings);
    }


}
