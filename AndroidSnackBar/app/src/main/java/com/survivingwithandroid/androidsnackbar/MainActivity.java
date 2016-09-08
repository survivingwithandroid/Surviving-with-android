package com.survivingwithandroid.androidsnackbar;

/*
 * Copyright (C) 2016, Francesco Azzola
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
 *
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View v = (View) findViewById(R.id.main);

        final TextView tv = (TextView) findViewById(R.id.data);

        Button b = (Button) findViewById(R.id.btn);

        //Snackbar.make(v, "Welcome to SwA", Snackbar.).show();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Activate snack bar


                Snackbar bar = Snackbar.make(v, "Weclome to SwA", Snackbar.LENGTH_LONG)
                         .setAction("Dismiss", new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                tv.setText("You pressed Dismiss!!");
                             }
                         });

                bar.setActionTextColor(Color.RED);

                TextView tv = (TextView) bar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.CYAN);
                bar.show();
            }
        });
    }
}
