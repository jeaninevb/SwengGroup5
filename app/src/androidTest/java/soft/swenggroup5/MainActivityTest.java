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

    // ======================================
    // Begin Tests for encodeHeader(java.io.File file)
    // There is 1 test (More will be added)
    // ======================================

    /**
     * test_encodeHeader_null()
     *
     * Test encodeHeader(File file) with a null input
     */
    @Test
    public void test_encodeHeader_null()throws IOException {
        assertEquals(MainActivity.encodeHeader(null),null);
    }
    /**
     * test_encodeHeader_validInput()
     *
     * Test encodeHeader(File file) with a valid input
     */
    @Test
    public void test_encodeHeader_validInput()throws IOException {
        File testTxtFile = File.createTempFile("test", ".txt");                  //Create a temporary test file
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

        List<Byte> expected = new ArrayList<Byte>();                    //Assign expected values
        byte[] exp = {(byte) 54, (byte) 124, (byte) 116, (byte) 101, (byte) 120,
                (byte) 116, (byte) 47, (byte) 112, (byte) 108, (byte) 97,
                (byte) 105, (byte) 110, (byte) 124, (byte) 45, (byte) 50, (byte) 49,
                (byte) 51, (byte) 52, (byte) 48, (byte) 51, (byte) 52, (byte) 57,
                (byte) 49, (byte) 124, (byte) 49, (byte) 0};
        byte[] b;
        do {                                                             //the method .hashCode() can return different values for the same file
            int hashCode = testTxtFile.hashCode();                       //therefore conditions have been added to ensure it matches the one generated
            b = String.valueOf(hashCode).getBytes();                     //by encodeHeader().
        }while(b.length!=10);

        int j =0;                                                            //Replaces random bytes in array exp with the correct hash code value
        for(int i=13;j<b.length;i++) {
            exp[i]= b[j++];
        }
        for(int i=0; i<exp.length;i++) {                                    //Adds the array to a List used for comparison
            expected.add(exp[i]);
        }
        assertEquals(MainActivity.encodeHeader(testTxtFile),expected);
    }
    /**
     * test_encodeHeader_invalidInput()
     *
     * Test encodeHeader(File file) with an invalid input
     */
    @Test
    public void test_encodeHeader_invalidInput() throws IOException {

        File testInvalidFileNoType = File.createTempFile("test_null_file", null);
        testInvalidFileNoType.deleteOnExit();
        assertEquals(MainActivity.encodeHeader(testInvalidFileNoType), null);
    }

    // ======================================
    // Ending Tests for encodeHeader(File file)
    // ======================================
}
