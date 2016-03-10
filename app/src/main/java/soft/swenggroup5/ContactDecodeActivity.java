package soft.swenggroup5;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Tomas on 10/03/2016.
 */
public class ContactDecodeActivity extends AppCompatActivity {

    ContactData data = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_decode);


        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String scannedData = extras.getString("scanned_data");

            try {
                data = (ContactData) DecoderUtils.decodeFile(scannedData);
                data.printData();

                String fileData = DecoderUtils.getFileData(scannedData);


                TextView contactName = (TextView) findViewById(R.id.contactName);
                contactName.setText("Contact: " + fileData);
//
//                TextView contactNumber = (TextView) findViewById(R.id.contactNumber);
//                contactName.setText("Contact: " + ContactSelectActivity.CONTACT_NAME);
            }
            catch (Exception e) {
                Log.e("onCreate", e.toString());
            }


        }

        Button scanAgain = (Button) findViewById(R.id.scanAgain);
        Button saveContact = (Button) findViewById(R.id.saveFile);

        scanAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                // TODO placeholder for now
            }
        });
        saveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                data.saveData(getApplicationContext());
            }
        });

    }

}
