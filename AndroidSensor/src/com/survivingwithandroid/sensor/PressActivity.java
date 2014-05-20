package com.survivingwithandroid.sensor;

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
import android.app.Activity;
import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class PressActivity extends Activity implements SensorEventListener {
	
	private TextView pressView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.press_layout);
		pressView = (TextView) findViewById(R.id.pressTxt);
		
		// Look for pressure sensor
		SensorManager snsMgr = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
		
		Sensor pS = snsMgr.getDefaultSensor(Sensor.TYPE_PRESSURE);
		
		snsMgr.registerListener(this, pS, SensorManager.SENSOR_DELAY_UI);
	}
	
	

	@Override
	protected void onResume() {
		
		super.onResume();
		SensorManager snsMgr = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
		
		Sensor pS = snsMgr.getDefaultSensor(Sensor.TYPE_PRESSURE);
		
		snsMgr.registerListener(this, pS, SensorManager.SENSOR_DELAY_UI);
	}



	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}



	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float[] values = event.values;
		pressView.setText("" + values[0]);
		
	}
	
	

}
