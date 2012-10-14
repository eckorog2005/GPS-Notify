/**
 * GPSNotifyActivity.java
 */

/**
 *  Copyright 2012 Roger Lamb 
 * 
 *  This file is part of GPS Notify.
 *
 *  GPS Notify is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  GPS Notify is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with GPS Notify.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.eskeyLamb.apps.gpsNotify;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

/**
 * the main page of the android app
 * 
 * @author rlamb
 *
 */
public class GPSNotifyActivity extends TabActivity implements OnTabChangeListener{

	private TabHost tabHost;

	/**
	 * when activity is created
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Resources res = getResources(); // Resource object to get Drawables
		tabHost = getTabHost();  // The activity TabHost
		TabHost.TabSpec spec;  // Resusable TabSpec for each tab
		Intent intent;  // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, ScheduledActivity.class);

		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("Scheduled").setIndicator("Scheduled",
				res.getDrawable(R.drawable.ic_tab_location))
				.setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, TaskActivity.class);
		spec = tabHost.newTabSpec("Task").setIndicator("Task",
				res.getDrawable(R.drawable.ic_tab_location))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, LocationActivity.class);
		spec = tabHost.newTabSpec("Location").setIndicator("Location",
				res.getDrawable(R.drawable.ic_tab_location))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, NewAlarmActivity.class);
		spec = tabHost.newTabSpec("New Alarm").setIndicator("New Alarm",
				res.getDrawable(R.drawable.ic_tab_location))
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setOnTabChangedListener(this);
		tabHost.setCurrentTab(0);
	}

	@Override
	public void onStart(){
		super.onStart();
		//start service
		final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

		//check if GPS is on
		if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
			buildAlertMessageNoGps();
		}else{
			startService(new Intent(this, GPSNotifyService.class));
		}
	}

	@Override
	/**
	 * when tab is changed
	 */
	public void onTabChanged(String tabId) {
		tabHost.getCurrentView().onWindowFocusChanged(true);
		//mBoundService.runService();
	}

	@Override
	/**
	 * when Activity is destroyed
	 */
	public void onDestroy(){
		super.onDestroy();
		stopService(new Intent(this, GPSNotifyService.class));
	}

	/**
	 * builds alert box for no GPS signal
	 */
	private void buildAlertMessageNoGps() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Your GPS seems to be disabled")
		.setCancelable(false)
		.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int id) {
				startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			}
		})
		.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int id) {
				finish();
			}
		});
		final AlertDialog alert = builder.create();
		alert.show();
	}
}