package com.survivingwithandroid.things;

/*
 * Copyright (C) 2017 Surviving with Android (http://www.survivingwithandroid.com)
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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

import static android.content.ContentValues.TAG;

/**
 * Created by francesco on 06/01/17.
 */

public class RGBThingActivity extends Activity {


    private Gpio redIO;
    private Gpio blueIO;
    private Gpio greenIO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w(TAG,"------------- onCreate -------------");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        try {
            PeripheralManagerService manager = new PeripheralManagerService();
            blueIO = manager.openGpio("BCM5");
            blueIO.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            greenIO = manager.openGpio("BCM6");
            greenIO.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);
            redIO = manager.openGpio("BCM13");
            redIO.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            redIO.setValue(false);
            blueIO.setValue(false);
            greenIO.setValue(false);
        } catch (IOException e) {
            Log.w(TAG, "Unable to access GPIO", e);
        }

        // Let's start getting component references
        Switch switchRed = (Switch) findViewById(R.id.switchRed);
        Switch switchBlue = (Switch) findViewById(R.id.switchBlue);
        Switch switchGreen = (Switch) findViewById(R.id.switchGreen);

        switchRed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               try {
                   redIO.setValue(!isChecked);
               }
               catch (IOException e) {
                 Log.w(TAG, "Red GPIO Error", e);
               }
            }
        });

        switchBlue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    blueIO.setValue(!isChecked);
                }
                catch (IOException e) {
                    Log.w(TAG, "Red GPIO Error", e);
                }
            }
        });

        switchGreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    greenIO.setValue(!isChecked);
                }
                catch (IOException e) {
                    Log.w(TAG, "Red GPIO Error", e);
                }
            }
        });
    }


    private void closePin(Gpio pin) {
        if (pin != null) {
            try {
                pin.close();
                pin = null;
            } catch (IOException e) {
                Log.w(TAG, "Unable to close GPIO", e);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        closePin(redIO);
        closePin(blueIO);
        closePin(greenIO);

    }
}
