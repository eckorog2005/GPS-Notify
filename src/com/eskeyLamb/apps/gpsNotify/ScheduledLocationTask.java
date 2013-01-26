/**
 * ScheduledLocationTask.java
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

import java.util.Calendar;

/**
 * This class holds the location and task to associate with a notification.
 * 
 * @author rlamb
 *
 * @version 1
 */
public class ScheduledLocationTask extends ScheduledTask{
	/**
	 * Location object
	 */
	private Location location;

	/**
	 * ready flag
	 */
	private boolean alertReady;

	/**
	 * Constructor
	 */
	public ScheduledLocationTask(){
		location = null;
		alertReady = true;
	}

	/**
	 * Constructor
	 * 
	 * @param loc the location to set
	 * @param task the task to set
	 */
	public ScheduledLocationTask(Location loc, Task task){
		this.location = loc;
		this.setTask(task);
		alertReady = true;
	}

	/**
	 * Constructor
	 * 
	 * @param loc the location to set
	 * @param task the task to set
	 * @param flag alarm flag
	 */
	public ScheduledLocationTask(Location loc, Task task, boolean flag){
		this.location = loc;
		this.setTask(task);
		alertReady = flag;
	}

	/**
	 * set the location object 
	 * 
	 * @param location the location to set
	 */
	public void setLocation(Location location){
		this.location = location;
	}

	/**
	 * get the location
	 * 
	 * @return the location
	 */
	public Location getLocation(){
		return location;
	}

	/**
	 * returns true if its time to alert user, false otherwise.
	 */
	public boolean alertUser(ScheduledTask task) {
		ScheduledLocationTask temp = (ScheduledLocationTask)task;
	    //temp.getLocation().setLatitude(24);
		//temp.getLocation().setLongitude(24);
		if(alertReady){
			if(Math.abs(this.getLocation().getLongitude() 
					- temp.getLocation().getLongitude()) <= .000556 &&
					Math.abs(this.getLocation().getLatitude() - 
							temp.getLocation().getLatitude()) <= .000556){
				if(this.getOccurrence().equals(Occurrence.ONCE)){
					alertReady = false;
					return true;
				}

				//get current time
				Calendar cal = Calendar.getInstance();
				int day = cal.get(Calendar.DAY_OF_MONTH);
				int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
				cal = Calendar.getInstance();
				cal.setTime(this.getLastUsed());
				if(day != cal.get(Calendar.DAY_OF_MONTH)){
					if(this.getOccurrence().equals(Occurrence.WEEKLY)){
						alertReady = false;
						return true;
					}
					Occurrence occur = this.getOccurrence();
					switch(dayOfWeek){
					case 1:
						if(occur.equals(Occurrence.SUNDAY) || occur.equals(Occurrence.WEEKEND)){
							alertReady = false;
							return true;
						}
						break;
					case 2:
						if(occur.equals(Occurrence.MONDAY) || occur.equals(Occurrence.DAILY)){
							alertReady = false;
							return true;
						}
						break;
					case 3:
						if(occur.equals(Occurrence.TUESDAY) || occur.equals(Occurrence.DAILY)){
							alertReady = false;
							return true;
						}
						break;
					case 4:
						if(occur.equals(Occurrence.WEDNESDAY) || occur.equals(Occurrence.DAILY)){
							alertReady = false;
							return true;
						}
						break;
					case 5:
						if(occur.equals(Occurrence.THURSDAY) || occur.equals(Occurrence.DAILY)){
							alertReady = false;
							return true;
						}
						break;
					case 6:
						if(occur.equals(Occurrence.FRIDAY) || occur.equals(Occurrence.DAILY)){
							alertReady = false;
							return true;
						}
						break;
					case 7:
						if(occur.equals(Occurrence.SATURDAY) || occur.equals(Occurrence.WEEKEND)){
							alertReady = false;
							return true;
						}
						break;
					}
				}
			}
		}else{
			if(checkDays()){
				if(Math.abs(this.getLocation().getLongitude() 
						- temp.getLocation().getLongitude()) > .000556 ||
						Math.abs(this.getLocation().getLatitude() - 
								temp.getLocation().getLatitude()) > .000556){
					this.alertReady = true;
				}
			}
		}

		return false;
	}

	private boolean checkDays() {
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		cal = Calendar.getInstance();
		cal.setTime(this.getLastUsed());
		if(day != cal.get(Calendar.DAY_OF_MONTH)){
			Occurrence occur = this.getOccurrence();
			if(occur.equals(Occurrence.WEEKLY)){
				return true;
			}
			switch(dayOfWeek){
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
	 * set the alert ready flag
	 * 
	 * @param flag what to set the flag to.
	 */
	public void setAlarmReady(boolean flag){
		alertReady = flag;
	}

	/**
	 * get the ready flag
	 */
	public boolean getAlarmReady(){
		return alertReady;
	}
}
