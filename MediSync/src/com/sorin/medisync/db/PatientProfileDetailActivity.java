package com.sorin.medisync.db;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sorin.medisync.R;
import com.sorin.medisync.qr.IntentIntegratorQR;

/*
 * TodoDetailActivity allows to enter a new todo item 
 * or to change an existing
 */
public class PatientProfileDetailActivity extends Activity {
	private Spinner mCategory;
	private EditText mTitleText;
	private EditText mBodyText;
	private Button bTitle;
	private Button bBody;
	private Uri todoUri;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.patient_edit);

		mCategory = (Spinner) findViewById(R.id.category);
		mTitleText = (EditText) findViewById(R.id.todo_edit_summary);
		mBodyText = (EditText) findViewById(R.id.todo_edit_description);
		Button confirmButton = (Button) findViewById(R.id.todo_edit_button);

		Bundle extras = getIntent().getExtras();

		// Check from the saved Instance
		todoUri = (bundle == null) ? null : (Uri) bundle
				.getParcelable(PatientProfileContentProvider.CONTENT_ITEM_TYPE);

		// Or passed from the other activity
		if (extras != null) {
			todoUri = extras
					.getParcelable(PatientProfileContentProvider.CONTENT_ITEM_TYPE);

			fillData(todoUri);
		}
		// define button animation

		final Animation animAlpha = AnimationUtils.loadAnimation(this,
				R.anim.anim_alpha);
		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				view.startAnimation(animAlpha);

				if (TextUtils.isEmpty(mTitleText.getText().toString())) {
					makeToast();
				} else {
					setResult(RESULT_OK);
					finish();
				}
			}

		});

	}

	private void fillData(Uri uri) {
		String[] projection = { PatientProfileTable.COLUMN_SUMMARY,
				PatientProfileTable.COLUMN_DESCRIPTION,
				PatientProfileTable.COLUMN_CATEGORY };
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);
		if (cursor != null) {
			cursor.moveToFirst();
			String category = cursor
					.getString(cursor
							.getColumnIndexOrThrow(PatientProfileTable.COLUMN_CATEGORY));

			for (int i = 0; i < mCategory.getCount(); i++) {

				String s = (String) mCategory.getItemAtPosition(i);
				if (s.equalsIgnoreCase(category)) {
					mCategory.setSelection(i);
				}
			}

			mTitleText
					.setText(cursor.getString(cursor
							.getColumnIndexOrThrow(PatientProfileTable.COLUMN_SUMMARY)));
			mBodyText
					.setText(cursor.getString(cursor
							.getColumnIndexOrThrow(PatientProfileTable.COLUMN_DESCRIPTION)));

			// Always close the cursor
			cursor.close();
		}
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putParcelable(PatientProfileContentProvider.CONTENT_ITEM_TYPE,
				todoUri);
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}

	private void saveState() {
		String category = (String) mCategory.getSelectedItem();
		String summary = mTitleText.getText().toString();
		String description = mBodyText.getText().toString();

		// Only save if either summary or description
		// is available

		if (description.length() == 0 && summary.length() == 0) {
			return;
		}

		ContentValues values = new ContentValues();
		values.put(PatientProfileTable.COLUMN_CATEGORY, category);
		values.put(PatientProfileTable.COLUMN_SUMMARY, summary);
		values.put(PatientProfileTable.COLUMN_DESCRIPTION, description);

		if (todoUri == null) {
			// New todo
			todoUri = getContentResolver().insert(
					PatientProfileContentProvider.CONTENT_URI, values);
		} else {
			// Update todo
			getContentResolver().update(todoUri, values, null, null);
		}
	}

	private void makeToast() {
		Toast.makeText(PatientProfileDetailActivity.this,
				"Please maintain a summary", Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle action buttons
		switch (item.getItemId()) {

		case R.id.action_qrscan:
			// Get instance of Vibrator from current Context
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

			// Vibrate for 30 milliseconds
			v.vibrate(30);

			// TODO Auto-generated method stub
			IntentIntegratorQR integratorQR = new IntentIntegratorQR(
					PatientProfileDetailActivity.this);
			integratorQR.initiateScan();
			return true;
		default:
			// Handle your other action bar items...
			return super.onOptionsItemSelected(item);
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		com.sorin.medisync.qr.IntentResult scanResult = IntentIntegratorQR
				.parseActivityResult(requestCode, resultCode, intent);
		if (scanResult != null) {
			// handle scan result
			String contents = scanResult.getContents();
			mTitleText.setText("Patient " + (contents));
			mBodyText.setText("" + (contents));
		} else {
			// TODO Auto-generated method stub
			IntentIntegratorQR integratorQR = new IntentIntegratorQR(
					PatientProfileDetailActivity.this);
			integratorQR.initiateScan();
		}

	}
}