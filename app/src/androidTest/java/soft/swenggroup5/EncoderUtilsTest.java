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

@RunWith(AndroidJUnit4.class)
@SmallTest
public class EncoderUtilsTest {

    // ======================================
    // Begin Tests for getMimeType(File file)
    // There are 3 tests
    // ======================================

    @Test
    public void test_getMimeType_onNullInput() {
        assertEquals(null, EncoderUtils.getMimeType(null));
    }

    @Test
    public void test_getMimeType_onInvalidInput() throws IOException {
        File testInvalidFileNoType = File.createTempFile("test_null_file", null);
        testInvalidFileNoType.deleteOnExit();
        assertEquals(null, EncoderUtils.getMimeType(testInvalidFileNoType));
    }

    @Test
    public void test_getMimeType_onValidInput() throws IOException {
        File testTxtFile = File.createTempFile("test_txt_file", ".txt");
        testTxtFile.deleteOnExit();
        assertEquals("text/plain", EncoderUtils.getMimeType(testTxtFile));

        File testPngFile = File.createTempFile("testing_png_file", ".png");
        testPngFile.deleteOnExit();
        assertEquals("image/png", EncoderUtils.getMimeType(testPngFile));

        File testContactFile = File.createTempFile("testing_contact_file", "."+ContactData.FILE_EXTENSION);
        testContactFile.deleteOnExit();
        assertEquals(ContactData.FILE_EXTENSION, EncoderUtils.getMimeType(testContactFile));
    }

    // ======================================
    // Begin Tests for encodeHeader(java.io.File file)
    //
    // ======================================

    @Test
    public void test_encodeHeader_null()throws IOException {
        assertEquals(null, EncoderUtils.encodeHeader(null));
    }

    @Test
    public void test_encodeHeader_validInput()throws IOException {
        File testTxtFile = File.createTempFile("test", ".txt");
        testTxtFile.deleteOnExit();
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
        int j =0;
        int k = 0;

        int l = 123;                                                           //Replaces random bytes in array exp with the correct hash code value
        for(int i=13;j<b.length;i++) {
            exp[i]= b[j++];
        }
        for(int i=0; i<exp.length;i++) {                                    //Adds the array to a List used for comparison
            expected.add(exp[i]);
        }
        assertEquals(expected, EncoderUtils.encodeHeader(testTxtFile));
    }

    @Test
    public void test_encodeHeader_invalidInput() throws IOException {

        File testInvalidFileNoType = File.createTempFile("test_null_file", null);
        testInvalidFileNoType.deleteOnExit();
        assertEquals(null, EncoderUtils.encodeHeader(testInvalidFileNoType));
    }
}