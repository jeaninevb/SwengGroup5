package soft.swenggroup5;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

/**
 * EncoderUtilsTest
 *
 * Test class for the EncoderUtils class
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class EncoderUtilsTest {

    /**
     * test_getMimeType
     *
     * Test for getMimeType(File file).
     * Test consists of various temp files being created and checking that getMimeType returns
     * the expected MIME type for the created files
     *
     * Remember to explicitly specify that the temp file is to be deleted on exit
     *
     * @throws IOException: if temp file could not be created
     */
    @Test
    public void test_getMimeType() throws IOException {
        File testTxtFile = File.createTempFile("test_txt_file", ".txt");
        testTxtFile.deleteOnExit();
        assertEquals(EncoderUtils.getMimeType(testTxtFile), "text/plain");


        File testPngFile = File.createTempFile("testing_png_file", ".png");
        testPngFile.deleteOnExit();
        assertEquals(EncoderUtils.getMimeType(testPngFile), "image/png");

    }
}