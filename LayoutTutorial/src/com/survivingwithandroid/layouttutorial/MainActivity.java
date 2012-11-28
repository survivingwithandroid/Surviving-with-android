package com.survivingwithandroid.layouttutorial;

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

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    private LinearLayout ll;
    private float startY;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ll = (LinearLayout) findViewById(R.id.slider);
        ll.setVisibility(View.GONE);
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = event.getY();
			break;
		case MotionEvent.ACTION_UP: {
			float endY = event.getY();
			
			if (endY < startY) {
				System.out.println("Move UP");
				ll.setVisibility(View.VISIBLE);
			}
			else {
				ll.setVisibility(View.GONE);
			}
	    }
		
		
	
	}
		return true;
	}
	

    
}
