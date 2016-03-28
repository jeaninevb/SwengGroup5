package soft.swenggroup5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Sam on 21/3/16.
 */
public class TextEntryActivity  extends AppCompatActivity {


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_entry);

        final EditText textEntryBox  = (EditText) findViewById(R.id.textinput);

        Button textQRCode = (Button) findViewById(R.id.textQRCode);
        Button mainMenu = (Button) findViewById(R.id.mainMenu);

        textQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                Log.d("onCreate_TEA", "Contents of text entry box:" + textEntryBox.getText().toString());
                String text  = textEntryBox.getText().toString();
                Intent i = new Intent(TextEntryActivity.this, TextEncodeActivity.class);
                i.putExtra("enteredText", text);
                startActivity(i);
            }
        });
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                startActivity(new Intent(TextEntryActivity.this, MainActivity.class));
            }
        });

    }
}