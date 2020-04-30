package com.example.myapplication.data.model;

public class MyListItem implements Cloneable {
    private String itemName = "";
    private String itemContents = "";

    public void SetName(String val){
        itemName = val;
    }
    public void SetContents(String val){
        itemContents = val;
    }

    public String getItemName(){
        return itemName;
    }

    public String getItemContents(){
        return itemContents;
    }

    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
}
