package model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.widget.Toast;

import com.example.bozsi.view.Details;
import com.example.bozsi.view.SavedFilms;

import static model.Database.SQL_CREATE_ENTRIES;
import static model.Database.SQL_DELETE_ENTRIES;

/***
 * Subclass that overrides the onCreate() and onUpgrade() callback methods.
 * */
public class DatabaseReader extends SQLiteOpenHelper {
    /**When changing the database schema,you must increment the version.*/
    public static final int DATABASE_VERSION = 1;
    /**Name of our database file.*/
    public static final String DATABASE_NAME = "Films94.db";

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

    static public void deletetitle(String titletodelete, DatabaseReader mDbHelper){
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

    static public boolean savetitle(String title,DatabaseReader mDbHelper,ContentValues values){
        boolean saved = false;
        /***
         * Open our database for reading.
         * */
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                Database.FeedEntry.Titles,
                Database.FeedEntry.Genreids,
                Database.FeedEntry.Overview,
                Database.FeedEntry.Releasedate,
                Database.FeedEntry.Popularity
        };
        /***
         * Sql query to check if the film is already stored in our database.
         * Searches the film's title in the database.
         * */
        String selection = Database.FeedEntry.Titles + "= ?";
        //Selection arguments (the film's title in this case)
        String[] selectionArgs = {title};
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
            long newRodId = db.insert(Database.FeedEntry.TABLE_NAME, null, values);
            //Close the cursor and release all of its resources
            cursor.close();
            return true;
        }
        else {
            //Method to move to the next row in the database
            while(cursor.moveToNext()){
                saved = false;
                //String which gets the title of the current row
                String check = cursor.getString(cursor.getColumnIndexOrThrow(Database.FeedEntry.Titles));
                /***
                 * If the database is not empty,we check if the film have already been saved.
                 * */
                for(int h = 0; h< Details.films.size(); h++){
                    if(Details.films.get(h).title.equals(check))
                    {
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
                    long newRodId = db.insert(Database.FeedEntry.TABLE_NAME,null,values);
                    //Close the cursor and release all of its resources
                    cursor.close();
                    //Tell the user that we save the film
                    return true;
                }
            }
        }
        //Close the cursor and release all of its resources
        cursor.close();
        return false;
    }

    static public void searchbypopularity(DatabaseReader mDbHelper,Double popularity){
        /***
         * Open our database for reading.
         * */
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                Database.FeedEntry.Titles,
                Database.FeedEntry.Genreids,
                Database.FeedEntry.Overview,
                Database.FeedEntry.Releasedate,
                Database.FeedEntry.Popularity
        };
        /***
         * Sql query to check if the film is already stored in our database.
         * Searches the film's title in the database.
         * */
        String selection = Database.FeedEntry.Popularity + ">= ?";
        //Selection arguments (the film's title in this case)
        String[] selectionArgs = {popularity.toString()};
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
        if(cursor.getCount()!=0) {
            //Method to move to the next row in the database
            while(cursor.moveToNext()){
                //String which gets the title of the current row
                Double check = cursor.getDouble(cursor.getColumnIndexOrThrow(Database.FeedEntry.Popularity));
                /***
                 * If the database is not empty,we check if the film have already been saved.
                 * */
                if(check >= popularity) {SavedFilms.films.add(new Films(
                        cursor.getString((cursor.getColumnIndexOrThrow(Database.FeedEntry.Titles))),
                        cursor.getString((cursor.getColumnIndexOrThrow(Database.FeedEntry.Genreids))),
                        cursor.getString((cursor.getColumnIndexOrThrow(Database.FeedEntry.Overview))),
                        cursor.getString((cursor.getColumnIndexOrThrow(Database.FeedEntry.Releasedate))),
                        cursor.getDouble((cursor.getColumnIndexOrThrow(Database.FeedEntry.Popularity)))
                ))
                ;}
            }
        }
        //Close the cursor and release all of its resources
        cursor.close();
    }
}
