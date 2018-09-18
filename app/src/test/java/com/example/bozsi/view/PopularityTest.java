package com.example.bozsi.view;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import model.Database;
import model.DatabaseReader;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@Config(manifest=Config.NONE)
public class PopularityTest {

    Context context;

    @Before
    public void setup(){
        context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void popularitysearch(){
        DatabaseReader mDbHelper = new DatabaseReader(context);
        Double popularity = 5.0;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Database.FeedEntry.Titles, "film1");
        values.put(Database.FeedEntry.Genreids, "12");
        values.put(Database.FeedEntry.Overview, "tesztfilm");
        values.put(Database.FeedEntry.Releasedate, "2018-05-17");
        values.put(Database.FeedEntry.Popularity, 4.5);
        db.insert(Database.FeedEntry.TABLE_NAME, null, values);
        values.clear();
        values.put(Database.FeedEntry.Titles, "film2");
        values.put(Database.FeedEntry.Genreids, "18");
        values.put(Database.FeedEntry.Overview, "tesztfilm2");
        values.put(Database.FeedEntry.Releasedate, "2018-05-17");
        values.put(Database.FeedEntry.Popularity, 5.0);
        db.insert(Database.FeedEntry.TABLE_NAME, null, values);
        values.clear();
        values.put(Database.FeedEntry.Titles, "film3");
        values.put(Database.FeedEntry.Genreids, "24");
        values.put(Database.FeedEntry.Overview, "tesztfilm");
        values.put(Database.FeedEntry.Releasedate, "2018-05-17");
        values.put(Database.FeedEntry.Popularity, 5.3);
        db.insert(Database.FeedEntry.TABLE_NAME, null, values);
        DatabaseReader.searchbypopularity(mDbHelper,popularity);
        assertTrue(SavedFilms.films.get(0).popularity >= popularity);
        assertTrue(SavedFilms.films.size()==2);
    }
}
