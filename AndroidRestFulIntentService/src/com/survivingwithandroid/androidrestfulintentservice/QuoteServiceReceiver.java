package com.survivingwithandroid.androidrestfulintentservice;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
/*
 * Copyright (C) 2014 Francesco Azzola - Surviving with Android (http://www.survivingwithandroid.com)
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
public class QuoteServiceReceiver extends ResultReceiver {

	private Listener listener;
	
	public QuoteServiceReceiver(Handler handler) {
		super(handler);		
	}
	
	public void setListener(Listener listener) {
		this.listener = listener;
	}

	@Override
	protected void onReceiveResult(int resultCode, Bundle resultData) {
		if (listener != null)
			listener.onReceiveResult(resultCode, resultData);
	}
	
	
	public static interface Listener {
		void onReceiveResult(int resultCode, Bundle resultData);
	}

}
