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

/**
 * Created by jvictoriab on 2/23/16.
 */
public class ContactEncodeActivity  extends AppCompatActivity {

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sending_codes);

        Button newContact = (Button) findViewById(R.id.selectNewContact);
        Button mainMenu = (Button) findViewById(R.id.mainMenu);

        newContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                startActivity(new Intent(ContactEncodeActivity.this, ContactSelectActivity.class));
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
                //startActivity(new Intent(ContactEncodeActivity.this, MainActivity.class));
            }
        });
        previousCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                //startActivity(new Intent(ContactEncodeActivity.this, MainActivity.class));
            }
        });







        File contactFile = ContactSelectActivity.DATA_FILE;
        try {

            ImageView contactQRCode = (ImageView) findViewById(R.id.imageView2);
            contactQRCode.setImageBitmap(
                    EncoderUtils.generateQRCodeBitmap(
                            EncoderUtils.encodeFile(contactFile)

                    )
            );

            contactQRCode.setAdjustViewBounds(true); //allow alteration to ImageViews size/scale
            contactQRCode.setScaleType(ImageView.ScaleType.FIT_CENTER);//scale as large as possible while still inside parent
            TextView contactName = (TextView) findViewById(R.id.contactName);
            contactName.setText("Contact: " + ContactSelectActivity.CONTACT_NAME);

/*            //textView for order of code pages
            TextView currentCode = (TextView) findViewById(R.id.currentCode);
            currentCode.setText("1/1");*/



        } catch (WriterException e) {
            Log.e("onActivityResult", e.toString());
        }


    }
}


        // Jeanine Burke:
        // Lingfeng, go to res/activity_contact_encode.xml and insert "Done" and "Select New Contact" buttons
        // each with specific IDs
        // Here, Create the "Done" button referencing the button ID in res/activity_contact_encode.xml
        // Create the "Select New Contact" button referencing the button ID in res/activity_contact_encode.xml
        // Look at MainActivity.java to compare.
