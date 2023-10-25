package com.example.cartor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ImageDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ImageDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_IMAGES = "images";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_IMAGE_URI = "image_uri";

    private static final String DATABASE_CREATE = "create table " + TABLE_IMAGES + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_IMAGE_URI + " text not null);";

    public ImageDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
