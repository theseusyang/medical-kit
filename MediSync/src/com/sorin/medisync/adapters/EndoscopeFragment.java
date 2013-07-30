package com.sorin.medisync.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sorin.medisync.R;

public class EndoscopeFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.endoscope, container, false);

		return view;
	}

	public EndoscopeFragment() {
		super();
	}
}
