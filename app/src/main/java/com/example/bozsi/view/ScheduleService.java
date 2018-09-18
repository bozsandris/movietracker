package com.example.bozsi.view;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;
/**
 * Class for clients to access.
 */
public class ScheduleService extends Service {
    /**
     * Object which gets the scheduleservice.
     */
    public class ServiceBinder extends Binder {
        /**Constructor.
         * @return object*/
        ScheduleService getService() {
            return ScheduleService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("ScheduleService", "Received start id " + startId + ": " + intent);

        // We want this service to continue running until it is explicitly stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**This is the object that receives interactions from clients.*/
    private final IBinder mBinder = new ServiceBinder();

    /**
     * Show an alarm for a certain date when the alarm is called it will pop up a notification.
     * @param c date when the alarm will be triggered
     */
    public void setAlarm(Calendar c) {
        // This starts a new thread to set the alarm
        // You want to push off your tasks onto a new thread to free up the UI to carry on responding
        new AlarmTask(this, c).run();
        Log.i("Alarm","New thread is started to set the alarm");
    }
}
