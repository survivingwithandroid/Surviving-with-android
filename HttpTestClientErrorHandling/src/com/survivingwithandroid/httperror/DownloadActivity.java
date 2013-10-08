package com.survivingwithandroid.httperror;

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


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.survivingwithandroid.httperror.R;



import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class DownloadActivity extends Activity {

	private MenuItem item;
	private String url = "http://10.0.2.2:8080/TestAndroid/DownloadServlet";
	private String name = "logo.png";
	ImageView imgView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);
		
		imgView = (ImageView) findViewById(R.id.imgView1);
		String[] params = new String[] {url, name};
		
		SendHttpRequestTask task = new SendHttpRequestTask();
		task.execute(params);
	}
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		item = menu.getItem(0);
		return true;
	}


	private class SendHttpRequestTask extends AsyncTask<String, Void, byte[]> {

		
		@Override
		protected byte[] doInBackground(String... params) {
			String url = params[0];
			String name = params[1];
			
			HttpClient client = HttpClient.getInstance();
			client.setUrl(url);

			byte[] data = client.downloadImage(name);
			
			return data;
		}

		@Override
		protected void onPostExecute(byte[] result) {
			Bitmap img = BitmapFactory.decodeByteArray(result, 0, result.length);
			imgView.setImageBitmap(img);
			item.setActionView(null);
			
		}
		
		
		
	}

	
}
