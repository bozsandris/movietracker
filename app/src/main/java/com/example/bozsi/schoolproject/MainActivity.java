package com.example.bozsi.schoolproject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends ListActivity {
    /***
     * Themoviedb url with api key and preferable options
     * */
    private String apikey = BuildConfig.Apikey;
    public String url =
    "https://api.themoviedb.org/3/movie/upcoming?api_key=" + apikey + "&language=en-EN&region=HU";
    Films films = new Films();
    /***
     * Films genres by their ids from the api documentation.
     * */
    Integer genres[] = {28,12,16,35,80,99,18,10751,14,36,27,10402,9648,10749,878,10770,53,10752,37};
    /***
     * Show the films which match the selected genre.
     * Onclick  starts a new intent with the details of the film and a save button,
     * which sets a reminder to the release date and stores the film in our database.
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                //We start a new thread if it isn't started yet
                if(t.getState() == Thread.State.NEW)
                {
                t.start();
                }
                //If it is started then we execute the code on the thread
                else t.run();
                //Freezes the thread for 4 seconds while we download the films
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
    /**
     * Downloads the films on a new thread in the background.
     * Then we store the films' details.
     * Title,average vote,overview,genre and release date are being stored.
     * */
    Thread t = new Thread() {
                public void run() {
                    OkHttpClient client = new OkHttpClient();
                    //Send a request to the given url
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        //On failure we log that there was an error
                        @Override
                        public void onFailure(Request request, IOException e) {
                            e.printStackTrace();
                            Log.e("Something went wrong","Couldn't download films.\n"+e.getMessage());
                        }
                        //On response we get back the Json file as a string and then we parse it
                        @Override
                        public void onResponse(Response response) throws IOException {

                            final String myResponse = response.body().string();

                            try {
                                JSONObject json = new JSONObject(myResponse);
                                JSONArray results = json.getJSONArray("results");
                                //Number of films we downloaded data about
                                int length = results.length();
                                //Store the film's title,genre,overview,release date and average vote
                                //First check if it already had been stored.
                                for (int i = 0; i < length; i++) {
                                    JSONObject objects = results.getJSONObject(i);
                                    if (!(films.title.contains(objects.getString("original_title")))) {
                                        films.Add(objects.getString("original_title"),
                                                objects.getString("genre_ids").substring(1),
                                                objects.getString("overview"),
                                                objects.getString("release_date"),
                                                objects.getDouble("vote_average"));
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            };
}
