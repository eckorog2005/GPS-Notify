/**
 * NewTaskActivity.java
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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

/**
 * New Task activity list for the android gui
 * 
 * @author rlamb
 *
 * @version 1
 */
public class NewTaskActivity extends Activity {

	/**
	 * when activity is created
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.new_task);

		String name = this.getIntent().getStringExtra("task");
		if(!name.equals("New Task")){
			EditText temp = (EditText)findViewById(R.id.editText1);
			temp.setText(name);
		}

		findViewById(R.id.button1).setOnClickListener(
				new OnClickListener() {
					public void onClick(final View v) {
						final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
						builder.setMessage("Deleting this task can affect " +
								"currently scheduled alarms, Continue?")
						.setCancelable(false)
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog, final int id) {
								ScheduledFactory schFactory = 
									ScheduledFactory.getInstance(v.getContext());
								TaskFactory temp = 
									TaskFactory.getInstance(v.getContext());
								ArrayList<ScheduledTask> list = schFactory.loadAll();
								for(ScheduledTask item:list){
									if(item instanceof ScheduledLocationTask){
										if(item.getTask().equals(getTextBox())){
											schFactory.delete(item);
										}
									}
								}
								temp.delete(getTextBox());
								finishTaskActivity();
							}
						}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								
							}
						});
						final AlertDialog alert = builder.create();
						alert.show();
					}
				});

		findViewById(R.id.button2).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						if(checkData()){
							TaskFactory temp = 
								TaskFactory.getInstance(v.getContext());
							Task task = new Task(getTextBox());
							temp.save(task);
							finishTaskActivity();
						}
					}
				});
	}

	/**
	 * finish this activity
	 */
	public void finishTaskActivity(){
		this.finish();
	}

	/**
	 * get data from the text box
	 * 
	 * @return
	 */
	public String getTextBox(){
		return ((EditText)findViewById(R.id.editText1))
		.getText().toString();
	}

	/**
	 * check data before save
	 * 
	 * @return
	 */
	private boolean checkData() {
		boolean flag = false;
		
		//see if any data is blank
		if(getTextBox() == null || getTextBox().equals("")){
			flag = true;
		}else if(TaskFactory.getInstance(getApplicationContext()).checkTaskName(getTextBox())){
			flag = true;
		}

		//show alert if incorrect data
		if(flag){
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Error in entering data " +
			"(blank task name or name already exists)")
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
}
