package com.example.bozsi.schoolproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
/***
 * Class which sets an alarm which will pop our notification in the right time.
 * */
class AlarmTask implements Runnable {
    /**The date selected for the alarm.*/
    private final Calendar date;
    /**The android system alarm manager.*/
    private final AlarmManager am;
    /**Your context to retrieve the alarm manager from.*/
    private final Context context;

    /**Class constructor.
     * @param context  context
     * @param date  date when the alarm will trigger*/
    public AlarmTask(Context context, Calendar date) {
        this.context = context;
        this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.date = date;
    }

    @Override
    public void run() {
        // Request to start our service when the alarm date is upon us
        // We don't start an activity as we just want to pop up a notification into the system bar
        Intent intent = new Intent(context, NotifyService.class);
        intent.putExtra(NotifyService.INTENT_NOTIFY, true);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        // Sets an alarm - note this alarm will be lost if the phone is turned off and on again
        am.set(AlarmManager.RTC, date.getTimeInMillis(),pendingIntent);
        Log.i("Alarm","Alarm which will trigger the notification is configured");
    }
}
