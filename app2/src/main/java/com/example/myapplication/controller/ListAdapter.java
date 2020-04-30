package com.example.myapplication.controller;

import android.content.Context;
import android.database.DataSetObserver;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
            text_content.setText(item.getItemContents());
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

    public void updateList(ArrayList<MyListItem> newList){
        personList.clear();
        personList.addAll(newList);
        this.notifyDataSetChanged();
    }
}
