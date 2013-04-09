package com.survivingwithandroid.listviewroundcorners;

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

import java.util.ArrayList;
import java.util.List;



import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ListView lv = (ListView) findViewById(R.id.listView);
		PlanetAdapter pa = new PlanetAdapter(initList(), this);
		
		lv.setAdapter(pa);
	}
	
	private List<Planet> initList() {
        // We populate the planets
       
		List<Planet> planetsList = new ArrayList<Planet>();
		
        planetsList.add(new Planet("Mercury", 10));
        planetsList.add(new Planet("Venus", 20));
        planetsList.add(new Planet("Mars", 30));
        planetsList.add(new Planet("Jupiter", 40));
        planetsList.add(new Planet("Saturn", 50));
        planetsList.add(new Planet("Uranus", 60));
        planetsList.add(new Planet("Neptune", 70));
     
    	return planetsList;
    }

	
}
