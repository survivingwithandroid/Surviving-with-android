package com.survivingwithandroid.androidthings.androidthings_facedetector;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.things.contrib.driver.ssd1306.BitmapHelper;
import com.google.android.things.contrib.driver.ssd1306.Ssd1306;
import java.io.IOException;

/*
 * Copyright (C) 2019 Francesco Azzola - Surviving with Android (https://www.survivingwithandroid.com)
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

public class DisplayManager {

    private static final String TAG = DisplayManager.class.getName();

    private Ssd1306 display;

    public DisplayManager() {}

    public void init() {
        try {
            display = new Ssd1306("I2C1");
            display.setContrast(100);
            Log.d(TAG, "Display initialized!");
        }
        catch(IOException ioe) {
            Log.e(TAG, "Error initialing the display");
            ioe.printStackTrace();
        }
    }

    public void setImage(Resources res, int resId) {
        display.clearPixels();
        Bitmap bmp = BitmapFactory.decodeResource(res, resId);
        BitmapHelper.setBmpData(display, 0,0, bmp, true);

        try {
            display.show();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
