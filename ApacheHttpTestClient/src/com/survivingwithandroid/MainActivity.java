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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;



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
		Button btnCookie = (Button) findViewById(R.id.btnCookie);

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

		btnCookie.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, CookieActivity.class);
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
			
			// Apache HTTP Reqeust
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			List<NameValuePair> nvList = new ArrayList<NameValuePair>();
			BasicNameValuePair bnvp = new BasicNameValuePair("name", name);
			// We can add more
			nvList.add(bnvp);
			post.setEntity(new UrlEncodedFormEntity(nvList));
			
			HttpResponse resp = client.execute(post);
			// We read the response
			InputStream is  = resp.getEntity().getContent();			
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder str = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                str.append(line + "\n");
            }
            is.close();
            buffer.append(str.toString());			
			// Done!
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
		
		return buffer.toString();
	}
}
