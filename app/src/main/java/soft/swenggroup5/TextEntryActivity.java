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
 * Created by Sam on 21/3/16.
 */
public class TextEntryActivity  extends AppCompatActivity {

    public static final String FILE_NAME_KEY = "soft.swenggroup5.EncodeActivity.FILENAMEKEY";
    public static final String FILE_PATH_KEY = "soft.swenggroup5.EncodeActivity.FILEPATHKEY";

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sending_codes);

        Button textQRCode = (Button) findViewById(R.id.textQRCode);
        Button mainMenu = (Button) findViewById(R.id.mainMenu);

        textQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                startActivity(new Intent(TextEntryActivity.this, ContactSelectActivity.class));
            }
        });
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                startActivity(new Intent(TextEntryActivity.this, MainActivity.class));
            }
        });

        //The intent passed on startActivity
        Intent receivedIntent = getIntent();
        String filePath = receivedIntent.getStringExtra(FILE_PATH_KEY);
        File textFile = new File(filePath);
        try {

            ImageView textQR = (ImageView) findViewById(R.id.imageView2);
            textQR.setImageBitmap(
                    EncoderUtils.generateQRCodeBitmap(
                            EncoderUtils.encodeFile(textFile)

                    )
            );

            textQR.setAdjustViewBounds(true); //allow alteration to ImageViews size/scale
            textQR.setScaleType(ImageView.ScaleType.FIT_CENTER);//scale as large as possible while still inside parent
            TextView textName = (TextView) findViewById(R.id.filenametextedit);
            textName.setText("Text: " + receivedIntent.getStringExtra(FILE_NAME_KEY));

            //textView for order of code pages
            TextView currentCode = (TextView) findViewById(R.id.currentCode);
            currentCode.setText("1/1");



        } catch (WriterException e) {
            Log.e("onActivityResult", e.toString());
        }


    }
}