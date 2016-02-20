package soft.swenggroup5;

import android.content.Context;
import android.content.Intent;

/**
 * ReceivedData
 *
 * Interface that will be implemented by classes that are created from decoding
 * QR codes (e.g. ContactData for decoded contacts, CalendarData for decoded calendar
 * events). Will be used in the Android Activity where the user is asked if they
 * wish to save the just transferred file, and to save it if so.
 *
 */
public interface ReceivedData {

    /**toString
     *
     * @return a "short" string that describes the received data
     */
    public String toString();

    /**
     * saveData
     *
     * saves the data held in the ReceivedData implementer
     * to the current device. How the implementers save data
     * may be very different, with some opening new Intents and
     * others just interacting with the file system.
     *
     * @param context the context ("the calling Activity") where this method
     *                is called. Needed to interact with much of the Android
     *                File and Intent systems.
     */
    public void saveData(Context context);

    /**
     * printData
     *
     * A debug method for printing out the current state of the object.
     *
     */
    public void printData();

}
