package com.sorin.medisync.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PatientProfileTable {
	// Database table
	public static final String TABLE_PATIENTS = "patients";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_CATEGORY = "category";
	public static final String COLUMN_SUMMARY = "summary";
	public static final String COLUMN_DESCRIPTION = "description";

	// Database creation SQL statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_PATIENTS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_CATEGORY
			+ " text not null, " + COLUMN_SUMMARY + " text not null,"
			+ COLUMN_DESCRIPTION + " text not null" + ");";

	// constructor method for DB creation
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	// upgrades DB and erases the old one
	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(PatientProfileTable.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);
		onCreate(database);
	}
}
