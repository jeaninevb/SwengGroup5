package soft.swenggroup5;


import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class DecoderUtilsTest{


    //The phone number of the test Contact data returned by getExpectedValidContactData()
    private static final String VALID_CONTACT_DATA_ENCODED_PHONE_NUMBER = "123 456 789";

    /**
     * test_decodeFile_Contact_valid
     *
     * tests: DecoderUtils.decodeFileData(data, ContactData.FILE_EXTENSION);
     *
     * Tests if a ContactData that has been encoded is equal to a ContactData constructed
     * from decoding the encoded data.
     *
     */
    @Test
    public void test_decodeFileData_contact_valid(){
        try {
            ContactData original = getExpectedValidContactData();
            Context context = InstrumentationRegistry.getContext();
            File f = original.toFile(context);
            List<Byte> encoded = EncoderUtils.getFileBytes(f);
            ReceivedData decodedData = DecoderUtils.decodeFileData(EncoderUtils.byteListToString(encoded), ContactData.FILE_EXTENSION);
            assertTrue(original.equals(decodedData));
        }catch(Exception e){
            Log.d("dfd_contact_invalid", e.toString());
        }
    }

    /**
     * test_decodeFile_fileExtension_invalid
     *
     * tests: DecoderUtils.decodeFileData(data, INVALID);
     *
     * Tests if decodeFileData will correctly return null when given
     * invalid file extensions.
     *
     */
    @Test
    public void test_decodeFileData_fileExtension_invalid(){
        try {
            ContactData original = getExpectedValidContactData();
            Context context = InstrumentationRegistry.getContext();
            File f = original.toFile(context);
            List<Byte> encoded = EncoderUtils.getFileBytes(f);
            assertTrue(null == DecoderUtils.decodeFileData(EncoderUtils.byteListToString(encoded), "Contact File"));
            assertTrue(null == DecoderUtils.decodeFileData(EncoderUtils.byteListToString(encoded), ""));
            assertTrue(null == DecoderUtils.decodeFileData(EncoderUtils.byteListToString(encoded), null));
            assertTrue(null == DecoderUtils.decodeFileData(EncoderUtils.byteListToString(encoded), "text"));
        }catch(Exception e){
            Log.d("dfd_extension_invalid", e.toString());
        }
    }


    public static String validEncodedContact() throws IOException{
        ContactData c = getExpectedValidContactData();
        List<Byte> encoded = EncoderUtils.encodeFile(c.toFile(InstrumentationRegistry.getContext()));
        return EncoderUtils.byteListToString(encoded);
    }

    //Return a valid test ContactData
    public static ContactData getExpectedValidContactData(){
        ContactData con = new ContactData();
        con.addName("Test Contact");
        con.addEmail("test@test.com", ContactsContract.CommonDataKinds.Email.TYPE_HOME);
        con.addPhoneNumber(VALID_CONTACT_DATA_ENCODED_PHONE_NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE_HOME);
        con.addPostalAddress("Test Street, USA");
        return con;
    }

    //Get the contact data that has just been stored on the device
    public static ContactData getResultValidContactData(Context context){
        //Gets a URI pointing to the beginning of the Contact data that contains the phone
        //+ number VALID_CONTACT_DATA_ENCODED_PHONE_NUMBER, this should be the data just
        //+ inserted into the phone.
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(VALID_CONTACT_DATA_ENCODED_PHONE_NUMBER));
        ContactData con = new ContactData(uri, context);
        return con;
    }

}
