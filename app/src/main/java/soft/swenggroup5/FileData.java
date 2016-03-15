package soft.swenggroup5;

import android.content.Context;
import android.content.Intent;

/**
 * Created by CrusaderCrab on 15/03/2016.
 */
public class FileData implements ReceivedData {

    private final String fileData;
    private final String fileExtension;

    public FileData(String data, String fileExtension){
        this.fileData = data;
        this.fileExtension = fileExtension;
        if( fileExtension == null){
            throw new java.lang.NullPointerException("EncodeUtil probably encoded a null mime");
        }
    }

    @Override
    public void saveData(Context context) {

    }

    @Override
    public Intent TEST_saveData(Context context) {
        return null;
    }

    @Override
    public void printData() {

    }
}
