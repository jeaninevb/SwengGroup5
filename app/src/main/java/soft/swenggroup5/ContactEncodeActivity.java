package soft.swenggroup5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;

import java.io.File;
import java.util.List;

/**
 * Created by jvictoriab on 2/23/16.
 */
public class ContactEncodeActivity  extends AppCompatActivity {

    private static final boolean DEBUG = false;
    public static final String FILE_NAME_KEY = "soft.swenggroup5.EncodeActivity.FILENAMEKEY";
    public static final String FILE_PATH_KEY = "soft.swenggroup5.EncodeActivity.FILEPATHKEY";

    public ImageView contactQRCode;
    public List<String> qrCodes;
    public int index;
    public int maxQrCodes;
    public TextView currentCode;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sending_codes);

        Log.d("onCreate_CEA", "On create");

        Button newContact = (Button) findViewById(R.id.selectNewContact);
        newContact.setText("Back");
        Button mainMenu = (Button) findViewById(R.id.mainMenu);

        contactQRCode = (ImageView) findViewById(R.id.imageView2);

        newContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                finish();
            }
        });
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                startActivity(new Intent(ContactEncodeActivity.this, MainActivity.class));
            }
        });


        //previous and next buttons for sending_codes.xml    haven't finished the onClick part
        Button nextCode = (Button) findViewById(R.id.nextCode);
        Button previousCode = (Button) findViewById(R.id.previousCode);

        nextCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                try {
                    Log.d("TAG", "Encoded into a qr code: " + qrCodes.get(index));
                    if(index < maxQrCodes) {
                        contactQRCode.setImageBitmap(EncoderUtils.generateQRCodeBitmap(qrCodes.get(index++)));
                        currentCode.setText((index) + "/" + maxQrCodes);
                    }
                }
                catch (Exception e) {}
                //startActivity(new Intent(ContactEncodeActivity.this, MainActivity.class));
            }
        });
        previousCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                try {
                    Log.d("TAG", "Encoded into a qr code: " + qrCodes.get(index));
                    if((index - 1) > 0) {
                        contactQRCode.setImageBitmap(EncoderUtils.generateQRCodeBitmap(qrCodes.get(--index - 1)));
                        currentCode.setText((index) + "/" + maxQrCodes);
                    }
                }
                catch (Exception e) {}
                //startActivity(new Intent(ContactEncodeActivity.this, MainActivity.class));
            }
        });

        //The intent passed on startActivity
        Intent receivedIntent = getIntent();
        String filePath = receivedIntent.getStringExtra(FILE_PATH_KEY);
        File contactFile = new File(filePath);
        try {
            qrCodes = EncoderUtils.encodeFileToQRStrings(contactFile);
            index = 0;
            Log.d("TAG", "Encoded into a qr code: " + qrCodes.get(index));
            contactQRCode.setImageBitmap(EncoderUtils.generateQRCodeBitmap(qrCodes.get(index++)));

            contactQRCode.setAdjustViewBounds(true); //allow alteration to ImageViews size/scale
            contactQRCode.setScaleType(ImageView.ScaleType.FIT_CENTER);//scale as large as possible while still inside parent
            TextView contactName = (TextView) findViewById(R.id.contactName);
            contactName.setText("Item: " + receivedIntent.getStringExtra(FILE_NAME_KEY));

            //textView for order of code pages
            currentCode = (TextView) findViewById(R.id.currentCode);
            maxQrCodes = (qrCodes.size());
            currentCode.setText("1/" + maxQrCodes);



        } catch (WriterException e) {
            if (DEBUG) Log.e("onActivityResult", e.toString());
        }


    }
}
