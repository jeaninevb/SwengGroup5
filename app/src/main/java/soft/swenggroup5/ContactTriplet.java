package soft.swenggroup5;

/**
 * Created by CrusaderCrab on 10/03/2016.
 */

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ContactTriplet
 *
 * A class used to represent a single piece of data about a Contact.
 * The Triplet holds three pieces of data, the 'main' piece (e.g. the actual phone number if this Triplet
 * represents a phone number). The 'Mime' of the 'main' data, i.e. is the 'main' data a phone number, an
 * email ..etc and 'meta data' which is extra info about 'main'. E.g. is it a home, work or mobile number
 *
 */
final class ContactTriplet
{
    //the valid values for mime
    public static final char PHONE = 'P';
    public static final char EMAIL = 'E';
    public static final char NAME = 'N';
    public static final char NO_DATA = '?';
    public static final char POSTAL = 'A';
    //the valid values for meta data
    public static final char WORK = 'W';
    public static final char HOME = 'H';
    public static final char MOBILE = 'M';
    public static final char OTHER = 'O';
    //used when converting a ContactData to JSON
    public static final String JSON_DATA_KEY = "Data";
    public static final String JSON_MIME_KEY = "Mime";
    public static final String JSON_META_DATA_KEY = "Meta";


    final private String data; //the main piece of data
    final private char mime;  //the type of data 'data' is
    final private char metaData; //extra data on 'data'

    public ContactTriplet(String data, char mime, char metaData)
    {
        this.data = data;
        this.mime = mime;
        this.metaData = metaData;
    }

    public ContactTriplet(JSONObject js) throws JSONException {
        data = js.getString(JSON_DATA_KEY);
        mime = js.getString(JSON_MIME_KEY).charAt(0);
        metaData = js.getString(JSON_META_DATA_KEY).charAt(0);
    }

    String getData(){
        return data;
    }
    char getMime(){
        return mime;
    }
    char getMetaData(){
        return metaData;
    }

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

    public JSONObject toJSONObject() throws JSONException{
        JSONObject js = new JSONObject();
        js.put(JSON_DATA_KEY, data);
        js.put(JSON_MIME_KEY, mime+"");
        js.put(JSON_META_DATA_KEY, metaData+"");
        return js;
    }
}
