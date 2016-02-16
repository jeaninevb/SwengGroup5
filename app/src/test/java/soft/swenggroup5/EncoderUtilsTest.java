package soft.swenggroup5;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * TODO temporary storage until we split MainActivity
 */
public class EncoderUtilsTest {

    /**
     *
     */
    @Test
    public void test_splitFileSize() {
        assertEquals(0, EncoderUtils.numberOfQRCodes(-1));
        assertEquals(0, EncoderUtils.numberOfQRCodes(0));
        assertEquals(1, EncoderUtils.numberOfQRCodes(1));
        assertEquals(2, EncoderUtils.numberOfQRCodes(2001));
        assertEquals(2, EncoderUtils.numberOfQRCodes(3999));
        assertEquals(3, EncoderUtils.numberOfQRCodes(4001));
        assertEquals(3, EncoderUtils.numberOfQRCodes(5000));
        assertEquals(5, EncoderUtils.numberOfQRCodes(10000));
    }


    // LINGFENG TODO

    //test null input for getFileBytes
    @Test
    public void test_getFileBytes_onNullInput(){
        assertEquals(null, EncoderUtils.getFileBytes(null));
    }

/*    //test invalid input for getFileBytes
    @Test
    public void test_getFileBytes_onInvalidInput() throws IOException {
        File testInvalidFile = File.createTempFile("test_null_file", null);
        testInvalidFile.deleteOnExit();
        assertEquals(EncoderUtils.getFileBytes(testInvalidFile), null);

    }*/

/*
    @Test
    public void test_getFileBytes_onValidInput() throws IOException {
        File testValidTxtFile = File.createTempFile("test_txt_file", ".txt");
        testValidTxtFile.deleteOnExit();
        assertEquals(EncoderUtils.getFileBytes(testValidTxtFile), "text/plain");

        File testValidPngFile = File.createTempFile("testing_png_file", ".png");
        testValidPngFile.deleteOnExit();
        assertEquals(EncoderUtils.getFileBytes(testValidPngFile), "image/png");

    }
*/

}

