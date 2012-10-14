/**
 * NewLocationActivity.java
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * Activity used to create a new location
 * 
 * @author rlamb
 *
 */
public class NewLocationActivity extends Activity {

	//locationmanager for gps use
	private LocationManager locationManager;

	//gps information provider
	private String provider;

	//longitude
	private double longitude;

	//latitude
	private double latitude;

	//items used to match viewlist
	private ArrayList<Address> items;

	//selected address
	private Address selected;

	//location listener to update location
	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(android.location.Location location) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * when activity is created
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//location
		android.location.Location location;

		selected = null;
		setContentView(R.layout.new_location);

		//configure location manager
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		provider = locationManager.getBestProvider(criteria, true);
		location = locationManager.getLastKnownLocation(provider);
		if(location != null){
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}
		locationManager.requestLocationUpdates(provider, 100, 1, locationListener);

		//delete
		findViewById(R.id.button1).setOnClickListener(
				new OnClickListener() {
					public void onClick(final View v) {
						final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
						builder.setMessage("Deleting this location can affect "+
						"currently scheduled alarms, Continue?")
						.setCancelable(false)
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {	
								ScheduledFactory schFactory = 
									ScheduledFactory.getInstance(v.getContext());
								LocationFactory temp = 
									LocationFactory.getInstance(v.getContext());
								ArrayList<ScheduledTask> list = schFactory.loadAll();
								for(ScheduledTask item:list){
									if(item instanceof ScheduledLocationTask){
										if(((ScheduledLocationTask)item).
												getLocation().getName().equals(
														getTextBoxName())){
											schFactory.delete(item);
										}
									}
								}
								temp.delete(getTextBoxName());
								finishTaskActivity();
							}
						}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {	
							}
						});
						final AlertDialog alert = builder.create();
						alert.show();
					}
				});

		//save
		findViewById(R.id.button2).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						if(checkData()){
							LocationFactory temp = 
								LocationFactory.getInstance(v.getContext());
							Location location = new Location(getTextBoxName());
							if(selected != null){
								location.setLatitude(selected.getLatitude());
								location.setLongitude(selected.getLongitude());
							}
							temp.save(location);
							finishTaskActivity();
						}
					}
				});

		//add text watcher to address
		EditText text = ((EditText)findViewById(R.id.editText1));
		text.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				return;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				return;
			}

			@Override
			public void afterTextChanged(Editable s) {
				if(s.length() > 5 && s.length()%3 == 0){
					getLocationResults();
				}
			}
		});

		text.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// If the event is a key-down event on the "enter" button
				if ((arg2.getAction() == KeyEvent.ACTION_DOWN) &&
						(arg1 == KeyEvent.KEYCODE_ENTER)) {
					// Perform action on key press
					getLocationResults();
					return true;
				}
				return false;
			}
		});

		//add listener to listview
		ListView list = (ListView)findViewById(R.id.listView1);
		list.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				selected = items.get(arg2);
				if(selected.getMaxAddressLineIndex()>0){
					Toast.makeText(getApplicationContext(),
							"selected list item " + (arg2+1) + ": "+
							selected.getAddressLine(0), Toast.LENGTH_SHORT)
							.show();
				}else{
					Toast.makeText(getApplicationContext(),
							"selected list item " + arg2+1, Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
		);
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


		//get passed in data for existing data
		String name = this.getIntent().getStringExtra("location");
		if(!name.equals("New Location")){
			Location loc = LocationFactory.getInstance(getApplicationContext()).load(name);
			EditText temp = (EditText)findViewById(R.id.editText2);
			temp.setText(name);

			List<Address> address = null;

			//start geocoder and get top 5 results
			Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
			try {
				address = geo.getFromLocation(loc.getLatitude(), loc.getLongitude(), 5);
				if(address.size() > 0){
					temp = (EditText)findViewById(R.id.editText1);
					temp.setText(address.get(0).getAddressLine(0));
				}
			} catch (IOException e) {

			}
		}
	}

	/**
	 * ends this activity
	 */
	public void finishTaskActivity(){
		this.finish();
	}

	/**
	 * get name used in textbox
	 * @return
	 */
	public String getTextBoxName(){
		return ((EditText)findViewById(R.id.editText2))
		.getText().toString();
	}

	/**
	 * get new locations from address entered
	 * 
	 */
	private void getLocationResults() {
		ListView view = (ListView)findViewById(R.id.listView1);
		List<Address> list = null;
		ArrayList<HashMap<String, String>>out = new ArrayList<HashMap<String,String>>();

		//start geocoder and get top 5 results
		Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
		try {
			list = geo.getFromLocationName(
					((EditText)findViewById(R.id.editText1)).getText().toString(), 
					5, latitude-1, longitude-1, latitude+1, longitude+1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		//clear current items
		items = new ArrayList<Address>();

		//create new list for user
		for(Address temp : list){
			HashMap<String, String> map = new HashMap<String, String>();
			items.add(temp);

			if(temp.getFeatureName() == null){
				map.put("name", "");
			}else{
				map.put("name", temp.getFeatureName());
			}
			if(temp.getMaxAddressLineIndex() >= 0){
				String line = "";
				for (int i = 0; i < temp.getMaxAddressLineIndex(); i++) {
					line += temp.getAddressLine(i)+"\n";
				}
				map.put("address", line);
			}else{
				map.put("address", "");
			}
			out.add(map);
		}

		//set new adapter
		SimpleAdapter myAdapter = new SimpleAdapter(
				this.getApplicationContext(), out, 
				R.layout.detailed_location_view, 
				new String[] {"name", "address"},
				new int[] {R.id.name, R.id.address});
		view.setAdapter(myAdapter);

		//reload view
		myAdapter.notifyDataSetChanged();
		view.invalidate();
	}

	/**
	 * check data before save
	 * 
	 * @return
	 */
	private boolean checkData() {
		boolean flag = false;
		String address = ((EditText)findViewById(R.id.editText1)).getText().toString();

		//check for any errors
		if(getTextBoxName() == null || getTextBoxName().equals("")){
			flag = true;
		}else if(address == null || address.equals("")){
			flag = true;
		}

		//if error, show alert
		if(flag){
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Error in entering data " +
			"(blank location name/address?)")
			.setCancelable(false)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(final DialogInterface dialog, final int id) {

				}
			});
			final AlertDialog alert = builder.create();
			alert.show();
			return false;
		}
		return true;
	}
}
