package com.example.bozsi.schoolproject;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/***
 * Class which downloads the films' details from themoviedb.org.
 * */
public class MainActivity extends ListActivity  {

    /**Parameter which is true when the films had been processed.*/
    boolean done;
    /**Our api key to the themoviedb.org site.*/
    private String apikey = BuildConfig.Apikey;
    /**The response string from the api which we use.*/
    public String responsestring;
    /**Themoviedb url with api key and preferable options.*/
    public String url =
    "https://api.themoviedb.org/3/movie/upcoming?api_key=" + apikey + "&language=en-EN&region=HU";
    /**Object which store the films' details.*/
    Films films = new Films();
    /***
     * Films genres by their ids from the api documentation.
     * */
    Integer genres[] = {28,12,16,35,80,99,18,10751,14,36,27,10402,9648,10749,878,10770,53,10752,37};
    /***
     * Method which stores the downloaded films in the films object.
     * @param result is a json converted to a string which we get after downloading the films.
     * We parse the result string and store the films' details.
     * Title,average vote,overview,genre and release date are being stored.
     * */
    public void addtolist(String result){
        Log.d("Info","We are in the add to list method");
        try {
            JSONObject json = new JSONObject(result);
            JSONArray results = json.getJSONArray("results");
            //Number of films we downloaded data about
            int length = results.length();
            //Store the film's title,genre,overview,release date and average vote
            //First check if it already had been stored.
            for (int i = 0; i < length; i++) {
                JSONObject objects = results.getJSONObject(i);
                //if (!(films.title.contains(objects.getString("original_title"))))
                {
                    films.Add(objects.getString("original_title"),
                            objects.getString("genre_ids").substring(1),
                            objects.getString("overview"),
                            objects.getString("release_date"),
                            objects.getDouble("vote_average"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            done=true;
        }done=true;
        Log.d("Value of done",""+done);
    }

    /***
     * Show the films which match the selected genre.
     * Onclick  starts a new intent with the details of the film and a save button,
     * which sets a reminder to the release date and stores the film in our database.
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //If a notification's intent started the activity,get the intent
        Bundle b = getIntent().getExtras();
        //Store it in a string and then delete the film from the database
        if( b != null){
            String titletodelete = b.getString("title");
            //Accessing the database by instantiating subclass of SQLiteOpenHelper
            DatabaseReader mDbHelper = new DatabaseReader(this);
            //Open the database for reading
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            String[] projection = {
                    BaseColumns._ID,
                    Database.FeedEntry.Titles};
            //The column which we want to check
                    String selection = Database.FeedEntry.Titles + "= ?";
            //Selection arguments (the film's title in this case)
            String[] selectionArgs = {titletodelete};
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
            //If we have matches then move to the row and delete it
            if(cursor!=null)
            {
                //Move the cursor to the next row of the database
                while(cursor.moveToNext()){
                        //Open the database for modification
                        db = mDbHelper.getWritableDatabase();
                        db.delete(Database.FeedEntry.TABLE_NAME, Database.FeedEntry.Titles+"='"+titletodelete+"'",null);
                        db.close();
                }
            }
        }
        setContentView(R.layout.activity_main);
        final Button refresh = findViewById(R.id.button);
        final Button back = findViewById(R.id.button3);
        //Spinner which contains genre options
        final Spinner spinner = findViewById(R.id.spinner);
        //Our adapter to fill up the spinner menu
        ArrayAdapter<CharSequence> genreadapter = ArrayAdapter.createFromResource(this,R.array.genres,android.R.layout.simple_spinner_item);
        //Sets the options from which we can choose when we scroll down the spinner
        genreadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(genreadapter);
        //Listener to the button which download the films for us.
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                done=false;
                //Start a new thread
                if(t.getState() == Thread.State.NEW) t.start();
                //If we had already started the thread,just call its run method
                else t.run();
                //Wait for the films to be downloaded
                while(!done){Log.d("Info","We are in the while cycle");}
                //Sets the download button invisible
                refresh.setVisibility(View.INVISIBLE);
                //Download the films which matches the chosen genre
                if(!(spinner.getSelectedItem().toString().equals( "None"))){
                    for(int i=0;i<films.genreids.size();i++){
                        if(!(films.genreids.get(i).contains(genres[spinner.getSelectedItemPosition()-1].toString()))) {films.Remove(i); i--;}}
                }
                //Sets the spinner menu invisible
                spinner.setVisibility(View.INVISIBLE);
                //Sets the listview visible to represent the films
                getListView().setVisibility(View.VISIBLE);
                //Sets our back button visible so we can get back to the opening state
                back.setVisibility(View.VISIBLE);
                //An arrayadapter to fill up our listview with the films' title
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<>(getListView().getContext(), android.R.layout.simple_list_item_1, films.title);
                getListView().setAdapter(adapter);
                //Sets a listener to the items of the listview
                getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    /***
                     * Start a new activity with the chosen film's details.
                     * */
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Details.getIndex(i,films);
                        Intent intent = new Intent(MainActivity.this,Details.class);
                        startActivity(intent);
                    }
                });
            }
        });
        //Set a listener to the back button
        back.setOnClickListener(new View.OnClickListener() {
            /***
             * Click on the button clears the films object and changes the UI back to the opening state.
             * */
            @Override
            public void onClick(View view) {
                clearfilms();
                 back.setVisibility(View.INVISIBLE);
                 getListView().setVisibility(View.INVISIBLE);
                 spinner.setVisibility(View.VISIBLE);
                 refresh.setVisibility(View.VISIBLE);
             }
        });
    }
    /**
     * Clears the stored films' title,average vote,
     * overview,genre and release date.
     * */
    public void clearfilms(){
        films.title.clear();
        films.voteavg.clear();
        films.overview.clear();
        films.genreids.clear();
        films.releasedate.clear();
    }
    /***
     * Send a request to themoviedb.org api and we get back a json response.
     * Then we convert it to a string and pass it to our addtolist() method,which
     * will store the films in our Film object.
     * */
    Thread t = new Thread(){
        public void run(){
            OkHttpClient client = new OkHttpClient();
            //Send a request to the given url
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = null;
            try {
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        try {responsestring = response.body().string();Log.d("Response text from api",responsestring);
                            addtolist(responsestring);}
                        catch (IOException e) {
                            e.printStackTrace();
                            responsestring = "error";
                        }
                    }
                });
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    };
}
