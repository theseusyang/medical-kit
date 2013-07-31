package com.sorin.medisync.adapters;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.sorin.medisync.R;
import com.sorin.medisync.data.CounterContentProvider;
import com.sorin.medisync.data.CounterHelper;

public class CursorAdapterFragment extends ListFragment implements
		LoaderCallbacks<Cursor> {
	private static final String[] columns = { CounterHelper.COLUMN_ID,
			CounterHelper.COLUMN_TEXT };

	private static final int[] controlIds = { android.R.id.text1,
			android.R.id.text2 };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		return inflater.inflate(R.layout.listview, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().restartLoader(0, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Loader<Cursor> loader = null;
		if (id == 0) {
			loader = new CursorLoader(getActivity(),
					CounterContentProvider.CONTENT_URI,
					CounterContentProvider.PROJECTION, null, null,
					CounterHelper.COLUMN_ID + " ASC");
		}
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		ListAdapter adapter = getListAdapter();
		if (adapter == null || !(adapter instanceof CursorAdapter)) {
			adapter = new SimpleCursorAdapter(getActivity(),
					android.R.layout.simple_list_item_2, cursor, columns,
					controlIds, 0);
			getActivity().invalidateOptionsMenu();
			setListAdapter(adapter);
		} else {
			((CursorAdapter) adapter).swapCursor(cursor);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.cursor_menu, menu);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		MenuItem item = menu.findItem(R.id.add);
		if (item != null) {
			item.setEnabled(getListAdapter() != null ? getListAdapter()
					.getCount() < CounterHelper.MAX_ROWS : false);
		}
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add:
			// checking if count has reached the end
			int count = getListAdapter().getCount();
			if (count < CounterHelper.MAX_ROWS) {
				item.setEnabled(false);
				AsyncQueryHandler queryHandler = new AsyncQueryHandler(
						getActivity().getContentResolver()) {
					@Override
					protected void onInsertComplete(int token, Object cookie,
							Uri uri) {
						getActivity().invalidateOptionsMenu();
						super.onInsertComplete(token, cookie, uri);
					}
				};
				// adding an item from db
				ContentValues values = new ContentValues();
				values.put(CounterHelper.COLUMN_TEXT,
						CounterHelper.NUMBERS[count]);
				queryHandler.startInsert(0, null,
						CounterContentProvider.CONTENT_URI, values);

			}
			return true;
		case R.id.delete:

			// checking if count has reached the end
			int count1 = getListAdapter().getCount();
			if (count1 < CounterHelper.MAX_ROWS) {
				item.setEnabled(false);
				AsyncQueryHandler queryHandler = new AsyncQueryHandler(
						getActivity().getContentResolver()) {
					@Override
					protected void onInsertComplete(int token, Object cookie,
							Uri uri) {
						getActivity().invalidateOptionsMenu();
						super.onInsertComplete(token, cookie, uri);
					}
				};
				// deleting an item from db
				ContentValues values = new ContentValues();
				CursorAdapterFragment adapter = new CursorAdapterFragment();
				values.remove(CounterHelper.COLUMN_TEXT);
				queryHandler.startDelete(0, values,
						CounterContentProvider.CONTENT_URI, null, null);
				adapter.notify();
			}
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);

	}
}
