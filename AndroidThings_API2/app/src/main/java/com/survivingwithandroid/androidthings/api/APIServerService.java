package com.survivingwithandroid.androidthings.api;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

import java.io.IOException;

/*
 * Copyright (C) 2018 Francesco Azzola - Surviving with Android (https://www.survivingwithandroid.com)
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

public class APIServerService extends IntentService {

    private String LOG_NAME = getClass().getName();
    private Component restComponent;
    private static final int PORT = 8090;

    public static final String START_API_SERVER = "com.survivingwithandroid.api.start";
    public static final String STOP_API_SERVER = "com.survivingwithandroid.api.stop";

    public APIServerService() {
        super("APiServerService");

        // start the Rest server
        restComponent = new Component();
        restComponent.getServers().add(Protocol.HTTP, PORT); // listen on 8090
        // Router to dispatch Request
        Router router = new Router();
        router.attach("/temp", SensorTempResource.class);
        router.attach("/press", SensorPressResource.class);

        restComponent.getDefaultHost().attach("/sensor", router);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            try {
                if (action.equals(START_API_SERVER)) {
                    Log.d(LOG_NAME, "Starting API Server");
                    restComponent.start();
                }
                else if (action.equals(STOP_API_SERVER)) {
                    Log.d(LOG_NAME, "Stopping API Server");
                    restComponent.stop();
                }
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
