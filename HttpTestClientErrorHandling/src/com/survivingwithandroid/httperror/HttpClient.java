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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;

public class HttpClient {
	private String url;
    private HttpURLConnection con;
    private OutputStream os;
    
	private String delimiter = "--";
    private String boundary =  "SwA"+Long.toString(System.currentTimeMillis())+"SwA";
    
    private HttpConnectionRetryHandler handler;
    
    private String proxyHost;
    private String proxyPort;
    
    private static HttpClient me;
    
    public static HttpClient getInstance() {
    	if (me == null)
    		me = new HttpClient();
    	
    	return me;
    }
    
	

	private HttpClient() {	}
	
	public void connect(String method, Properties props) throws HttpClientException {
		boolean status  = true;
		int attemptNumber = 0;
		while (status) {
			try {
				attemptNumber++;
				doConnection(method, props);
				status = false;
			}
			catch(Throwable t) {
				if (handler != null) {
					status = handler.shouldRetry(t, attemptNumber);
					if (!status)
						throw new HttpClientException(t);
				}
				else {					
					throw new HttpClientException(t);
				}
			}
		}
		
	}
	
	private void doConnection(String method, Properties props ) throws IOException {
		System.out.println("URL ["+url+"]");
		
		   HttpURLConnection con = null;
		  
			
			if (proxyPort != null && proxyHost != null) {
				Proxy p = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, Integer.parseInt(proxyPort) ));
				con = (HttpURLConnection) ( new URL(url)).openConnection(p);
			}
			else
				 con = (HttpURLConnection) ( new URL(url)).openConnection();
			
			con.setRequestMethod(method);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setConnectTimeout(5 * 1000);
			
			if (props != null) {
				
				Enumeration keys = props.keys();
				
				while (keys.hasMoreElements()) {
					String key = (String) keys.nextElement();
					String val = props.getProperty(key);
					
					con.setRequestProperty(key, val);
				}
			}
			
			con.connect();
		
	}
	
	public byte[] downloadImage(String imgName) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			System.out.println("URL ["+url+"] - Name ["+imgName+"]");
			
			connect("Post", null);
			
			con.getOutputStream().write( ("name=" + imgName).getBytes());
			
			InputStream is = con.getInputStream();
			byte[] b = new byte[1024];
			
			while ( is.read(b) != -1)
				baos.write(b);
			
			con.disconnect();
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
		
		return baos.toByteArray();
	}

	public void connectForMultipart() throws Exception {
		
		Properties props = new Properties();
		props.setProperty("Connection", "Keep-Alive");
		props.setProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

		connect("POST", props);
		os = con.getOutputStream();
	}
	
	public void addFormPart(String paramName, String value) throws Exception {
		writeParamData(paramName, value);
	}
	
	public void addFilePart(String paramName, String fileName, byte[] data) throws Exception {
		os.write( (delimiter + boundary + "\r\n").getBytes());
		os.write( ("Content-Disposition: form-data; name=\"" + paramName +  "\"; filename=\"" + fileName + "\"\r\n"  ).getBytes());
		os.write( ("Content-Type: application/octet-stream\r\n"  ).getBytes());
		os.write( ("Content-Transfer-Encoding: binary\r\n"  ).getBytes());
		os.write("\r\n".getBytes());
   
		os.write(data);
		
		os.write("\r\n".getBytes());
	}
	
	public void finishMultipart() throws Exception {
		os.write( (delimiter + boundary + delimiter + "\r\n").getBytes());
	}
	
	
	public String getResponse() throws Exception {
		InputStream is = con.getInputStream();
		byte[] b1 = new byte[1024];
		StringBuffer buffer = new StringBuffer();
		
		while ( is.read(b1) != -1)
			buffer.append(new String(b1));
		
		con.disconnect();
		
		return buffer.toString();
	}
	

	
	private void writeParamData(String paramName, String value) throws Exception {
		
		
		os.write( (delimiter + boundary + "\r\n").getBytes());
		os.write( "Content-Type: text/plain\r\n".getBytes());
		os.write( ("Content-Disposition: form-data; name=\"" + paramName + "\"\r\n").getBytes());;
		os.write( ("\r\n" + value + "\r\n").getBytes());
			
		
	}

	public HttpConnectionRetryHandler getHandler() {
		return handler;
	}

	public void setHandler(HttpConnectionRetryHandler handler) {
		this.handler = handler;
	}

	public HttpURLConnection getConnection() {
		return con;
	}

	
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setProxy(String host, String port) {
		this.proxyHost = host;
		this.proxyPort = port;
	}
	
}
