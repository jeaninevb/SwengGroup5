package soft.swenggroup5;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Tomas on 10/03/2016.
 */
public class ContactDecodeActivity extends AppCompatActivity {

    private static final boolean DEBUG = false;
    ReceivedData data = null;
    public static String type = null;
    Button saveContact;
    Button scanAgain;
    String fileData;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_decode);

        Bundle extras = getIntent().getExtras();


        if(extras != null) {
            String scannedData = extras.getString("scanned_data");
            Log.d("onCreate_CDA", "Scanned data: " + scannedData);

            try {
                data =  DecoderUtils.decodeFile(scannedData);

                fileData = DecoderUtils.getFileData(scannedData);

                scanAgain = (Button) findViewById(R.id.scanAgain);
                saveContact = (Button) findViewById(R.id.saveFile);

                Log.d("onCreate_CDA", "File data: " + fileData);
                if (type.equals("Text Data")) {
                    Log.d("onCreate_CDA", "Is a text data");
                    saveContact.setText("Copy");
                    Log.d("onCreate_CDA", "Is a text data");

                    TextView fileName = (TextView) findViewById(R.id.contactName);
                    fileName.setText(data.toString() + " " + fileData);
                }

                else {
                    TextView fileName = (TextView) findViewById(R.id.contactName);
                    fileName.setText(data.toString());
                }

//
//                TextView contactNumber = (TextView) findViewById(R.id.contactNumber);
//                contactName.setText("Contact: " + ContactSelectActivity.CONTACT_NAME);
            }
            catch (Exception e) {
                if (DEBUG) Log.e("onCreate", e.toString());

                TextView fileName = (TextView) findViewById(R.id.contactName);
                fileName.setText("QR code incorrectly read. Please scan again.");
            }


        }


        scanAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                startActivity(new Intent(ContactDecodeActivity.this, CameraActivity.class));
                ContactDecodeActivity.this.finish();
            }
        });
        saveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                switch (type) {
                    case ("File Data"):
                        if(data != null) {
                            data.saveData(ContactDecodeActivity.this);
                        }else{
                            Toast toast = Toast.makeText(
                                    ContactDecodeActivity.this,
                                    "QR code incorrectly read.\nPlease scan again.",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        break;
                    case("Text Data"):
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("label", fileData);
                        clipboard.setPrimaryClip(clip);
                        Toast textDataToast = Toast.makeText(
                                ContactDecodeActivity.this,
                                "Copied to clipboard",
                                Toast.LENGTH_SHORT);
                        textDataToast.show();

                    case("Contact Data"):
                        if(data != null) {
                            data.saveData(ContactDecodeActivity.this);
                        }else{
                            Toast toast = Toast.makeText(
                                    ContactDecodeActivity.this,
                                    "QR code incorrectly read.\nPlease scan again.",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        break;
                }
            }
        });

    }

}
