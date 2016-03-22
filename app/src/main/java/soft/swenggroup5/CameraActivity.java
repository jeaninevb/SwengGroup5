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
    private int TOTAL_QR_CODES=-1;                          //Set to unreachable value
    private int CURRENT_INDEX=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        scannedStrings = new ArrayList<String>();
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
        INTEGRATOR.setPrompt("Scan the files qr code");
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
        //if cancelled go back to home screen
        if(resultCode == RESULT_CANCELED ){
            finish();
            return;
        }
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String qrString = result.getContents();
            if (qrString == null) {
                Log.d("onActivityResult", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else if( newQRCode(qrString) ){ //if a new QR code add to list of codes
                Log.d("onActivityResult", "Scanned");
                addQRtoList(qrString);
                if(finalQRReceived(qrString)) { //if this was the final QR code,
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
        if(DecoderUtils.getQRCodeIndex(input)==CURRENT_INDEX+1) {   //If it is next qr code add
            CURRENT_INDEX++;
            scannedStrings.add(input);
        }
    }

    //checks if the passed qr code has not already been scanned and stored
    //TODO: Actual Implementation
    private boolean newQRCode(String input){
        if(DecoderUtils.getQRCodeIndex(input)==0) {
            TOTAL_QR_CODES = DecoderUtils.getTotalQRCodeNumber(input);
            CURRENT_INDEX = 0;
            return true;
        }
        return false;
    }

    //checks if the passed qr code is the last code in a file transfer
    //TODO: Actual Implementation
    private boolean finalQRReceived(String input){
        if(DecoderUtils.getQRCodeIndex(input)==TOTAL_QR_CODES && CURRENT_INDEX==TOTAL_QR_CODES) {
            return true;
        }
        return false;
    }

    //returns all the codes as a single string
    //TODO: Actual Implementation
    private String getTotalData(){
        StringBuilder data = new StringBuilder();
        for(int i=0; i<scannedStrings.size();i++){
            data.append(scannedStrings.get(i));
        }
        return data.toString();
    }


}
