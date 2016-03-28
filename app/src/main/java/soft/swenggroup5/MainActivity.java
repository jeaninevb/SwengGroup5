package soft.swenggroup5;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final boolean DEBUG = false;

    /**
     * Constants
     *
     * INTEGRATOR: Object that is used to access the integrated scanner
     */
    private final IntentIntegrator INTEGRATOR = new IntentIntegrator(this);
    private boolean hasPermissions;
    private String[] neededPermissions = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 81;

    /**
     * onCreate
     *
     * Entry point for app when started. Initializes the generated qr code, the scan button,
     * and attaches a listener to the scan button.
     *
     * @param savedInstanceState: required param to instantiate the super class
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * Create buttons and their event listeners
         */
        Button scanButton = (Button)findViewById(R.id.generate);
        Button contactButton = (Button)findViewById(R.id.contactButton);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermissions();
                if(hasPermissions) {
                    startCameraScan();
                }else{
                    displayToast("Permission have not yet been granted.");
                }
            }
        });

        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                getPermissions();
                if (hasPermissions) {
                    Log.d("onCreate_MA", "Send button clicked");
                    startActivity(new Intent(MainActivity.this, EncodeOptionsActivity.class));
                }else{
                    displayToast("Permission have not yet been granted.");
                }
            }
        });
    }

    private void displayToast(String text){
        Toast toast = Toast.makeText(
                MainActivity.this,
                text,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * getPermissions
     *
     * checks if an app has the needed permissions and updates 'hasPermissions' accordingly. If this is run
     * on a pre API 23 device it will just set 'hasPermissions' to true. If run on an API 23 device onwards, this
     * method will open the runtime permission request windows if permissions are missing. If permissions are
     * permanently denied displays a warning.
     *
     */
    private void getPermissions(){
        boolean needToShowRationale = false; //is there any permission that has been permanently denied / first time
        ArrayList<String> neededPerms = new ArrayList<String>(); //list of not-granted permissions
        for(String currentPermission : neededPermissions){//go through all possible permissions
            //get current permission state for this permission
            int hasThisPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                    currentPermission);
            if(hasThisPermission != PackageManager.PERMISSION_GRANTED)
            {
                neededPerms.add(currentPermission);
                //is this permission permanently denied / first time ask
                if(!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        currentPermission)) {
                    needToShowRationale = true;
                }
            }
        }
        //if list is empty, no needed permissions, ie all permissions already granted
        if(neededPerms.size() <= 0){
            hasPermissions = true;
            return;
        }
        final String [] permissionList = neededPerms.toArray(new String[1]);
        if(needToShowRationale) {
            //make message box explaining reason these permissions are needed, opens permission box if 'ok' pressed
            showMessageOKCancel("Please allow access to all these functions",
                    new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    permissionList,
                                    REQUEST_CODE_ASK_PERMISSIONS);
                        }
                    }
            );
        }else{ //not all permissions granted, but none permanently denied
            ActivityCompat.requestPermissions(MainActivity.this,
                    permissionList,
                    REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    /**
     * onRequestPermissionResult
     *
     * The callback return method when a "Request Permission" dialog window closes.
     *
     * @param requestCode : the code passed on the dialog window's creation
     * @param permissions : the list of permissions passed to the dialog window on it's creation
     * @param grantResults : the resulting state of the permissions in the previous parameter
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                //go through all permission statuses, if any still denied mark hasPermissions = false
                for(Integer result : grantResults){
                    if(result != PackageManager.PERMISSION_GRANTED){
                        hasPermissions = false;
                        return;
                    }
                }
                hasPermissions = true;
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //Used to create a dialog window to display a warning when permissions are permanently denied.
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void startCameraScan(){
        Intent cameraIntent = new Intent(MainActivity.this, CameraActivity.class);
        startActivity(cameraIntent);
    }

    private void startContactSelect(){
        // switch to the contact select activity
        startActivity(new Intent(MainActivity.this, ContactSelectActivity.class));
    }
}
