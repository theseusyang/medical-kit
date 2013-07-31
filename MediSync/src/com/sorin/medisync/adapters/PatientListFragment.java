package com.sorin.medisync.adapters;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sorin.medisync.R;
import com.sorin.medisync.data.CounterContentProvider;
import com.sorin.medisync.data.CounterHelper;

public class PatientListFragment extends ListFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String[] values = new String[] { "Joachim Hermann", "Stefanie Kraus",
				"Ted Thompson", "Gunther Joachim", "Patricia Charleroi",
				"Sorin Irimies", "Wung Xiu", "Lynda Bauer", "Tod Toddson",
				"Chen Wong", "Wung Xiu", "Lynda Schillinger", "Bart Toddson",
				"Wung Xiu", "Lynda Bauer", "Tod Toddson", "Shang Xiu",
				"Lynda Bauer", "Jim Rob" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_view, container, false);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.patient_menu, menu);
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
