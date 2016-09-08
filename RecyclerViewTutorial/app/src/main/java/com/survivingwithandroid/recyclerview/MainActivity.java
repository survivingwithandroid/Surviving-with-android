package com.survivingwithandroid.recyclerview;

/*
* Copyright (C) 2016 Francesco Azzola - Surviving with Android (http://www.survivingwithandroid.com)
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


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Country> countryList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view);
        rv.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getApplicationContext(), R.drawable.item_decorator)));

        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        createList();
        CountryAdapter ca = new CountryAdapter(countryList);
        rv.setAdapter(ca);

        rv.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), rv,
                new RecyclerItemListener.RecyclerTouchListener() {
                   public void onClickItem(View v, int position) {
                       System.out.println("On Click Item interface");
                   }

                   public void onLongClickItem(View v, int position) {
                      System.out.println("On Long Click Item interface");
                   }
        }));

        // Add decorator
       // rv.addItemDecoration(new VerticalSpacingDecoration(64));
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

    private void createList() {
        countryList.add(new Country("Italy", 59.83));
        countryList.add(new Country("France", 66.03));
        countryList.add(new Country("Germany", 80.62));
        countryList.add(new Country("Spain", 46.77));
        countryList.add(new Country("Portugal", 10.46));
        countryList.add(new Country("Austria", 8.47));
        countryList.add(new Country("Netherlands", 16.8));
        countryList.add(new Country("Belgium", 11.2));
        countryList.add(new Country("Denmark", 5.67));
        countryList.add(new Country("Ireland", 4.63));
        countryList.add(new Country("Norway", 5.19));
        countryList.add(new Country("Sweden", 9.85));
        countryList.add(new Country("Finland", 5.47));
        countryList.add(new Country("Greece", 10.76));

    }
}
