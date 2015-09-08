package survivingwithandroid.com.androiddatabinding.weather;/*
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
 * 28/07/15
 */

import android.content.Context;

import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.WeatherConfig;
import com.survivingwithandroid.weather.lib.client.okhttp.WeatherDefaultClient;
import com.survivingwithandroid.weather.lib.provider.openweathermap.OpenweathermapProviderType;

public class WClient {

    private static WClient me;
    private WeatherClient client;

    private WClient()  {}

    public static WClient getInstance(Context ctx) {
        if (me == null) {
            me = new WClient();
            try {
                me.client = (new com.survivingwithandroid.weather.lib.WeatherClient.ClientBuilder())
                        .attach(ctx)
                        .httpClient(WeatherDefaultClient.class)
                        .provider(new OpenweathermapProviderType())
                        .config(new WeatherConfig())
                        .build();
            }
            catch(Throwable t) {}

        }

        return me;
    }

    public WeatherClient getClient() {
        return this.client;
    }
}
