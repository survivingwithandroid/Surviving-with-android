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


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;

public class ConnectionMonitorReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context ctx, Intent intent) {
		System.out.println("Receive..");
		NetworkInfo netInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
		
		if (netInfo == null)
			return ;
		
		System.out.println("Here");
		if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			String proxyHost = System.getProperty( "http.proxyHost" );

	        String proxyPort = System.getProperty( "http.proxyPort" );
	        
	        if (proxyHost != null)
	        	HttpClient.getInstance().setProxy(proxyHost, proxyPort);
	        else
	        	HttpClient.getInstance().setProxy(null, null);
	        
		}
		
		
	}

}
