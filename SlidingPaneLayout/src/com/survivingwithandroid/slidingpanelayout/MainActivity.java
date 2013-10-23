package com.survivingwithandroid.slidingpanelayout;
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

import com.survivingwithandroid.slidingpanelayout.ListBookmarkFragment.BookmarkListener;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity implements BookmarkListener{
	SlidingPaneLayout pane;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pane = (SlidingPaneLayout) findViewById(R.id.sp);
		pane.setPanelSlideListener(new PaneListener());
		
		if (!pane.isSlideable()) {
			getFragmentManager().findFragmentById(R.id.leftpane).setHasOptionsMenu(false);
			getFragmentManager().findFragmentById(R.id.rightpane).setHasOptionsMenu(true);
		}
	}


	
	private class PaneListener implements SlidingPaneLayout.PanelSlideListener {

		@Override
		public void onPanelClosed(View view) {
			System.out.println("Panel closed");
			
			getFragmentManager().findFragmentById(R.id.leftpane).setHasOptionsMenu(false);
			getFragmentManager().findFragmentById(R.id.rightpane).setHasOptionsMenu(true);
		}

		@Override
		public void onPanelOpened(View view) {
			System.out.println("Panel opened");	
			getFragmentManager().findFragmentById(R.id.leftpane).setHasOptionsMenu(true);
			getFragmentManager().findFragmentById(R.id.rightpane).setHasOptionsMenu(false);
		}

		@Override
		public void onPanelSlide(View view, float arg1) {
			System.out.println("Panel sliding");
		}
		
	}



	@Override
	public void onChangeBookmark(String bookmark) {
		// We get notified when user clicks on a bookmark in the ListBookmarkFragment
		System.out.println("Bookmark ["+bookmark+"]");
		ViewBookmarkFragment f = (ViewBookmarkFragment) getFragmentManager().findFragmentById(R.id.rightpane);
		f.setBookmark(bookmark);
		
	}

}
