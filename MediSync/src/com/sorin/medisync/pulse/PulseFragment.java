package com.sorin.medisync.pulse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import at.abraxas.amarino.AmarinoIntent;

import com.sorin.medisync.R;

public class PulseFragment extends Fragment {
	PulseGraphView pulseView;
	private static final String DEVICE_ADDRESS = "00:06:66:03:73:7B";
	private PulseGraphView pulseGraph;
	private TextView pulseValueView;
	private ArduinoReceiver arduinoReceiver;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate view
		View v = inflater.inflate(R.layout.bt_pulse, container, false);
		if (savedInstanceState == null) {

		} else {

		}
		pulseGraph = (PulseGraphView) v.findViewById(R.id.pulse_graph);
		pulseValueView = (TextView) v.findViewById(R.id.pulse_value);
		return v;

	}

	public PulseFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

	}

	/**
	 * ArduinoReceiver is responsible for catching broadcasted Amarino events.
	 * 
	 * It extracts data from the intent and updates the graph accordingly.
	 */
	public class ArduinoReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String data = null;

			// the device address from which the data was sent, we don't need it
			// here but to demonstrate how you retrieve it
			final String address = intent
					.getStringExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS);

			// the type of data which is added to the intent
			final int dataType = intent.getIntExtra(
					AmarinoIntent.EXTRA_DATA_TYPE, -1);

			// we only expect String data though, but it is better to check if
			// really string was sent
			// later Amarino will support differnt data types, so far data comes
			// always as string and
			// you have to parse the data to the type you have sent from
			// Arduino, like it is shown below
			if (dataType == AmarinoIntent.STRING_EXTRA) {
				data = intent.getStringExtra(AmarinoIntent.EXTRA_DATA);

				if (data != null) {
					pulseValueView.setText(data);
					try {
						// since we know that our string value is an int number
						// we can parse it to an integer
						final int sensorReading = Integer.parseInt(data);
						pulseGraph.addDataPoint(sensorReading);
					} catch (NumberFormatException e) { /*
														 * oh data was not an
														 * integer
														 */
					}
				}
			}
		}
	}
}