package soft.swenggroup5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Tomas on 28/03/2016.
 */
public class EncodeOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enocde_choices);

        final String [] choices  = {
                "Contacts",
                "Text Entry",
                "File",
                "Calendar Events"
        };

        //get the listView and give it an adapter that will make it display our files
        ListView list = (ListView)findViewById(R.id.choose_encode_options);
        list.setAdapter(
                new ArrayAdapter<String>(
                        this,
                        R.layout.layout_choices_row, //use this layout as a ListView row
                        R.id.filenametext, //place the String data into this Textview
                        choices) //get info from this array
        );

        //set up ListView OnClick Listener, this is for the row, not the button on the list row
        list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                        Log.d("onCreate_EOA", "Item : " + myItemInt + " clicked");
                        if(myItemInt == 0) {
                            Log.d("onCreate_EOA", "Item : " + myItemInt + " clicked");
                            startActivity(new Intent(EncodeOptionsActivity.this, ContactSelectActivity.class));
                        }
                        else if (myItemInt == 1){
                            Log.d("onCreate_EOA", "Item : " + myItemInt + " clicked");
                            startActivity(new Intent(EncodeOptionsActivity.this, TextEntryActivity.class));
                        }
                        else if (myItemInt == 2) {
                            Log.d("onCreate_EOA", "Item : " + myItemInt + " clicked");
                            startActivity(new Intent(EncodeOptionsActivity.this, FileBrowserActivity.class));
                        }
                        else if( myItemInt == 3) {
                            Log.d("onCreate_EOA", "Item : " + myItemInt + " clicked");
                            // TODO
                        }
                    }
                }
        );

    }
}
