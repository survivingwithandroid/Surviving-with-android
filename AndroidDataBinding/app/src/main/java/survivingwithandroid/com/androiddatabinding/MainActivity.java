package survivingwithandroid.com.androiddatabinding;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.exception.WeatherLibException;
import com.survivingwithandroid.weather.lib.exception.WeatherProviderInstantiationException;
import com.survivingwithandroid.weather.lib.model.CurrentWeather;
import com.survivingwithandroid.weather.lib.model.Weather;
import com.survivingwithandroid.weather.lib.request.WeatherRequest;

import survivingwithandroid.com.androiddatabinding.databinding.ActivityMainBinding;
import survivingwithandroid.com.androiddatabinding.model.Data;
import survivingwithandroid.com.androiddatabinding.weather.CustomImageMapper;
import survivingwithandroid.com.androiddatabinding.weather.WClient;

public class MainActivity extends AppCompatActivity {

    private WeatherClient client;
    private ActivityMainBinding binding;
    private Data data = new Data();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setData(data);
        initToolbar();
        try {
            getWeather();
        } catch (WeatherProviderInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            try {
                getWeather();
            } catch (WeatherProviderInstantiationException e) {
                e.printStackTrace();
            }
            return true;
        }
        else if (id == R.id.action_search) {
            Intent i = new Intent(this, LocationActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
    }

    public void getWeather() throws WeatherProviderInstantiationException {
        Log.d("SwA", "Get weather");
        client = WClient.getInstance(this).getClient();

        client.getCurrentCondition(new WeatherRequest("3171180"), new WeatherClient.WeatherEventListener() {
            @Override
            public void onWeatherRetrieved(CurrentWeather currentWeather) {
                Weather weather =  currentWeather.weather;
                //data = new Data();

                data.setTemp( String.valueOf(weather.temperature.getTemp()) );
                data.setPressure(String.valueOf(weather.currentCondition.getPressure()));
                data.setHumidity(String.valueOf(weather.currentCondition.getHumidity()));

                data.setWind(String.valueOf(weather.wind.getSpeed()) + " " + String.valueOf(weather.wind.getDeg()));
                data.setDescr(String.valueOf(weather.currentCondition.getDescr()));
                data.setCity(weather.location.getCity() + "," + weather.location.getCountry());
                System.out.println("Icon [" +CustomImageMapper.getWeatherDrawable(weather.currentCondition.getWeatherId())+ "]");
                //data.setIcon(CustomImageMapper.getWeatherDrawable(weather.currentCondition.getWeatherId()));
                data.setIcon(R.drawable.snowcircleday);
            }

            @Override
            public void onWeatherError(WeatherLibException e) {

            }

            @Override
            public void onConnectionError(Throwable throwable) {
                    // We will handle the error
            }
        });


    }
}
