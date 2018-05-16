package com.example.bozsi.schoolproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * This service is started when an Alarm has been raised.
 *
 * We pop a notification into the status bar for the user to click on
 * When the user clicks the notification a new activity is opened.
 *
 */
public class NotifyService extends Service{
    /**
     * Class for clients to access.
     */
    public class ServiceBinder extends Binder {
        /**Return the object to our service binder.
         * @return object*/
        NotifyService getService() {
            return NotifyService.this;
        }
    }

    /**List to store film titles.*/
    static List<String> Titles = new ArrayList<>();

    /** Unique id to identify the notification.*/
    private static final int NOTIFICATION = 123;
    /** Name of an intent extra we can use to identify if this service was started to create a notification.*/
    public static final String INTENT_NOTIFY = "com.example.bozsi.schoolproject.INTENT_NOTIFY";
    /** The system notification manager.*/
    private NotificationManager mNM;

    /**Store the title to which we set an alarm.
     * @param title is the title of the film which we get from the
     * Details class when we set a reminder.*/
    public static void addtitle(String title){
        Titles.add(title);
    }

    @Override
    public void onCreate() {
        Log.i("NotifyService", "onCreate()");
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        /** If this service was started by our AlarmTask intent then we want to show our notification.*/
        if(intent.getBooleanExtra(INTENT_NOTIFY, false))
            showNotification();

        /** We don't care if this service is stopped as we have already delivered our notification.*/
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** This is the object that receives interactions from clients. */
    private final IBinder mBinder = new ServiceBinder();

    /**
     * Creates a notification and shows it in the OS drag-down status bar.
     */
    private void showNotification() {
        /***
         * Title of the notification,which we get from our list.
         * We added the title to the list earlier,when we set the reminder.
         * After getting the title we remove it from the list.
         * */
        CharSequence title = Titles.get(Titles.size()-1);
        Titles.remove(Titles.size()-1);
        /** This is the scrolling text of the notification.*/
        CharSequence text = "The film you'd like to see will be presented tomorrow at your local cinema! Go check it out!";
        /** What time to show on the notification.*/
        long time = System.currentTimeMillis();
        /**The activity which the tap on the notification will start.*/
        Intent newintent = new Intent(this, MainActivity.class);
        /**Pass the title of the film to the activity to delete the film from the database.*/
        newintent.putExtra("title", title);
        /** The PendingIntent to launch our activity if the user selects this notification.*/
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, newintent, 0);

        /**Set the options of our notification.*/
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"M_CH_ID")
                //Title
        .setContentTitle(title)
                //Description
        .setContentText(text)
                //Icon
        .setSmallIcon(R.drawable.ic_stat_onesignal_default)
                //Action on click
        .setContentIntent(contentIntent)
                //Can be dismissed or not
        .setOngoing(true)
                //Time
        .setWhen(time)
                //Cancel on tap
        .setAutoCancel(true);

        /** Send the notification to the system.*/
        mNM.notify(NOTIFICATION, builder.build());

        Log.i("Notification","Notification is configured and sent to the system");

        /** Stop the service when we are finished.*/
        stopSelf();
    }
}
