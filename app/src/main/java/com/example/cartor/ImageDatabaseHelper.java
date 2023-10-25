package com.example.cartor;

import android.content.Context;
import android.database.Cursor;
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
    public String getLatestImageURI() {
        String imageUri = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {COLUMN_IMAGE_URI};
        String orderBy = COLUMN_ID + " DESC"; // Sort by ID in descending order
        Cursor cursor = db.query(TABLE_IMAGES, projection, null, null, null, null, orderBy);

        if (cursor.moveToFirst()) {
            imageUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URI));
        }

        cursor.close();
        db.close();

        return imageUri;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
