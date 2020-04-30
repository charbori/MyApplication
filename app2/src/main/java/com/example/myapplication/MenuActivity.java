package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.controller.ListAdapter;
import com.example.myapplication.data.model.MyListItem;
import com.example.myapplication.lib.FeedReaderContract;
import com.example.myapplication.lib.FeedReaderDbHelper;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;

import static android.widget.Toast.LENGTH_SHORT;

public class MenuActivity extends AppCompatActivity {

    HashMap<String, String> str = new HashMap<String, String>();

    ArrayList<String> title, subtitle;
    ArrayList<MyListItem> personList;
    ListView list;
    private static final String TAG_PHONE = "phone";

    SQLiteDatabase sampleDB = null;
    ListAdapter adapter, listAdapter;

    MaterialButton btRefresh, btInsertData, btUpdate, btDelete;

    SQLiteDatabase dbWriter, dbReader;


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY, " +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " TEXT, " +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;

    FeedReaderDbHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.like_list);

        Context context = getApplicationContext();

        dbHelper = new FeedReaderDbHelper(getApplicationContext());

        dbWriter = dbHelper.getWritableDatabase();
        dbReader = dbHelper.getReadableDatabase();

        list = (ListView) findViewById(R.id.listView);
        btRefresh = (MaterialButton) findViewById(R.id.reselect_data);
        btInsertData = (MaterialButton) findViewById(R.id.insert_data);
        btUpdate = (MaterialButton) findViewById(R.id.update_data);
        btDelete = (MaterialButton) findViewById(R.id.delete_data);

        sampleDB = context.openOrCreateDatabase(FeedReaderDbHelper.DATABASE_NAME, context.MODE_PRIVATE,null);

        personList = new ArrayList<MyListItem>();

        //dbHelper.onCreate(sampleDB);
        //initInsertEntryDB(title, subtitle);
        showList();
        getEntryDB();

        setViewListener();

    }

    protected void showList(){

        if(personList != null){
//            adapter = new SimpleAdapter(
//                    this, personList, R.layout.list_item,
//                    new String[]{FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
//                            FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE},
//                    new int[]{R.id.name, R.id.phone}
//            );

            listAdapter = new ListAdapter(this, R.layout.list_item, personList);

            list.setAdapter(listAdapter);

        }else{
            Log.d("db","personList data is none");
        }
    }

    void initInsertEntryDB(HashMap<String, String> titleList ){

        ContentValues values = new ContentValues();

        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, titleList.get(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE));
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, titleList.get(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE));

        long newRodId = dbWriter.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);

        if(newRodId == -1){
            Log.d("db","db insert fail");
        }else{

            Log.d("db","db insert success");
        }

    }

    void updateListView(){


    }
    void getEntryDB(){

        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE
        };

        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = { "My Title" };

        String sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

        Cursor cursor = dbReader.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,    //table to query
                projection,                                 //array of columns return
                null,                                  //columns for the where clause
                null,                              //value for the where clause
                null,
                null,
                sortOrder
        );

        while(cursor.moveToNext()){
            String strTitle = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE));
            String strSubtitle = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE));

            Log.d("db","data title : " + strTitle);
            Log.d("db","data subtitle : " + strSubtitle);

            MyListItem myItem = new MyListItem();

            myItem.SetName(strTitle);
            myItem.SetContents(strSubtitle);

            if(str != null){
                personList.add(myItem);
            }else{
                Log.d("db","data none");
            }
        }

        cursor.close();
    }

    void setViewListener(){
        btRefresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        btInsertData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                HashMap<String, String> tempData = new HashMap<>();

                tempData.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, "tempTest");
                tempData.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, "tempInsertTestData");

                MyListItem myListItem = new MyListItem();
                myListItem.SetName(tempData.get(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE));
                myListItem.SetContents(tempData.get(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE));

                //
                initInsertEntryDB(tempData);

                Toast.makeText(getApplicationContext(), "size:" + listAdapter.getCount(),LENGTH_SHORT).show();

                personList.add(myListItem);

                MenuActivity.this.runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                listAdapter.updateList(personList);
                            }
                        }
                );
            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        btDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });
    }

}