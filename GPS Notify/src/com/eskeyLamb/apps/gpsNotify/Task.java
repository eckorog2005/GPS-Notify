/**
 * Task.java
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

/**
 * This class holds a task information
 * 
 * @author rlamb
 *
 * @version 1
 */
public class Task {
	/**
	 * the task name
	 */
	private String taskName;
	/**
	 * number of times the task was used (for stat information only)
	 */
	private long numOfUses;
	
	/**
	 * Constructor
	 */
	public Task(){
		taskName = "";
		numOfUses = 0;
	}
	
	/**
	 * Constructor
	 * 
	 * @param taskName the task name
	 * @param note the note for the task
	 */
	public Task(String taskName){
		this.taskName = taskName;
	}
	
	/**
	 * get the task name
	 * 
	 * @return the task name
	 */
	public String getTaskName(){
		return this.taskName;
	}
	
	/**
	 * set the task name
	 * 
	 * @param taskName the task name to set
	 */
	public void setTaskName(String taskName){
		this.taskName = taskName;
	}
	
	/**
	 * get the number of uses
	 * 
	 * @return the number of uses
	 */
	public long getNumOfUses(){
		return numOfUses;
	}
	
	/**
	 * set the number of uses
	 * 
	 * @param numOfUses set the number of uses
	 */
	public void setNumOfUses(long numOfUses){
		this.numOfUses = numOfUses;
	}
}
