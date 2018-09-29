package com.survivingwithandroid.androidthings;

import android.app.Activity;
import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // testHD44780();
       // testTM1637();
       // testMax7219();
        testSsd1306();
    }


    private void testHD44780() {
        LCDDisplay lcd = LCDDisplay.getInstance();
       // lcd.display("Android Things",2);
        lcd.shift("Android", 100);
    }

    private void testTM1637() {
        TM1637Display display = TM1637Display.getInstance();

        display.display("1420");
    }

    private void testMax7219() {
        Max7219Display display = Max7219Display.getInstance();
        //display.drawBorder();
        display.drawTable();

    }

    private void testSsd1306() {
        SSD1306Display display = SSD1306Display.getInstance();

        //display.drawLine(10);
        display.drawBitmap(getResources());

    }
}
