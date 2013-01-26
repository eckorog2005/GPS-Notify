/**
 * ScheduledActivity.java
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

import java.util.ArrayList;
import java.util.HashMap;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

/**
 * the scheduled list for the android gui
 * 
 * @author rlamb
 *
 * @version 1
 */
public class ScheduledActivity extends ListActivity {

	/**
	 * when activity is created
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//set list adapter
		SimpleAdapter adapterForList = new SimpleAdapter(
				this.getApplicationContext(), getData(), 
				R.layout.detailed_list_view, 
				new String[] {"task", "type", "occurrence"},
				new int[] {R.id.task, R.id.type, R.id.occurrence});

		setListAdapter(adapterForList);

		//get view and make active
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		/**
		 * do to do when an item is selected
		 */
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				@SuppressWarnings("unchecked")
				HashMap<String,String> text = (HashMap<String,String>) parent.getItemAtPosition(position);
				openScheduled(view, text.get("task"), text.get("type"));
			}
		});
	}

	/**
	 * ran when the window gaines or loses focus from user
	 */
	public void onWindowFocusChanged(boolean hasFocus){
		super.onWindowFocusChanged(hasFocus);

		//set list adapter
		SimpleAdapter adapterForList = new SimpleAdapter(
				this.getApplicationContext(), getData(), 
				R.layout.detailed_list_view, 
				new String[] {"task", "type", "occurrence"},
				new int[] {R.id.task, R.id.type, R.id.occurrence});

		setListAdapter(adapterForList);
	}
	
	/**
	 * get the data to add to the list
	 * 
	 * @return formatted data to enter
	 */
	private ArrayList<HashMap<String,String>> getData() {
		
		//load all scheduled tasks from the database
		ArrayList<ScheduledTask> list = 
			ScheduledFactory.getInstance(this.getApplicationContext()).loadAll();
		
		//output array
		ArrayList<HashMap<String,String>> out = 
			new ArrayList<HashMap<String,String>>();
		
		//build output data for each scheduled task
		for(int i = 0; i < list.size(); i++){
			ScheduledTask task = list.get(i);
			HashMap<String,String> temp = new HashMap<String,String>();
			temp.put("task", task.getTask().getTaskName());
			if(task instanceof ScheduledLocationTask){
				//location name
				temp.put("type", ((ScheduledLocationTask)task).getLocation().getName());
			}else{
				//format time
				String m ;
				int hour = ((ScheduledTimeTask)task).getTime().getHours();
				if(hour > 12){
					hour = hour- 12;
					m = "PM";
				}else{
					m = "AM";
				}
				String time = String.format("%02d:%02d:%02d %s", 
						hour, 
						((ScheduledTimeTask)task).getTime().getMinutes(), 
						((ScheduledTimeTask)task).getTime().getSeconds(), m);
				temp.put("type", time);
			}
			temp.put("occurrence", task.getOccurrence().name());
			out.add(temp);
		}
		return out;
	}

	/**
	 * open delete tab (currently an error)
	 * 
	 * @param view the view of the activity
	 * @param taskName the task name
	 */
	private void openScheduled(View view, final String taskName, final String type){
		
		//create alert to delete alarm or cancel
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Would you like to delete this task?")
		.setCancelable(false)
		.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int id) {
				String temp = "location";
				ScheduledFactory factory = ScheduledFactory.getInstance(getApplicationContext());
				if(type.contains(":00 AM") || type.contains(":00 PM")){
					temp = "time";
				}
				ScheduledTask task = factory.load(taskName, temp);
				if(task == null){
					task = factory.load(taskName, temp);
				}
				factory.delete(task);
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int id) {
				//do nothing
			}
		});
		final AlertDialog alert = builder.create();
		alert.show();
	}
}
