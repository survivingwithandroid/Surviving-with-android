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

import java.util.ArrayList;
import java.util.List;

import com.survivingwithandroid.jsontutorial.model.Person;
import com.survivingwithandroid.jsontutorial.model.Person.Address;
import com.survivingwithandroid.jsontutorial.model.Person.PhoneNumber;
import com.survivingwithandroid.jsontutorial.util.JsonUtil;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Person p = new Person();
		p.setName("Android");
		p.setSurname("Swa");
		Person.Address add =  p.new Address();
		add.setAddress("infinite space, 000");
		add.setCity("Android city");
		add.setState("World");
		
		p.setAddress(add);
		
		Person.PhoneNumber pn = p.new PhoneNumber();
		pn.setNumber("11111");
		pn.setType("work");
		
		List<Person.PhoneNumber> pnList = new ArrayList<Person.PhoneNumber>();
		pnList.add(pn);
		
		Person.PhoneNumber pn1 = p.new PhoneNumber();
		pn1.setNumber("2222");
		pn1.setType("home");
		pnList.add(pn1);
		
		p.setPhoneList(pnList);
		
		
		SendDataTask task = new SendDataTask();
		task.execute(new Person[] {p});
		
	}
	
	
	private class SendDataTask extends AsyncTask<Person, Void, String> {

		@Override
		protected String doInBackground(Person... params) {
			HttpClient client = new HttpClient();
			String result = client.postJsonData(JsonUtil.toJSon(params[0]));
			return result;
		}

		@Override
		protected void onPostExecute(String result) {			
			super.onPostExecute(result);
			((TextView) MainActivity.this.findViewById(R.id.textResult)).setText(result);
		}
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
