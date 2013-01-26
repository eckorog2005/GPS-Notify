/**
 * GPSNotifyThread.java
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

import java.lang.Thread;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * This thread is used to monitor the scheduled tasks.
 * 
 * @author rlamb
 *
 */
public class GPSNotifyThread extends Thread{

	//list of scheduled tasks
	private ScheduledList list;

	//service of the application to send notifications
	private GPSNotifyService parent;

	//flag for thread loop
	private boolean working;

	//tasks to send to the user
	private ArrayList<ScheduledTask> sendOut;

	//locationmanager for gps use
	private LocationManager locationManager;

	//gps information provider
	private String provider;

	//scheduled location task to mark time
	private ScheduledLocationTask currLocation = 
		new ScheduledLocationTask(new Location(), new Task());

	//location listener to update location
	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(android.location.Location location) {
			currLocation.getLocation().setLatitude(location.getLatitude());
			currLocation.getLocation().setLongitude(location.getLongitude());
		}

		@Override
		public void onProviderDisabled(String provider) {

		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	/**
	 * run thread
	 */
	@Override
	public void run(){
		while(working){
			boolean flag =true;
			if( locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER )
					&& !isProviderEnable()){
				loadLocation();
			}else if(!locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER )){
				flag = false;
			}
			
			try {
				sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			list = new ScheduledList();
			ArrayList<ScheduledTask> temp = 
				ScheduledFactory.getInstance(
						parent.getApplicationContext()).loadAll();
			for(int i = 0; i < temp.size(); i++){
				list.addNewTask(temp.get(i));
			}
			sendOut = list.tasksReady(currLocation, parent.getApplicationContext(), flag);
			if(sendOut != null){
				notifyTasks();
			}
		}
		this.interrupt();
	}

	/**
	 * send tasks out to notify user and adjust the database
	 */
	public void notifyTasks(){
		if(sendOut == null){
			return;
		}
		Calendar cal = Calendar.getInstance();
		for(int i = 0; i < sendOut.size(); i++){
			ScheduledTask temp = sendOut.get(i);
			if(temp.getOccurrence() == Occurrence.ONCE){
				ScheduledFactory.getInstance(
						parent.getApplicationContext()).delete(temp);
			}else{
				temp.setLastUsed(new Date(cal.getTimeInMillis()));
				ScheduledFactory.getInstance(
						parent.getApplicationContext()).save(temp);
			}
			parent.showNotification(temp.getTask().getTaskName());
		}
		sendOut = null;
		return;
	}

	/**
	 * stops the thread loop and shuts down the task
	 */
	public void shutdown(){
		this.working = false;
	}

	/**
	 * constructor for thread
	 * 
	 * @param parent service that called the thread
	 */
	public GPSNotifyThread(GPSNotifyService parent){
		this.parent = parent;
		this.working = true;
		sendOut = new ArrayList<ScheduledTask>();

		//configure location manager
		locationManager = (LocationManager)parent.getSystemService(
				Context.LOCATION_SERVICE);
		if( locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER )){
			loadLocation();
		}
	}
	
	private void loadLocation(){
		android.location.Location location;
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		provider = locationManager.getBestProvider(criteria, true);
		if(provider != null){
			location = locationManager.getLastKnownLocation(provider);
			if(location != null){
				currLocation.getLocation().setLatitude(location.getLatitude());
				currLocation.getLocation().setLongitude(location.getLongitude());
			}
		}
		locationManager.requestLocationUpdates(
				provider, 180000, 50, locationListener);
	}
	
	private boolean isProviderEnable(){
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		provider = locationManager.getBestProvider(criteria, true);
		if(provider != null){
			return true;
		}
		return false;
	}
}
