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

package com.survivingwithandroid.expandablelistview;

import java.util.ArrayList;
import java.util.List;

import com.survivingwithandroid.expandablelistview.model.Category;
import com.survivingwithandroid.expandablelistview.model.ItemDetail;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ExpandableListView;

public class MainActivity extends Activity {

	private List<Category> catList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_main);
        
        ExpandableListView exList = (ExpandableListView) findViewById(R.id.expandableListView1);
        exList.setIndicatorBounds(5, 5);
        ExpandableAdapter exAdpt = new ExpandableAdapter(catList, this);
        exList.setIndicatorBounds(0, 20);
        exList.setAdapter(exAdpt);
    }

    private void initData() {
    	catList = new ArrayList<Category>();
    	
    	Category cat1 = createCategory("Games", "Game for console", 1);
    	cat1.setItemList(createItems("Game Item", "This is the game n.", 5));

    	Category cat2 = createCategory("Mobile Phone", "All the mobile phone", 2);
    	cat2.setItemList(createItems("Phone Item", "This is the phone n.", 5));

    	catList.add(cat1);
    	catList.add(cat2);
    }
    
    private Category createCategory(String name, String descr, long id) {
    	return new Category(id, name, descr);
    }
    
    
    private List<ItemDetail> createItems(String name, String descr, int num) {
    	List<ItemDetail> result = new ArrayList<ItemDetail>();
    	
    	for (int i=0; i < num; i++) {
    		ItemDetail item = new ItemDetail(i, 0, "item" + i, "Descr" + i);
    		result.add(item);
    	}
    	
    	return result;
    }
}
