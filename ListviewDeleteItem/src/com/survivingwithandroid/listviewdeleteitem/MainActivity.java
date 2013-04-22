package com.survivingwithandroid.listviewdeleteitem;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ListView lv = (ListView) findViewById(R.id.listView);
		
		List<ItemDetail> itemList = createItems(50);

		// Load animation
		final Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_anim);
		
		final ArrayAdapter<ItemDetail> aAdpt = new ArrayAdapter<ItemDetail>(this, android.R.layout.simple_list_item_1, itemList);
		lv.setAdapter(aAdpt);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, final View view, final int position,
					long id) {

				anim.setAnimationListener(new Animation.AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						
						view.setHasTransientState(true);
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						ItemDetail item = aAdpt.getItem(position);
						aAdpt.remove(item);
						view.setHasTransientState(false);
					}
				});
				view.startAnimation(anim);
			}
		});
		
	}

	
	private List<ItemDetail> createItems(int size) {
		List<ItemDetail> result = new ArrayList<ItemDetail>();
		for (int i=0; i < size; i++) {
			ItemDetail item = new ItemDetail(i, "Item " + i);
			result.add(item);
		}
		
		return result;
	}
	

}
