package com.example.passtask9_1.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.passtask9_1.model.Restaurants;
import com.example.passtask9_1.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    public DatabaseHelper(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_RESTAURANT_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "(" + Util.RESTAURANT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + Util.name + " TEXT," + Util.LATITUDE + " TEXT," + Util.LONGITUDE + " ,TEXT)";
        sqLiteDatabase.execSQL(CREATE_RESTAURANT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String DROP_RESTAURANT_TABLE = "DROP TABLE IF EXISTS";

        sqLiteDatabase.execSQL(DROP_RESTAURANT_TABLE, new String[]{Util.TABLE_NAME});

        onCreate(sqLiteDatabase);
    }

    public long insertRestaurant (Restaurants restaurant)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Util.name, restaurant.getName());
        contentValues.put(Util.LATITUDE, restaurant.getLatitude());
        contentValues.put(Util.LONGITUDE, restaurant.getLongitude());

        long newRowId = db.insert(Util.TABLE_NAME, null, contentValues);
        db.close();
        return newRowId;
    }

    public List<Restaurants> fetchAllRestaurants (){
        List<Restaurants> restaurantList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectAll = " SELECT * FROM " + Util.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                Restaurants restaurant = new Restaurants();

                restaurant.setRestaurant_id(cursor.getInt(0));
                restaurant.setName(cursor.getString(1));
                restaurant.setLatitude(cursor.getString(2));
                restaurant.setLongitude(cursor.getString(3));

                restaurantList.add(restaurant);

            } while (cursor.moveToNext());

        }

        return restaurantList;
    }

}
