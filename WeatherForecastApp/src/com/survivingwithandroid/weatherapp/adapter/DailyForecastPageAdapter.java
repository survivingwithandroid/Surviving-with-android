/*
 * Copyright (C) 2013 Surviving with Android (http://www.survivingwithandroid.com)
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

package com.survivingwithandroid.weatherapp.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.survivingwithandroid.weatherapp.fragment.DayForecastFragment;
import com.survivingwithandroid.weatherapp.model.DayForecast;
import com.survivingwithandroid.weatherapp.model.WeatherForecast;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author Francesco
 *
 */
public class DailyForecastPageAdapter extends FragmentPagerAdapter {

	private int numDays;
	private FragmentManager fm;
	private WeatherForecast forecast;
	private final static SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MM");
	
	public DailyForecastPageAdapter(int numDays, FragmentManager fm, WeatherForecast forecast) {
		super(fm);
		this.numDays = numDays;
		this.fm = fm;
		this.forecast = forecast;
		
	}
	
	
	// Page title
	@Override
	public CharSequence getPageTitle(int position) {
		// We calculate the next days adding position to the current date
		Date d = new Date();
		Calendar gc =  new GregorianCalendar();
		gc.setTime(d);
		gc.add(GregorianCalendar.DAY_OF_MONTH, position);
		
		return sdf.format(gc.getTime());
		
		
	}



	@Override
	public Fragment getItem(int num) {
		DayForecast dayForecast = (DayForecast) forecast.getForecast(num);
		DayForecastFragment f = new DayForecastFragment();
		f.setForecast(dayForecast);
		return f;
	}

	/* 
	 * Number of the days we have the forecast
	 */
	@Override
	public int getCount() {
		
		return numDays;
	}

}
