/**
 * TaskActivity.java
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

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * This activity list all the tasks in the database for user manipulation.
 * 
 * @author rlamb
 * 
 * @version 1
 *
 */
public class TaskActivity extends ListActivity {

	/**
	 * when activity is created
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//set list adapter
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, makeList()));

		//get view and make active
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		/**
		 * do to do when an item is selected
		 */
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				openTask(view);
			}
		});
	}
	
	/**
	 * called when the activity gains or loses focus.
	 * 
	 * @param hasFocus boolean to see if window has focus or not.
	 */
	public void onWindowFocusChanged(boolean hasFocus){
		super.onWindowFocusChanged(hasFocus);
		
		//set list adapter
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, makeList()));	
	}
	

	/**
	 * make the list of locations
	 * 
	 * @return list of strings
	 */
	private String[] makeList() {
		// TODO Auto-generated method stub
		ArrayList<String> dbList = TaskFactory.getInstance(
				getBaseContext()).getKeys();
		String [] list = new String[dbList.size()];
		dbList.toArray(list);
		String[] out = new String[list.length+1];
		out[0] = "New Task";
		System.arraycopy(list, 0, out, 1, list.length);
		return out;
	}

	/**
	 * make new task
	 */
	private void openTask(View view){
		Intent i = new Intent(this, NewTaskActivity.class);
		i.putExtra("task", ((TextView) view).getText().toString());
		startActivity(i);
	}
}
