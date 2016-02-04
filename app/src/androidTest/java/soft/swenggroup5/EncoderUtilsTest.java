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

    // ======================================
    // Begin Tests for getMimeType(File file)
    // There are 3 tests
    // ======================================

    /**
     * test_getMimeType_onNullInput
     *
     * Test getMimeType with a null input
     */
    @Test
    public void test_getMimeType_onNullInput() {
        assertEquals(EncoderUtils.getMimeType(null), null);
    }

    /**
     * test_getMimeType_onInvalidInput
     *
     * Test getMimeType with an invalid file input
     *
     * @throws IOException: if temp file not created successfully
     */
    @Test
    public void test_getMimeType_onInvalidInput() throws IOException {
        File testInvalidFileNoType = File.createTempFile("test_null_file", null);
        testInvalidFileNoType.deleteOnExit();
        assertEquals(EncoderUtils.getMimeType(testInvalidFileNoType), null);

        // The below throws a null pointer exception within File. This input cannot be created
        // File testInvalidFileNoName = File.createTempFile(null, ".txt");
    }

    /**
     * test_getMimeType_onValidInput
     *
     * Test getMimeType with a valid File
     *
     * @throws IOException: if temp file not created successfully
     */
    @Test
    public void test_getMimeType_onValidInput() throws IOException {
        File testTxtFile = File.createTempFile("test_txt_file", ".txt");
        testTxtFile.deleteOnExit();
        assertEquals(EncoderUtils.getMimeType(testTxtFile), "text/plain");

        File testPngFile = File.createTempFile("testing_png_file", ".png");
        testPngFile.deleteOnExit();
        assertEquals(EncoderUtils.getMimeType(testPngFile), "image/png");
    }

    // ======================================
    // Ending Tests for getMimeType(File file)
    // ======================================
}