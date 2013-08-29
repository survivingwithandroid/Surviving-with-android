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

import java.util.List;

import com.survivingwithandroid.expandablelistview.model.Category;
import com.survivingwithandroid.expandablelistview.model.ItemDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableAdapter extends BaseExpandableListAdapter {

	private List<Category> catList;
	private int itemLayoutId;
	private int groupLayoutId;
	private Context ctx;
	
	public ExpandableAdapter(List<Category> catList, Context ctx) {
		
		this.itemLayoutId = R.layout.item_layout;
		this.groupLayoutId = R.layout.group_layout;
		this.catList = catList;
		this.ctx = ctx;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return catList.get(groupPosition).getItemList().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return catList.get(groupPosition).getItemList().get(childPosition).hashCode();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		View v = convertView;
		
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater)ctx.getSystemService
				      (Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.item_layout, parent, false);
		}
		
		TextView itemName = (TextView) v.findViewById(R.id.itemName);
		TextView itemDescr = (TextView) v.findViewById(R.id.itemDescr);
		
		ItemDetail det = catList.get(groupPosition).getItemList().get(childPosition);
		
		itemName.setText(det.getName());
		itemDescr.setText(det.getDescr());
		
		return v;
		
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		int size = catList.get(groupPosition).getItemList().size();
		System.out.println("Child for group ["+groupPosition+"] is ["+size+"]");
		return size;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return catList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
	  return catList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return catList.get(groupPosition).hashCode();
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		View v = convertView;
		
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater)ctx.getSystemService
				      (Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.group_layout, parent, false);
		}
		
		TextView groupName = (TextView) v.findViewById(R.id.groupName);
		TextView groupDescr = (TextView) v.findViewById(R.id.groupDescr);
		
			
		Category cat = catList.get(groupPosition);
		
		groupName.setText(cat.getName());
		groupDescr.setText(cat.getDescr());
		
		return v;

	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
