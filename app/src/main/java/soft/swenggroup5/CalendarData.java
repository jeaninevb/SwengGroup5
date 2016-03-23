package soft.swenggroup5;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.CalendarContract;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * An implementation of ReceivedData for saving Calendar Event Info to a user's device
 */
public class CalendarData implements ReceivedData {

    private boolean DEBUG = true;
    /*
            As it is unsure what the Calendar QR codes will actually
            transfer CalendarData will for the moment have the ability
            to transfer as much data as possible
     */
    //All the possible values that a calendar event can hold
    private String title;
    private String location;
    private String description;
    //Use the GeorgianCalendar class to get the values begin/EndTime should be
    private long beginTime = Long.MIN_VALUE;
    private long endTime = Long.MIN_VALUE;
    private boolean allDay; //does the event last all day
    private int accessLevel = Integer.MIN_VALUE; //If synched with google calendar, should this event be shown, can either
                                                //+ be Events.ACCESS_PRIVATE or Events.ACCESS_PUBLIC
    private int availability = Integer.MIN_VALUE;//If synched with google calendar, should the account owner be shown as
                                                 //+ available or busy during the event's duration
    private String recuranceRule; //How should this event recur, i.e. is it a weekly event, hourly...
                                  //+ leave null for no recurs, recurs must be given in RFC 5544 format
    private String attendeeList; //a comma separated list of all the attendees emails


    @Override
    public void saveData(Context context) {
        if (Build.VERSION.SDK_INT >= 14) {
            //An intent that opens the default calendar app on the 'add Event' activity. The fields in the
            //+ activity are given default values based on the data added to the Intent
            Intent intent = new Intent(Intent.ACTION_INSERT).setData(CalendarContract.Events.CONTENT_URI);
            if(title!=null)                 intent.putExtra(CalendarContract.Events.TITLE, title);
            if(location!=null)              intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location);
            if(description!= null)          intent.putExtra(CalendarContract.Events.DESCRIPTION, description);
            if(beginTime!=Long.MIN_VALUE)   intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime);
            if(endTime!=Long.MIN_VALUE)     intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime);
                                            intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, allDay);
            if(accessLevel!=Integer.MIN_VALUE)intent.putExtra(CalendarContract.Events.ACCESS_LEVEL, accessLevel);
            if(availability!=Integer.MIN_VALUE)intent.putExtra(CalendarContract.Events.AVAILABILITY, availability);
            if(recuranceRule!=null)         intent.putExtra(CalendarContract.Events.RRULE, recuranceRule);
            if(attendeeList!=null)          intent.putExtra(Intent.EXTRA_EMAIL, attendeeList);
            context.startActivity(intent);
        }
        else {
            //Similar to the code in the above 14 SDK version however
            Calendar cal = Calendar.getInstance();
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setType("vnd.android.cursor.item/event");
            if(title!=null)                     intent.putExtra("title", title);
            //if(location!=null) //Don't know the needed key to store this info in the intent yet
            if(description!= null)              intent.putExtra("description", description);
            if(beginTime!=Long.MIN_VALUE)       intent.putExtra("beginTime", beginTime);
            if(endTime!=Long.MIN_VALUE)         intent.putExtra("endTime", endTime);
                                                intent.putExtra("allDay", allDay);
            if(accessLevel!=Integer.MIN_VALUE);
            if(availability!=Integer.MIN_VALUE) intent.putExtra("visibility", availability);
            if(recuranceRule!=null)             intent.putExtra("rrule", recuranceRule);
            //if(attendeeList!=null) //Don't know the needed key to store this info in the intent yet
            context.startActivity(intent);
        }

    }

    @Override
    public Intent TEST_saveData(Context context) {
        return null;
    }

    @Override
    public void printData() {
       if(DEBUG) Log.d("CalendarData.printData","Title: "+title);
    }

    @Override
    public String toString(){
        return ("Calendar Event"+(title==null?"":": "+title));
    }

    //a method so that we can get a full event for testing without having to fill out the details everytime
    public static CalendarData testFactory(){
        CalendarData cd = new CalendarData();
        cd.title = "Test Calendar Event Title";
        cd.location = "Test Calendar Event Location";
        cd.description = "Test Calendar Event Description";
        cd.beginTime = new GregorianCalendar(2016, 4, 22).getTimeInMillis(); //this will start at midnight
        cd.endTime = cd.beginTime + ( 60 * 60 * 1000 * 4 ); //lasts four hours
        cd.allDay = false;
        cd.accessLevel = CalendarContract.Events.ACCESS_DEFAULT;
        cd.availability= CalendarContract.Events.AVAILABILITY_BUSY;
        //recurance List not given as when a recurring event does not display in the calendar as normal, so awkward for tests
        //cd.recuranceRule = "FREQ=WEEKLY"; //a weekly event
        cd.attendeeList = "test1@gmail.com, test2@hotmail.com, test3@example.com";
        return cd;
    }
}
