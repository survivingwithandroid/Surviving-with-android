package com.survivingwithandroid.androidthings.motors;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;


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

public class MotorController {

    PeripheralManager service = PeripheralManager.getInstance();
    private Gpio pin1Motor1;
    private Gpio pin2Motor1;

    private Gpio pin1Motor2;
    private Gpio pin2Motor2;

    public MotorController() {
        try {

            // Right
            pin1Motor1 = service.openGpio("BCM17");
            pin2Motor1 = service.openGpio("BCM27");

            // Left
            pin1Motor2 = service.openGpio("BCM23");
            pin2Motor2 = service.openGpio("BCM24");


            pin1Motor1.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            pin2Motor1.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            pin1Motor2.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            pin2Motor2.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void forward() {
        setPinValues(true, false, false, true);
    }

    public void backward() {
        setPinValues(false, true, true, false);
    }

    public void stop(){
        setPinValues(false, false,false,false);
    }

    public void turnLeft() {
        setPinValues(false, false, false, true);
    }

    public void turnRight() {
        setPinValues(true, false, false, false);
    }



    private void setPinValues(boolean p11, boolean p12, boolean p21, boolean p22 ) {

        try {
            pin1Motor1.setValue(p11);
            pin2Motor1.setValue(p12);
            pin1Motor2.setValue(p21);
            pin2Motor2.setValue(p22);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
