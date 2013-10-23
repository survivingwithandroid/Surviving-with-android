package com.survivingwithandroid.jsontutorial.util;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.survivingwithandroid.jsontutorial.model.Person;
import com.survivingwithandroid.jsontutorial.model.Person.PhoneNumber;

public class JsonUtil {
	
	public static String toJSon(Person person) {
		
		try {
			// Here we convert Java Object to JSON 
			JSONObject jsonObj = new JSONObject();
			
			jsonObj.put("name", person.getName()); // Set the first name/pair 
			jsonObj.put("surname", person.getSurname());
			
			JSONObject jsonAdd = new JSONObject(); // we need another object to store the address
			jsonAdd.put("address", person.getAddress().getAddress());
			jsonAdd.put("city", person.getAddress().getCity());
			jsonAdd.put("state", person.getAddress().getState());
			
			// We add the object to the main object
			jsonObj.put("address", jsonAdd);
			
			// and finally we add the phone number
			// In this case we need a json array to hold the java list
			JSONArray jsonArr = new JSONArray();
			
			for (PhoneNumber pn : person.getPhoneList() ) {
				JSONObject pnObj = new JSONObject();
				pnObj.put("num", pn.getNumber());
				pnObj.put("type", pn.getType());
				jsonArr.put(pnObj);
			}
			
			jsonObj.put("phoneNumber", jsonArr);
			
			return jsonObj.toString();
			
		}
		catch(JSONException ex) {
			ex.printStackTrace();
		}
		
		return null;
		
	}

}
