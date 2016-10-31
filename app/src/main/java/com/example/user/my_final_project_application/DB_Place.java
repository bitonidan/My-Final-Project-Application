package com.example.user.my_final_project_application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by User on 18/09/2016.
 */
public class DB_Place extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "favoritplaces";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_ADDRESS = "address";
    public static final String COL_LAN = "lan";
    public static final String COL_LAT = "lat";
    public static final String COL_ICON = "icon";


    public DB_Place(Context context) {
        super(context, "favoritplaces.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE %1$s ( %2$s INTEGER PRIMARY KEY AUTOINCREMENT, %3$s TEXT , %4$s TEXT, %5$s REAL ,%6$s REAL , %7$s TEXT ) ",
                TABLE_NAME, COL_ID, COL_NAME, COL_ADDRESS, COL_LAN, COL_LAT,COL_ICON));

    }


    public void insert_Place(Place place) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, place.getName());
        values.put(COL_ADDRESS, place.getAddress());
        values.put(COL_LAN, place.getLan());
        values.put(COL_LAT, place.getLat());
        values.put(COL_ICON, place.getIcon());

        db.insert(TABLE_NAME, null, values);
    }

    public Cursor get_place_Cursor() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME,null);

    }

    public Place getPlacebyId(long id) {
        Place place = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE _id=" + id, null );
        if (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(COL_NAME));
            String address = c.getString(c.getColumnIndex(COL_ADDRESS));
            double lan = c.getDouble(c.getColumnIndex(COL_LAN));
            double lat = c.getDouble(c.getColumnIndex(COL_LAT));
            String icon= c.getString(c.getColumnIndex(COL_ICON));
            place = new Place(name, address, lat, lan,icon);
        }
        c.close();
        return place;
    }


    public ArrayList<Place> getAllPlaces(){
        Place pf;
        ArrayList<Place> places = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        while (c.moveToNext()) {
            long id= c.getLong(c.getColumnIndex(COL_ID));
            String name = c.getString(c.getColumnIndex(COL_NAME));
            String address = c.getString(c.getColumnIndex(COL_ADDRESS));
            double lat = c.getDouble(c.getColumnIndex(COL_LAT));
            double lan = c.getDouble(c.getColumnIndex(COL_LAN));
            String icon = c.getString(c.getColumnIndex(COL_ICON));

            pf = new Place(name, address, lan, lat, icon,id);
            places.add(pf);
        }
        return places;
    }

    public Place Get_Place_By_Position (int pos) {
        Place place = null;
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE _id = " + pos, null);
        if (c.moveToNext()) {

            String name = c.getString(c.getColumnIndex(COL_NAME));
            String address = c.getString(c.getColumnIndex(COL_ADDRESS));
            double lat=c.getDouble(c.getColumnIndex(COL_LAT));
            double lng=c.getDouble(c.getColumnIndex(COL_LAN));
            String icon = c.getString(c.getColumnIndex(COL_ICON));
            place = new Place(name,address,lat,lng,icon );
        }
        c.close();
        return place;
    }



    public void delete_all_places (){
            SQLiteDatabase db =getWritableDatabase();
            db.delete(TABLE_NAME,null,null);
            db.close();
        }

    public void Delete_Place_By_Id(long id){
        SQLiteDatabase db =getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + "=" + id ,null);
        db.close();
    }



        @Override
        public void onUpgrade (SQLiteDatabase db,int i, int i1){

        }
    }

