package com.survivingwithandroid.androidthings.nearby;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "NearbyApp";
    private NearbyDsvManager dsvManager;

    private EditText et;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn);
        et = (EditText) findViewById(R.id.ed);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = et.getText().toString();
                Log.d(TAG, "Txt ["+txt+"]");
                dsvManager.sendData(txt);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "Ask for permission");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Log.d(TAG, "Permission granted");
                NearbyDsvManager dsvManager = new NearbyDsvManager(this,listener);

            }
            else {
                Log.d(TAG, "Permission granted");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1410);
            }
        }
        else {
            Log.d(TAG, "Permission granted 1");
               dsvManager = new NearbyDsvManager(this, listener);
        }


    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d(TAG, "On Request Permission");
        switch (requestCode) {
            case 1410:
                dsvManager = new NearbyDsvManager(this.getApplication(), listener);
                break;
        }
    }

   private NearbyDsvManager.EventListener listener = new NearbyDsvManager.EventListener() {
       @Override
       public void onDiscovered() {
           Toast.makeText(MainActivity.this, "Endpoint discovered", Toast.LENGTH_LONG).show();

       }

       @Override
       public void startDiscovering() {
           Toast.makeText(MainActivity.this, "Start discovering...", Toast.LENGTH_LONG).show();
       }

       @Override
       public void onConnected() {
           Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_LONG).show();

       }
   };


}
