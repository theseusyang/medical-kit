package com.sorin.medisync.adapters;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PatientListFragment extends ListFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String[] values = new String[] { "John Smith", "Jennifer Johnson",
				"Bob McKinsey", "Hermann Hesse", "Adam Schindler",
				"Sorin Irimies", "Wung Xiu", "Lynda Bauer", "Tod Toddson",
				"Betina Smith" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Do something with the data

	}
}
