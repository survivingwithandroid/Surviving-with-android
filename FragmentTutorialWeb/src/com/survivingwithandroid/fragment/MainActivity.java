package com.survivingwithandroid.fragment;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

/*
 * Copyright (C) 2012-2013 Surviving with Android (http://www.survivingwithandroid.com)
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

public class MainActivity extends Activity implements ChangeLinkListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}

	@Override
	public void onLinkChange(String link) {
		System.out.println("Listener");
		// Here we detect if there's dual fragment
        if (findViewById(R.id.fragPage) != null) {
        	WebViewFragment wvf = (WebViewFragment) getFragmentManager().findFragmentById(R.id.fragPage);
       	
    		if (wvf == null) {
    			System.out.println("Dual fragment - 1");
    			wvf = new WebViewFragment();
    			wvf.init(link);
    			// We are in dual fragment (Tablet and so on)
    			FragmentManager fm = getFragmentManager();
    			FragmentTransaction ft = fm.beginTransaction();
    			
    			
    			//wvf.updateUrl(link);
    			ft.replace(R.id.fragPage, wvf);
    			ft.commit();
    			
    		}
    		else {
        	 Log.d("SwA", "Dual Fragment update");
        	 wvf.updateUrl(link);
    		}
        }
		else {
			System.out.println("Start Activity");
			Intent i = new Intent(this, WebViewActivity.class);
			i.putExtra("link", link);
			startActivity(i);
		}
		
	}



}
