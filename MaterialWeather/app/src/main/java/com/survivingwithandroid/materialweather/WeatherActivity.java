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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.WeatherConfig;
import com.survivingwithandroid.weather.lib.exception.WeatherLibException;
import com.survivingwithandroid.weather.lib.model.City;
import com.survivingwithandroid.weather.lib.model.CurrentWeather;
import com.survivingwithandroid.weather.lib.provider.openweathermap.OpenweathermapProviderType;
import com.survivingwithandroid.weather.lib.request.WeatherRequest;

import org.w3c.dom.Text;

import java.util.List;


public class WeatherActivity extends ActionBarActivity {

    private static WeatherClient weatherclient;
    private Toolbar toolbar;
    private ListView cityListView;
    private City currentCity;

    // Widget
    private TextView tempView;
    private ImageView weatherIcon;
    private TextView pressView;
    private TextView humView;
    private TextView windView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //
        tempView = (TextView) findViewById(R.id.temp);
        weatherIcon = (ImageView) findViewById(R.id.weather_icon);
        pressView = (TextView) findViewById(R.id.pressure);
        humView = (TextView) findViewById(R.id.hum);
        windView = (TextView) findViewById(R.id.wind);

        initWeatherClient();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_search) {
            // We show the dialog
            Dialog d = createDialog();
            d.show();
        }
        return super.onOptionsItemSelected(item);
    }


    private void initWeatherClient() {
        WeatherClient.ClientBuilder builder = new WeatherClient.ClientBuilder();
        WeatherConfig config = new WeatherConfig();
        config.unitSystem = WeatherConfig.UNIT_SYSTEM.M;
        config.lang = "en"; // If you want to use english
        config.maxResult = 5; // Max number of cities retrieved
        config.numDays = 6; // Max num of days in the forecast

        try {
            weatherclient = builder.attach(this)
                    .provider(new OpenweathermapProviderType())
                    .httpClient(com.survivingwithandroid.weather.lib.client.volley.WeatherClientDefault.class)
                    .config(config)
                    .build();
        }
        catch(Throwable t) {
            // we will handle it later
        }
    }

    private void getWeather() {
        weatherclient.getCurrentCondition(new WeatherRequest(currentCity.getId()),
                             new WeatherClient.WeatherEventListener() {
                                 @Override
                                 public void onWeatherRetrieved(CurrentWeather currentWeather) {
                                     // We have the current weather now
                                     // Update subtitle toolbar
                                     toolbar.setSubtitle(currentWeather.weather.currentCondition.getDescr());
                                     tempView.setText(String.format("%.0f",currentWeather.weather.temperature.getTemp()));
                                     pressView.setText(String.valueOf(currentWeather.weather.currentCondition.getPressure()));
                                     windView.setText(String.valueOf(currentWeather.weather.wind.getSpeed()));
                                     humView.setText(String.valueOf(currentWeather.weather.currentCondition.getHumidity()));
                                     weatherIcon.setImageResource(WeatherIconMapper.getWeatherResource(currentWeather.weather.currentCondition.getIcon(), currentWeather.weather.currentCondition.getWeatherId()));

                                    setToolbarColor(currentWeather.weather.temperature.getTemp());
                                 }

                                 @Override
                                 public void onWeatherError(WeatherLibException e) {

                                 }

                                 @Override
                                 public void onConnectionError(Throwable throwable) {

                                 }
                             });
    }

    private Dialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.select_city_dialog, null);
        builder.setView(v);

        EditText et = (EditText) v.findViewById(R.id.ptnEdit);
        cityListView = (ListView) v.findViewById(R.id.cityList);
        cityListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentCity = (City) parent.getItemAtPosition(position);

            }
        });

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 3) {
                    // We start searching
                    weatherclient.searchCity(s.toString(), new WeatherClient.CityEventListener() {
                        @Override
                        public void onCityListRetrieved(List<City> cities) {
                            CityAdapter ca = new CityAdapter(WeatherActivity.this, cities);
                            cityListView.setAdapter(ca);

                        }

                        @Override
                        public void onWeatherError(WeatherLibException e) {

                        }

                        @Override
                        public void onConnectionError(Throwable throwable) {

                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // We update toolbar
                toolbar.setTitle(currentCity.getName() + "," + currentCity.getCountry());
                // Start getting weather
                getWeather();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }


    private void setToolbarColor(float temp) {
        int color = -1;

        if (temp < -10)
            color = getResources().getColor(R.color.primary_indigo);
        else if (temp >=-10 && temp <=-5)
            color = getResources().getColor(R.color.primary_blue);
        else if (temp >-5 && temp < 5)
            color = getResources().getColor(R.color.primary_light_blue);
        else if (temp >= 5 && temp < 10)
            color = getResources().getColor(R.color.primary_teal);
        else if (temp >= 10 && temp < 15)
            color = getResources().getColor(R.color.primary_light_green);
        else if (temp >= 15 && temp < 20)
            color = getResources().getColor(R.color.primary_green);
        else if (temp >= 20 && temp < 25)
            color = getResources().getColor(R.color.primary_lime);
        else if (temp >= 25 && temp < 28)
            color = getResources().getColor(R.color.primary_yellow);
        else if (temp >= 28 && temp < 32)
            color = getResources().getColor(R.color.primary_amber);
        else if (temp >= 32 && temp < 35)
            color = getResources().getColor(R.color.primary_orange);
        else if (temp >= 35)
            color = getResources().getColor(R.color.primary_red);

        toolbar.setBackgroundColor(color);

    }


    // This is the City Adapter used to fill the listview when user searchs for the city
    class CityAdapter extends ArrayAdapter<City> {

        private List<City> cityList;
        private Context ctx;

        public CityAdapter(Context ctx, List<City> cityList) {
            super(ctx, R.layout.city_row);
            this.cityList = cityList;
            this.ctx = ctx;
        }

        @Override
        public City getItem(int position) {
            if (cityList != null)
                return cityList.get(position);
            return null;
        }

        @Override
        public int getCount() {
            if (cityList == null)
                return 0;

            return cityList.size();
        }

        @Override
        public long getItemId(int position) {
            if (cityList == null)
                return -1;

            return cityList.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.city_row, null, false);
            }

            TextView tv = (TextView) v.findViewById(R.id.descrCity);

            tv.setText(cityList.get(position).getName() + "," + cityList.get(position).getCountry());

            return v;
        }
    }
}
