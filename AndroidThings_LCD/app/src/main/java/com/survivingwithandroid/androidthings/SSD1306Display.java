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

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.things.contrib.driver.ssd1306.BitmapHelper;
import com.google.android.things.contrib.driver.ssd1306.Ssd1306;

import java.io.IOException;

public class SSD1306Display {

    private  static SSD1306Display me;
    private Ssd1306 display;

    private SSD1306Display() {
        init();
    }

    public static SSD1306Display getInstance() {
        if (me == null)
            me = new SSD1306Display();

        return me;
    }

    private void init() {
        try {
            display = new Ssd1306("I2C1");
            display.clearPixels();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void drawBitmap(Resources res) {
        Bitmap img = BitmapFactory.decodeResource(res, R.drawable.weathersun);
        BitmapHelper.setBmpData(display, 0,0, img, false);
        try {
            display.show();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void drawLine(int y) {
        for (int x = 0; x < display.getLcdWidth(); x++)
            display.setPixel(x, y, true);

        try {
            display.show();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
