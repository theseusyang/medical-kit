package com.sorin.medisync.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ArrayAdapterImageViewHolder implements LoaderCallbacks<Bitmap>
{
	private static final String TAG = ArrayAdapterImageFragment.TAG;

	private static int nextLoaderId = 0;
	private final int loaderId;
	private final Bundle bundle = new Bundle();
	private final Context context;
	private String name = null;
	public final TextView text;
	public final ImageView image;
	public final ProgressBar progress;

	public ArrayAdapterImageViewHolder(Context context, TextView text, ImageView image, ProgressBar progress)
	{
		this.context = context;
		this.text = text;
		this.image = image;
		this.progress = progress;
		this.loaderId = nextLoaderId++;
	}

	@Override
	public Loader<Bitmap> onCreateLoader(int id, Bundle args)
	{
		return new ArrayAdapterImageLoader(context, args.getString("name"));
	}

	@Override
	public void onLoadFinished(Loader<Bitmap> loader, Bitmap bitmap)
	{
		if(bitmap != null)
		{
			image.setImageBitmap(bitmap);
		}
		image.setVisibility(View.VISIBLE);
		progress.setVisibility(View.GONE);
	}

	@Override
	public void onLoaderReset(Loader<Bitmap> loader)
	{
		loader.abandon();
	}
	
	public void setName(String name)
	{
		text.setText(name);
		image.setVisibility(View.GONE);
		progress.setVisibility(View.VISIBLE);
		if(!name.equals(this.name) && context instanceof FragmentActivity)
		{
			bundle.putString("name", name);
			((FragmentActivity)context).getSupportLoaderManager().restartLoader(loaderId, bundle, this);
			Log.d(TAG, "Restarting loader: " + loaderId + " : " + name);
		}
	}

}
