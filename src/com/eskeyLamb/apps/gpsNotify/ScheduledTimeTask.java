/**
 * ScheduledTimeTask.java
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

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;


/**
 * This object contains the Scheduled time and task to use to 
 * notify the user.
 * 
 * @author rlamb
 *
 * @version 1
 */
public class ScheduledTimeTask extends ScheduledTask{

	/**
	 * the time for the task to be notified
	 */
	private Time time;

	/**
	 * Constructor
	 */
	public ScheduledTimeTask(){
		setTime(null);
	}

	/**
	 * Constructor
	 * 
	 * @param time time for the alert
	 * @param task the task to associate with the alert
	 */
	public ScheduledTimeTask(Time time, Task task){
		this.setTime(time);
		this.setTask(task);
	}

	/**
	 * returns true if its time to alert user, false otherwise.
	 * 
	 * @param task Data to compare this task to alert the user
	 */
	public boolean alertUser(ScheduledTask task) {
		
		//get current time
		Calendar cal = Calendar.getInstance();
		int currHour = cal.get(Calendar.HOUR_OF_DAY);
		int currMin = cal.get(Calendar.MINUTE);
		int day = cal.get(Calendar.DAY_OF_WEEK);
		int currMonthDay = cal.get(Calendar.DAY_OF_MONTH);
		
		//get task time
		Date date = new Date(this.getTime().getTime());
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		cal.setTime(getLastUsed());
		int dayMonthLast = cal.get(Calendar.DAY_OF_MONTH);
		
		//check data with current info to alert user or not
		if(currMonthDay != dayMonthLast && currHour == hour && currMin == min){
			Occurrence occur = this.getOccurrence();
			if(occur.equals(Occurrence.ONCE) || occur.equals(Occurrence.WEEKLY)){
				return true;
			}
			switch(day){
			case 1:
				if(occur.equals(Occurrence.SUNDAY) || occur.equals(Occurrence.WEEKEND)){
					return true;
				}
				break;
			case 2:
				if(occur.equals(Occurrence.MONDAY) || occur.equals(Occurrence.DAILY)){
					return true;
				}
				break;
			case 3:
				if(occur.equals(Occurrence.TUESDAY) || occur.equals(Occurrence.DAILY)){
					return true;
				}
				break;
			case 4:
				if(occur.equals(Occurrence.WEDNESDAY) || occur.equals(Occurrence.DAILY)){
					return true;
				}
				break;
			case 5:
				if(occur.equals(Occurrence.THURSDAY) || occur.equals(Occurrence.DAILY)){
					return true;
				}
				break;
			case 6:
				if(occur.equals(Occurrence.FRIDAY) || occur.equals(Occurrence.DAILY)){
					return true;
				}
				break;
			case 7:
				if(occur.equals(Occurrence.SATURDAY) || occur.equals(Occurrence.WEEKEND)){
					return true;
				}
				break;
			}
		}
		return false;
	}

	/**
	 * set the time
	 * 
	 * @param time the time to set
	 */
	public void setTime(Time time) {
		this.time = time;
	}

	/**
	 * get the time
	 * 
	 * @return the time
	 */
	public Time getTime() {
		return time;
	}
}
