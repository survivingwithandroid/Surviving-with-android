package com.survivingwithandroid.androidthings.androidthings_facedetector;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.FirebaseApp;

/*
 * Copyright (C) 2019 Francesco Azzola - Surviving with Android (https://www.survivingwithandroid.com)
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

public class MainActivity extends Activity {

    private static final String TAG = FaceDetector.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView img = (ImageView) findViewById(R.id.img);

        final DisplayManager display = new DisplayManager();
        display.init();
        display.setImage(getResources(), R.drawable.neutral_face);

        FirebaseApp.initializeApp(this);
        FaceDetector fc = new FaceDetector(this, img, getMainLooper());
        fc.setListener(new FaceDetector.CameraListener() {
            @Override
            public void onError() {
                // Handle error
            }

            @Override
            public void onSuccess(FaceDetector.FACE_STATUS status) {
                Log.d(TAG, "Face ["+status+"]");

                switch (status) {
                    case SMILING:
                       display.setImage(getResources(), R.drawable.smiling_face);
                        break;
                    case LEFT_EYE_OPEN_RIGHT_CLOSE:
                        display.setImage(getResources(), R.drawable.right_eyes_closed);
                        break;
                    case RIGHT_EYE_OPEN_LEFT_CLOSE:
                        display.setImage(getResources(), R.drawable.left_eyes_closed);
                        break;
                     default:
                        display.setImage(getResources(), R.drawable.neutral_face);
                }

            }
        });

        fc.capture();
    }
}
