/**
 * GPSNotifyService.java
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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * service for the GPS Notify application
 * 
 * @author rlamb
 *
 * @version 1
 */
public class GPSNotifyService extends Service {

	//notification manager
	private NotificationManager mNM;
	
	//thread to monitor scheduled tasks
	private GPSNotifyThread thread;
	
	//notifcation id
	private int notifyNum;


	/**
	 * Class for clients to access.  Because we know this service always
	 * runs in the same process as its clients, we don't need to deal with
	 * IPC.
	 */
	public class LocalBinder extends Binder {
		GPSNotifyService getService() {
			return GPSNotifyService.this;
		}
	}

	@Override
	/**
	 * when service is first started
	 */
	public void onCreate() {
		notifyNum = 1;
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		thread = new GPSNotifyThread(this);
		thread.start();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("GPSNotifyService", "Received start id " + startId + ": " + intent);
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	/**
	 * when service is destroyed
	 */
	public void onDestroy() {
		// Cancel the persistent notification.
		mNM.cancel(1);
		thread.shutdown();
		
		// Tell the user we stopped.
		//Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	// This is the object that receives interactions from clients.  See
	// RemoteService for a more complete example.
	private final IBinder mBinder = new LocalBinder();

	/**
	 * Show a notification while this service is running.
	 */
	public void showNotification(String task) {
		// In this sample, we'll use the same text for the ticker and the expanded notification
		CharSequence text = task;

		// Set the icon, scrolling text and timestamp
		Notification notification = new Notification(R.drawable.clock_alarm, text,
				System.currentTimeMillis());

		// The PendingIntent to launch our activity if the user selects this notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, GPSNotifyActivity.class), 0);

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(this, getText(R.string.local_service_label),
				text, contentIntent);

		//notification settings
		notification.flags = 
			 Notification.FLAG_AUTO_CANCEL 
			/*| Notification.FLAG_ONLY_ALERT_ONCE*/;
		
		notification.defaults = Notification.DEFAULT_LIGHTS |
		Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS 
		| Notification.DEFAULT_VIBRATE;
			

		// Send the notification.
		mNM.notify(notifyNum, notification);
		
		//add to notifyNum
		if(notifyNum >= 100){
			notifyNum = 1;
		}else{
			notifyNum++;
		}
	}
}
