package com.sorin.medisync.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class CounterContentProvider extends ContentProvider
{
	private static final int ITEMS = 1;
	private static final int ITEM_ID = 2;

	private static final String AUTHORITY = "com.stylingandroid.adapters";
	private static final String BASE_PATH = "counter";
	
	public static final String[] PROJECTION = {CounterHelper.COLUMN_ID, CounterHelper.COLUMN_TEXT};

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/items";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/item";

	private CounterHelper helper;

	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static
	{
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, ITEMS);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", ITEM_ID);
	}

	@Override
	public boolean onCreate()
	{
		helper = new CounterHelper(getContext());
		return helper != null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(CounterHelper.TABLE_NAME);

		int uriType = sURIMatcher.match(uri);
		switch (uriType)
		{
		case ITEMS:
			break;
		case ITEM_ID:
			queryBuilder.appendWhere(CounterHelper.COLUMN_ID + "=" + uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public String getType(Uri uri)
	{
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = helper.getWritableDatabase();
		long id = 0;
		switch (uriType)
		{
		case ITEMS:
			if(getRowCount() + values.size() <= CounterHelper.MAX_ROWS)
			{
				id = sqlDB.insert(CounterHelper.TABLE_NAME, null, values);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = helper.getWritableDatabase();
		int rowsDeleted = 0;
		switch (uriType)
		{
		case ITEMS:
			rowsDeleted = sqlDB.delete(CounterHelper.TABLE_NAME, selection, selectionArgs);
			break;
		case ITEM_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection))
			{
				rowsDeleted = sqlDB.delete(CounterHelper.TABLE_NAME, CounterHelper.COLUMN_ID + "=" + id, null);
			}
			else
			{
				rowsDeleted = sqlDB.delete(CounterHelper.TABLE_NAME, CounterHelper.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
	{

		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = helper.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType)
		{
		case ITEMS:
			rowsUpdated = sqlDB.update(CounterHelper.TABLE_NAME, values, selection, selectionArgs);
			break;
		case ITEM_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection))
			{
				rowsUpdated = sqlDB.update(CounterHelper.TABLE_NAME, values, CounterHelper.COLUMN_ID + "=" + id, null);
			}
			else
			{
				rowsUpdated = sqlDB.update(CounterHelper.TABLE_NAME, values, CounterHelper.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	private long getRowCount()
	{
		return DatabaseUtils.queryNumEntries(helper.getReadableDatabase(), CounterHelper.TABLE_NAME);
	}
}
