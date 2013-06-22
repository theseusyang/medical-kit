package com.sorin.medisync.adapters;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.sorin.medisync.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.AsyncTaskLoader;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Please note: This code is designed to demonstrate some
 * basic concepts for loading images for display within
 * ListViews. It is not production standard because
 * it does not handle lack of connectivity, and
 * should support image caching to further 
 * improve performance.
 * Please refer to the accompanying article for
 * more information:
 * http://blog.stylingandroid.com/archives/1737
 */

public class ArrayAdapterImageLoader extends AsyncTaskLoader<Bitmap>
{
	private static final String TAG = ArrayAdapterImageFragment.TAG;

	private static String density = null;

	private final String name;
	private boolean cancelled = false;

	public ArrayAdapterImageLoader(Context context, String name)
	{
		super(context);
		this.name = name;
		if (density == null)
		{
			DisplayMetrics dm = context.getResources().getDisplayMetrics();
			switch (dm.densityDpi)
			{
			case DisplayMetrics.DENSITY_LOW:
				density = "ldpi";
				break;
			case DisplayMetrics.DENSITY_MEDIUM:
				density = "mdpi";
				break;
			case DisplayMetrics.DENSITY_HIGH:
				density = "hdpi";
				break;
			case DisplayMetrics.DENSITY_XHIGH:
				density = "xhdpi";
				break;
			default:
				density = "mdpi";
				break;
			}
		}
	}

	@Override
	public Bitmap loadInBackground()
	{
		URL url = null;
		Bitmap bitmap = null;
		HttpURLConnection http = null;
		try
		{
			url = new URL(getContext().getString(R.string.image_base_url, density, name));
			URLConnection conn = url.openConnection();
			if(conn instanceof HttpURLConnection)
			{
				http = (HttpURLConnection)conn;
				if(http.getResponseCode() == HttpURLConnection.HTTP_OK && http.getContentType().startsWith("image"))
				{
					bitmap = BitmapFactory.decodeStream(http.getInputStream());
				}
			}
		}
		catch (Exception e)
		{
			Log.e(TAG, "Error loading image", e);
		}
		finally
		{
			if(http != null)
			{
				http.disconnect();
			}
		}
		return cancelled ? null : bitmap;
	}
	
	@Override
	protected void onStartLoading()
	{
		super.onStartLoading();
		forceLoad();
	}
	
	@Override
	protected void onAbandon()
	{
		super.onAbandon();
		cancelled = true;
	}

}
