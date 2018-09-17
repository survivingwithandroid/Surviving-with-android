package com.survivingwithandroid.androidthings.api;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

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

public class SensorPressResource extends ServerResource {

    @Get("json")
    public Representation getPressure() {
        JSONObject result = new JSONObject();
        float press = DeviceManager.getInstance().readPress();
        try {
            result.put("press", press);
        }
        catch(JSONException jsoe) {

        }

        return new StringRepresentation(result.toString(), MediaType.APPLICATION_ALL_JSON);
    }
}
