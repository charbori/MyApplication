package com.example.myapplication.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.lib.FeedReaderDbHelper;

import java.util.logging.Handler;

public class ListDataController {

    SQLiteDatabase dbWriter, dbReader;
    FeedReaderDbHelper dbHelper;

    public ListDataController(Context context){
        dbHelper = new FeedReaderDbHelper(context);
        dbWriter = dbHelper.getWritableDatabase();
        dbReader = dbHelper.getReadableDatabase();
    }

    public SQLiteDatabase getDBWriter(){

        if(dbWriter == null){
            dbWriter = dbHelper.getWritableDatabase();
        }

        return dbWriter;
    }

    public SQLiteDatabase getDBReader(){
        if(dbReader == null){
            dbReader = dbHelper.getReadableDatabase();
        }

        return dbReader;
    }

}
