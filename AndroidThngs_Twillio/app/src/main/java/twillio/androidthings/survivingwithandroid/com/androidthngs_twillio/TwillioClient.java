package twillio.androidthings.survivingwithandroid.com.androidthngs_twillio;

/*
 * Copyright (C) 2018 Francesco Azzola
 *  Surviving with Android (https://www.survivingwithandroid.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import android.util.Log;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import okio.BufferedSink;

public class TwillioClient {

    private static final String TWILLIO_ACCOUNT_SID = "";
    private static final String TWILLIO_AUTH_TOKEN = "";


    private static final String TWILLIO_BASE_URL = "https://api.twilio.com/2010-04-01/Accounts/" + TWILLIO_ACCOUNT_SID + "/Messages.json";

    private static final String ORIGIN_PHONE_NUMBER = "";
    private static final String DEST_PHONE_NUMBER = " ";

    private static OkHttpClient client;

    private static final String TAG = TwillioClient.class.getName();

    public static void sendSMS(String body) {

        client = new OkHttpClient.Builder().authenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                String credential = Credentials.basic(TWILLIO_ACCOUNT_SID, TWILLIO_AUTH_TOKEN);
                return response.request().newBuilder().header("Authorization", credential).build();
            }
        }).build();

        RequestBody reqBody = new FormBody.Builder()
                .add("To", DEST_PHONE_NUMBER)
                .add("From", ORIGIN_PHONE_NUMBER)
                .add("Body", body)
                .build();

        Request req = new Request.Builder()
                .url(TWILLIO_BASE_URL)
                .post(reqBody)
                .build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "Ok message sent");
                Log.d(TAG, "Response ["+
                        response.body().string()
                        +"]");
            }
        });

    }
}
