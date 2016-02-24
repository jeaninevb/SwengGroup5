package soft.swenggroup5;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class DecoderUtilsTest {

    @Test
    public void test_validateFile_onNullInput() {
        assertEquals(false, DecoderUtils.validateFile(null, 0));
    }

    @Test
    public void test_validateFile_onInvalidInput() throws IOException {
        File f = File.createTempFile("test_validateFile_onInvalidInput", ".txt");
        f.deleteOnExit();
        assertEquals(false, DecoderUtils.validateFile(f, 0));
    }

    @Test
    public void test_validateFile_onValidInput() throws IOException {
        File f = File.createTempFile("test_validateFile_onValidInput", ".txt");
        f.deleteOnExit();
        assertEquals(true, DecoderUtils.validateFile(f, f.hashCode()));
    }
}
