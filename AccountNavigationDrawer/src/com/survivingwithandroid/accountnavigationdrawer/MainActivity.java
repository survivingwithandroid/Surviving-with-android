package com.survivingwithandroid.accountnavigationdrawer;

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.TransactionTooLargeException;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private GoogleAccountCredential credential;
    private static Drive service;
	private static String MIME_FOLDER = "application/vnd.google-apps.folder";
	private static final int REQUEST_AUTHORIZATION = 1;
	private FrameLayout frame;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String[] itmes = getResources().getStringArray(R.array.items_array);
		
		
		frame = (FrameLayout) findViewById(R.id.content_frame);
		
		Spinner spinner = (Spinner) findViewById(R.id.spinnerAccount);
		
		String[] items = new String[]{"item1","item2","item3"};
		
		AccountManager accMgr = AccountManager.get(this);
		
		Account[] accountList = accMgr.getAccounts();
		final String[] accountNames = new String[accountList.length + 1];
		int i=1;
		accountNames[0] = getResources().getString(R.string.infospinner);
		
		for (Account account : accountList) {
			String name = account.name;
			accountNames[i++] = name;
		}
		
		ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, accountNames);
		spinner.setAdapter(adp);
		
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("Pos ["+position+"]");
				if (position == 0)
					return ;
				
				String currentAccount = accountNames[position];
				credential = GoogleAccountCredential.usingOAuth2(MainActivity.this, DriveScopes.DRIVE);
				credential.setSelectedAccountName(currentAccount);
				service = getDriveService(credential);
				AsyncAuth auth = new AsyncAuth();
				auth.execute("");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {		
			}
		});
		
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, itmes));

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        // We should handle actionbar event correctly
	}
	
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 switch (requestCode) {
	        case REQUEST_AUTHORIZATION:
	      	  System.out.println("Auth request");
	          if (resultCode == Activity.RESULT_OK) {
	        	  AsyncAuth auth = new AsyncAuth();
				  auth.execute("");
	          }

		 }
	}




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private class AsyncAuth extends AsyncTask<String, Void, List<File>> {

		
		@Override
		protected void onPreExecute() {
			WaitFragment wf = new WaitFragment();
			
			FragmentManager manager = MainActivity.this.getFragmentManager();
			FragmentTransaction trans = manager.beginTransaction();
		    trans.replace(R.id.content_frame, wf);
		    trans.commit();
		}

		@Override
		protected List<File>  doInBackground(String... params) {
			List<File> fileList = new ArrayList<File>();
			try {
			    Files.List request = service.files().list().setQ("mimeType = '" + MIME_FOLDER + "'");
				
			    FileList files = request.execute();
			    fileList = files.getItems();
			    System.out.println("File List ["+fileList+"]");

			}
			catch(UserRecoverableAuthIOException e) {
				 System.out.println("Intent e ["+e.getIntent()+"]");
	  		     startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);  	  
			} catch (IOException e1) {			
				e1.printStackTrace();
			}
			
			return fileList;
		}

		@Override
		protected void onPostExecute(List<File> result) {
			FragmentManager manager = MainActivity.this.getFragmentManager();
			FragmentTransaction trans = manager.beginTransaction();
		    
			ListFragment lf = new ListFragment();
			List<String> itemList = new ArrayList<String>();
			for (File f : result) {
				itemList.add(f.getTitle());
				System.out.println("Title ["+f.getTitle()+"]");
			}
			lf.setItemList(itemList);
			
			trans.replace(R.id.content_frame, lf);
		    trans.commit();

		}
		
		
		
	}
	
	private Drive getDriveService(GoogleAccountCredential credential) {
	    return new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential)
	        .build();
   }
}
