package com.sorin.medisync.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CounterHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "counter.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_NAME = "counter";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TEXT = "text";

	public static final String[] NUMBERS = { "Otto Hermann",
			"Oberarzt: Dieter Steinberger", "Franziska Singen",
			"Gunther Joachim", "Patricia Charleroi", "Sorin Irimies",
			"Wung Xiu", "Lynda Bauer", "Tod Toddson", "Betina Smith" };

	public static final long MAX_ROWS = NUMBERS.length;

	public CounterHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + TABLE_NAME + " (" + COLUMN_ID
				+ " integer primary key autoincrement, " + COLUMN_TEXT
				+ " text not null);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

}
