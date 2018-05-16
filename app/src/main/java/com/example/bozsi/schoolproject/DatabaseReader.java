package com.example.bozsi.schoolproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.bozsi.schoolproject.Database.SQL_CREATE_ENTRIES;
import static com.example.bozsi.schoolproject.Database.SQL_DELETE_ENTRIES;

/***
 * Subclass that overrides the onCreat() and onUpgrade() callback methods.
 * */
public class DatabaseReader extends SQLiteOpenHelper {
    /**When changing the database schema,you must increment the version.*/
    public static final int DATABASE_VERSION = 1;
    /**Name of our database file.*/
    public static final String DATABASE_NAME = "Films.db";

    /**Class constructor.
     * @param context context*/
    public DatabaseReader (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    /**Oncreate method executes sql query which creates the database.
     * @param db Sqlite database*/
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    /***
     * This database is only a cache for online data,so its upgrade
     * policy is just to simply discard the data and start over.
     * */
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
