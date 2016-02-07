package soft.swenggroup5;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * MainActivityTest
 *
 * Test class for the MainActivity class
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class MainActivityTest {
    @Test
    public void test_encodeHeader()throws IOException {
        File testTxtFile = File.createTempFile("test", ".txt");
        testTxtFile.deleteOnExit();
        // write hello to the temp file
        FileOutputStream s = new FileOutputStream(testTxtFile);
        s.write('*');
        s.write('t');
        s.write('e');
        s.write('s');
        s.write('t');
        s.write('*');


        s.close();

        List<Byte> expected = new ArrayList<Byte>();
        byte[] exp = {(byte) 54, (byte) 124, (byte) 116, (byte) 101, (byte) 120,
                (byte) 116, (byte) 47, (byte) 112, (byte) 108, (byte) 97,
                (byte) 105, (byte) 110, (byte) 124, (byte) 45, (byte) 50, (byte) 49,
                (byte) 51, (byte) 52, (byte) 48, (byte) 51, (byte) 52, (byte) 57,
                (byte) 49, (byte) 124, (byte) 49, (byte) 0};
        byte[] b;
        do {
            int x = testTxtFile.hashCode();
            b = String.valueOf(x).getBytes();
        }while(b.length!=10);

        int j =0;
        for(int i=13;j<b.length;i++) {
            exp[i]= b[j++];
        }
        for(int i=0; i<exp.length;i++) {
            expected.add(exp[i]);
        }
       assertEquals(MainActivity.encodeHeader(testTxtFile),expected);
    }

}
