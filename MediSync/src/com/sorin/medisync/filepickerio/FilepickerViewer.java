package com.sorin.medisync.filepickerio;

import io.filepicker.FilePicker;
import io.filepicker.FilePickerAPI;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.sorin.medisync.R;

public class FilepickerViewer extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filepicker_view);

		Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// Get an image using filepicker.io
				FilePickerAPI.setKey("AfUgxtOkfQhK3sDUTCqsez");
				Intent intent = new Intent(FilepickerViewer.this,
						FilePicker.class);
				intent.setType("image/*"); // only get images
				startActivityForResult(intent,
						FilePickerAPI.REQUEST_CODE_GETFILE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;

		Uri uri = data.getData();

		// print out the fpurl
		System.out.println("FPUrl: " + data.getExtras().getString("fpurl"));

		// Display the image
		ImageView imageView = (ImageView) findViewById(R.id.imageView1);
		try {
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(
					this.getContentResolver(), uri);
			// Scale the image since android get display images larger than 2048
			// in one direction
			int MAXSIZE = 2048;
			if (bitmap.getHeight() > MAXSIZE || bitmap.getWidth() > MAXSIZE) {
				// scale
				int h = bitmap.getHeight();
				int w = bitmap.getWidth();
				h = h * Math.min(w, MAXSIZE) / w;
				w = Math.min(w, MAXSIZE);
				w = w * Math.min(h, MAXSIZE) / h;
				h = Math.min(h, MAXSIZE);
				bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
			}
			imageView.setImageBitmap(bitmap);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
