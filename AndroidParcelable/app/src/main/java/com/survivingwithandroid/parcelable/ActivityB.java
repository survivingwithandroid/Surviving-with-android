package com.survivingwithandroid.parcelable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.survivingwithandroid.parcelable.model.ContactInfo;

import java.util.List;
/*
* Copyright (C) 2014 Francesco Azzola - Surviving with Android (http://www.survivingwithandroid.com)
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

public class ActivityB extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_b);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Activity B");
        TextView tv = (TextView) findViewById(R.id.txtContent);

        Intent i  = getIntent();
        ContactInfo ci = i.getExtras().getParcelable("contact");
        tv.setText(ci.toString());

/*
        List<ContactInfo> ciArr = (List) i.getParcelableArrayListExtra("contact");
        StringBuffer buffer = new StringBuffer();
        for (int x=0; x < ciArr.size(); x++) {
            buffer.append(ciArr.get(x).toString() + "\r\n");
        }

        tv.setText(buffer.toString());
*/

    }


}
