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

import com.leinardi.android.things.driver.hd44780.Hd44780;

public class LCDDisplay {

    private Hd44780 driver;
    private static LCDDisplay me;

    private static final String I2C_PIN = "I2C1";
    private static final int I2C_ADDR = 0x27;
    // Change it according to your display
    private static final int GEOMETRY = Hd44780.Geometry.LCD_20X4;


    int[] empty = {0b000000, 0b000000, 0b000000, 0b000000, 0b000000, 0b000000, 0b000000, 0b000000};

    private LCDDisplay() {
        init();
    }

    public static final LCDDisplay getInstance() {
        if (me == null) {
            me = new LCDDisplay();
        }
        return me;
    }

    public void display(String data) {
        try {
            driver.cursorHome();
            driver.clearDisplay();
            driver.setBacklight(true);
            driver.setText(data);
            driver.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void display(String data, int line) {
        try {
            driver.cursorHome();
            driver.clearDisplay();
            driver.setBacklight(true);
            driver.setText(data, line);
            driver.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shift(final String word, final int speed) {
        final int size = 20 - word.length();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    driver.createCustomChar(empty, 0);

                    for (int i = 0; i <= size; i++) {
                        if (i > 0) {
                            driver.setCursor(i - 1, 0);
                            driver.writeCustomChar(0);

                        }
                        driver.setCursor(i, 0);
                        driver.setText(word);
                        Thread.sleep(speed);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t = new Thread(r);
        t.start();


    }

    private void init() {
        try {
            driver = new Hd44780(I2C_PIN, I2C_ADDR, GEOMETRY);
            driver.cursorHome();
            driver.clearDisplay();
            driver.setBacklight(true);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


}
