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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudrail.si.CloudRail;
import com.cloudrail.si.interfaces.Profile;
import com.cloudrail.si.services.Facebook;
import com.squareup.picasso.Picasso;

import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends AppCompatActivity {

    private ResponseReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CloudRail.setAppKey("57f669f076b9451e63146006");
        Button btnFbk = (Button) findViewById(R.id.btnFbk);
        Button btnGp = (Button) findViewById(R.id.btnGoo);
        Button btnTwi = (Button) findViewById(R.id.btnTwitter);
        Button btnLnk = (Button) findViewById(R.id.btnLink);
        Button btnLGit = (Button) findViewById(R.id.btnGithub);

        registerReciever();

        btnFbk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoginService(SocialProfile.FACEBOOK);
            }
        });

        btnGp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoginService(SocialProfile.GOOGLE_PLUS);
            }
        });

        btnTwi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoginService(SocialProfile.TWITTER);
            }
        });

        btnLnk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoginService(SocialProfile.LINKEDIN);
            }
        });

        btnLGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoginService(SocialProfile.GITHUB);
            }
        });
    }

    private void startLoginService(SocialProfile profile) {
        Intent i = new Intent(this, LoginService.class);
        i.putExtra(LoginService.PROFILE_INFO, profile );
        startService(i);
    }

    private void createDialog(String fullName, String email, String url) {
        final MaterialDialog dialog = new MaterialDialog(this);
        View v = LayoutInflater.from(this).inflate(R.layout.profile_layout, null);

        ( (TextView) v.findViewById(R.id.usrName)).setText(fullName);
        ( (TextView) v.findViewById(R.id.usrEmail)).setText(email);

        Picasso.with(this).load(url).into( (ImageView) v.findViewById(R.id.prfImg));

        dialog.setPositiveButton("OK", new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialog.dismiss();

            }
        }).setContentView(v).show();
    }


    public class ResponseReceiver extends BroadcastReceiver {

        public static final String ACTION = "com.survivingwithandroidp.PROFILE";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Prf", "Receive info");

            String fullName = intent.getStringExtra("fullName");
            String email =  intent.getStringExtra("email");
            String url = intent.getStringExtra("url");
            createDialog(fullName, email, url);
        }
    }

    private void registerReciever() {
        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION);
        receiver = new ResponseReceiver();
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(receiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReciever();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.unregisterReceiver(receiver);
    }
}
