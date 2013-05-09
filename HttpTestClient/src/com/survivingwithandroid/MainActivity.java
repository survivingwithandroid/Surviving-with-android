package com.survivingwithandroid;

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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;



import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity {

	private MenuItem item;
	private String url = "http://10.0.2.2:8080/TestAndroid/TestServlet";
	EditText edtResp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final EditText edtTxt = (EditText) findViewById(R.id.editText1);
		Button btnSend = (Button) findViewById(R.id.buttonSend);
		Button btnDownload = (Button) findViewById(R.id.btnDownload);
		Button btnUpload = (Button) findViewById(R.id.btnUpload);

		edtResp = (EditText) findViewById(R.id.edtResp);
		
		btnSend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String name = edtTxt.getText().toString();
				item.setActionView(R.layout.progress);
				SendHttpRequestTask t = new SendHttpRequestTask();
				
				String[] params = new String[]{url, name};
				t.execute(params);
			}
		});
		
		btnDownload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, DownloadActivity.class);
				startActivity(i);
			}
		});	
		
		
		btnUpload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, UploadActivity.class);
				startActivity(i);
			}
		});		

	}
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		item = menu.getItem(0);
		return true;
	}


	private class SendHttpRequestTask extends AsyncTask<String, Void, String> {

		
		@Override
		protected String doInBackground(String... params) {
			String url = params[0];
			String name = params[1];
			
			String data = sendHttpRequest(url, name);
			System.out.println("Data ["+data+"]");
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			edtResp.setText(result);
			item.setActionView(null);
			
		}
		
		
		
	}

	private String sendHttpRequest(String url, String name) {
		StringBuffer buffer = new StringBuffer();
		try {
			System.out.println("URL ["+url+"] - Name ["+name+"]");
			
			HttpURLConnection con = (HttpURLConnection) ( new URL(url)).openConnection();
			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.connect();
			con.getOutputStream().write( ("name=" + name).getBytes());
			
			InputStream is = con.getInputStream();
			byte[] b = new byte[1024];
			
			while ( is.read(b) != -1)
				buffer.append(new String(b));
			
			con.disconnect();
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
		
		return buffer.toString();
	}
}
