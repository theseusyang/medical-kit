package com.sorin.medisync.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapView;
import com.sorin.medisync.R;

public class HospitalMapFragment extends Fragment {

	MapView m;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// inflat and return the layout
		View v = inflater.inflate(R.layout.map_view, container, false);
		m = (MapView) v.findViewById(R.id.map_view_id);
		m.onCreate(savedInstanceState);

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		m.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		m.onPause();
	}
}
