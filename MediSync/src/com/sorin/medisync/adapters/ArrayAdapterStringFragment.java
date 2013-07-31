package com.sorin.medisync.adapters;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.sorin.medisync.R;

public class ArrayAdapterStringFragment extends ListFragment {
	private static final String[] items = new String[] { "One", "Two", "Three",
			"Four", "Five" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.listview, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ListAdapter adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, items);
		setListAdapter(adapter);
	}
}
