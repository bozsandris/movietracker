package com.example.bozsi.view;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
public class SavedFilms extends ListActivity {
    DatabaseReader mDbHelper = new DatabaseReader(this);
    /**Textview to show the details of the film.*/
    TextView textView;
    /**Index of a film.*/
    static int index;
    Double popularity;
    /**Object to store our films.*/
    static public List<Films> films = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.saved_films);
        //Button to store the film and set a reminder
        final Button load = findViewById(R.id.button);
        final EditText number = findViewById(R.id.editText);
        //Listener to the save button
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListView().setVisibility(View.VISIBLE);
                popularity = Double.valueOf(String.valueOf(number.getText()));
                number.setVisibility(View.INVISIBLE);
                load.setVisibility(View.INVISIBLE);
                Log.i("Button pressed","Load button pressed");
                DatabaseReader.searchbypopularity(mDbHelper,popularity);
                //An arrayadapter to fill up our listview with the films' title
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<>(getListView().getContext(), android.R.layout.simple_list_item_1);
                for(int i=0;i<films.size();i++) adapter.add(films.get(i).gettitle());
                getListView().setAdapter(adapter);
                //Sets a listener to the items of the listview
                getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    /***
                     * Start a new activity with the chosen film's details.
                     * */
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.i("Listview","Item clicked from listview");
                        DatabaseReader.deletetitle(getListView().getItemAtPosition(i).toString(),mDbHelper);
                        films.remove(i);
                        getListView().setVisibility(View.INVISIBLE);
                        number.setVisibility(View.VISIBLE);
                       load.setVisibility(View.VISIBLE);
                       }
                });
                 }
        });
    }
}
