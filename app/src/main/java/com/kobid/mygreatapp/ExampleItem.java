package com.kobid.mygreatapp;

public class ExampleItem {

    private int mImageResource;

    private String mText1;
    private String mText2;
    private String mDocId;

    public ExampleItem() {
    }

    public ExampleItem(String text1, String text2,String DocId) {
        //mImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
        mDocId = DocId;
    }

    /*public ExampleItem(Map<String,Object> data) {
        mText1= (String)data.get("title");
        mText2= (String) data.get("Description");
    }*/

   /* public int getImageResource() {
        return mImageResource;
    }*/

//    public int getmImageResource() {
//        return mImageResource;
//    }


    public String getmText1() {
        return mText1;
    }

    public String getmText2() {
        return mText2;
    }

    public String getmDocId() {
        return mDocId;
    }
}