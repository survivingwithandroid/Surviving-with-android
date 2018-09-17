package com.survivingwithandroid.androidthings.api;

import com.google.android.things.contrib.driver.bmx280.Bmx280;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

/*
 * Copyright (C) 2018 Francesco Azzola - Surviving with Android (https://www.survivingwithandroid.com)
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

public class DeviceManager {

    private static DeviceManager me;
    private PeripheralManagerService pmService;

    private static final String IC2_BUS = "I2C1";
    private Bmx280 sensor;


    private DeviceManager() {
        init();
    }

    public static final DeviceManager getInstance() {
        if (me == null)
            me = new DeviceManager();

        return me;
    }


    private void init() {
        try {
            sensor = new Bmx280(IC2_BUS);
            sensor.setTemperatureOversampling(Bmx280.OVERSAMPLING_1X);
            sensor.setPressureOversampling(Bmx280.OVERSAMPLING_1X);
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }

    }

    public float readTemp() {
        if (sensor != null)
            try {
                return sensor.readTemperature();
            } catch (IOException e) {
                e.printStackTrace();
            }

        return 0;
    }

    public float readPress() {
        if (sensor != null)
            try {
                return sensor.readPressure();
            } catch (IOException e) {
                e.printStackTrace();
            }

        return 0;
    }
}
