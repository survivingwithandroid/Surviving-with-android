package com.survivingwithandroid.cloudrail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.cloudrail.si.interfaces.CloudStorage;
import com.cloudrail.si.services.Box;
import com.cloudrail.si.services.Dropbox;
import com.cloudrail.si.services.GoogleDrive;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

/*
 * Copyright (C) 2016 Francesco Azzola
 *  Surviving with Android (http://www.survivingwithandroid.com)
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

public class MainActivity extends AppCompatActivity {

    static int REQUEST_FILE = 10;
    private CloudStorage cs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FilePickerActivity.class);
                intent.putExtra(FilePickerActivity.ARG_FILE_FILTER,  Pattern.compile(".*\\.jpg$"));
                intent.putExtra(FilePickerActivity.ARG_DIRECTORIES_FILTER, false);
                intent.putExtra(FilePickerActivity.ARG_SHOW_HIDDEN, true);
                startActivityForResult(intent, 1);
            }
        });

        // Let's init dropBox
        // cs = getDropBoxStorage();


        // Let's init Box
        //cs = getBoxStorage();

        // Let's init Google drive
        cs = getGoogleDriveStorage();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            Toast.makeText(this, "File path ["+filePath+"]", Toast.LENGTH_LONG).show();
            uploadFile(filePath);
        }
    }

    private CloudStorage getDropBoxStorage() {
        CloudStorage cs = new Dropbox(this,
                                getResources().getString(R.string.dropBoxClient),
                                getResources().getString(R.string.dropBoxSecretKey));

        return cs;
    }

    private CloudStorage getBoxStorage() {
        CloudStorage cs = new Box(this, getResources().getString(R.string.boxClientId),
                                         getResources().getString(R.string.boxSecretKey));

        return cs;
    }

    private CloudStorage getGoogleDriveStorage() {
        CloudStorage cs = new GoogleDrive(this, getResources().getString(R.string.googleClientId),
                getResources().getString(R.string.googleSecretKey));

        return cs;
    }

    private void uploadFile(final String path) {


        new Thread() {
            @Override
            public void run() {
                // Get asset

                InputStream is;

                try {
                    File f = new File(path);
                    String name = f.getName();
                    is = new FileInputStream(f);

                    long size = f.length();



                    System.out.println("IS ["+is+"] - Size ["+size+"]");
                    cs.upload("/" + name, is, size, true);

                }
                catch(IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }.start();
    }
}
