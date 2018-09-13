package com.survivingwithandroid.androidthings.nearby;

import android.util.Log;

import com.leinardi.android.things.driver.hd44780.Hd44780;

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

public class LCDManager {


        private Hd44780 mLcd;

        public void displayString(String data) {
            try {
                Log.d("LCd", "Writing");
                if (mLcd == null)
                    mLcd  = new Hd44780("I2C1", 0x27, Hd44780.Geometry.LCD_20X4);
                mLcd.setBacklight(true);
                mLcd.cursorHome();
                mLcd.clearDisplay();
                mLcd.setText(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
