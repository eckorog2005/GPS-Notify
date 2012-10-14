/**
 * ScheduledList.java
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

import android.content.Context;

/**
 * Holds all the scheduled tasks that the user set up.
 * 
 * @author rlamb
 *
 * @version 1
 */
public class ScheduledList {

	/**
	 * list of ScheduledTasks
	 */
	private ArrayList<ScheduledTask> list;
	
	/**
	 * Constructor
	 */
	public ScheduledList(){
		list = new ArrayList<ScheduledTask>();
	}
	
	/**
	 * add a new task to the list
	 * 
	 * @param newTask task to add to the list
	 */
	public void addNewTask(ScheduledTask newTask){
		list.add(newTask);
	}
	
	/**
	 * remove the task from the list
	 * 
	 * @param idx index of the task
	 * @return the scheduled task that was removed
	 */
	public ScheduledTask removeTask(int idx){
		return list.remove(idx);
	}
	
	/**
	 * get a scheduled task from the list
	 * 
	 * @param idx index of the scheduled list
	 * @return the scheduled task at idx
	 */
	public ScheduledTask getTask(int idx){
		return list.get(idx);
	}
	
	/**
	 * return the length of the list
	 * 
	 * @return the size of the list
	 */
	public int getLength(){
		return list.size();
	}
	
	/**
	 * Returns a list of scheduled tasks that are ready for the 
	 * user to be notified about.
	 * @param flag 
	 * 
	 * @return the scheduled tasks to notify the users
	 */
	public ArrayList<ScheduledTask> tasksReady(ScheduledTask currLocation, Context provider, boolean flag){
		ArrayList<ScheduledTask> out = new ArrayList<ScheduledTask>();
		for (int i = 0; i < list.size(); i++) {
			boolean alert = false;
			if(list.get(i) instanceof ScheduledLocationTask){
				if(!flag){
					continue;
				}
				alert = ((ScheduledLocationTask)list.get(i)).getAlarmReady();
			}
			if(list.get(i).alertUser(currLocation)){
				out.add(list.get(i));
			}
			if(list.get(i) instanceof ScheduledLocationTask && 
					((ScheduledLocationTask)list.get(i)).getAlarmReady() != 
						alert){
				ScheduledFactory.getInstance(provider).save(list.get(i));
			}
		}
		return out;
	}
}
