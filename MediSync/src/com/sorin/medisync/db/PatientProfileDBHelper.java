package com.sorin.medisync.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//helper class designed for table CRUD rules
public class PatientProfileDBHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "patientprofiletable.db";

	private static final int DATABASE_VERSION = 1;

	public PatientProfileDBHelper(Context context) {

		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	// Method is called during creation of the database

	@Override
	public void onCreate(SQLiteDatabase database) {

		PatientProfileTable.onCreate(database);

	}

	// Method is called during an upgrade of the database,

	// e.g. if you increase the database version

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,

	int newVersion) {

		PatientProfileTable.onUpgrade(database, oldVersion, newVersion);

	}

}
