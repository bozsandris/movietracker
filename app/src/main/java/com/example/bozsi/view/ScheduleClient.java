package com.example.bozsi.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;
/***
 * The client which will connect to our service.
 * */
class ScheduleClient {
    /**The hook into our service.*/
    private ScheduleService mBoundService;
    /**The context to start the service in.*/
    private Context mContext;
    /**A flag if we are connected to the service or not.*/
    private boolean mIsBound;

    /**Gets the context in which we will start the service.
     * @param context  context*/
    public ScheduleClient(Context context) {
        mContext = context;
    }

    /***
     * Call this to connect your activity to your service.
     **/
    public void doBindService() {
        // Establish a connection with our service
        mContext.bindService(new Intent(mContext, ScheduleService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
        Log.i("Service","Activity connected to the service");
    }

    /***
     * When you attempt to connect to the service, this connection will be called with the result.
     * If we have successfully connected we instantiate our service object so that we can call methods on it.
     **/
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with our service has been established,
            // giving us the service object we can use to interact with our service.
            mBoundService = ((ScheduleService.ServiceBinder) service).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            mBoundService = null;
        }
    };

    /***
     * Tell our service to set an alarm for the given date.
     * @param c a date to set the notification for
     **/
    public void setAlarmForNotification(Calendar c){
        mBoundService.setAlarm(c);
        Log.i("Service","Service is setting alarm to the specified date");
    }

    /***
     * When you have finished with the service,call this method to stop it
     * releasing your connection and resources.
     **/
    public void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            mContext.unbindService(mConnection);
            mIsBound = false;
            Log.i("Service","Service unbound");
        }
    }
}
