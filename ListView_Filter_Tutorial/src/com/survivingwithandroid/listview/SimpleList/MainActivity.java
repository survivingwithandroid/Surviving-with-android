/*
 * Copyright (C) 2012 jfrankie (http://www.survivingwithandroid.com)
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


package com.survivingwithandroid.listview.SimpleList;

/**
 * @author Surviving with android (jfrankie)
 *
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	// The data to show
	List<Planet> planetsList = new ArrayList<Planet>();
	PlanetAdapter aAdpt;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initList();
        
        // We get the ListView component from the layout
        ListView lv = (ListView) findViewById(R.id.listView);
        
        
        // This is a simple adapter that accepts as parameter
        // Context
        // Data list
        // The row layout that is used during the row creation
        // The keys used to retrieve the data
        // The View id used to show the data. The key number and the view id must match
        //aAdpt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, planetsList);
        aAdpt = new PlanetAdapter(planetsList, this);
        lv.setAdapter(aAdpt);
        
        // React to user clicks on item
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
					long id) {
				
				
				// We know the View is a <extView so we can cast it
				TextView clickedView = (TextView) view;

				Toast.makeText(MainActivity.this, "Item with id ["+id+"] - Position ["+position+"] - Planet ["+clickedView.getText()+"]", Toast.LENGTH_SHORT).show();

			}
		   });
        
          // we register for the contextmneu        
          registerForContextMenu(lv);
          
          // TextFilter
          lv.setTextFilterEnabled(true);
          EditText editTxt = (EditText) findViewById(R.id.editTxt);          

          editTxt.addTextChangedListener(new TextWatcher() {
          
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					System.out.println("Text ["+s+"] - Start ["+start+"] - Before ["+before+"] - Count ["+count+"]");
					if (count < before) {
						// We're deleting char so we need to reset the adapter data
						aAdpt.resetData();
					}
						
					aAdpt.getFilter().filter(s.toString());
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
				}
			});
        }
   
    
     // We want to create a context Menu when the user long click on an item
    @Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterContextMenuInfo aInfo = (AdapterContextMenuInfo) menuInfo;
		
		// We know that each row in the adapter is a Map
		Planet planet =  aAdpt.getItem(aInfo.position);
		
		menu.setHeaderTitle("Options for " + planet.getName());
		menu.add(1, 1, 1, "Details");
		menu.add(1, 2, 2, "Delete");
		
	}
    
    
    

    // This method is called when user selects an Item in the Context menu
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		AdapterContextMenuInfo aInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		planetsList.remove(aInfo.position);
		aAdpt.notifyDataSetChanged();
		return true;
	}


	private void initList() {
        // We populate the planets
       
        planetsList.add(new Planet("Mercury", 10));
        planetsList.add(new Planet("Venus", 20));
        planetsList.add(new Planet("Mars", 30));
        planetsList.add(new Planet("Jupiter", 40));
        planetsList.add(new Planet("Saturn", 50));
        planetsList.add(new Planet("Uranus", 60));
        planetsList.add(new Planet("Neptune", 70));
     
    	
    }
    
    
    // Handle user click
    public void addPlanet(View view) {
    	final Dialog d = new Dialog(this);
    	d.setContentView(R.layout.dialog);
    	d.setTitle("Add planet");
    	d.setCancelable(true);
    	
    	final EditText edit = (EditText) d.findViewById(R.id.editTextPlanet);
    	Button b = (Button) d.findViewById(R.id.button1);
    	b.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				String planetName = edit.getText().toString();
				MainActivity.this.planetsList.add(new Planet(planetName, 0));
				MainActivity.this.aAdpt.notifyDataSetChanged(); // We notify the data model is changed
				d.dismiss();
			}
		});
    	
    	d.show();
    }

   
}
