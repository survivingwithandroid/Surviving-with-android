package com.survivingwithandroid.androidthings;

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

import com.google.android.things.contrib.driver.tm1637.NumericDisplay;

import java.io.IOException;

public class TM1637Display {

    private NumericDisplay display;



    private String DATA_PIN = "BCM4";
    private String CLK_PIN  = "BCM3";

    private static TM1637Display me;


    public static TM1637Display getInstance() {
        if (me == null)
            me = new TM1637Display();

        return me;
    }

    private TM1637Display() {
        init();
    }


    private void init() {
        try {
            display = new NumericDisplay(DATA_PIN, CLK_PIN);
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void display(String data) {
        try {
            display.display(data);
            display.setColonEnabled(true);
            display.setBrightness(NumericDisplay.MAX_BRIGHTNESS);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }
}
