package com.survivingwithandroid.ubiapp;/*
 * Copyright (C) 2015, francesco Azzola 
 *
 *(http://www.survivingwithandroid.com)
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
 *
 * 05/12/15
 */

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UbidotsClient {

    private UbiListener listener;

    public UbiListener getListener() {
        return listener;
    }

    public void setListener(UbiListener listener) {
        this.listener = listener;
    }

    public void handleUbidots(String varId, String apiKey, final UbiListener listener) {

        final List<Value> results = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder().addHeader("X-Auth-Token", apiKey)
                .url("http://things.ubidots.com/api/v1.6/variables/" + varId + "/values")
                .build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("Chart", "Network error");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                Log.d("Chart", body);

                try {
                    JSONObject jObj = new JSONObject(body);
                    JSONArray jRes = jObj.getJSONArray("results");
                    for (int i=0; i < jRes.length(); i++) {
                        JSONObject obj = jRes.getJSONObject(i);
                        Value val = new Value();
                        val.timestamp = obj.getLong("timestamp");
                        val.value  = (float) obj.getDouble("value");
                        results.add(val);
                    }

                    listener.onDataReady(results);

                }
                catch(JSONException jse) {
                    jse.printStackTrace();
                }

            }
        });

    }


    protected static class Value {
        float value;
        long timestamp;
    }

    protected interface  UbiListener {
        public void onDataReady(List<Value> result);
    }
}
