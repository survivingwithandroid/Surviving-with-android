/*
 * Copyright (C) 2014 Francesco Azzola
 *  Surviving with Android (http://www.survivingwithandroid.com)
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
package com.survivingwithandroid.materialweather;

import android.util.Log;

import com.survivingwithandroid.materialweather.R;

public class WeatherIconMapper {

    public static int getWeatherResource(String id, int wId) {
        // Log.d("App", "Id ["+id+"]");
        if (wId == 500)
            return R.drawable.w500d;

        if (wId == 501)
            return R.drawable.w501d;

        if (wId == 212)
            return R.drawable.w212d;

        if (id.equals("01d"))
            return R.drawable.w01d;
        else if (id.equals("01n"))
            return R.drawable.w01n;
        else if (id.equals("02d") || id.equals("02n"))
            return R.drawable.w02d;
        else if (id.equals("03d") || id.equals("03n"))
            return R.drawable.w03d;
        else if (id.equals("03d") || id.equals("03n"))
            return R.drawable.w03d;
        else if (id.equals("04d") || id.equals("04n"))
            return R.drawable.w04d;
        else if (id.equals("09d") || id.equals("09n"))
            return R.drawable.w500d;
        else if (id.equals("10d") || id.equals("10n"))
            return R.drawable.w501d;
        else if (id.equals("11d") || id.equals("11n"))
            return R.drawable.w212d;
        else if (id.equals("13d") || id.equals("13n"))
            return R.drawable.w13d;
        else if (id.equals("50d") || id.equals("50n"))
            return R.drawable.w50d;


        return R.drawable.w01d;

    }

}

