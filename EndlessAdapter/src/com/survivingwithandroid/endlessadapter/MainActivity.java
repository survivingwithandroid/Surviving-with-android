package com.survivingwithandroid.endlessadapter;

/*
 * Copyright (C) 2012 Surviving with Android (http://www.survivingwithandroid.com)
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

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity implements EndlessListView.EndlessListener {

	private final static int ITEM_PER_REQUEST = 50;
	EndlessListView lv;
	
	int mult = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		lv = (EndlessListView) findViewById(R.id.el);
		
		
		EndlessAdapter adp = new EndlessAdapter(this, createItems(mult), R.layout.row_layout);
		lv.setLoadingView(R.layout.loading_layout);
		lv.setAdapter(adp);
		
		lv.setListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private class FakeNetLoader extends AsyncTask<String, Void, List<String>> {

		@Override
		protected List<String> doInBackground(String... params) {	
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
			return createItems(mult);
		}

		@Override
		protected void onPostExecute(List<String> result) {			
			super.onPostExecute(result);
			lv.addNewData(result);
		}
		
		
		
	}
	
	
	
	private List<String> createItems(int mult) {
		List<String> result = new ArrayList<String>();
		
		for (int i=0; i < ITEM_PER_REQUEST; i++) {
			result.add("Item " + (i * mult));
		}
		
		return result;
	}

	@Override
	public void loadData() {
		System.out.println("Load data");
		mult += 10;
		// We load more data here
		FakeNetLoader fl = new FakeNetLoader();
		fl.execute(new String[]{});
		
	}

}
