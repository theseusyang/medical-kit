package com.sorin.medisync.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sorin.medisync.R;

public class ArrayAdapterImageFragment extends ListFragment {
	public static final String TAG = "ArrayAdapterImageFragment";
	private static final List<String> items = new ArrayList<String>();

	private class ItemAdapter extends ArrayAdapter<String> {

		public ItemAdapter(Context context) {
			super(context, android.R.layout.simple_list_item_2, items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ArrayAdapterImageViewHolder holder = null;
			if (view == null) {
				view = LayoutInflater.from(getContext()).inflate(
						R.layout.image_item, parent, false);
				TextView text = (TextView) view
						.findViewById(android.R.id.text1);
				ImageView image = (ImageView) view
						.findViewById(android.R.id.icon);
				ProgressBar progress = (ProgressBar) view
						.findViewById(android.R.id.progress);
				view.setTag(new ArrayAdapterImageViewHolder(getContext(), text,
						image, progress));
			}
			if (holder == null && view != null) {
				Object tag = view.getTag();
				if (tag instanceof ArrayAdapterImageViewHolder) {
					holder = (ArrayAdapterImageViewHolder) tag;
				}
			}
			String item = getItem(position);
			if (item != null && holder != null) {
				holder.setName(item);
			}
			return view;
		}
	}

	static {
		items.add("emo_im_angel");
		items.add("emo_im_cool");
		items.add("emo_im_embarrassed");
		items.add("emo_im_foot_in_mouth");
		items.add("emo_im_happy");
		items.add("emo_im_kissing");
		items.add("emo_im_laughing");
		items.add("emo_im_lips_are_sealed");
		items.add("emo_im_money_mouth");
		items.add("emo_im_sad");
		items.add("emo_im_surprised");
		items.add("emo_im_tongue_sticking_out");
		items.add("emo_im_undecided");
		items.add("emo_im_winking");
		items.add("emo_im_wtf");
		items.add("emo_im_yelling");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.listview, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ListAdapter adapter = new ItemAdapter(getActivity());
		setListAdapter(adapter);
	}

}
