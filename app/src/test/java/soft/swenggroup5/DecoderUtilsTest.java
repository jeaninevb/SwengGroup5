package soft.swenggroup5;


import android.provider.ContactsContract;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import static org.junit.Assert.assertEquals;

public class DecoderUtilsTest {

    private static final String CONTACT_TEST_FILE_NAME = "ContactData1272593348.condata";
    private static final String CONTACT_TEST_FILE_LENGTH = "72";
    private static final String CONTACT_TEST_MIME_TYPE = "condata";
    private static final String CONTACT_TEST_HASH_CODE = "1552044068";
    private static final String CONTACT_TEST_NUM_QR_CODES = "1";

    private static final String CONTACT_TEST_GENERATED_HEADER =
            CONTACT_TEST_FILE_NAME + EncoderUtils.DELIMITER
            + CONTACT_TEST_FILE_LENGTH + EncoderUtils.DELIMITER
            + CONTACT_TEST_MIME_TYPE + EncoderUtils.DELIMITER
            + CONTACT_TEST_HASH_CODE + EncoderUtils.DELIMITER
            + CONTACT_TEST_NUM_QR_CODES;

    private static final String CONTACT_TEST_FILE_DATA = "#Test Contact#N#?#test@test.com#E#H#123 456 789#P#H#Test Street, USA#A#?";
    private static final String CONTACT_TEST_ENCODED_DATA =
            CONTACT_TEST_GENERATED_HEADER + EncoderUtils.END_DLIMITER + CONTACT_TEST_FILE_DATA;

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

    @Test
    public void test_getHeader_onValidInput(){
        assertEquals(CONTACT_TEST_GENERATED_HEADER, DecoderUtils.getHeader(CONTACT_TEST_ENCODED_DATA));
    }

    @Test
    public void test_getFileData_onValidInput(){
        assertEquals(CONTACT_TEST_FILE_DATA, DecoderUtils.getFileData(CONTACT_TEST_ENCODED_DATA));
    }

    @Test
    public void test_decodeHeader_onValidInput(){
        Hashtable<String,String> values = DecoderUtils.decodeHeader(CONTACT_TEST_GENERATED_HEADER);
        assertEquals(CONTACT_TEST_FILE_NAME, values.get("File Name"));
        assertEquals(CONTACT_TEST_FILE_LENGTH, values.get("File Length"));
        assertEquals(CONTACT_TEST_MIME_TYPE, values.get("Mime Type"));
        assertEquals(CONTACT_TEST_HASH_CODE, values.get("Hash Code"));
        assertEquals(CONTACT_TEST_NUM_QR_CODES, values.get("Number of QR Codes"));
    }

    @Test
    public void test_decodeFile_onValidInput() throws IOException{
        ReceivedData resData = DecoderUtils.decodeFile(CONTACT_TEST_ENCODED_DATA);
        assertEquals("Test Contact" + " Contact Data", resData.toString());
        ContactData exp = new ContactData();
        exp.addPhoneNumber("123 456 789", ContactsContract.CommonDataKinds.Phone.TYPE_HOME);
        exp.addPostalAddress("Test Street, USA");
        exp.addEmail("test@test.com", ContactsContract.CommonDataKinds.Email.TYPE_HOME);
        exp.addName("Test Contact");
        assertEquals(exp, resData);
    }
}
