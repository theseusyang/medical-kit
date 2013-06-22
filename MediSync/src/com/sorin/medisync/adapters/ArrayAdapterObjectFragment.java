package com.sorin.medisync.adapters;

import java.util.ArrayList;
import java.util.List;

import com.sorin.medisync.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class ArrayAdapterObjectFragment extends ListFragment
{
	private static final List<Item> items = new ArrayList<Item>();

	private static class Item
	{
		public final String line1;
		public final String line2;
		
		public Item(String line1, String line2)
		{
			this.line1 = line1;
			this.line2 = line2;
		}
	}
	
	public static class ViewHolder 
	{
		public final TextView text1;
		public final TextView text2;
		
		public ViewHolder(TextView text1, TextView text2)
		{
			this.text1 = text1;
			this.text2 = text2;
		}
	}
	
	private class ItemAdapter extends ArrayAdapter<Item>
	{

		public ItemAdapter(Context context)
		{
			super(context, android.R.layout.simple_list_item_2, items);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View view = convertView;
			ViewHolder holder = null;
			if(view == null)
			{
				view = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
				TextView text1 = (TextView)view.findViewById(android.R.id.text1);
				TextView text2 = (TextView)view.findViewById(android.R.id.text2);
				view.setTag(new ViewHolder(text1, text2));
			}
			if(holder == null && view != null)
			{
				Object tag = view.getTag();
				if(tag instanceof ViewHolder)
				{
					holder = (ViewHolder)tag;
				}
			}
			Item item = getItem(position);
			if(item != null && holder != null)
			{
				holder.text1.setText(item.line1);
				holder.text2.setText(item.line2);
			}
			return view;
		}
	}
	
	static
	{
		items.add(new Item("Title One", "Subtitle One"));
		items.add(new Item("Title Two", "Subtitle Two"));
		items.add(new Item("Title Three", "Subtitle Three"));
		items.add(new Item("Title Four", "Subtitle Four"));
		items.add(new Item("Title Five", "Subtitle Five"));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.list_view, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		ListAdapter adapter = new ItemAdapter(getActivity());
		setListAdapter(adapter);
	}
}
