package com.survivingwithandroid.fragment;

import java.util.ArrayList;
import java.util.List;

import com.survivingwithandroid.fragment.model.LinkData;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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

public class LinkListFragment extends Fragment {
	
	private static List<LinkData> linkDataList = new ArrayList<LinkData>();
	private LinkAdapter la;
	
	static {
		linkDataList.add(new LinkData("SwA", "http://www.survivingwithandroid.com"));

		linkDataList.add(new LinkData("Android", "http://www.android.com"));
		linkDataList.add(new LinkData("Google Mail", "http://mail.google.com"));
	}
	
	public LinkListFragment() {		
	}
	


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Here we set our custom adapter. Now we have the reference to the activity
	
	}


	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("SwA", "LV onCreateView");
		View v = inflater.inflate(R.layout.linklist_layout, container, false);
		ListView lv = (ListView) v.findViewById(R.id.urls);
		la = new LinkAdapter(linkDataList, getActivity());
		lv.setAdapter(la);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			 public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
                     long id) {
				LinkData data = ( (LinkAdapter) la ).getItem(position);
				( (ChangeLinkListener)  getActivity()).onLinkChange(data.getLink());
			}

		});
		return v;
	}






	@Override
	public void onAttach(Activity activity) {
		// We verify that our activity implements the listener
		if (! (activity instanceof ChangeLinkListener) )
			throw new ClassCastException();
		
		
		super.onAttach(activity);
	}
	
	

}
