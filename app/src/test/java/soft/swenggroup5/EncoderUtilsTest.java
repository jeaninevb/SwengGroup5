package soft.swenggroup5;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class EncoderUtilsTest {

    @Test
    public void test_numberOfQRCodes_onNullInput() {
        // can't occur
    }

    @Test
    public void test_numberOfQRCodes_onInvalidInput() {
        assertEquals(0, EncoderUtils.numberOfQRCodes(-1));
        assertEquals(0, EncoderUtils.numberOfQRCodes(-1000));
    }

    @Test
    public void test_numberOfQRCodes_onValidInput() {
        assertEquals(0, EncoderUtils.numberOfQRCodes(0));
        assertEquals(1, EncoderUtils.numberOfQRCodes(1));
        assertEquals(2, EncoderUtils.numberOfQRCodes(2001));
        assertEquals(2, EncoderUtils.numberOfQRCodes(3999));
        assertEquals(3, EncoderUtils.numberOfQRCodes(4001));
        assertEquals(3, EncoderUtils.numberOfQRCodes(5000));
        assertEquals(5, EncoderUtils.numberOfQRCodes(10000));
    }

    //test null input for getFileBytes
    @Test
    public void test_getFileContents_onNullInput(){
        assertEquals(null, EncoderUtils.getFileContents(null));
    }

    @Test
    public void test_getFileContents_onInvalidInput() throws IOException {
        File testInvalidFile = File.createTempFile("test_null_file", null);
        testInvalidFile.deleteOnExit();
        assertEquals("", EncoderUtils.getFileContents(testInvalidFile));
    }

    @Test
    public void test_getFileContents_onValidInput() throws IOException {
        File testValidTxtFile = File.createTempFile("test_txt_file", ".txt");
        testValidTxtFile.deleteOnExit();
        FileOutputStream s = new FileOutputStream(testValidTxtFile);
        s.write('*');
        s.write('t');
        s.write('e');
        s.write('s');
        s.write('t');
        s.write('*');
        s.close();
        String expected = "*test*";
        assertEquals(expected, EncoderUtils.getFileContents(testValidTxtFile));


    }
}

