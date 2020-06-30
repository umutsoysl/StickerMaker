package com.stickers.laks.whatssappforsticker.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class Database extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "StickerMakerDb";


    private static final String TABLE_NAME = "STICKER_INFORMATIONEXT";
    private static final String TABLE_NAME2 = "STICKER_INFORMATION";

    private static String ID = "id";
    private static String PATH = "path";
    private static String NAME = "name";
    private static String AUTHOR = "author";
    private static String CREATEDATE = "createDate";
    private static String PROCESSID = "pid";
    private static String ISHEADER = "isHeader";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String tabloExt = "CREATE TABLE " + TABLE_NAME + "( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PROCESSID + " TEXT , "
                + NAME + " TEXT , "
                + AUTHOR + " TEXT , "
                + PATH + " TEXT , "
                + ISHEADER + " TEXT , "
                + CREATEDATE + " TEXT  "
                +" ) ";
        db.execSQL(tabloExt);

        String tablo = "CREATE TABLE " + TABLE_NAME2 + "( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PROCESSID + " TEXT , "
                + NAME + " TEXT , "
                + AUTHOR + " TEXT  "
                +" ) ";
        db.execSQL(tablo);

    }


    public void addStickerPacketInfo( String stickerId, String name, String author) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PROCESSID, stickerId);
        values.put(NAME, name);
        values.put(AUTHOR, author);
        db.insert(TABLE_NAME2, null, values);
        db.close();
    }



    public void addStickerPacket( String stickerId, String name, String author, String path, String createDate,String isHeader) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PROCESSID, stickerId);
        values.put(NAME, name);
        values.put(AUTHOR, author);
        values.put(PATH,path);
        values.put(CREATEDATE,createDate);
        values.put(ISHEADER,isHeader);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<HashMap<String, String>> getStickerPacket(){

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME2;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> infoList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                infoList.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        return infoList;
    }


    public ArrayList<HashMap<String, String>> getStickersPath(String id){

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE pid = '"+ id +"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> infoList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                infoList.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        return infoList;
    }

   public boolean isHasStickerPacket(String id){

       Cursor cursor = null;
       SQLiteDatabase db = this.getReadableDatabase();
       try {

           cursor = db.rawQuery("SELECT name FROM "+TABLE_NAME2+" WHERE pid = '"+ id +"'",null);
           if(cursor.getCount() > 0) {
              return true;
           }else{
               return false;
           }

       }finally {
           cursor.close();
       }

   }

    public String getStickerRootPath(String id){

        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            cursor = db.rawQuery("SELECT name,author FROM "+TABLE_NAME+" WHERE pid = '"+ id +"'",null);
            if(cursor.getCount() > 0) {

                cursor.moveToFirst();

                String name = cursor.getString(cursor.getColumnIndex("name"));;
                String author = cursor.getString(cursor.getColumnIndex("author"));

                return author+"/"+name;
            }else{
                return null;
            }

        }finally {
            cursor.close();
        }

    }

   public void updateStickerPathTroy(String id , String path){

       Cursor cursor = null;
       SQLiteDatabase db = this.getReadableDatabase();
       try {

           cursor = db.rawQuery("UPDATE "+TABLE_NAME+" SET path = '"+path +"' WHERE isHeader='1' and pid = '"+ id +"'",null);
       }finally {
           cursor.close();
       }
   }

    public void updateStickerPath(String Stickerid , String path ,int id){

        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            cursor = db.rawQuery("UPDATE "+TABLE_NAME+" SET path = '"+path +"' WHERE isHeader='0' and pid = '"+ Stickerid +"' and id ="+id ,null);
        }finally {
            cursor.close();
        }
    }


   public String getHeaderStickerId(String id){
       Cursor cursor = null;
       SQLiteDatabase db = this.getReadableDatabase();
       try {

           cursor = db.rawQuery("SELECT path FROM "+TABLE_NAME+" WHERE isHeader='1' and pid = '"+ id +"'",null);
           if(cursor.getCount() > 0) {
               cursor.moveToFirst();
              return cursor.getString(cursor.getColumnIndex("path"));
           }else{
               return null;
           }

       }finally {
           cursor.close();
       }

   }

    public void removeStickerPath(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    public void dropTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }
}