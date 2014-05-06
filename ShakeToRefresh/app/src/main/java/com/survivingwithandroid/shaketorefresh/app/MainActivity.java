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

package com.survivingwithandroid.shaketorefresh.app;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends Activity implements ShakeEventManager.ShakeListener {

    private String[] dataList;
    private ArrayAdapter<String> adpt;
    private ListView listView;
    private ShakeEventManager sd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createData();
        adpt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adpt);
        sd = new ShakeEventManager();
        sd.setListener(this);
        sd.init(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void createData() {
        int start = (int) (Math.random() * 10);
        dataList = new String[15];

        for (int i=0; i < dataList.length; i++)
            dataList[i] = "Item_" + (start + i);
    }

    @Override
    public void onShake() {
        createData();
        adpt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        Toast.makeText(this, "Refresh data...", Toast.LENGTH_SHORT).show();
        listView.setAdapter(adpt);
    }


    @Override
    protected void onResume() {
        super.onResume();
        sd.register();
    }


    @Override
    protected void onPause() {
        super.onPause();
        sd.deregister();
    }
}
