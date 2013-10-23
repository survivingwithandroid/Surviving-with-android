package com.survivingwithandroid.jsontutorial;

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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HttpClient {

	private static String URL = "http://validate.jsontest.com";

	public String postJsonData(String data) {
			
		try {
			StringBuffer buffer = new StringBuffer();
			// Apache HTTP Reqeust
			System.out.println("Sending data..");
			System.out.println("Data: [" + data + "]");
			org.apache.http.client.HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(URL);
			List<NameValuePair> nvList = new ArrayList<NameValuePair>();
			BasicNameValuePair bnvp = new BasicNameValuePair("json", data);
			// We can add more
			nvList.add(bnvp);
			post.setEntity(new UrlEncodedFormEntity(nvList));

			HttpResponse resp = client.execute(post);
			// We read the response
			InputStream is = resp.getEntity().getContent();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			StringBuilder str = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				str.append(line + "\n");
			}
			is.close();
			buffer.append(str.toString());
			// Done!
			
			return buffer.toString();
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return null;
	}
}
