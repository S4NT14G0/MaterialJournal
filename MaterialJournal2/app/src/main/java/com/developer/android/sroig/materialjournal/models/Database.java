package com.developer.android.sroig.materialjournal.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Santiago Roig on 11/5/2015.
 */
public class Database extends SQLiteOpenHelper {

    // Singleton Instance of our DB
    private static Database instance;

    private static int VERSION = 1;
    private static final String DB_NAME = "MaterialDB";
    private static final String TABLE_NAME = "JournalItems";
    public static final String[] COLUMNS = {"Id", "Title", "TextEntry", "Image", "Date", "Location"};

    public static synchronized  Database getInstance (Context context) {
        // If we don't have an instance of our DB then create one
        if (instance == null) {
            instance = new Database(context.getApplicationContext());
        }
        // Return instance of our DB
        return  instance;
    }

    // Constructor for our DB
    private  Database (Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //not used in this project, implementation is optional
        VERSION = newVersion;
        Log.d("Database - onUpgrade", "OK");
        onCreate(db);
    }

    public void addTable() {

         //Execute our SQL statement to create our Journal Table
        instance.getWritableDatabase().execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "\n" +
                        "(\n" +
                        COLUMNS[0] + " INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        COLUMNS[1] + " VARCHAR(255),\n" +
                        COLUMNS[2] + " VARCHAR(255), \n" +
                        COLUMNS[3] + " BLOB, \n" +
                        COLUMNS[4] + " VARCHAR(255), \n" +
                        COLUMNS[5] + " VARCHAR(255) \n" +
                        ");");
    }

    public long addRow(JournalItem item) {

        try {
            //use ContentValues to insert values in the table
            ContentValues values = new ContentValues();
            values.put(COLUMNS[1], item.getTitle());
            values.put(COLUMNS[2], item.getText());
            // Will need to convert image into bytes
            values.put(COLUMNS[3], convertImageToBytes(item.getImage()));
            values.put(COLUMNS[4], item.getDate().toString());
            values.put(COLUMNS[5], item.getLocation().toString());

            return instance.getWritableDatabase().insert(TABLE_NAME, null, values);

        } catch (NullPointerException npe) {

            Log.d("Database - addRow", "NPE - Table doesn't exist");
        }

        // If the insert fails
        return -1;
    }

    public JournalItem getRow(int rowIndex) {
        SQLiteDatabase db = instance.getReadableDatabase();

        Cursor cursor =  db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMNS[0] + " = " + rowIndex , null);


        if (cursor != null)
            cursor.moveToFirst();
        else
            return null;

        // Construct our item
        JournalItem item = new JournalItem();

        int test = cursor.getCount();

        // Build all of the fields
        item.setId(cursor.getInt(cursor.getColumnIndex(COLUMNS[0])));
        item.setTitle(cursor.getString(cursor.getColumnIndex(COLUMNS[1])));
        item.setText(cursor.getString(cursor.getColumnIndex(COLUMNS[2])));
        item.setImage(decodeBytesToImage(cursor.getBlob(cursor.getColumnIndex(COLUMNS[3]))));
        item.setDate(stringToDate(cursor.getString(cursor.getColumnIndex(COLUMNS[4]))));
        item.setLocation(cursor.getString(cursor.getColumnIndex(COLUMNS[5])));

        return  item;
    }

    public  void deleteRow(int rowIndex) {
        SQLiteDatabase db = instance.getWritableDatabase();

        db.delete(TABLE_NAME, COLUMNS[0] + " ="  + rowIndex, null);
    }

    public void deleteAll() {
        instance.getWritableDatabase().execSQL("delete from " + TABLE_NAME);
        instance.getWritableDatabase().execSQL("Delete from sqlite_sequence where name='" + TABLE_NAME + "'");
    }

    private int indexOfColumn(String name) {

        for (int i = 0; i < COLUMNS.length; ++i) {
            if (COLUMNS[i].equals(name)) {
                return i;
            }
        }
        return 0;
    }

    public int rowCount() {

        try {

            int count = (int) DatabaseUtils.queryNumEntries(instance.getReadableDatabase(), TABLE_NAME);

            return count;
        } catch (SQLiteException sqle) {

            return 0;
        }
    }

    public void updateRow(int rowIndex, JournalItem item) {
        try {
            //use ContentValues to insert values in the table
            ContentValues values = new ContentValues();
            values.put(COLUMNS[1], item.getTitle());
            values.put(COLUMNS[2], item.getText());
            // Will need to convert image into bytes
            values.put(COLUMNS[3], convertImageToBytes(item.getImage()));
            values.put(COLUMNS[4], dateToString(item.getDate()));
            values.put(COLUMNS[5], item.getLocation().toString());

            instance.getWritableDatabase().update(TABLE_NAME,values, COLUMNS[0] + " = " + rowIndex, null);

        } catch (NullPointerException npe) {

            Log.d("Database - addRandomRow", "NPE - Table doesn't exist");
        }
    }

    public  byte[] convertImageToBytes (Bitmap image) {
        //calculate how many bytes our image consists of.
        int bytes = image.getByteCount();
        //Create a new buffer
        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        //Move the byte data to the buffer
        image.copyPixelsToBuffer(buffer);

        //Get the underlying array containing the data.
        byte[] imageBytes = buffer.array();

        return imageBytes;
    }

    public  Bitmap decodeBytesToImage (byte[] imageBytes) {
        // Decode our array of bits back into an image
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    public String dateToString(Calendar date) {
        String strdate = null;

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        if (date != null) {
            strdate = sdf.format(date.getTime());
        }

        return strdate;
    }

    public Calendar stringToDate(String date) {
        Calendar cal = Calendar.getInstance();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            cal.setTime(sdf.parse(date));// all done
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return  cal;
    }
}