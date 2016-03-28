package soft.swenggroup5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Sam on 28/03/2016.
 */
public class TextDecodeActivity extends AppCompatActivity {

    private static final boolean DEBUG = false;
    ReceivedData data = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_decode);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            String scannedData = extras.getString("scanned_data");
            Log.d("onCreate_CDA", "Scanned data: " + scannedData);

            try {
                data =  DecoderUtils.decodeFile(scannedData);
                data.printData();

                String fileData = DecoderUtils.getFileData(scannedData);


                TextView fileName = (TextView) findViewById(R.id.textEntry);
                fileName.setText("Encoded text: '" + data.toString());
//
//                TextView contactNumber = (TextView) findViewById(R.id.contactNumber);
//                contactName.setText("Contact: " + ContactSelectActivity.CONTACT_NAME);
            }
            catch (Exception e) {
                if (DEBUG) Log.e("onCreate", e.toString());

                TextView fileName = (TextView) findViewById(R.id.textEntry);
                fileName.setText("QR code incorrectly read. Please scan again.");
            }


        }

        Button scanAgain = (Button) findViewById(R.id.scanAgain);
        Button saveText = (Button) findViewById(R.id.saveText);

        scanAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                startActivity(new Intent(TextDecodeActivity.this, CameraActivity.class));
                TextDecodeActivity.this.finish();
            }
        });
        saveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                if(data != null) {
                    data.saveData(TextDecodeActivity.this);
                }else{
                    Toast toast = Toast.makeText(
                            TextDecodeActivity.this,
                            "QR code incorrectly read.\nPlease scan again.",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}