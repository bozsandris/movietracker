package com.example.bozsi.schoolproject;

import android.provider.BaseColumns;

public final class Database {
    /***
     * To prevent someone from accidentally instantiating the contract class,
     * make the constructor private.
     * */
        private Database(){}

        // Inner class that defines the table contents
        public static class FeedEntry implements BaseColumns{
            public static final String TABLE_NAME = "films";
            public static final String Titles = "Original_title";
        }

        /***
         * Creating the database using SQL helper.
         * */
        protected static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FeedEntry.TABLE_NAME + "(" +
                        FeedEntry._ID + " INTEGER PRIMARY KEY," +
                        FeedEntry.Titles + " TEXT)";
        protected static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
}
