package com.sorin.medisync.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sorin.medisync.R;

public class ViewPagerFragment2 extends Fragment {
	private static String[] titles = new String[] { "X-Ray Sample 1",
			"X-Ray Sample 2", "X-Ray Sample 3", "X-Ray Sample 4",
			"X-Ray Sample 5" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.viewpager_2, container, false);
		ViewPager viewPager1 = (ViewPager) view.findViewById(R.id.viewpager1);
		ViewPager viewPager2 = (ViewPager) view.findViewById(R.id.viewpager2);

		viewPager1.setAdapter(new MyFragmentPagerAdapter(getFragmentManager()));
		viewPager2.setAdapter(new MyFragmentPagerAdapter(getFragmentManager()));

		return view;
	}

	private class MyPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TextView text = new TextView(getActivity());
			text.setText(titles[position]);
			container.addView(text);
			return text;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			if (object instanceof View) {
				container.removeView((View) object);
			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}
	}

	public static class MyFragment extends Fragment {
		private static final String TAG = "MyFragment";

		public MyFragment() {
			super();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.image_item, container, false);
			View text = view.findViewById(android.R.id.text1);
			if (text != null && text instanceof TextView) {
				((TextView) text).setText(titles[getArguments().getInt(
						"position")]);
			}
			return view;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			Log.d(TAG, "Attached " + getArguments().getInt("position"));
		}

		@Override
		public void onDetach() {
			Log.d(TAG, "Detached " + getArguments().getInt("position"));
			super.onDetach();
		}
	}

	private class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Bundle args = new Bundle();
			args.putInt("position", position);
			return Fragment.instantiate(getActivity(),
					MyFragment.class.getName(), args);
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}
	}

}
