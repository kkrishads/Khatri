package com.myinfos.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.myinfos.models.InfoDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 14/07/2016.
 */
public class MyDb extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyInfo.db";
    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_FNAME = "fname";
    public static final String CONTACTS_COLUMN_LNAME = "lname";
    public static final String CONTACTS_COLUMN_EMAIL = "email";
    public static final String CONTACTS_COLUMN_ADDR = "addr";
    public static final String CONTACTS_COLUMN_CITY = "city";
    public static final String CONTACTS_COLUMN_AREA = "area";
    public static final String CONTACTS_COLUMN_PHONE = "phone";
    public static final String CONTACTS_COLUMN_MOBILE = "mobile";
    public static final String CONTACTS_COLUMN_LATITUDE = "latitude";
    public static final String CONTACTS_COLUMN_LONGITUDE = "longitude";
    public static final String CONTACTS_COLUMN_PINCODE = "pincode";

    public MyDb(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + CONTACTS_TABLE_NAME + "("
                + CONTACTS_COLUMN_ID + " TEXT PRIMARY KEY," + CONTACTS_COLUMN_FNAME + " TEXT," + CONTACTS_COLUMN_LNAME + " TEXT,"
                + CONTACTS_COLUMN_EMAIL + " TEXT," + CONTACTS_COLUMN_ADDR + " TEXT,"
                + CONTACTS_COLUMN_CITY + " TEXT," + CONTACTS_COLUMN_AREA + " TEXT,"
                + CONTACTS_COLUMN_PHONE + " TEXT," + CONTACTS_COLUMN_MOBILE + " TEXT," + CONTACTS_COLUMN_PINCODE + " TEXT,"
                + CONTACTS_COLUMN_LATITUDE + " TEXT,"
                + CONTACTS_COLUMN_LONGITUDE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    // Adding new contact
    public void addContact(InfoDetails contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONTACTS_COLUMN_ID, contact.id);
        values.put(CONTACTS_COLUMN_FNAME, contact.fname);
        values.put(CONTACTS_COLUMN_LNAME, contact.lname);
        values.put(CONTACTS_COLUMN_EMAIL, contact.email);
        values.put(CONTACTS_COLUMN_ADDR, contact.address);
        values.put(CONTACTS_COLUMN_CITY, contact.city);
        values.put(CONTACTS_COLUMN_AREA, contact.area);
        values.put(CONTACTS_COLUMN_PHONE, contact.landline);
        values.put(CONTACTS_COLUMN_MOBILE, contact.mobile);
        values.put(CONTACTS_COLUMN_LATITUDE, contact.latitude);
        values.put(CONTACTS_COLUMN_LONGITUDE, contact.longitude);
        values.put(CONTACTS_COLUMN_PINCODE, contact.pincode);
        // Inserting Row
        db.insert(CONTACTS_TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    public InfoDetails getContact(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + CONTACTS_TABLE_NAME + " WHERE " + CONTACTS_COLUMN_ID + "=?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});
        if (cursor != null)
            cursor.moveToFirst();

        InfoDetails contact = new InfoDetails();
        contact.id = cursor.getString(cursor.getColumnIndex(CONTACTS_COLUMN_ID));
//        contact.fname = cursor.getString(1);
//        contact.lname = cursor.getString(2);
//        contact.email = cursor.getString(3);
//        contact.address = cursor.getString(4);
//        contact.city = cursor.getString(5);
//        contact.area = cursor.getString(6);
//        contact.landline = cursor.getString(7);
//        contact.mobile = cursor.getString(8);
//        contact.latitude = cursor.getString(9);
//        contact.longitude = cursor.getString(10);
        // return contact
        return contact;
    }

    // code to get all contacts in a list view
    public List<InfoDetails> getAllContacts() {
        List<InfoDetails> contactList = new ArrayList<InfoDetails>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + CONTACTS_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                InfoDetails contact = new InfoDetails();
                contact.setId(cursor.getString(0));
                contact.setFname(cursor.getString(1));
                contact.setLname(cursor.getString(2));
                contact.setEmail(cursor.getString(3));
                contact.setAddress(cursor.getString(4));
                contact.setCity(cursor.getString(5));
                contact.setArea(cursor.getString(6));
                contact.setLandline(cursor.getString(7));
                contact.setMobile(cursor.getString(8));
                contact.setPincode(cursor.getString(9));
                contact.setLatitude(cursor.getString(10));
                contact.setLongitude(cursor.getString(11));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + CONTACTS_TABLE_NAME);
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + CONTACTS_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
