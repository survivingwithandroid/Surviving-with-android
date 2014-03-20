package com.survivingwithandroid.androidrestfulintentservice;


import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class MainActivity extends Activity implements QuoteServiceReceiver.Listener {

	private TextView askValue;
	private TextView bidValue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	   
	   final EditText symEdt = (EditText) findViewById(R.id.edtSym);
	   Button b = (Button) findViewById(R.id.btnGo);
	   askValue = (TextView) findViewById(R.id.askValue);
	   bidValue = (TextView) findViewById(R.id.bidValue);
	        
	        b.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                Log.d("Srv", "Client get quote!");
	                // Create the stock
	                final Stock stock = new Stock();
	                stock.setSymbol(symEdt.getText().toString());
	                Log.d("SwA", "Calling method");					               
					startService(createCallingIntent(stock));
	                
	            }
	        });
	    }
	
	
	private Intent createCallingIntent(Stock stock) {
		Intent i = new Intent(this, QuoteService.class);
		QuoteServiceReceiver receiver = new QuoteServiceReceiver(new Handler());
		receiver.setListener(this);		
		i.putExtra("rec", receiver);
		i.putExtra("stock", stock);
		return i;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
    // Callback method
	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		Stock stock = resultData.getParcelable("stock");
		Log.d("Srv", "Stock ["+stock+"]");
		
		askValue.setText("" + stock.getAskValue());
		bidValue.setText("" + stock.getBidValue());
	}
 

}
