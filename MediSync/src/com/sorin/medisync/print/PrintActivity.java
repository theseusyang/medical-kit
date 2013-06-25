package com.sorin.medisync.print;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.sorin.medisync.R;

public class PrintActivity extends Activity {
	public Activity activity;
	private String title;
	private String message;
	private String buttonYes;
	private String buttonNo;

	public static final String DEFAULT_TITLE = "Install Google Cloud Print?";
	public static final String DEFAULT_MESSAGE = "This application requires Google Cloud Print. Would you like to install it?";
	public static final String DEFAULT_YES = "Yes";
	public static final String DEFAULT_NO = "No";
	private static final String GOOGLE_CLOUD_PRINT_PACKAGE = "com.google.android.apps.cloudprint";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// Put the package name here...
		boolean installed = appInstalledOrNot("com.google.android.apps.cloudprint");
		if (installed) {
			// This intent will help you to launch if the package is already
			// installed
			Intent LaunchIntent = getPackageManager()
					.getLaunchIntentForPackage(
							"com.google.android.apps.cloudprint");
			startActivity(LaunchIntent);

			System.out.println("App already installed om your phone");

		} else {
			showDownloadDialog();
			System.out.println("App is not installed om your phone");
		}
	}

	public AlertDialog showDownloadDialog() {
		AlertDialog.Builder downloadDialog = new AlertDialog.Builder(activity);
		downloadDialog.setTitle(title);
		downloadDialog.setMessage(message);
		downloadDialog.setPositiveButton(buttonYes,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						String packageName = GOOGLE_CLOUD_PRINT_PACKAGE;
						Uri uri = Uri.parse("market://details?id="
								+ packageName);
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						try {
							activity.startActivity(intent);
						} catch (ActivityNotFoundException anfe) {
							System.out.println("Market is not installed");
						}
					}
				});
		downloadDialog.setNegativeButton(buttonNo,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
					}
				});
		return downloadDialog.show();
	}

	private boolean appInstalledOrNot(String uri) {
		PackageManager pm = getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		return app_installed;
	}
}
