package com.sorin.medisync.adapters;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HospitalStaffFragment extends ListFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String[] values = new String[] { "Chefarzt: Otto Hermann",
				"Oberarzt: Dieter Steinberger", "Oberarzt: Franziska Singen",
				"Internist: Gunther Joachim",
				"Assistentarzt: Patricia Charleroi", "Sorin Irimies",
				"Wung Xiu", "Lynda Bauer", "Tod Toddson",
				"Assistentarzt: Betina Smith" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

	}
}
