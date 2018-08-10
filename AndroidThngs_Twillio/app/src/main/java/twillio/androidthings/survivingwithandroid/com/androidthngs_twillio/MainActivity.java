package twillio.androidthings.survivingwithandroid.com.androidthngs_twillio;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.contrib.driver.bmx280.Bmx280SensorDriver;

import java.io.IOException;

/*
 * Copyright (C) 2018 Francesco Azzola
 *  Surviving with Android (https://www.survivingwithandroid.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

public class MainActivity extends Activity {

    private Bmx280SensorDriver driver;
    private SensorManager sensorManager;

    private long lastTime;

    private static final String TAG = MainActivity.class.getName();

    private SensorManager.DynamicSensorCallback sensorCallback = new SensorManager.DynamicSensorCallback() {
        @Override
        public void onDynamicSensorConnected(Sensor sensor) {
            if (sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE)
               sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            else if (sensor.getType() == Sensor.TYPE_PRESSURE)
                sensorManager.registerListener(pressSensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        @Override
        public void onDynamicSensorDisconnected(Sensor sensor) {
            super.onDynamicSensorDisconnected(sensor);
        }
    };

    private SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float temp = event.values[0];
            // Check the temp
           // Log.d(TAG, "Temperature: " + temp);

            if (temp > 30) {

                if ( lastTime == 0 || (System.currentTimeMillis() - lastTime) > 60000) {
                    Log.d(TAG, "Sending message...");
                    TwillioClient.sendSMS("Alert! the temperatus is over 30°C");
                    lastTime = System.currentTimeMillis();
                }
                else {
                //    Log.d(TAG, "Message Already sent...waiting");
                }
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    private SensorEventListener pressSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float press = event.values[0];
            // Check the temp
           // Log.d(TAG, "Pressure: " + press);

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSensor();
    }

    private void initSensor() {
        try {
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            sensorManager.registerDynamicSensorCallback(sensorCallback);
            driver = new Bmx280SensorDriver("I2C1") ;
            driver.registerTemperatureSensor();

        }
        catch(IOException ioe) {
            Log.e(TAG, "Error initializing the driver", ioe);
        }

    }
}
