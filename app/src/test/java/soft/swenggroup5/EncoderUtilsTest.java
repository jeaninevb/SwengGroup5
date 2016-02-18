package soft.swenggroup5;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    public void test_getFileBytes_onNullInput(){
        assertEquals(null, EncoderUtils.getFileBytes(null));
    }

    @Test
    public void test_getFileBytes_onInvalidInput() throws IOException {
        File testInvalidFile = File.createTempFile("test_null_file", null);
        testInvalidFile.deleteOnExit();
        assertEquals(new ArrayList<Byte>(), EncoderUtils.getFileBytes(testInvalidFile));
    }

    @Test
    public void test_getFileBytes_onValidInput() throws IOException {
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
        List<Byte> expected = new ArrayList<Byte>();
        expected.add((byte) '*');
        expected.add((byte) 't');
        expected.add((byte) 'e');
        expected.add((byte) 's');
        expected.add((byte) 't');
        expected.add((byte) '*');
        assertEquals(expected, EncoderUtils.getFileBytes(testValidTxtFile));


    }

    @Test
    public void test_byteListToString_onNullInput() {
        assertEquals(null, EncoderUtils.byteListToString(null));
    }

    @Test
    public void test_byteListToString_onInvalidInput() {
        List<Byte> testList = new ArrayList<Byte>();
        assertEquals("", EncoderUtils.byteListToString(testList));
    }

    @Test
    public void test_byteListToString_onValidInput() {
        List<Byte> testList = new ArrayList<Byte>();
        testList.add((byte)'A');
        testList.add((byte)'B');
        testList.add((byte)'C');
        assertEquals("ABC", EncoderUtils.byteListToString(testList));
    }
}

