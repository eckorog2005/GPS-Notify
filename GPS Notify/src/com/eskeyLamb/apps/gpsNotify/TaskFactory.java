/**
 * GroupTaskFactory.java
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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * <p>This class is used to save and load Group and Task objects.
 * 
 * @author rlamb
 * 
 * @version 1
 * 
 */
public class TaskFactory {

	/**
	 * private variable to make singleton class
	 */
	private static TaskFactory instance = null;
	/**
	 * SQLite database helper for android
	 */
	private GPSNotifyDatabaseHelper dbhelper;

	/**
	 * get the single instance of this class
	 * 
	 * @param context android context object
	 * @return the single object
	 */
	public static TaskFactory getInstance(Context context) {
		if(instance == null) {
			instance = new TaskFactory(context);
		}
		return instance;
	}

	/**
	 * Constructor
	 * 
	 * @param context android context object
	 */
	private TaskFactory(Context context){
		this.dbhelper = GPSNotifyDatabaseHelper.getHelper(context);
	}

	/**
	 * Save the task object
	 * 
	 * @param task the task object to be saved to the database
	 */
	public void save(Task task){
		//intialize database for writing
		SQLiteDatabase db = this.dbhelper.getWritableDatabase();
		db.setLockingEnabled(true);

		//check if entered already
		Cursor cursor = db.query("Task", new String[]{"taskName"}, 
				"taskName = '"+task.getTaskName()+"'", null, null, null, null);

		if(!cursor.moveToNext()){
			//add content values to add to the database
			ContentValues values = new ContentValues();
			values = new ContentValues();
			values.put("taskName", task.getTaskName());
			values.put("numOfUses", task.getNumOfUses());
			db.insert("Task", null, values);
		}

		//close the database
		//db.close();
	}

	/**
	 * load the task object from the database by the task name
	 * 
	 * @param taskName name of the task
	 * @return the task object
	 */
	public Task load(String taskName){
		Task temp = null;

		//open the database
		SQLiteDatabase db = this.dbhelper.getWritableDatabase();
		db.setLockingEnabled(true);
		
		//search for the group name
		Cursor cursor = db.query("Task", 
				new String[]{"taskName", "numOfUses"}, 
				"taskName = '"+taskName+"'", null, null, 
				null, null);

		if(cursor.moveToNext()){
			temp = new Task(cursor.getString(0));
			temp.setNumOfUses(cursor.getLong(1));
		}

		//close the database
		//db.close();
		return temp;
	}

	/**
	 * load all the Task objects in the database
	 * 
	 * @return ArrayList of Task objects
	 */
	public ArrayList<Task> loadAll(){
		//declarations and db initialization
		ArrayList<Task> out = new ArrayList<Task>();
		SQLiteDatabase db = this.dbhelper.getWritableDatabase();
		db.setLockingEnabled(true);

		//search all the groups
		Cursor cursor = db.query("Task", 
				new String[]{"taskName", "numOfUses"}, 
				null, null, null, 
				null, null);
		while(cursor.moveToNext()){
			Task task = new Task(cursor.getString(0));
			task.setNumOfUses(cursor.getLong(1));
			out.add(task);
		}

		//close the database
		//db.close();
		return out;
	}

	/**
	 * Get the list of Task names in the database
	 * 
	 * @return ArrayList of Task names in the database
	 */
	public ArrayList<String> getKeys(){
		//declarations and db initialization
		ArrayList<String> out = new ArrayList<String>();
		SQLiteDatabase db = this.dbhelper.getWritableDatabase();
		db.setLockingEnabled(true);
		
		//get each group name
		Cursor cursor = db.query("Task", new String[]{"taskName"}, 
				null, null, null, null, "taskName COLLATE NOCASE ASC");
		//add each name to list
		while(cursor.moveToNext()){
			out.add(cursor.getString(0));
		}

		//close database
		//db.close();
		return out;
	}

	/**
	 * get the task ID in the database from the task name
	 * 
	 * @param taskName the name of the task
	 * @return the id of the task name, or -1 if not found
	 */
	public int getTaskID(String taskName){
		//declarations and db initialization
		int out;
		SQLiteDatabase db = this.dbhelper.getWritableDatabase();
		db.setLockingEnabled(true);
		
		//search the database for the task id
		Cursor cursor = db.query("Task", new String[]{"_id"}, 
				"taskName = '"+taskName+"'", null, null, null, null);
		//store id, or store the error id
		if(cursor.moveToNext()){
			out = cursor.getInt(0);
		}else{
			out = -1;
		}

		//close the database
		//db.close();
		return out;
	}

	/**
	 * delete the task in the database based off name
	 * 
	 * @param taskName the task name to delete
	 */
	public void delete(String taskName){
		//open the database
		SQLiteDatabase db = this.dbhelper.getWritableDatabase();
		db.setLockingEnabled(true);
		
		//delete the task
		db.delete("Task", "taskName = '"+taskName+"'", null);

		//close the database
		//db.close();
	}


	/**
	 * check to see if the task name is already in the database
	 * 
	 * @param name the name of the task to check
	 * @return true if name exists, otherwise false
	 */
	public boolean checkTaskName(String name){
		//open the database
		SQLiteDatabase db = this.dbhelper.getWritableDatabase();
		db.setLockingEnabled(true);
		
		//search for name
		Cursor cursor = db.query("Task", new String[]{"taskName"}, 
				"taskName = '"+name+"'", null, null, null, null);

		//close database
		//db.close();

		//if found return true, else false
		if(cursor.moveToNext()){
			return true;
		}else{
			return false;
		}
	}
}
