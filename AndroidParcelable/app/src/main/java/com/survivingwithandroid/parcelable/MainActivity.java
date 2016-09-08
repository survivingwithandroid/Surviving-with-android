package com.survivingwithandroid.parcelable;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.survivingwithandroid.parcelable.model.ContactInfo;

import java.util.ArrayList;
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

public class MainActivity extends ActionBarActivity {

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Activity A");
        btn = (Button) findViewById(R.id.btnSend);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ActivityB.class);

                // Contact Info
               ContactInfo ci = createContact("Francesco", "Surviving with android", 1);
               i.putExtra("contact", ci);
           //    List<ContactInfo> cList = createContactList();
           //   i.putParcelableArrayListExtra("contact", (ArrayList) cList);


                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ContactInfo createContact(String name, String surname, int idx) {
        ContactInfo ci = new ContactInfo();
        ci.setName("Francesco" + idx);
        ci.setSurname("Azzola" + idx);
        ci.setIdx(idx);

        ContactInfo.Address add = new ContactInfo.Address();
        add.setCity("London");
        add.setStreet("str Surviving with android");
        add.setNumber(1);

        ci.setAddress(add);

        return ci;
    }

    private List<ContactInfo> createContactList() {
        List<ContactInfo> cList = new ArrayList<ContactInfo>();
        for (int i=0; i < 10; i++)
            cList.add(createContact("Name " + i, "Surname " + i, i));

        return cList;
    }
}
