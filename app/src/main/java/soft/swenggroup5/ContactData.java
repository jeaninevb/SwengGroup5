package soft.swenggroup5;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * ContactData
 *
 * Implements ReceivedData
 *
 * A class used to hold data on a Contact and convert it to and from a text file format. The class can
 * also produce an Intent that will insert a Contact into the Android device using the default Contacts
 * App.
 *
 * At the moment ContactData can only store the Contact name, phone number(s), email(s) and postal
 * address.
 *
 */
public class ContactData implements ReceivedData{

    public static final String FILE_EXTENSION = "condata";
    public static final char DELIMITER = '#'; //delimiter in the text file ContactData creates/reads
    private ArrayList<ContactTriplet> data; //holds all the data about acontact, ContactTriplet is defined below


    /**
     * ContactData
     *
     * Constructs an empty ContactData. Add data to this with the below 'add' methods and then convert
     * it to a file using toFile(). In the SOFT app, this will be used in the Sender code as described above.
     */
    public ContactData(){
        data = new ArrayList<ContactTriplet>();
    }

    /**
     * ContactData
     *
     * Creates a ContactData instance holding the data held in the given file. In the SOFT app this will
     * be used in the Receiver code once the QR codes are decoded.
     *
     * @param scanner : a scanner containing contact data. Must be constructed using a file
     *                in the format of files created by ContactData's toFile() method
     * @throws java.io.IOException :  from creating a Scanner using a File
     */
    public ContactData(Scanner scanner){
        data = new ArrayList<ContactTriplet>();
        scanner.useDelimiter(""+DELIMITER);
        while(scanner.hasNext()){
            String stringData = scanner.next();
            char mime = scanner.next().charAt(0); //can't read single chars so read as string and get first char
            char metaData = scanner.next().charAt(0);
            data.add(new ContactTriplet(stringData,mime,metaData));
        }
    }

    /**
     * ContactData
     *
     * create a Contact Data based off a uri that holds a path into the Contacts "database" in the
     * Android device
     *
     * @param contactUri : the path to the start point of a Contact in the Contact "Database"
     * @param context    : needed to query the database in the Android device
     */
    public ContactData(Uri contactUri, Context context){
        data = new ArrayList<ContactTriplet>();

        // handle contact results
        String name;    // the chosen contacts name
        String mime;    // the id of the piece of data in the contact we are looking at
        int dataIdx;    // Index of DATA1 column
        int data2Idx;   // Index of DATA2 column
        int mimeIdx;    // Index of MIMETYPE column
        int nameIdx;    // Index of DISPLAY_NAME column
        /*
        Contacts are stored in a "database" that we query and then traverse using Cursors
        First we get the name of the chosen contact
         */
        Cursor cursor = context.getContentResolver().query(contactUri, //start query here
                new String[]{ContactsContract.Contacts.DISPLAY_NAME}, //return table with one column, the name
                null, null, null); //no filter, sorting

        //move Cursor to first element in returned table, will be name of chosen contact
        if (cursor.moveToFirst()) {
            //Get Column index that holds names, will be zero but this is less fragile code
            nameIdx = cursor.getColumnIndex(
                    ContactsContract.Contacts.DISPLAY_NAME);
            name = cursor.getString(nameIdx); //get the string at row 0, DISPLAY_NAME column
            addName(name);
                    /*
                    Now that we have the name we query the contact "database" again, filtering results
                    to only return results on the same row as our chosen contacts name

                     */
            // Set up the projection, the projection describes what data to return and how to order it
            // in the the created "table"
            String[] projection = {
                    ContactsContract.Data.DISPLAY_NAME, //Name
                    ContactsContract.Contacts.Data.DATA1, //Generic data piece (will be phone or email)
                    ContactsContract.Contacts.Data.DATA2, //Meta info on DATA1 (e.g. is home, work)
                    ContactsContract.Contacts.Data.MIMETYPE}; //Describes what is stored in DATA1 (i.e. PHONE or EMAIL)

            // Query ContactsContract.Data
            cursor = context.getContentResolver().query(
                    ContactsContract.Data.CONTENT_URI, //start query here
                    projection,
                    ContactsContract.Data.DISPLAY_NAME + " = ?",
                    new String[]{name},
                    null);

            //move to start of created result table
            if (cursor.moveToFirst()) {
                // Get the indexes of the MIME type and data1 and data2
                mimeIdx = cursor.getColumnIndex(
                        ContactsContract.Contacts.Data.MIMETYPE);
                dataIdx = cursor.getColumnIndex(
                        ContactsContract.Contacts.Data.DATA1);
                data2Idx = cursor.getColumnIndex(
                        ContactsContract.Contacts.Data.DATA2);

                // Match the data to the MIME type, store in variables
                do {
                    mime = cursor.getString(mimeIdx);
                    if (ContactsContract.CommonDataKinds.Email
                            .CONTENT_ITEM_TYPE.equalsIgnoreCase(mime)) {
                        String email = cursor.getString(dataIdx);
                        int emailType = cursor.getInt(data2Idx);
                        addEmail(email, emailType);
                    }
                    if (ContactsContract.CommonDataKinds.Phone
                            .CONTENT_ITEM_TYPE.equalsIgnoreCase(mime)) {
                        String phone = cursor.getString(dataIdx);
                        int phoneType = cursor.getInt(data2Idx);
                        addPhoneNumber(phone, phoneType);
                    }
                    if (ContactsContract.CommonDataKinds.StructuredPostal.
                            CONTENT_ITEM_TYPE.equalsIgnoreCase(mime)) {
                        String postal = cursor.getString(dataIdx);
                        addPostalAddress(postal);
                    }

                } while (cursor.moveToNext());
            }
        }
    }

    /**
     * toString (from ReceivedData)
     *
     * @return  a string that can be used to identify (but not uniquely) the data
     *          contained in the called ContactData.
     */
    public String toString(){
        String name = "Unknown";
        for(ContactTriplet triplet : data)
            if(triplet.getMime() == ContactTriplet.NAME)
                name = triplet.getData();
        return name + " Contact Data";
    }

    //For debugging purposes
    //TODO: Remove this method (must also be removed from ReceivedData)
    public void printData(){
        for(ContactTriplet ct : data){
            Log.d("ContactData_printData ",ct.getData()+" "+ct.getMime()+" "+ct.getMetaData());
        }
    }

    /**
     * saveData (from ReceivedData)
     *
     * Inserts the Contact data on the device the app is running. Does this
     * by starting an instance of the default Contact App with the Intent created
     * by getInsertIntent
     *
     * @param context the context ("the calling Activity") where this method
     *                is called. Needed to interact with much of the Android
     */
    public void saveData(Context context){
        context.startActivity( getInsertIntent(context));
    }
    /**
     * getInsertIntent
     *
     * Creates an Intent that will open the default Contact App and try
     * to insert a contact based on the info in the ContactData object this is called on.
     *
     *
     * At the moment it can only add at most 3 of the same type of data, i.e. only 3 phone numbers,
     * only 3 email addresses ...etc
     *
     * @param context : The currently activity Activity object
     *
     * @return intent : the created Intent
     */
    private Intent getInsertIntent(Context context){
        //creates an Intent that will open the default Contact app to insert a new contact
        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

        //now we pass default data for the new contact
        int phoneNumberCount = 0;
        int emailCount = 0;
        for(ContactTriplet ct : data){
            if(ct.getMime() == ContactTriplet.PHONE){
                intent.putExtra(getCurrentPhoneExtraConstant(phoneNumberCount), ct.getData());
                switch(ct.metaData){//pass what type of phone number you just added
                    case ContactTriplet.HOME:
                        intent.putExtra(getCurrentPhoneTypeExtraConstant(phoneNumberCount),
                                ContactsContract.CommonDataKinds.Phone.TYPE_HOME);
                        break;
                    case ContactTriplet.WORK:
                        intent.putExtra(getCurrentPhoneTypeExtraConstant(phoneNumberCount),
                                ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
                        break;
                    case ContactTriplet.OTHER:
                        intent.putExtra(getCurrentPhoneTypeExtraConstant(phoneNumberCount),
                                ContactsContract.CommonDataKinds.Phone.TYPE_OTHER);
                        break;
                    default: //unknown type, will be given default unused type (usually HOME or OTHER)
                }
                phoneNumberCount++;
            }else if(ct.getMime() == ContactTriplet.EMAIL){
                intent.putExtra(getCurrentEmailExtraConstant(emailCount), ct.data);
                switch(ct.metaData){
                    case ContactTriplet.HOME:
                        intent.putExtra(getCurrentEmailTypeExtraConstant(emailCount),
                                ContactsContract.CommonDataKinds.Email.TYPE_HOME);
                        break;
                    case ContactTriplet.WORK:
                        intent.putExtra(getCurrentEmailTypeExtraConstant(emailCount),
                                ContactsContract.CommonDataKinds.Email.TYPE_WORK);
                        break;
                    case ContactTriplet.OTHER:
                        intent.putExtra(getCurrentEmailTypeExtraConstant(emailCount),
                                ContactsContract.CommonDataKinds.Email.TYPE_OTHER);
                        break;
                    default:
                        break;
                }
                emailCount++;
                //no meta data given for name or Address for now.
            }else if(ct.getMime() == ContactTriplet.NAME){
                intent.putExtra(ContactsContract.Intents.Insert.NAME, ct.data);
            }
            else if(ct.getMime() == ContactTriplet.POSTAL){
                intent.putExtra(ContactsContract.Intents.Insert.POSTAL, ct.data);
            }
        }
        return intent;
    }

    /**
     * getCurrentPhoneExtraConstant
     *
     * For a phone number to be passed to the "Insert Contact" Intent in the getInsertIntent()
     * method it must be passed with the correct String ID, and for each distinct phone number added
     * a different ID string must be used. This method returns the id strings.
     * @param phoneNumberCount :  How many phone numbers have already been added to the Intent
     * @return : the correct String ID to pass the phone number to the Contact Intent without overriding
     *           any previous phone numbers passed.
     */
    private String getCurrentPhoneExtraConstant(int phoneNumberCount){
        if(phoneNumberCount==0) return ContactsContract.Intents.Insert.PHONE;
        if(phoneNumberCount==1) return ContactsContract.Intents.Insert.SECONDARY_PHONE;
        return ContactsContract.Intents.Insert.TERTIARY_PHONE;
    }

    /**
     * getCurrentPhoneTypeExtraConstant
     *
     * Similar to getCurrentPhoneExtraConstant but this returns the string ID relating to
     * the meta-data (e.g. WORK number, HOME number) corresponding to the phone numbers passed
     * to the "Insert Contact" Intent created by getInsertIntent()
     *
     * @param phoneNumberCount : How many phone numbers have already been added to the Intent
     * @return : the String ID for meta-data relating to the "phoneNumberCounth" phone number
     */
    private String getCurrentPhoneTypeExtraConstant(int phoneNumberCount){
        if(phoneNumberCount==0) return ContactsContract.Intents.Insert.PHONE_TYPE;
        if(phoneNumberCount==1) return ContactsContract.Intents.Insert.SECONDARY_PHONE_TYPE;
        return ContactsContract.Intents.Insert.TERTIARY_PHONE_TYPE;
    }

    /**
     * getCurrentEmailExtraConstant
     *
     * Same as getCurrentPhoneExtraConstant but for Email's.
     *
     * @param emailCount
     * @return
     */
    private String getCurrentEmailExtraConstant(int emailCount){
        if(emailCount==0) return ContactsContract.Intents.Insert.EMAIL;
        if(emailCount==1) return ContactsContract.Intents.Insert.SECONDARY_EMAIL;
        return ContactsContract.Intents.Insert.TERTIARY_EMAIL;
    }

    /**
     * getCurrentEmailTypeExtraConstant
     *
     * Same as getCurrentPhoneTypeExtraConstant but for Email's.
     *
     * @param emailCount
     * @return
     */
    private String getCurrentEmailTypeExtraConstant(int emailCount){
        if(emailCount==0) return ContactsContract.Intents.Insert.EMAIL_TYPE;
        if(emailCount==1) return ContactsContract.Intents.Insert.SECONDARY_EMAIL_TYPE;
        return ContactsContract.Intents.Insert.TERTIARY_EMAIL_TYPE;
    }

    /**
     * toFile
     *
     * Creates a text file containing all the data held within the ContactData instance this
     * method is called upon. The file created can be passed to a ContactData constructor and it will
     * then contain all the data that was held inside the given file.
     *
     * @param context : The currently active Activity Object, needed to get the path location
     *                  of where this app should store files
     * @return : a text file containing the data held in the ContactData instance
     *           this method is called upon
     * @throws java.io.IOException : From construction of FileWriter
     */
    public File toFile(Context context) throws java.io.IOException{
        //create the Contact file
        File outputDir = context.getCacheDir(); // get folder path for this app
        //createTempFile makes a file with a random filename, but we must still delete the file later ourselves
        File outputFile = File.createTempFile("ContactData", "."+FILE_EXTENSION, outputDir);
        outputFile.deleteOnExit();
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        //write the content of Contact to the file
        for(ContactTriplet triplet : data){
            writer.write(DELIMITER);
            writer.write(triplet.getData());
            writer.write(DELIMITER);
            writer.write(triplet.getMime());
            writer.write(DELIMITER);
            writer.write(triplet.getMetaData());
        }
        writer.close();

        return outputFile;
    }


    /**
     * addEmail
     * used to add Email data to the ContactData instance this is called upon
     * @param address : the email address as a string
     * @param type : meta-data on the passed email Address, it must be one of the TYPE constants
     *               held in "ContactsContract.CommonDataKinds.Email" e.g.
     *               ContactsContract.CommonDataKinds.Email.TYPE_WORK
     */
    public void addEmail(String address, int type){
        switch(type){
            case ContactsContract.CommonDataKinds.Email.TYPE_WORK:
                data.add(new ContactTriplet(address, ContactTriplet.EMAIL, ContactTriplet.WORK));
                break;
            case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
                data.add(new ContactTriplet(address, ContactTriplet.EMAIL, ContactTriplet.HOME));
                break;
            case ContactsContract.CommonDataKinds.Email.TYPE_OTHER:
                data.add(new ContactTriplet(address, ContactTriplet.EMAIL, ContactTriplet.OTHER));
                break;
            default:
                data.add(new ContactTriplet(address, ContactTriplet.EMAIL, ContactTriplet.NO_DATA));
                break;
        }
    }

    /**
     * addEmail
     * used to add Email data to the ContactData instance this is called upon. The address is stored
     * with no meta-data.
     * @param address : the email address as a string
     */
    public void addEmail(String address){
        data.add(new ContactTriplet(address, ContactTriplet.EMAIL, ContactTriplet.NO_DATA));
    }

    /**
     * addPhoneNumber
     * used to add a phone number to the ContactData instance this is called upon
     * @param number : the phone number as a string, it may contain dashes or spaces
     * @param type  : meta-data on the passed phone number, it must be one of the TYPE constants
     *               held in "ContactsContract.CommonDataKinds.Phone" e.g.
     *               ContactsContract.CommonDataKinds.Phone.TYPE_WORK
     */
    public void addPhoneNumber(String number, int type){
        switch(type){
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                data.add(new ContactTriplet(number, ContactTriplet.PHONE, ContactTriplet.WORK));
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                data.add(new ContactTriplet(number, ContactTriplet.PHONE, ContactTriplet.HOME));
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                data.add(new ContactTriplet(number, ContactTriplet.PHONE, ContactTriplet.MOBILE));
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                data.add(new ContactTriplet(number, ContactTriplet.PHONE, ContactTriplet.OTHER));
                break;
            default:
                data.add(new ContactTriplet(number, ContactTriplet.PHONE, ContactTriplet.NO_DATA));
                break;
        }
    }

    /**
     * addPhoneNumber
     * used to add a phone number to the ContactData instance this is called upon. The phone number
     * is stored with no meta-data
     * @param number : the phone number as a string, it may contain dashes or spaces
     */
    public void addPhoneNumber(String number) {
        data.add(new ContactTriplet(number, ContactTriplet.PHONE, ContactTriplet.NO_DATA));
    }

    /**
     * addName
     * used to add a contact name to the ContactData instance this is called upon. The name
     * is stored with no meta-data
     * @param name : The name as a string, it may contain any characters bar DELIMITER
     */
    public void addName(String name){
        data.add(new ContactTriplet(name, ContactTriplet.NAME, ContactTriplet.NO_DATA));
    }

    /**
     * addPostalAddress
     * used to add a postal address to the ContactData instance this is called upon. The address
     * is stored with no meta-data
     * @param address : The address as a String, it may contain any characters bar DELIMITER
     */
    public void addPostalAddress(String address){
        data.add(new ContactTriplet(address, ContactTriplet.POSTAL, ContactTriplet.NO_DATA));
    }

    @Override
    public boolean equals(Object y){
        if(y==this) return true;
        if(y==null) return false;
        if(this.getClass() != y.getClass()) return false;
        ContactData that = (ContactData)y;
        if(data.size() != that.data.size()) return false;
        for(int i = 0; i < data.size(); i++){
            if(!data.get(i).equals(that.data.get(i))) return false;
        }
        return true;
    }

    /**
     * ContactTriplet
     *
     * A "struct" class used to hold a single piece of data about a Contact.
     *
     */
    class ContactTriplet
    {
        public static final char PHONE = 'P';
        public static final char EMAIL = 'E';
        public static final char NAME = 'N';
        public static final char NO_DATA = '?';
        public static final char WORK = 'W';
        public static final char HOME = 'H';
        public static final char MOBILE = 'M';
        public static final char OTHER = 'O';
        public static final char POSTAL = 'A';


        String data;
        char mime;
        char metaData;

        public ContactTriplet(String data, char mime, char metaData)
        {
            this.data = data;
            this.mime = mime;
            this.metaData = metaData;
        }

        String getData(){ return data;}
        char getMime(){ return mime;}
        char getMetaData(){ return metaData;}

        @Override
        public boolean equals(Object y){
            if(y==this) return true;
            if(y==null) return false;
            if(this.getClass() != y.getClass()) return false;
            ContactTriplet that = (ContactTriplet)y;
            if(!data.equals(that.data)) return false;
            if(mime != that.mime) return false;
            if(metaData != that.metaData) return false;
            return true;
        }
    }

    /**
     * TEST_saveData
     *
     * to be able to do Instrumentation test with artificial input access to the current
     * Intent is needed. This method just returns the Intent that is usually called within
     * saveData(context)
     *
     * @param context needed to interact with Android system
     * @return intent : the created Intent that will open the default contacts app to insert
     *                  a new contact
     */
    public Intent TEST_saveData(Context context){
        return getInsertIntent(context);
    }
}
