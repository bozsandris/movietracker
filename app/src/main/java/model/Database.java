package model;

import android.provider.BaseColumns;
/***
 * Class which contains the basic informations of our database.
 * */
public final class Database {
    /***
     * To prevent someone from accidentally instantiating the contract class,
     * make the constructor private.
     * */
        private Database(){}

        /** Inner class that defines the table contents.*/
        public static class FeedEntry implements BaseColumns{
            /**Table's name as a string.*/
            public static final String TABLE_NAME = "films";
            /**Column's name as string.*/
            public static final String Titles = "Original_title";
            public static final String Genreids = "Genreids";
            public static final String Overview = "Overview";
            public static final String Releasedate = "Release_date";
            public static final String Popularity = "Popularity";
        }

        /***
         * Creating the database using SQL helper.
         * */
        protected static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FeedEntry.TABLE_NAME + "(" +
                        FeedEntry._ID + " INTEGER PRIMARY KEY," +
                        FeedEntry.Titles + " TEXT,"
                + FeedEntry.Genreids + " TEXT,"
                + FeedEntry.Overview + " TEXT,"
                + FeedEntry.Releasedate + " TEXT,"
                + FeedEntry.Popularity + " DOUBLE)";
        /**Defines the delete entries sql command.*/
        protected static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
}
