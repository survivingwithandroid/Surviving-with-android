package survivingwithandroid.com.parsepushtutorial;
/*
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
 * 31/08/15
 */

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParsePush;

public class ParseTutorialApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Application");
        Parse.initialize(this, "sfFzOxotpsdOHWJE3nMmD0erLgFoecttKvC9CzIc", "nwbMEy7l4STpyNrABdrQxpEjdKIynSbuec56QbEz");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        // Remove it if you don't want to use channels
        ParsePush.subscribeInBackground("temboo");
    }
}
