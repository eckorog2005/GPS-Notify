/**
 * NewAlarmActivity.java
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

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewFlipper;

/**
 * This activity enables the user to make a new scheduled task.
 * 
 * @author rlamb
 *
 */
public class NewAlarmActivity extends Activity {

	/**
	 * when activity is created
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// load view
		setContentView(R.layout.new_alarm);

		//load spinners
		loadData();

		//load the type of schedule spinner
		Spinner spinner = (Spinner) findViewById(R.id.spinner2);
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
				this, R.array.timeLoc_array, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter2);
		spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());

		//save alarm
		findViewById(R.id.button2).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						if(checkData()){
							ScheduledFactory temp = 
								ScheduledFactory.getInstance(v.getContext());
							String type = getScheduleType();
							ScheduledTask task = null;
							//time task
							if(type.equals("Time")){
								ScheduledTimeTask timeTask = new ScheduledTimeTask();
								timeTask.setTime(getTime());
								task = timeTask;

								//location task
							}else{
								ScheduledLocationTask locTask = 
									new ScheduledLocationTask();
								locTask.setLocation(LocationFactory.getInstance(
										v.getContext()).load(getLocation()));
								task = locTask;
							}
							task.setTask(
									TaskFactory.getInstance(v.getContext()).load(
											getTaskName()));
							task.setOccurrence(getOccurrence());
							task.setLastUsed(new Date(0));
							temp.save(task);
							showToast();
						}
					}
				});
	}

	/**
	 * loads the data for each spinner
	 * 
	 */
	private void loadData() {
		int position = 0;
		String[] tasks = getTasks();
		String[] occurrences = getOccurrences();
		String[] locations = getLocations();

		//load the task spinner
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		position = spinner.getSelectedItemPosition();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_item, tasks);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		if(position < tasks.length){
			spinner.setSelection(position);
		}

		//load the occurrence list
		spinner = (Spinner) findViewById(R.id.spinner3);
		position = spinner.getSelectedItemPosition();
		ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(
				this.getApplicationContext(), 
				android.R.layout.simple_spinner_item, occurrences);
		adapter3.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter3);
		if(position < occurrences.length){
			spinner.setSelection(position);
		}

		//load the location list
		spinner = (Spinner) findViewById(R.id.spinner4);
		position = spinner.getSelectedItemPosition();
		ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(
				this.getApplicationContext(), 
				android.R.layout.simple_spinner_item, locations);
		adapter4.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter4);
		if(position < locations.length){
			spinner.setSelection(position);
		}
	}

	/**
	 * gets the task name
	 * 
	 * @return
	 */
	private String getTaskName(){
		return 
		((Spinner)findViewById(R.id.spinner1)).getSelectedItem().toString();
	}

	/**
	 * gets either time or location to identify scheduled task.
	 * 
	 * @return
	 */
	private String getScheduleType() {
		String temp = ((Spinner)findViewById(R.id.spinner2)).getSelectedItem().toString();
		return temp;
	}

	/**
	 * get the occurrence of alarm
	 * 
	 * @return
	 */
	private Occurrence getOccurrence(){
		return
		Occurrence.valueOf((
				(Spinner)findViewById(R.id.spinner3)).getSelectedItem()
				.toString());
	}

	/**
	 * get the location used
	 * 
	 * @return
	 */
	private String getLocation(){
		if(getScheduleType().equals("Location")){
			return 
			((Spinner)findViewById(R.id.spinner4)).getSelectedItem().toString();
		}else{
			return null;
		}
	}

	/**
	 * get the time
	 * 
	 * @return
	 */
	private Time getTime(){
		TimePicker time = (TimePicker)findViewById(R.id.timePicker1);
		int hour = time.getCurrentHour().intValue();
		int min = time.getCurrentMinute().intValue();
		Time date = new Time(hour, min, 0);
		date.setMinutes(min);
		return date;
	}


	/**
	 * get the locations names for the spinner
	 * 
	 * @return
	 */
	private String[] getLocations() {
		ArrayList<String> dbList = LocationFactory.getInstance(
				getBaseContext()).getKeys();
		String [] list = new String[dbList.size()];
		dbList.toArray(list);
		String[] out = new String[list.length];
		System.arraycopy(list, 0, out, 0, list.length);
		return out;
	}

	/**
	 * get the occurrences for the spinner
	 * 
	 * @return
	 */
	private String[] getOccurrences() {
		Occurrence[] values = Occurrence.values();
		String[] out = new String[values.length - 1];
		for (int i = 0; i < values.length - 1; i++) {
			out[i] = values[i+1].name();
		}
		return out;
	}

	/**
	 * get the task names for the spinner
	 * 
	 * @return
	 */
	private String[] getTasks() {
		ArrayList<String> dbList = TaskFactory.getInstance(
				getBaseContext()).getKeys();
		String [] list = new String[dbList.size()];
		dbList.toArray(list);
		String[] out = new String[list.length];
		System.arraycopy(list, 0, out, 0, list.length);
		return out;
	}

	/**
	 * ends the activity
	 * 
	 */
	public void finishTaskActivity(){
		this.finish();
	}

	/**
	 * change the input selections based on time or location
	 * 
	 * @param item
	 */
	public void flipView(String item){
		ViewFlipper flip = (ViewFlipper)findViewById(R.id.viewFlipper1);
		if(item.equals("Time")){
			flip.setDisplayedChild(0);
		}else{
			flip.setDisplayedChild(1);
		}
	}

	/**
	 * on occurrence spinner, listen for selection
	 * 
	 * @author rlamb
	 *
	 */
	public class MyOnItemSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent,
				View view, int pos, long id) {
			flipView(parent.getItemAtPosition(pos).toString());
		}

		public void onNothingSelected(AdapterView<?> parent) {
			// Do nothing.
		}
	}

	/**
	 * called when user sees the activity or leaves
	 * 
	 */
	public void onWindowFocusChanged(boolean hasFocus){
		super.onWindowFocusChanged(hasFocus);
		loadData();
	}

	/**
	 * check data before save
	 * 
	 * @return
	 */
	private boolean checkData() {
		boolean flag = false;
		//check data for the errors
		if(getTaskName() == null || getTaskName().equals("")){
			flag = true;
		}else if((getLocation() == null || getLocation().equals(""))&& getScheduleType().equals("Location")){
			flag = true;
		}else if(ScheduledFactory.getInstance(getApplicationContext()).load(getTaskName(),getScheduleType().toLowerCase()) != null){
			flag = true;
		}

		//if error, show alert
		if(flag){
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Error in entering data " +
					"(blank information/task name already used)")
			.setCancelable(false)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(final DialogInterface dialog, final int id) {
				
				}
			});
			final AlertDialog alert = builder.create();
			alert.show();
			return false;
		}
		return true;
	}
	
	/**
	 * show the save toast
	 */
	private void showToast(){
		Toast.makeText(
				getApplicationContext(), "new alarm saved", Toast.LENGTH_SHORT).show();
	}
}
