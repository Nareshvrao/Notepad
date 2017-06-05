package com.example.apple.myapplication.util;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "noteManager";

	// Contacts table name
	private static final String TABLE_CONTACTS = "notepad";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_TITLE = "name";
	private static final String KEY_TEXT = "text";
	private static final String KEY_IMAGE = "image_url";
	private static final String KEY_TIME = "time";



	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
				+ KEY_TEXT + " TEXT," + KEY_IMAGE + " TEXT," + KEY_TIME + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new noteEntitity
	public void addNoteDetail(NoteEntitity noteEntitity) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, noteEntitity.getTitle()); // NoteEntitity Name
		values.put(KEY_TEXT, noteEntitity.getText()); // NoteEntitity TEXT
		values.put(KEY_IMAGE, noteEntitity.getImageUrl()); // NoteEntitity imageurl
		values.put(KEY_TIME, noteEntitity.getTime()); // NoteEntitity time

		// Inserting Row
		db.insert(TABLE_CONTACTS, null, values);
		db.close(); // Closing database connection
	}

	// Getting single contact
	public NoteEntitity getNote(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
				KEY_TITLE, KEY_TEXT ,   KEY_IMAGE , KEY_TIME }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		NoteEntitity noteEntitity = new NoteEntitity(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2) , cursor.getString(3) , cursor.getString(4));
		// return noteEntitity
		return noteEntitity;
	}
	
	// Getting All Contacts
	public List<NoteEntitity> getAllNotes() {
		List<NoteEntitity> noteEntitityList = new ArrayList<NoteEntitity>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				NoteEntitity noteEntitity = new NoteEntitity();
				noteEntitity.setID(Integer.parseInt(cursor.getString(0)));
				noteEntitity.setTitle(cursor.getString(1));
				noteEntitity.setText(cursor.getString(2));
				noteEntitity.setImageUrl(cursor.getString(3));
				noteEntitity.setTime(cursor.getString(4));

				// Adding noteEntitity to list
				noteEntitityList.add(noteEntitity);
			} while (cursor.moveToNext());
		}

		// return contact list
		return noteEntitityList;
	}

	// Updating single noteEntitity
	public int updateContact(NoteEntitity noteEntitity) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, noteEntitity.getTitle()); // NoteEntitity Name
		values.put(KEY_TEXT, noteEntitity.getText()); // NoteEntitity TEXT
		values.put(KEY_IMAGE, noteEntitity.getImageUrl()); // NoteEntitity imageurl
		values.put(KEY_TIME, noteEntitity.getTime()); // NoteEntitity time

		// updating row
		return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(noteEntitity.getID()) });
	}

	/*// Deleting single noteEntitity
	public void deleteNote(NoteEntitity noteEntitity) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
				new String[] { String.valueOf(noteEntitity.getID()) });
		db.close();
	}*/


	/*// Getting noteEntitities Count
	public int getContactsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}
*/
}
