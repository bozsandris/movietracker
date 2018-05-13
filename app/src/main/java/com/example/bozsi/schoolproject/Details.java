package com.example.bozsi.schoolproject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/***
 * Class which shows the details of the film with a save button.
 * Click on the button creates a database to which we save the film,
 * and sets a reminder to watch the film when it comes out.
 * */
public class Details extends AppCompatActivity {
    /**Accessing the database by instantiating subclass of SQLiteOpenHelper.*/
    DatabaseReader mDbHelper = new DatabaseReader(this);
    /**Textview to show the details of the film.*/
    TextView textView;
    /**Index of a film.*/
    static int index;
    /**Client which connects to our service.*/
    private ScheduleClient scheduleClient;
    /**Object to store our films.*/
    static Films films = new Films();
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
        String text = films.title.get(index)+"\n"+films.overview.get(index)+"\nRelease date: "+films.releasedate.get(index)+"\nAverage vote: "+films.voteavg.get(index);
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
                /***
                 * Open our database for reading.
                 * */
                SQLiteDatabase db = mDbHelper.getReadableDatabase();
                String[] projection = {
                        BaseColumns._ID,
                        Database.FeedEntry.Titles
                };
                /***
                 * Sql query to check if the film is already stored in our database.
                 * Searches the film's title in the database.
                 * */
                String selection = Database.FeedEntry.Titles + "= ?";
                //Selection arguments (the film's title in this case)
                String[] selectionArgs = {films.title.get(index)};
                //Cursor interface provides read-write access to our database
                Cursor cursor = db.query(
                        Database.FeedEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,

                        null,
                        null,
                        null
                );
                /***
                 * If the database is empty we save the film and set a reminder one day before
                 * the film's release date.
                 * */
                if(cursor.getCount()==0) {
                    //Open the database for writing
                    db = mDbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    //Save the film's title to the database
                    values.put(Database.FeedEntry.Titles, films.title.get(index));
                    long newRodId = db.insert(Database.FeedEntry.TABLE_NAME, null, values);
                    //Tell the user that the film is saved successfully
                    Toast.makeText(Details.this, "Saved", Toast.LENGTH_LONG).show();
                    //Set a notification to the film
                    Setnotification(index);
                }
                else {
                    //Method to move to the next row in the database
                    while(cursor.moveToNext()){
                        //String which gets the title of the current row
                        String check = cursor.getString(cursor.getColumnIndexOrThrow(Database.FeedEntry.Titles));
                        /***
                         * If the database is not empty,we check if the film have already been saved.
                         * */
                        for(int h=0;h<films.title.size();h++){
                            if(films.title.get(h).equals(check))
                            {
                                //Tell the user that the film is already saved
                                Toast.makeText(Details.this,"Film already saved",Toast.LENGTH_LONG).show();
                                saved =true;
                                break;
                            }
                        }
                        /***
                         * If it have not been saved yet,then we save it and set a reminder.
                         * */
                        if(!saved) {
                            //Open the database for writing
                            db = mDbHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            //Store the film title
                            values.put(Database.FeedEntry.Titles, films.title.get(index));
                            long newRodId = db.insert(Database.FeedEntry.TABLE_NAME,null,values);
                            //Tell the user that we save the film
                            Toast.makeText(Details.this,"Saved",Toast.LENGTH_LONG).show();
                            //Set a notification to the film
                            Setnotification(index);
                        }
                    }
                }
                //Close the cursor and release all of its resources
                cursor.close();
                //Disables the save button because the film is saved
                save.setEnabled(false);
            }
        });
    }
    /***
     * Gets the index of a film and the film object which contains films.
     * Called from the MainActivity.
     * @param i is the index
     * @param films2 film object
     * */
    public static void getIndex(int i,Films films2){
        index = i;
        films = films2;
    }
    /***
     * Sets a notification at 15 PM one day before the date,on which the film will be released in the cinemas.
     * @param index is the index of the film's position in the lists
     * */
    public void Setnotification(int index){
        //Parsing the presentation date of the film
        String date = films.releasedate.get(index);
        //Year of the date
        String yearstring = date.substring(0,4);
        //Month of the date
        String monthstring = date.substring(5,7);
        //Day of the date
        String daystring = date.substring(8,10);
        //Conver the string to integer
        int day = Integer.parseInt(daystring);
        int month = Integer.parseInt(monthstring);
        int year = Integer.parseInt(yearstring);
        // Create a new calendar and set it to one day before release date at 15pm
        Calendar c = Calendar.getInstance();
        //Months start from 0,so we have to subtract 1
        c.set(year, month-1, day-1);
        c.set(Calendar.HOUR_OF_DAY, 15);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
        scheduleClient.setAlarmForNotification(c);
        //Set our films title as the future notification's title
        NotifyService.addtitle(films.title.get(index));
        // Notify the user what they just did
        Toast.makeText(this, "Notification set for: " + day +"/"+ month +"/"+(year)+" 15:00 PM",Toast.LENGTH_SHORT).show();
    }
    /***
     * When the activity is stopped,it stops the connection to the service which helped us set an alarm.
     * This stops us leaking our activity into the system.
     * */
    @Override
    protected void onStop() {
        if(scheduleClient != null)
            scheduleClient.doUnbindService();
        super.onStop();
    }
}
