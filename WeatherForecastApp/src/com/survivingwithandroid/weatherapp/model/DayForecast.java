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

package com.survivingwithandroid.weatherapp.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Francesco
 *
 */
public class DayForecast {

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	public Weather weather = new Weather();
	public ForecastTemp forecastTemp = new ForecastTemp();
	public long timestamp;
	
	public class ForecastTemp {
		public float day;
		public float min;
		public float max;
		public float night;
		public float eve;
		public float morning;
	}
	
	public String getStringDate() {
		return sdf.format(new Date(timestamp));
	}
}
