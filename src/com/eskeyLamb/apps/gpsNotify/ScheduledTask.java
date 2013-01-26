/**
 * ScheduledTask.java
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

/**
 * This class is used to store a task that is scheduled
 * 
 * @author rlamb
 *
 * @version 1
 */
public abstract class ScheduledTask {
	/**
	 * time the alarm should go off; e.g. One day, daily, etc.
	 */
	private Occurrence occurrence;
	
	/**
	 * last time alarm went off
	 */
	private Date lastUsed;
	
	/**
	 * note to attach to each task
	 */
	private String note;
	
	/**
	 * task that is scheduled
	 */
	private Task task;
	
	/**
	 * Constructor
	 */
	public ScheduledTask(){
		lastUsed = null;
		occurrence = Occurrence.NONE;
		note = "";
	}
	
	/**
	 * set the occurrence
	 * 
	 * @param occurrence the occurrence to set
	 */
	public void setOccurrence(Occurrence occurrence){
		this.occurrence = occurrence;
	}
	
	/**
	 * get the occurrence
	 * 
	 * @return the occurrence
	 */
	public Occurrence getOccurrence(){
		return occurrence;
	}
	
	/**
	 * set the last date used
	 * 
	 * @param date the date to set
	 */
	public void setLastUsed(Date date){
		this.lastUsed = date;
	}
	
	/**
	 * get the date last used
	 * 
	 * @return the date last used
	 */
	public Date getLastUsed(){
		return lastUsed;
	}
	
	/**
	 * get the task that was scheduled
	 * 
	 * @return the task used
	 */
	public Task getTask(){
		return task;
	}
	
	/**
	 * set the task
	 * 
	 * @param task the task to set
	 */
	public void setTask(Task task){
		this.task = task;
	}
	
	/**
	 * get the note for the task
	 * 
	 * @return the note
	 */
	public String getNote(){
		return note;
	}
	
	/**
	 * set the note for the task
	 * 
	 * @param note the note to set
	 */
	public void setNote(String note){
		this.note = note;
	}
	
	/**
	 * abstract method to determine when the alerm sound be notified.
	 * 
	 * @return true to alert user, false otherwise.
	 */
	public abstract boolean alertUser(ScheduledTask task);
}
