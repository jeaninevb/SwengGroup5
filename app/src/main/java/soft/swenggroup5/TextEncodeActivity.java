package soft.swenggroup5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by Sam on 28/3/16.
 */
public class TextEncodeActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sending_codes);

        Button finish = (Button) findViewById(R.id.selectNewContact);
        Button mainMenu = (Button) findViewById(R.id.mainMenu);

        finish.setText("Go back");

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                finish();
            }
        });

        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                startActivity(new Intent(TextEncodeActivity.this, MainActivity.class));
            }
        });


        //previous and next buttons for sending_codes.xml    haven't finished the onClick part
        Button nextCode = (Button) findViewById(R.id.nextCode);
        Button previousCode = (Button) findViewById(R.id.previousCode);

        nextCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                //startActivity(new Intent(ContactEncodeActivity.this, MainActivity.class));
            }
        });
        previousCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                //startActivity(new Intent(ContactEncodeActivity.this, MainActivity.class));
            }
        });

        TextView title = (TextView) findViewById(R.id.contactName);
        title.setText("Your text:");

        //The intent passed on startActivity
        Bundle extras = getIntent().getExtras();
        String textToEncode = extras.getString("enteredText");
        Log.d("onCreate_TEA", "Text to encode: " + textToEncode);

        try {
            File temp = File.createTempFile("EnteredText", ".customtxtentry");
            temp.deleteOnExit();

            FileOutputStream stream = new FileOutputStream(temp);
            try {
                stream.write(textToEncode.getBytes());
            } finally {
                stream.close();
            }

            Log.d("onCreate_TEA", "About to encode file into QR strings");
            List<String> qrCodes = EncoderUtils.encodeFileToQRStrings(temp);
            Log.d("onCreate_TEA", "Finished encoding file into QR strings");
            ImageView contactQRCode = (ImageView) findViewById(R.id.imageView2);
            Log.d("TAG", "Encoded into a qr code: " + qrCodes.get(0));
            contactQRCode.setImageBitmap(EncoderUtils.generateQRCodeBitmap(qrCodes.get(0)));

            contactQRCode.setAdjustViewBounds(true); //allow alteration to ImageViews size/scale
            contactQRCode.setScaleType(ImageView.ScaleType.FIT_CENTER);//scale as large as possible while still inside parent

            //textView for order of code pages
            TextView currentCode = (TextView) findViewById(R.id.currentCode);
            currentCode.setText("1/1");

        }
        catch (Exception e) {}



    }
}


// Jeanine Burke:
// Lingfeng, go to res/activity_contact_encode.xml and insert "Done" and "Select New Contact" buttons
// each with specific IDs
// Here, Create the "Done" button referencing the button ID in res/activity_contact_encode.xml
// Create the "Select New Contact" button referencing the button ID in res/activity_contact_encode.xml
// Look at MainActivity.java to compare.
