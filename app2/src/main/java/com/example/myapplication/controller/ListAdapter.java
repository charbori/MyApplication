package com.example.myapplication.controller;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.R;
import com.example.myapplication.data.model.MyListItem;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListAdapter extends ArrayAdapter<MyListItem>{

    private ArrayList<MyListItem> personList;
    private Context context;
    private int rowResourceId;
    private LinearLayout newListLayout;

    public ListAdapter(Context context, int textViewResourceId, ArrayList<MyListItem> list){
        super(context, textViewResourceId, list);

        this.context = context;
        this.rowResourceId = textViewResourceId;
        personList = list;

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

            text_name.setText(item.getItemName());
            text_name.setTag(R.id.card_box1,position);
            text_content.setText(item.getItemContents());
            text_content.setTag(R.id.card_box1,position);

            text_name.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    Log.d("db", "layout rootLayout text");

                    return false;
                }
            });
            text_content.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    Log.d("db", "layout rootLayout content");

                    return false;
                }
            });

        }

        parent.setFocusable(false);
        Log.d("parent", "parent id : " + parent.getId());

        newListLayout = (LinearLayout) v.findViewById(R.id.newListLayout);
        newListLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.d("db", "layout rootLayout");

                return false;
            }
        });

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
