package com.survivingwithandroid.socialp;

/*
 * Copyright (C) 2016 Surviving with Android (http://www.survivingwithandroid.com)
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

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.cloudrail.si.interfaces.Profile;
import com.cloudrail.si.services.Facebook;
import com.cloudrail.si.services.GitHub;
import com.cloudrail.si.services.GooglePlus;
import com.cloudrail.si.services.LinkedIn;
import com.cloudrail.si.services.Twitter;

/**
 * Created by francesco on 13/10/16.
 */

public class LoginService extends IntentService {

    public static String PROFILE_INFO = "profile_info";

    public LoginService() {
        super("LoginService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("Prf", "Login service");
        SocialProfile socialProfile = null;

        Profile profile = null;

        if (intent != null)
            socialProfile = (SocialProfile) intent.getSerializableExtra(LoginService.PROFILE_INFO);

        switch(socialProfile) {
            case FACEBOOK:
               profile = getFacebookProfile();
                break;
            case GOOGLE_PLUS:
                profile = getGoogleProfile();
                break;
            case TWITTER:
                profile = getTwitterProfile();
                break;
            case LINKEDIN:
                profile = getLinkedinProfile();
                break;
            case GITHUB:
                profile = getGithubProfile();
                break;
        }


        String fullName = profile.getFullName();
        String email = profile.getEmail();
        String url = profile.getPictureURL();

        sendBroatcast(fullName, email, url);
    }

    private Profile getFacebookProfile() {
        Log.d("Prf", "Facebook Profile");
        Profile profile = new Facebook(this, "xxxx", "yyyy");
        return profile;
    }

    private Profile getGoogleProfile() {
        Log.d("Prf", "Google Profile");
        Profile profile = new GooglePlus(this, "xxxx","yyy");
        return profile;
    }

    private Profile getTwitterProfile() {
        Log.d("Prf", "Twitter Profile");
        Profile profile = new Twitter(this, "xxxx", "yyyyy");
        return profile;
    }

    private Profile getLinkedinProfile() {
        Log.d("Prf", "Linkedin Profile");
        Profile profile = new LinkedIn(this, "xxxx", "yyyy");
        return profile;
    }

    private Profile getGithubProfile() {
        Log.d("Prf", "Github Profile");
        Profile profile = new GitHub(this, "xxxx", "yyyy");
        return profile;
    }


    private void sendBroatcast(String fullName, String email, String url) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MainActivity.ResponseReceiver.ACTION);
        broadcastIntent.putExtra("fullName", fullName);
        broadcastIntent.putExtra("email", email);
        broadcastIntent.putExtra("url", url);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.sendBroadcast(broadcastIntent);
    }
}
