package com.survivingwithandroid.sensor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/*
* Copyright (C) 2014 Francesco Azzola - Surviving with Android (http://www.survivingwithandroid.com)
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
public class MainActivity extends Activity {
	private SensorManager sensorManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		// Get the reference to the sensor manager
		sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
		
		// Get the list of sensor 
		//List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
		
		List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_PRESSURE);
		
		List<Map<String, String>> sensorData = new ArrayList<Map<String,String>>();
		
		
		for (Sensor sensor: sensorList) {
			Map<String, String> data = new HashMap<String, String>();
			data.put("name", sensor.getName());
			data.put("vendor", sensor.getVendor());
			sensorData.add(data);
		}
		
		
		SimpleAdapter sa = new SimpleAdapter(this, sensorData, android.R.layout.simple_list_item_2, new String[]{"name", "vendor"}, new int[]{android.R.id.text1, android.R.id.text2});
		
		ListView lv = (ListView) findViewById(R.id.sensorList);
		lv.setAdapter(sa);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				
				Intent i = new Intent(MainActivity.this, PressActivity.class);
				startActivity(i);
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
