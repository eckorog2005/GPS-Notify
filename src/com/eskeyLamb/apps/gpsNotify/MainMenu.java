/**
 * MainMenu.java
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

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * Main menu tab for the android gui
 * 
 * @author rlamb
 *
 */
public class MainMenu extends TabActivity {

	/**
	 * when activity is created
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost();  // The activity TabHost
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


		tabHost.setCurrentTab(0);
	}
}
