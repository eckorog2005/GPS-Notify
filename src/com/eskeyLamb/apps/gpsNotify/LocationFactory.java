/**
 * LocationFactory.java
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
 * This factory is in charge of saving and loading location objects to the 
 * database.
 * 
 * @author rlamb
 *
 * @version 1
 */
public class LocationFactory {

	/**
	 * instance of this class to enforce the Singleton pattern
	 */
	private static LocationFactory instance = null;
	/**
	 * android database helper
	 */
	private GPSNotifyDatabaseHelper dbhelper;

	/**
	 * get the single instance of this class
	 * 
	 * @param context the android context of the program
	 * @return the instance of the class
	 */
	public static LocationFactory getInstance(Context context) {
		if(instance == null) {
			instance = new LocationFactory(context);
		}
		return instance;
	}

	/**
	 * Constructor
	 * 
	 * @param context android context data
	 */
	private LocationFactory(Context context){
		this.dbhelper = GPSNotifyDatabaseHelper.getHelper(context);
	}

	/**
	 * save a Location object to the database
	 * 
	 * @param location Location object to save
	 */
	public void save(Location location){
		//open the writable database
		ContentValues values = new ContentValues();
		SQLiteDatabase db = this.dbhelper.getWritableDatabase();
		db.setLockingEnabled(true);

		//check if entered already
		Cursor cursor = db.query("Location", new String[]{"locationName"}, 
				"locationName = '"+location.getName()+"'", null, null,
				null, null);

		if(!cursor.moveToNext()){
			//save the location to the database
			values.put("locationName", location.getName());
			values.put("latitude", location.getLatitude());
			values.put("longitude", location.getLongitude());
			db.insert("Location", null, values);
		}else{
			values.put("latitude", location.getLatitude());
			values.put("longitude", location.getLongitude());
			db.update("Location", values, 
					"locationName = '"+location.getName()+"'",null);
		}

		//close the database
		//db.close();
	}

	/**
	 * load the given location name into a Location
	 *  
	 * @param locName the location name
	 * @return the Location object that contains the name
	 */
	public Location load(String locName){
		//declarations
		Location location = null;

		//open the writable database
		SQLiteDatabase db = this.dbhelper.getReadableDatabase();
		db.setLockingEnabled(true);

		//search for the location name
		Cursor cursor = db.query("Location", 
				new String[]{"locationName", "latitude", "longitude"}, 
				"locationName = '"+locName+"'", null, null, null, null);

		//see if the location is in the database and load data, else return null
		if(cursor.moveToNext()){
			location = new Location(cursor.getString(0));
			location.setLatitude(cursor.getDouble(1));
			location.setLongitude(cursor.getDouble(2));
		}
		
		//close the database
		//db.close();

		return location;
	}

	/**
	 * load all locations in the database to a list
	 * 
	 * @return Arraylist of Locations
	 */
	public ArrayList<Location> loadAll(){
		//declarations
		Location location;
		ArrayList<Location> out = new ArrayList<Location>();

		//open the writable database
		SQLiteDatabase db = this.dbhelper.getReadableDatabase();
		db.setLockingEnabled(true);

		//gather all locations in the database
		Cursor cursor = db.query("Location", 
				new String[]{"locationName", "latitude", "longitude"}, 
				null, null, null, null, null);

		//load each location object
		while(cursor.moveToNext()){
			location = new Location(cursor.getString(0));
			location.setLatitude(cursor.getDouble(1));
			location.setLongitude(cursor.getDouble(2));
			out.add(location);
		}

		//close the database
		//db.close();
		return out;
	}

	/**
	 * Get all the Locations names in the database
	 * 
	 * @return Arraylist of Location names
	 */
	public ArrayList<String> getKeys(){
		//declarations
		ArrayList<String> out = new ArrayList<String>();

		//open the readable database
		SQLiteDatabase db = this.dbhelper.getReadableDatabase();
		db.setLockingEnabled(true);

		//get all the Locations names
		Cursor cursor = db.query("Location", 
				new String[]{"locationName"}, 
				null, null, null, null, "locationName COLLATE NOCASE ASC");

		//add each on to the array list
		while(cursor.moveToNext()){
			out.add(cursor.getString(0));
		}

		//close the database and return
		//db.close();
		return out;
	}

	/**
	 * return the Location ID given the name
	 * 
	 * @param locationName name of the location to search for
	 * @return the location ID, or -1 if not found
	 */
	public int getID(String locationName){
		//declarations
		int out;

		//open the readable database
		SQLiteDatabase db = this.dbhelper.getReadableDatabase();
		db.setLockingEnabled(true);

		//search for the location name
		Cursor cursor = db.query("Location", new String[]{"_id"}, 
				"locationName = '"+locationName+"'", null, null, null, null);

		//if found, get the id else reutn -1
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
	 * delete the given location name
	 * 
	 * @param locationName the location name
	 */
	public void delete(String locationName){
		//open the writable database
		SQLiteDatabase db = this.dbhelper.getWritableDatabase();
		db.setLockingEnabled(true);

		//delete all locations with name
		db.delete("Location", "locationName = '"+locationName+"'", null);

		//close the database
		//db.close();
	}

	/**
	 * check to see if the location name is already in the database
	 * 
	 * @param name the name of the location to check
	 * @return true if name exists, otherwise false
	 */
	public boolean checkLocationName(String name){
		//open the database
		SQLiteDatabase db = this.dbhelper.getWritableDatabase();
		db.setLockingEnabled(true);

		//search for name
		Cursor cursor = db.query("Location", new String[]{"locationName"}, 
				"locationName = '"+name+"'", null, null, null, null);

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
