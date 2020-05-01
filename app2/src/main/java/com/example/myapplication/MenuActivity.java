package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.controller.ListAdapter;
import com.example.myapplication.controller.ListDataController;
import com.example.myapplication.controller.SelectedListAdapter;
import com.example.myapplication.data.model.MyListItem;
import com.example.myapplication.lib.FeedReaderContract;
import com.example.myapplication.lib.FeedReaderDbHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.Toast.LENGTH_SHORT;

public class MenuActivity extends AppCompatActivity {

    HashMap<String, String> str = new HashMap<String, String>();

    ArrayList<String> title, subtitle;

    LinkedList<String> listDeleteData;
    ListView list;
    private static final String TAG_PHONE = "phone";

    SQLiteDatabase sampleDB = null;
    ListAdapter listAdapter;
    SelectedListAdapter adapter;

    ConstraintLayout listOuterLayout, listRootLayout;

    @BindView(R.id.buttonLayout) ConstraintLayout buttonLayout;
    @BindView(R.id.buttonSelectedLayout) ConstraintLayout buttonSelectedLayout;
    @BindView(R.id.backList) ExtendedFloatingActionButton backViewList;
    @BindView(R.id.deleteSelectedData) ExtendedFloatingActionButton deleteContent;

    MaterialButton btRefresh, btInsertData, btUpdate, btDelete;

    SQLiteDatabase dbWriter, dbReader;

    Handler handler;

    int SET_BASE_LIST = 1111233;
    int SET_SELECTED_LIST = 1195840;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY, " +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " TEXT, " +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;

    FeedReaderDbHelper dbHelper;

    ListDataController listDataController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.like_list);

        listDeleteData = new LinkedList<>();

        Context context = getApplicationContext();

        listDataController = new ListDataController(getApplicationContext());

        dbWriter = listDataController.getDBWriter();
        dbReader = listDataController.getDBReader();

        list = (ListView) findViewById(R.id.listView);
        btRefresh = (MaterialButton) findViewById(R.id.reselect_data);
        btInsertData = (MaterialButton) findViewById(R.id.insert_data);
        btUpdate = (MaterialButton) findViewById(R.id.update_data);
        btDelete = (MaterialButton) findViewById(R.id.delete_data);

        sampleDB = context.openOrCreateDatabase(FeedReaderDbHelper.DATABASE_NAME, context.MODE_PRIVATE,null);

        listOuterLayout = (ConstraintLayout) findViewById(R.id.list_layout);
        listRootLayout = (ConstraintLayout) findViewById(R.id.rootLayout);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                int index = 0;
                String strMsg = (String) msg.obj;
                boolean noneData = true;

                while(listDeleteData.size() > index){

                    if(listDeleteData.get(index).equals(strMsg)){
                        listDeleteData.remove(index);
                        noneData = false;
                        Log.d("delete", "do push() delete contents in queue");
                    }

                    index++;
                }

                if(noneData){
                    listDeleteData.push((String) msg.obj);

                    Log.d("delete", "do push() queue");
                }
            }
        };

        ButterKnife.bind(this);

        //showList();
        getEntryDB();

        setViewListener();

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

    ArrayList<MyListItem> getListData(){
        ArrayList<MyListItem> itemList = new ArrayList<>();

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
                itemList.add(myItem);
            }else{
                Log.d("db","data none");
            }
        }

        cursor.close();

        return itemList;
    }

    void updateSelectedData(){

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
    }

    void deletedSelectedData(String selectName){

        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE
        };

        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE + " LIKE ?";
        String[] selectionArgs = { selectName };
        int deletedRows = dbWriter.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);

        Log.d("delete","deleted row : " + deletedRows);

    }

    void getEntryDB(){

        //DB에서 list 데이터 추출
        ArrayList<MyListItem> itemList = getListData();

        setBaseList();
    }

    void setBaseList(){

        //DB에서 list 데이터 추출
        ArrayList<MyListItem> itemList = getListData();

        listAdapter = new ListAdapter(getApplicationContext(), R.layout.list_item, itemList);

        try{
            new SetListClass().execute(SET_BASE_LIST);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    void setSeletedBoxList(){

        //DB에서 list 데이터 추출
        ArrayList<MyListItem> itemList = getListData();

        adapter = new SelectedListAdapter(getApplicationContext(), R.layout.selected_list_item, itemList, handler);

        try{
            new SetListClass().execute(SET_SELECTED_LIST);
        }catch (Exception e){
            e.printStackTrace();
        }

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
                tempData.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, "size:" + listAdapter.getSize());

                MyListItem myListItem = new MyListItem();
                myListItem.SetName(tempData.get(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE));
                myListItem.SetContents(tempData.get(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE));

                //
                initInsertEntryDB(tempData);

                Toast.makeText(getApplicationContext(), "size:" + listAdapter.getCount(),LENGTH_SHORT).show();

//                try{
//                    new SetListClass().execute();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }


                listAdapter.updateList(getListData());
                listAdapter.notifyDataSetChanged();
                list.invalidateViews();

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
                setSeletedBoxList();

                buttonLayout.setVisibility(View.GONE);
                buttonSelectedLayout.setVisibility(View.VISIBLE);
            }
        });
        list.setOnItemClickListener(listener);

        //터치이벤트 전달하기
        listRootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("db", "layout rootLayout menuactivity");
                return false;
            }
        });

        //자식 레이아웃에 전달하기
        listOuterLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("db", "layout list_layout menuactivity");
                return false;
            }
        });

        backViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBaseList();

                buttonLayout.setVisibility(View.VISIBLE);
                buttonSelectedLayout.setVisibility(View.GONE);
            }
        });

        deleteContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean doneDeletedAction = false;

                while(listDeleteData.size() != 0){
                    Log.d("delete", "do index : " + listDeleteData.peek());
                    deletedSelectedData(listDeleteData.poll());

                    doneDeletedAction = true;
                }

                if(doneDeletedAction){
                    setSeletedBoxList();
                }
            }
        });

    }
    AdapterView.OnItemClickListener listener= new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("db", "position click : " + position);
        }

    };

    private class SetListClass extends AsyncTask{
        long total = 0;

        @Override
        protected Object doInBackground(Object[] objects) {
            //getEntryDB();
            int cal = (int) objects[0];

            if(cal == SET_BASE_LIST){
                MenuActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        list.setAdapter(listAdapter);
                        list.invalidateViews();
                        list.setOnItemClickListener(new ListView.OnItemClickListener(){

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Log.d("db","position onItemClick : " + position);
                            }

                        });
                    }
                });
            }else if(cal == SET_SELECTED_LIST){
                MenuActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        list.setAdapter(adapter);
                        list.invalidateViews();
                        list.setOnItemClickListener(new ListView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Log.d("db","position onItemClick : " + position);

                            }
                        });
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPreExecute() { super.onPreExecute(); }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
        }
    }

}