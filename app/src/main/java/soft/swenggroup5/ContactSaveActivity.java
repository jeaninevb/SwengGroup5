package soft.swenggroup5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by McGroarty on 07/03/2016.
 */
public class ContactSaveActivity  extends AppCompatActivity {

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_save);

        Button newContact = (Button) findViewById(R.id.selectNewContact);
        Button mainMenu = (Button) findViewById(R.id.mainMenu);

        newContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                startActivity(new Intent(ContactSaveActivity.this, ContactSelectActivity.class));
            }
        });
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                startActivity(new Intent(ContactSaveActivity.this, MainActivity.class));
            }
        });

        String data = getIntent().getExtras().getString("DATA");
        Log.d("Data: ", data);
        try {
            DecoderUtils.decodeFile(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
