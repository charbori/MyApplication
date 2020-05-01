package com.example.myapplication.controller;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.data.model.MyListItem;

import java.util.ArrayList;

public class SelectedListAdapter extends ArrayAdapter<MyListItem> {

    private ArrayList<MyListItem> personList;
    private Context context;
    private int rowResourceId;
    private Handler handler;

    public SelectedListAdapter(Context context, int textViewResourceId, ArrayList<MyListItem> list, Handler handler){
        super(context, textViewResourceId, list);

        this.context = context;
        this.rowResourceId = textViewResourceId;
        personList = list;
        this.handler = handler;

    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {

        return personList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if(v == null){
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(this.rowResourceId, null);
        }

        MyListItem item = personList.get(position);

        if(item != null){
            TextView text_name = (TextView) v.findViewById(R.id.name);
            TextView text_content = (TextView) v.findViewById(R.id.phone);
            CheckBox checkBox_content = (CheckBox) v.findViewById(R.id.check);

            text_name.setText(item.getItemName());
            text_name.setTag(R.id.card_box1,position);
            text_content.setText(item.getItemContents());
            text_content.setTag(R.id.card_box1,position);
            checkBox_content.setTag(R.id.card_box1, position);

            text_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("db","onclick name: " + v.getTag(R.id.card_box1) + "-> position");

                }
            });

            text_content.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.d("db","onclick contents: " + v.getTag(R.id.card_box1) + "-> position");

                }
            });

            checkBox_content.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.d("db","onclick contents: " + v.getTag(R.id.card_box1) + "-> position");

                    Message msg = handler.obtainMessage(0, String.valueOf(text_content.getText()));
                    handler.sendMessage(msg);

                }
            });
        }

        return v;
    }

//    @Override
//    public int getItemViewType(int position) {
//        return 1;
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        return 1;
//    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public int getSize(){
        return personList.size();
    }
    public void updateList(ArrayList<MyListItem> newList){

        personList.clear();
        personList.addAll(newList);
        //this.notifyDataSetChanged();
    }
}
