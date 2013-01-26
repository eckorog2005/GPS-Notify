/**
 * GPSNotifyDatabaseHelper.java
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

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Extension of the SQLiteOpenHelper to use for GPS Notify app.
 * 
 * @author rlamb
 *
 *@version 1
 */
public class GPSNotifyDatabaseHelper extends SQLiteOpenHelper{

	/**
	 * andorid context data
	 */
	private Context context;
	
	/**
	 * singleton referecnce
	 */
	private static GPSNotifyDatabaseHelper instance;

    public static synchronized GPSNotifyDatabaseHelper getHelper(Context context)
    {
        if (instance == null)
            instance = new GPSNotifyDatabaseHelper(context);

        return instance;
    }

	/**
	 * Constructor
	 * 
	 * @param context android app data
	 */
	private GPSNotifyDatabaseHelper(Context context){
		super(context, "GPS_NOTIFY_DB", null, 1);
		this.context = context;
	}

	/**
	 * Ran when the database is first created.  creates the tables used.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		//declarations
		int bytesRead = 0;
		String sqlCommands = "";
		byte[] buffer = new byte[100];

		try {
			//open text file
			BufferedInputStream in = new BufferedInputStream(
					context.getAssets().open("data.txt"));

			//read each SQLite command
			while((bytesRead = in.read(buffer)) != -1){
				sqlCommands = sqlCommands.concat(
						new String(buffer, 0, bytesRead));
			}

			//close file
			in.close();
			
			//split string and run each line
			String[] command = sqlCommands.split("\r\n");
			for(int i = 0; i < command.length; i++){
				db.execSQL(command[i]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * When new version is added, this function is called to make changes.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}


}
