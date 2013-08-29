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
package com.survivingwithandroid.weatherapp.fragment;

import com.survivingwithandroid.weatherapp.R;
import com.survivingwithandroid.weatherapp.WeatherHttpClient;

import com.survivingwithandroid.weatherapp.adapter.DailyForecastPageAdapter;
import com.survivingwithandroid.weatherapp.model.DayForecast;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Francesco
 *
 */
public class DayForecastFragment extends Fragment {
	
	private DayForecast dayForecast;
	private ImageView iconWeather;
	
	public DayForecastFragment() {}
	
	public void setForecast(DayForecast dayForecast) {
		
		this.dayForecast = dayForecast;
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dayforecast_fragment, container, false);
		
		TextView tempView = (TextView) v.findViewById(R.id.tempForecast);
		TextView descView = (TextView) v.findViewById(R.id.skydescForecast);
		tempView.setText( (int) (dayForecast.forecastTemp.min - 275.15) + "-" + (int) (dayForecast.forecastTemp.max - 275.15) );
		descView.setText(dayForecast.weather.currentCondition.getDescr());
		iconWeather = (ImageView) v.findViewById(R.id.forCondIcon);
		// Now we retrieve the weather icon
		JSONIconWeatherTask task = new JSONIconWeatherTask();
		task.execute(new String[]{dayForecast.weather.currentCondition.getIcon()});
		
		return v;
	}

	
	
	private class JSONIconWeatherTask extends AsyncTask<String, Void, byte[]> {
		
		@Override
		protected byte[] doInBackground(String... params) {
			
			byte[] data = null;
			
			try {
				
				// Let's retrieve the icon
				data = ( (new WeatherHttpClient()).getImage(params[0]));
				
			} catch (Exception e) {				
				e.printStackTrace();
			}
			
			return data;
	}
		
		
		
		
	@Override
		protected void onPostExecute(byte[] data) {			
			super.onPostExecute(data);
			
			if (data != null) {
				Bitmap img = BitmapFactory.decodeByteArray(data, 0, data.length); 
				iconWeather.setImageBitmap(img);
			}
		}



  }
}
