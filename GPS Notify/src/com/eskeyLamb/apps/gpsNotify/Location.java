/**
 * Location.java
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
 * Holds the data to keep track of a location
 * 
 * @author rlamb
 *
 * @version 1
 */
public class Location {
	
	/**
	 * the name of the location
	 */
	private String name;
	
	/**
	 * the longitude of the location
	 */
	private double longitude;
	
	/**
	 * the latitude of the location
	 */
	private double latitude;
	
	/**
	 * Constructor
	 */
	public Location(){
		this.name = "";
		this.longitude = 0;
		this.longitude = 0;
	}
	
	/**
	 * Constructor
	 * 
	 * @param name the name of the location
	 */
	public Location(String name){
		this.name = name;
		this.longitude = 0;
		this.latitude = 0;
	}
	
	/**
	 * get the latitude
	 * 
	 * @return the latitude
	 */
	public double getLatitude(){
		return this.latitude;
	}
	
	/**
	 * set the latitude
	 * 
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude){
		this.latitude = latitude;
	}
	
	/**
	 * get the longitude
	 * 
	 * @return the longitude
	 */
	public double getLongitude(){
		return this.longitude;
	}
	
	/**
	 * set hte longitude
	 * 
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude){
		this.longitude = longitude;
	}
	
	/**
	 * the name of the location
	 * 
	 * @return the location name
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * set the location name
	 * 
	 * @param name the name to be set
	 */
	public void setName(String name){
		this.name = name;
	}
}
