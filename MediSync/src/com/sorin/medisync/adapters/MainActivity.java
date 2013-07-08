package com.sorin.medisync.adapters;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.sorin.medisync.R;
import com.sorin.medisync.adapters.ShakeDetectorActivity.OnShakeListener;
import com.sorin.medisync.bt.DeviceListActivity;
import com.sorin.medisync.data.ListViewAdapter;
import com.sorin.medisync.data.ListViewItemModel;
import com.sorin.medisync.filepickerio.FilepickerSaver;
import com.sorin.medisync.filepickerio.FilepickerViewer;
import com.sorin.medisync.map.InfoMapActivity;
import com.sorin.medisync.qr.IntentIntegratorQR;

public class MainActivity extends FragmentActivity {

	// constant containing package name string
	private static final String CLOUDPRINT_PACKAGE_NAME = "com.google.android.apps.cloudprint";

	private int selection = 0;
	private int oldSelection = -1;
	// list view arrays
	private String[] menuItemsData;
	private String[] menuItemsTools;
	private String[] menuItemsEmergency;
	// list view names and classes
	private String[] listNames = null;
	private String[] listClasses = null;
	public static Camera cam = null;// has to be static, otherwise onDestroy()
									// destroys it
	private ActionBarDrawerToggle drawerToggle = null;
	private DrawerLayout drawerLayout = null;
	private ListView navList = null;

	private static final String OPENED_KEY = "OPENED_KEY";
	private SharedPreferences prefs = null;
	private Boolean opened = null;
	// The following are used for the shake detection
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private ShakeDetectorActivity mShakeDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.medisync_main);

		// ShakeDetector initialization
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mShakeDetector = new ShakeDetectorActivity();
		mShakeDetector.setOnShakeListener(new OnShakeListener() {

			@Override
			public void onShake(int count) {
				// Closes main activity when shaking phone
				MainActivity.this.finish();

			}

		});

		listNames = getResources().getStringArray(R.array.all_list_item_titles);
		listClasses = getResources().getStringArray(R.array.nav_classes);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		navList = (ListView) findViewById(R.id.drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// we need to detect when the drawer settles to an open or closed state,
		// and then change the title text accordingly
		// we can now do this through our ActionBarDrawerToggle instance by
		// overriding the onDrawerOpened() and onDrawerClosed() methods, and we
		// can also restore our item selection:
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.open, R.string.close) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				updateContent();
				invalidateOptionsMenu();
				if (opened != null && opened == false) {
					opened = true;
					if (prefs != null) {
						Editor editor = prefs.edit();
						editor.putBoolean(OPENED_KEY, true);
						editor.apply();
					}
				}
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(R.string.app_name);
				invalidateOptionsMenu();
			}
		};
		drawerLayout.setDrawerListener(drawerToggle);

		navList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int pos, long id) {
				selection = pos;
				drawerLayout.closeDrawer(navList);
			}
		});

		updateContent();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		new Thread(new Runnable() {

			@Override
			public void run() {
				prefs = getPreferences(MODE_PRIVATE);
				opened = prefs.getBoolean(OPENED_KEY, false);
				if (opened == false) {
					drawerLayout.openDrawer(navList);
				}
			}

		}).start();
		// initializes list view menu
		_initMenu();
		updateContent();
	}

	@Override
	public void onResume() {
		super.onResume();
		// Add the following line to register the Session Manager Listener
		// onResume
		mSensorManager.registerListener(mShakeDetector, mAccelerometer,
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	public void onPause() {
		// Add the following line to unregister the Sensor Manager onPause
		mSensorManager.unregisterListener(mShakeDetector);
		super.onPause();
	}

	@Override
	protected void onResumeFragments() {
		// TODO Auto-generated method stub
		super.onResumeFragments();
		updateContent();
	}

	private void _initMenu() {
		ListViewAdapter mAdapter = new ListViewAdapter(this);

		// Add First Header
		mAdapter.addHeader(R.string.menu_data);

		// Add first block

		menuItemsData = getResources().getStringArray(R.array.menu_data);
		String[] menuDataIcons = getResources().getStringArray(
				R.array.data_menu_icons);

		int dataIcons = 0;
		for (String item : menuItemsData) {

			int id_data_title = getResources().getIdentifier(item, "string",
					this.getPackageName());
			int id_data_icon = getResources()
					.getIdentifier(menuDataIcons[dataIcons], "drawable",
							this.getPackageName());

			ListViewItemModel mItem = new ListViewItemModel(id_data_title,
					id_data_icon);
			mAdapter.addItem(mItem);
			dataIcons++;
		}
		// Add second header

		mAdapter.addHeader(R.string.menu_tools);
		// Add second block
		menuItemsTools = getResources().getStringArray(R.array.menu_tools);
		String[] menuToolsIcons = getResources().getStringArray(
				R.array.tools_menu_icons);

		int toolsIcons = 0;
		for (String item : menuItemsTools) {

			int id_tools_title = getResources().getIdentifier(item, "string",
					this.getPackageName());
			int id_tools_icon = getResources().getIdentifier(
					menuToolsIcons[toolsIcons], "drawable",
					this.getPackageName());
			// creating drawer menu model
			ListViewItemModel mItem = new ListViewItemModel(id_tools_title,
					id_tools_icon);
			mAdapter.addItem(mItem);
			toolsIcons++;
		}
		// Add third header

		mAdapter.addHeader(R.string.menu_emergency);
		// Add third block
		menuItemsEmergency = getResources().getStringArray(
				R.array.menu_emergency);
		String[] menuEmerIcons = getResources().getStringArray(
				R.array.emergency_menu_icons);

		int emerIcons = 0;

		for (String item : menuItemsEmergency) {

			int id_emer_title = getResources().getIdentifier(item, "string",
					this.getPackageName());
			int id_emer_icon = getResources()
					.getIdentifier(menuEmerIcons[emerIcons], "drawable",
							this.getPackageName());

			// creating drawer menu model
			ListViewItemModel mItem = new ListViewItemModel(id_emer_title,
					id_emer_icon);
			mAdapter.addItem(mItem);
			emerIcons++;
		}

		if (navList != null)
			navList.setAdapter(mAdapter);

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {

		case R.id.action_print:

			try {
				Intent LaunchIntent = getPackageManager()
						.getLaunchIntentForPackage(
								"com.google.android.apps.cloudprint");
				startActivity(LaunchIntent);
			} catch (Exception e) {
				String packageName = CLOUDPRINT_PACKAGE_NAME;
				Uri uri = Uri.parse("market://details?id=" + packageName);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
				Toast.makeText(
						this,
						"Google Cloud Print not installed.\nPlease install "
								+ "Cloud Print" + " from Google Play Store.",
						Toast.LENGTH_LONG).show();
			}

			return true;
		case R.id.action_qrscan:
			// Get instance of Vibrator from current Context
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

			// Vibrate for 300 milliseconds
			v.vibrate(300);

			IntentIntegratorQR integratorQR = new IntentIntegratorQR(
					MainActivity.this);
			integratorQR.initiateScan();

			Toast.makeText(this, "Scan Qr Code", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_filepicker_save:

			startActivity(new Intent(this, FilepickerSaver.class));

			Toast.makeText(this, "Save data on cloud services",
					Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_filepicker_view:

			startActivity(new Intent(this, FilepickerViewer.class));

			Toast.makeText(this, "View data from cloud services",
					Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_map:

			startActivity(new Intent(this, InfoMapActivity.class));

			return true;
		case R.id.action_bt:

			startActivity(new Intent(this, DeviceListActivity.class));

			Toast.makeText(this, "View data from cloud", Toast.LENGTH_SHORT)
					.show();
			return true;
		case R.id.action_websearch:
			// create intent to perform web search for this planet
			Intent intent = new Intent(Intent.ACTION_SEARCH);
			intent.putExtra(SearchManager.QUERY, getApplicationContext()
					.getDatabasePath(DROPBOX_SERVICE));
			// catch event that there's no activity to handle intent
			if (intent.resolveActivity(getPackageManager()) != null) {
				startActivity(intent);
			} else {
				Toast.makeText(this, R.string.action_search_database,
						Toast.LENGTH_LONG).show();
			}
			return true;
		default:
			// Handle your other action bar items...
			return super.onOptionsItemSelected(item);
		}
	}

	// make sure that any menus specific to the content panel is hidden when the
	// drawer is visible as it could make things confusing for the user if there
	// are actions available on content which is not in scope because the
	// navigation drawer is visible.
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (drawerLayout != null && navList != null) {
			MenuItem item = menu.findItem(R.id.action_qrscan);
			if (item != null) {
				item.setVisible(!drawerLayout.isDrawerOpen(navList));
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}

	private void updateContent() {
		getActionBar().setTitle(listNames[selection]);
		if (selection != oldSelection) {
			FragmentTransaction tx = getSupportFragmentManager()
					.beginTransaction();
			tx.replace(R.id.main, Fragment.instantiate(MainActivity.this,
					listClasses[selection]));
			tx.commit();
			oldSelection = selection;
		}
	}

}