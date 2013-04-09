/*
 * Copyright (C) 2013 jfrankie (http://www.survivingwithandroid.com)
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
package com.survivingwithandroid.listviewroundcorners;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PlanetAdapter extends ArrayAdapter<Planet> {

	private List<Planet> planetList;
	private Context context;
	
	
	
	public PlanetAdapter(List<Planet> planetList, Context ctx) {
		super(ctx, R.layout.row_layout, planetList);
		this.planetList = planetList;
		this.context = ctx;
	}
	
	public int getCount() {
		return planetList.size();
	}

	public Planet getItem(int position) {
		return planetList.get(position);
	}

	public long getItemId(int position) {
		return planetList.get(position).hashCode();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		PlanetHolder holder = new PlanetHolder();
		
		// First let's verify the convertView is not null
		if (convertView == null) {
			// This a new view we inflate the new layout
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.row_layout, null);
			// Now we can fill the layout with the right values
			TextView tv = (TextView) v.findViewById(R.id.name);
			
			holder.planetNameView = tv;

			v.setTag(holder);
			
			if ( position % 2 == 0)
			  v.setBackgroundResource(R.drawable.listview_selector_even);
			else
				v.setBackgroundResource(R.drawable.listview_selector_odd);
		}
		else 
			holder = (PlanetHolder) v.getTag();
		
		Planet p = planetList.get(position);
		holder.planetNameView.setText(p.getName());
		
		return v;
	}
	
	/* *********************************
	 * We use the holder pattern        
	 * It makes the view faster and avoid finding the component
	 * **********************************/
	
	private static class PlanetHolder {
		public TextView planetNameView;
	}
	

}
