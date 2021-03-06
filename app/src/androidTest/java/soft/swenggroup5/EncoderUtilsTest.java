package soft.swenggroup5;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class EncoderUtilsTest{

    @Test
    public void test_getMimeType_onNullInput() {
        assertEquals(null, EncoderUtils.getMimeType(null));
    }

    @Test
    public void test_getMimeType_onInvalidInput() throws IOException {
        File testInvalidFileNoType = File.createTempFile("test_getMimeType_onInvalidInput", null);
        testInvalidFileNoType.deleteOnExit();
        assertEquals(null, EncoderUtils.getMimeType(testInvalidFileNoType));
    }

    @Test
    public void test_getMimeType_onValidInput() throws IOException {
        File testTxtFile = File.createTempFile("test_getMimeType_onValidInput", ".txt");
        testTxtFile.deleteOnExit();
        assertEquals("text/plain", EncoderUtils.getMimeType(testTxtFile));

        File testPngFile = File.createTempFile("test_getMimeType_onValidInput", ".png");
        testPngFile.deleteOnExit();
        assertEquals("image/png", EncoderUtils.getMimeType(testPngFile));

        File testContactFile =
                File.createTempFile("testing_contact_file", "." + ContactData.FILE_EXTENSION);
        testContactFile.deleteOnExit();
        assertEquals(ContactData.FILE_EXTENSION, EncoderUtils.getMimeType(testContactFile));
    }

    @Test
    public void test_generateHeader_null() throws IOException {
        assertEquals(null, EncoderUtils.generateHeader(null));
    }

    @Test
    public void test_generateHeader_validInput() throws IOException {
        File testTxtFile = File.createTempFile("test_encodeHeader_validInput", ".txt");
        testTxtFile.deleteOnExit();
        FileOutputStream s = new FileOutputStream(testTxtFile);
        s.write('*');
        s.write('t');
        s.write('e');
        s.write('s');
        s.write('t');
        s.write('*');
        s.close();

        // expected = "test_encodeHeader_validInput~6~text/plain~hash(file)~1\0"
        String expected = testTxtFile.getName()
                + "~6~text/plain~" + testTxtFile.hashCode() + "~1" + "\0";

        assertEquals(expected, EncoderUtils.generateHeader(testTxtFile));
    }

    @Test
    public void test_generateHeader_invalidInput() throws IOException {

        File testInvalidFileNoType = File.createTempFile("test_encodeHeader_invalidInput", null);
        testInvalidFileNoType.deleteOnExit();

        // expected = "0~null~hash(file)~1\0"
        String expected = testInvalidFileNoType.getName()
                + "~0~null~" + testInvalidFileNoType.hashCode() + "~1" + "\0";

        assertEquals(expected, EncoderUtils.generateHeader(testInvalidFileNoType));
    }
}