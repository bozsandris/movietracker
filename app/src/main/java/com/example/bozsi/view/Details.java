package com.example.bozsi.view;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import model.Database;
import model.DatabaseReader;
import model.Films;

/***
 * Class which shows the details of the film with a save button.
 * Click on the button creates a database to which we save the film,
 * and sets a reminder to watch the film when it comes out.
 * */
public class Details extends AppCompatActivity {
    DatabaseReader mDbHelper = new DatabaseReader(this);
    /**Textview to show the details of the film.*/
    TextView textView;
    /**Index of a film.*/
    static int index;
    /**Client which connects to our service.*/
    private ScheduleClient scheduleClient;
    /**Object to store our films.*/
    static public List<Films> films = new ArrayList<>();
    /**A value to check if the film is stored.*/
    Boolean saved=false;
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.second_activity);
        //Textview which shows the film's details
        textView = findViewById(R.id.textView);
        //Button to store the film and set a reminder
        final Button save = findViewById(R.id.button2);
        // Create a new service client and bind our activity to this service
        scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();
        //Text with the film's title,overview,release date and average vote
        String text = films.get(index).title+"\n"+films.get(index).overview+"\nRelease date: "+films.get(index).releasedate
                +"\nPopularity: "+films.get(index).popularity;
        //Sets the aligment of the text to center
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        //Sets the size of the text to 22
        textView.setTextSize(22);
        //Sets the textview's text to our text which contains the film's details
        textView.setText(text);
        //Listener to the save button
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Button pressed","Save button pressed");
                ContentValues values = new ContentValues();
                //Save the film's title to the database
                values.put(Database.FeedEntry.Titles, films.get(index).title);
                values.put(Database.FeedEntry.Genreids, films.get(index).genreids);
                values.put(Database.FeedEntry.Overview, films.get(index).overview);
                values.put(Database.FeedEntry.Releasedate, films.get(index).releasedate);
                values.put(Database.FeedEntry.Popularity, films.get(index).popularity);
                if(DatabaseReader.savetitle(films.get(index).title,mDbHelper,values))
                {
                    //Tell the user that the film is saved successfully
                    Toast.makeText(Details.this, "Saved", Toast.LENGTH_LONG).show();
                    Log.i("Database","Film added to database");
                    Setnotification(index);
                    //Disables the save button because the film is saved
                    save.setEnabled(false);
                }
                else{                        //Tell the user that the film is already saved
                    Toast.makeText(Details.this,"Film is already saved",Toast.LENGTH_LONG).show();
                    Log.i("Database","Film is already saved");}
            }
        });
    }
    /***
     * Gets the index of a film and the film object which contains films.
     * Called from the MainActivity.
     * @param i is the index
     * @param films2 film object
     * */
    public static void getIndex(int i,List<Films> films2){
        Log.i("Details","getIndex()");
        index = i;
        films = films2;
    }
    /***
     * Sets a notification at 15 PM one day before the date,on which the film will be released in the cinemas.
     * @param index is the index of the film's position in the lists
     * */
    public void Setnotification(int index){
        //Parsing the presentation date of the film
        String date = films.get(index).releasedate;
        //Year of the date
        String yearstring = date.substring(0,4);
        //Month of the date
        String monthstring = date.substring(5,7);
        //Day of the date
        String daystring = date.substring(8,10);
        //Convert the string to integer
        int day = Integer.parseInt(daystring);
        int month = Integer.parseInt(monthstring);
        int year = Integer.parseInt(yearstring);
        // Create a new calendar and set it to one day before release date at 15pm
        Calendar c = Calendar.getInstance();
        //Months start from 0,so we have to subtract 1
        c.set(year, month-1, 16,9,0,0);
        // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
        scheduleClient.setAlarmForNotification(c);
        //Set our films title as the future notification's title
        NotifyService.addtitle(films.get(index).title);
        // Notify the user what they just did
        Toast.makeText(this, "Notification set for: " + day +"/"+ month +"/"+(year)+" 15:00 PM",Toast.LENGTH_SHORT).show();
        Log.i("Notification","Notification will be set for: " + day +"/"+ month +"/"+(year)+" 15:00 PM");
    }
    /***
     * When the activity is stopped,it stops the connection to the service which helped us set an alarm.
     * This stops us leaking our activity into the system.
     * */
    @Override
    protected void onStop() {
        if(scheduleClient != null)
            scheduleClient.doUnbindService();
        Log.i("Service","Service unbound");
        super.onStop();
    }
}
