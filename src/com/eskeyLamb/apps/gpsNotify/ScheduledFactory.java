/**
 * ScheduledFactory.java
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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * This class is used to load and save Scheduled Tasks.
 * 
 * @author rlamb
 * 
 * @version 1
 *
 */
public class ScheduledFactory {

	/**
	 * private instance of the class for singleton format
	 */
	private static ScheduledFactory instance = null;
	/**
	 * SQLite database helper for android
	 */
	private GPSNotifyDatabaseHelper dbhelper;
	/**
	 * holds the context of the android app to call other factories.
	 */
	private Context context;

	/**
	 * get the single instance of this class
	 * 
	 * @param context android context object
	 * @return the single object
	 */
	public static ScheduledFactory getInstance(Context context) {
		if(instance == null) {
			instance = new ScheduledFactory(context);
		}
		return instance;
	}

	/**
	 * Constructor
	 * 
	 * @param context context android context object
	 */
	private ScheduledFactory(Context context){
		this.dbhelper = GPSNotifyDatabaseHelper.getHelper(context);
		this.context = context;
	}

	/**
	 * save a scheduled task to the database
	 * 
	 * @param task the scheduled task to save
	 */
	public void save(ScheduledTask task){
		//declarations
		int taskID;

		//save the task
		TaskFactory.getInstance(context).save(task.getTask());

		//get the task id
		taskID = TaskFactory.getInstance(context).getTaskID(
				task.getTask().getTaskName());

		//save the scheduled task as a time or location one
		if(task instanceof ScheduledTimeTask){
			ScheduledTimeTaskDBWrite((ScheduledTimeTask) task, taskID);
		}else{
			ScheduledLocationTaskDBWrite((ScheduledLocationTask) task, taskID);
		}
	}

	/**
	 * Write a scheduled location task to the database
	 * 
	 * @param task scheduled location task to save
	 * @param taskID the task id of the scheduled object
	 */
	private void ScheduledLocationTaskDBWrite(ScheduledLocationTask task, 
			int taskID) {
		//declarations
		int locID;
		ContentValues values = new ContentValues();

		//open database for writing
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		db.setLockingEnabled(true);

		ScheduledTask temp = this.load(task.getTask().getTaskName(), "location");
		
		if(temp ==null){
		//save the location
		LocationFactory.getInstance(context).save(task.getLocation());

		//get the location id
		locID = LocationFactory.getInstance(context).getID(
				task.getLocation().getName());

		//write scheduled location to the database
		values.put("taskID", taskID);
		values.put("locationID", locID);
		values.put("occurrence", task.getOccurrence().name());
		values.put("note", task.getNote());
		values.put("lastUsed", task.getLastUsed().getTime());
		values.put("alartReady", Boolean.toString(task.getAlarmReady()));
		db.insert("ScheduledLocationTask", null, values);
		}else{
			values.put("lastUsed", task.getLastUsed().getTime());
			values.put("alartReady", Boolean.toString(task.getAlarmReady()));
			db.update("ScheduledLocationTask", values, "taskID = "+taskID+"", null);
		}
		//close the database
		//db.close();
	}

	/**
	 * Write a scheduled loaction task to the database
	 * 
	 * @param task scheduled location task to save
	 * @param taskID the task id of the scheduled object
	 */
	private void ScheduledTimeTaskDBWrite(ScheduledTimeTask task, int taskID) {
		//declarations
		ContentValues values = new ContentValues();

		//open the writable database
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		db.setLockingEnabled(true);

		
		ScheduledTask temp = load(task.getTask().getTaskName(), "time");
		if(temp == null){
		
		//write the scheduled time task
		values.put("taskID", taskID);
		values.put("time", task.getTime().getTime());
		values.put("occurrence", task.getOccurrence().name());
		values.put("note", task.getNote());
		values.put("lastUsed", task.getLastUsed().getTime());
		db.insert("ScheduledTimeTask", null, values);

		}else{
			values.put("lastUsed", task.getLastUsed().getTime());
			db.update("ScheduledTimeTask", values, "taskID = "+taskID+"", null);
		}
		//close the database
		//db.close();
	}

	/**
	 * load the scheduled task from the database
	 * 
	 * @param taskName the scheduled task name to load
	 * @return the schedule task object or null if not found
	 */
	public ScheduledTask load(String taskName, String type){
		//declarations
		ScheduledTask out = null;
		
		//open the database for writing
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		db.setLockingEnabled(true);
		
		//query for the task name
		Cursor cursor = db.query("Task", new String[]{"_id", "taskName"}, 
				"taskName = '"+taskName+"'", null, null, null, null);
		
		//if found, load the task object, else return null
		if(cursor.moveToNext()){
			Task task = TaskFactory.getInstance(this.context).load(
					cursor.getString(1));
			int id = cursor.getInt(0);
			//query to see if the task is a location task.
			cursor = db.query("ScheduledLocationTask", new String[]{"locationID", "occurrence", "note", "lastUsed", "alartReady"},
					"taskID = '"+id+"'", null, null, null, null);
			//if location task, load as a ScheduledLocationTask, else see if 
			//time task
			if(cursor.moveToNext() && type.equals("location")){
				//load the scheduled location data
				id = cursor.getInt(0);
				Occurrence occurrence = Occurrence.valueOf(cursor.getString(1));
				String note = cursor.getString(2);
				Date date = new Date(cursor.getLong(3));
				boolean flag = Boolean.parseBoolean(cursor.getString(4));
				//search for the location data and load up
				cursor = db.query("Location", new String[]{"locationName"},
						"_id = '"+id+"'", null, null, null, null);
				cursor.moveToNext();
				out = new ScheduledLocationTask(
						LocationFactory.getInstance(context).load(
								cursor.getString(0)), task, flag);
				out.setOccurrence(occurrence);
				out.setLastUsed(date);
				out.setNote(note);
			}else{
				//see if the task is a scheduled time task
				cursor = db.query("ScheduledTimeTask", new String[]{"time", "occurrence", "note", "lastUsed"},
						"taskID = '"+id+"'", null, null, null, null);
				if(cursor.moveToNext() && type.equals("time")){
					//load up the time task data
					out = new ScheduledTimeTask(new Time(cursor.getLong(0)), task);
					Date date = new Date(cursor.getLong(3));
					out.setLastUsed(date);
					out.setOccurrence(Occurrence.valueOf(cursor.getString(1)));
					out.setNote(cursor.getString(2));
				}
			}
		}
		
		//close the database
		//db.close();
		return out;
	}
	
	/**
	 * delete the scheduled task from the database
	 * 
	 * @param scheduledTask the scheduled task to remove
	 */
	public void delete(ScheduledTask scheduledTask){		
		//see which scheduledTask it is and call the correct function
		if(scheduledTask instanceof ScheduledTimeTask){
			this.deleteScheduledTimeTask((ScheduledTimeTask)scheduledTask);
		}else if(scheduledTask instanceof ScheduledLocationTask){
			this.deleteScheduledLocationTask((ScheduledLocationTask)scheduledTask);
		}
	}

	/**
	 * delete a scheduled time task
	 * 
	 * @param scheduledTask the task to delete
	 */
	public void deleteScheduledTimeTask(ScheduledTimeTask scheduledTask){
		//open the database
		SQLiteDatabase db = this.dbhelper.getWritableDatabase();
		db.setLockingEnabled(true);
		
		//search for the id
		Cursor cursor = db.query("ScheduledTimeTask join Task on taskID = Task._id",
				new String[]{"ScheduledTimeTask._id"}, "Task.taskName = '"+
				scheduledTask.getTask().getTaskName()+"' and time = '"+
				scheduledTask.getTime().getTime()+"'", null, null, null, null);
		
		if(cursor.moveToNext()){
			db.delete("ScheduledTimeTask", "_id = "+cursor.getInt(0), null);
		}
		
		//close the database
		//db.close();
	}
	
	/**
	 * delete a scheduled location task from the database
	 * 
	 * @param scheduledTask the task to delete
	 */
	public void deleteScheduledLocationTask(ScheduledLocationTask scheduledTask){
		//open the database
		SQLiteDatabase db = this.dbhelper.getWritableDatabase();
		db.setLockingEnabled(true);
		
		//search for the id
		Cursor cursor = db.query("ScheduledLocationTask join Task, Location on"+
				"(taskID = Task._id and locationID = Location._id)",
				new String[]{"ScheduledLocationTask._id"}, "Task.taskName = '"+
				scheduledTask.getTask().getTaskName()+"' and locationName = '"+
				scheduledTask.getLocation().getName()+"'", null, null, null, null);
		
		if(cursor.moveToNext()){
			db.delete("ScheduledLocationTask", "_id = "+cursor.getInt(0), null);
		}
		
		//close the database
		//db.close();
	}

	/**
	 * loads all the scheduled tasks in the database
	 * 
	 * @return an Arraylist of scheduledTasks
	 */
	public ArrayList<ScheduledTask> loadAll(){
		//declarations
		ArrayList<ScheduledTask> out = new ArrayList<ScheduledTask>();
		
		//open the readable database
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		db.setLockingEnabled(true);
		
		//get all tasks that are scheduled
		Cursor cursor = db.query("Task join ScheduledLocationTask on " +
				"(ScheduledLocationTask.taskID = Task._id)", 
				new String[]{"taskName"},null, null, null, null, null);
				
		//load each task found
		while(cursor.moveToNext()){
			out.add(this.load(cursor.getString(0), "location"));
		}
		
		//get all tasks that are scheduled
		cursor = db.query("Task join ScheduledTimeTask on " +
				"(ScheduledTimeTask.taskID = Task._id)", 
				new String[]{"taskName"},null, null, null, null, null);
		
		//load each task found
		while(cursor.moveToNext()){
			out.add(this.load(cursor.getString(0), "time"));
		}
		
		
		//close the database
		//db.close();
		
		return out;
	}

	/**
	 * get the names of the scheduled tasks
	 * 
	 * @return an Arraylist of task names
	 */
	public ArrayList<String> getKeys(){
		//declarations
		ArrayList<String> out = new ArrayList<String>();
		
		//open the readable database
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		db.setLockingEnabled(true);
		
		//find all the tasks that are scheduled
		Cursor cursor = db.query("Task join ScheduledLocationTask, " +
				"ScheduledTimeTask on (ScheduledLocationTask.taskID = Task._id" +
				" or ScheduledTimeTask.taskID = Task._id)", 
				new String[]{"Task.taskName"},null, null, null, null, null);
		
		//close the database
		//db.close();
		
		//add each name to the list
		while(cursor.moveToNext()){
			out.add(cursor.getString(0));
		}
		return out;
	}
}
