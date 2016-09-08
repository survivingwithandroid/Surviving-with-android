package survivingwithandroid.com.uvindex;

/*
 * Copyright (C) 2016, francesco Azzola
 *
 *(http://www.survivingwithandroid.com)
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
 *
 */

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener,
                   GoogleApiClient.ConnectionCallbacks,
                   LocationListener {

    private GoogleApiClient googleClient;
    private OkHttpClient httpClient;


    private static final String APP_ID = "83cbfb2b392a6f8caec0e83ab641e8d9";

    private static final String UV_URL =  "http://api.openweathermap.org/v3/uvi/";

    private TextView coordView;
    private TextView uvView;
    private TextView msgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordView = (TextView) findViewById(R.id.textCoord);
        uvView = (TextView) findViewById(R.id.textUV);
        msgView = (TextView) findViewById(R.id.textMsg);

        initGoogleClient();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getUVIndex();
            }
        });

        // Init Http Client
        httpClient = new OkHttpClient();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initGoogleClient() {
        googleClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    // Connection failure
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showErrorMessage();
        return;
    }

    // Connection established
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getUVIndex();
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onStop() {
        googleClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onStart() {
        googleClient.connect();
        super.onStart();
    }

    private void getUVIndex() {
        System.out.println("Get UV Index");
        if (!googleClient.isConnected()) {
            // Try to connect again
            googleClient.connect();
        }

        if (!googleClient.isConnected()) {
            showErrorMessage();
            return;
        }

        try {
            Location loc = getLocation();

                LocationRequest req = new LocationRequest();
                req.setInterval(5* 1000);
                req.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                LocationServices.FusedLocationApi.requestLocationUpdates(googleClient, req, this);

        }
        catch(SecurityException t) {
            t.printStackTrace();
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    private Location getLocation() {
        try {
            Location loc = LocationServices.FusedLocationApi.getLastLocation(googleClient);
            return loc;
        }
        catch(SecurityException se) {}
        return null;
    }


    private void showErrorMessage() {
        Snackbar.make(findViewById(R.id.mainLyt), "Unable to connect to Google Play service", Snackbar.LENGTH_LONG);
    }

    private void handleConnection(Location loc) {

        double lat = loc.getLatitude();
        double lon = loc.getLongitude();

        // Make HTTP request according to Openweathermap API
       String url = UV_URL + ((int) lat) + "," + ( (int) lon) + "/current.json?appid=" + APP_ID;
        System.out.println("URL ["+url+"]");
       Request request = new Request.Builder()
               .url(url)
               .build();
       httpClient.newCall(request).enqueue(new Callback() {
           @Override
           public void onFailure(Call call, IOException e) {
               // Handle failure in HTTP request
           }

           @Override
           public void onResponse(Call call, Response response) throws IOException {
                // Ok we have the response...parse it
               try {
                   JSONObject obj = new JSONObject(response.body().string());
                   final double uvIndex = obj.optDouble("data");
                   System.out.println("UV Index ["+uvIndex+"]");
                   JSONObject jsonLoc = obj.getJSONObject("location");
                   final double cLon = jsonLoc.getDouble("longitude");
                   final double cLat = jsonLoc.getDouble("latitude");

                   Handler handler = new Handler(MainActivity.this.getMainLooper());
                   handler.post(new Runnable() {
                       @Override
                       public void run() {
                           coordView.setText(cLat + "," + cLon);
                           uvView.setText(String.valueOf(uvIndex));
                           if (uvIndex <= 2.9) {
                               uvView.setTextColor(Color.GREEN);
                               msgView.setText("Low");
                           }
                           else if (uvIndex <= 5.9) {
                               uvView.setTextColor(Color.YELLOW);
                               msgView.setText("Moderate");
                           }
                           else if (uvIndex <= 7.9) {
                               uvView.setTextColor(Color.parseColor("#DF7401"));
                               msgView.setText("High");
                           }
                           else if (uvIndex <= 10.9) {
                               uvView.setTextColor(Color.RED);
                               msgView.setText("Very High");
                           }
                           else if (uvIndex >= 11) {
                               uvView.setTextColor(Color.parseColor("#8258FA"));
                               msgView.setText("Extreme");
                           }
                       }
                   });
               }
               catch(JSONException jex) {
                   jex.printStackTrace();
               }
           }
       });
    }

    @Override
    public void onLocationChanged(Location location) {
        handleConnection(location);
    }
}
