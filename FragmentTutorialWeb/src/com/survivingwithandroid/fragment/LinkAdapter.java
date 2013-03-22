package com.survivingwithandroid.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.survivingwithandroid.fragment.model.LinkData;


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

public class LinkAdapter extends ArrayAdapter<LinkData>{

	private Context ctx;
	private List<LinkData> linkDataList;
	
	public LinkAdapter(List<LinkData> linkDataList, Context ctx) {
		super(ctx, R.layout.row_layout,linkDataList);
		this.ctx = ctx;
		this.linkDataList = linkDataList;
	}

	@Override
	public int getCount() {
		return linkDataList.size();
	}

	@Override
	public LinkData getItem(int position) {
		return linkDataList.get(position);
	}

	
	@Override
	public long getItemId(int position) {
		return linkDataList.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		   Log.d("SwA", "Adapter getView");
           View v = convertView;
           if (convertView == null) {
	   			LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	   			v = inflater.inflate(R.layout.row_layout, null);
           }
           
           TextView tName = (TextView) v.findViewById(R.id.name);
           TextView lName = (TextView) v.findViewById(R.id.link);
           
           LinkData ld = linkDataList.get(position);
           tName.setText(ld.getName());
           lName.setText(ld.getLink());
           
           return v;
	
	}
	
	
	
}
