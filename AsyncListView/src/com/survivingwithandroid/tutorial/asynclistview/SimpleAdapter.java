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

package com.survivingwithandroid.tutorial.asynclistview;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class SimpleAdapter extends ArrayAdapter<Contact> {
	
	private List<Contact> itemList;
	private Context context;
		
	public SimpleAdapter(List<Contact> itemList, Context ctx) {
		super(ctx, android.R.layout.simple_list_item_1, itemList);
		this.itemList = itemList;
		this.context = ctx;		
	}
	
	public int getCount() {
		if (itemList != null)
			return itemList.size();
		return 0;
	}

	public Contact getItem(int position) {
		if (itemList != null)
			return itemList.get(position);
		return null;
	}

	public long getItemId(int position) {
		if (itemList != null)
			return itemList.get(position).hashCode();
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.list_item, null);
		}
		
		Contact c = itemList.get(position);
		TextView text = (TextView) v.findViewById(R.id.name);
		text.setText(c.getName());

		TextView text1 = (TextView) v.findViewById(R.id.surname);
		text1.setText(c.getSurname());

		TextView text2 = (TextView) v.findViewById(R.id.email);
		text2.setText(c.getEmail());

		TextView text3 = (TextView) v.findViewById(R.id.phone);
		text3.setText(c.getPhoneNum());

		return v;
		
	}

	public List<Contact> getItemList() {
		return itemList;
	}

	public void setItemList(List<Contact> itemList) {
		this.itemList = itemList;
	}

	
}
