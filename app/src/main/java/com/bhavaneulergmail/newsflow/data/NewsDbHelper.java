package com.bhavaneulergmail.newsflow.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class NewsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "news.db";
    private static final int DATABASE_VERSION = 1;


    public NewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_TABLE = " CREATE TABLE " + NewsContract.NewsEntry.TABLE_NAME + "(" + NewsContract.NewsEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT ," + NewsContract.NewsEntry.HEADLINE + " TEXT UNIQUE NOT NULL ," + NewsContract.NewsEntry.NEWS_URL +
                " TEXT ," + NewsContract.NewsEntry.AUTHOR + " TEXT );";


        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
