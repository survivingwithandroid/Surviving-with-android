package com.survivingwithandroid.androidthings.motors;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

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

public class RobotHttpServer extends NanoHTTPD {

    private String TAG = getClass().getName();
    private Context context;
    private CommandListener listener;

    public RobotHttpServer(int port, Context context, CommandListener listener) {
        super(port);
        this.context = context;
        this.listener = listener;
        Log.d(TAG, "Starting Server");
        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Response serve(IHTTPSession session) {
        Map<String, String> params = session.getParms();

        String control = params.get("control");
        String action = params.get("btn");

        Log.d(TAG, "Serve - Control ["+control+"] - Action ["+action+"]");


        if (action != null && !"".equals(action))
          listener.onCommand(action);

        return newFixedLengthResponse(readHTMLFile().toString());
    }



    private StringBuffer readHTMLFile() {
        BufferedReader reader = null;
        StringBuffer buffer = new StringBuffer();
        try {
            reader = new BufferedReader(
                    new InputStreamReader
                            (context.getAssets().open("controller.html"), "UTF-8"));


            String mLine;
            while ((mLine = reader.readLine()) != null) {
                buffer.append(mLine);
            }
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
        finally {
            if (reader != null)
                try {
                    reader.close();
                }
                catch (IOException ioe) {}
        }

        return buffer;
    }


    public static interface CommandListener {
        public void onCommand(String command);
    }

}
