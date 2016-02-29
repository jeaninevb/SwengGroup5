package soft.swenggroup5;


import android.provider.ContactsContract;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

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

    private static final String encodedContactData = "ContactData1272593348.condata~72~condata~1552044068~1\0#Test Contact#N#?#test@test.com#E#H#123 456 789#P#H#Test Street, USA#A#?";
    private static final String encodedContactHeader = "ContactData1272593348.condata~72~condata~1552044068~1";
    private static final String encodedContactFileData = "#Test Contact#N#?#test@test.com#E#H#123 456 789#P#H#Test Street, USA#A#?";

    @Test
    public void test_getHeader_onValidInput(){
        String encoded = encodedContactData;
        String resHeader = DecoderUtils.getHeader(encoded);
        assertEquals(resHeader, encodedContactHeader);
    }

    @Test
    public void test_getFileData_onValidInput(){
        String encoded = encodedContactData;
        String resFileData = DecoderUtils.getFileData(encoded);
        assertEquals(resFileData, encodedContactFileData);
    }

    @Test
    public void test_decodeHeader_onValidInput(){
        Hashtable<String,String> values = DecoderUtils.decodeHeader(encodedContactHeader);
        assertEquals( "ContactData1272593348.condata", values.get("File Name"));
        assertEquals( "72", values.get("File Length"));
        assertEquals( "condata", values.get("Mime Type"));
        assertEquals( "1552044068", values.get("Hash Code"));
        assertEquals( "1", values.get("Number of QR Codes"));
    }

    @Test
    public void test_decodeFile_onValidInput() throws IOException{
        ReceivedData resData = DecoderUtils.decodeFile(encodedContactData);
        assertEquals("Test Contact" + " Contact Data", resData.toString());
        ContactData exp = new ContactData();
        exp.addPhoneNumber("123 456 789", ContactsContract.CommonDataKinds.Phone.TYPE_HOME);
        exp.addPostalAddress("Test Street, USA");
        exp.addEmail("test@test.com", ContactsContract.CommonDataKinds.Email.TYPE_HOME);
        exp.addName("Test Contact");
        assertEquals(exp, resData);
    }

}
