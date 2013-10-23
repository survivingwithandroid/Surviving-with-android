package com.survivingwithandroid.slidingpanelayout.adapter;
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

import java.util.List;

import com.survivingwithandroid.slidingpanelayout.R;
import com.survivingwithandroid.slidingpanelayout.ListBookmarkFragment.Bookmark;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LinkAdapter extends ArrayAdapter<Bookmark> {
	
	private List<Bookmark> bookList;
	private Context ctx;
	
	public LinkAdapter(List<Bookmark> bookList, Context ctx) {
		super(ctx, R.layout.bookmark_layout, bookList);
		this.ctx = ctx;
		this.bookList = bookList;
	}

	@Override
	public int getCount() {
		return bookList.size();
	}

	@Override
	public Bookmark getItem(int position) {		
		return bookList.get(position);
	}

	@Override
	public long getItemId(int position) {		
		return bookList.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		if ( v == null) {
			LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = (View) inf.inflate(R.layout.bookmark_layout, null);
			TextView tName = (TextView) v.findViewById(R.id.bkmName);
			TextView tlink = (TextView) v.findViewById(R.id.bkmLink);
			
			tName.setText(bookList.get(position).name);
			tlink.setText(bookList.get(position).link);
		}
		
		return v;
	}
	
	
	

}
