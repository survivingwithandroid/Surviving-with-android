package com.survivingwithandroid.androidthings.androidthings_facedetector;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;


import java.util.Collections;
import java.util.List;

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

public class FaceDetector implements ImageReader.OnImageAvailableListener {

    private static final String TAG = FaceDetector.class.getName();

    private CameraListener listener;

    private ImageReader mImageReader;
    private Context ctx;

    private static int IMAGE_WIDTH = 320;
    private static int IMAGE_HEIGHT = 240;

    private HandlerThread backgroundThread;
    private Handler backgroundHandler;

    private int[] output;
    private byte[][] cachedYuvBytes;
    private Bitmap displayBitmap;

    private FirebaseVisionImage firebaseImage;
    private FirebaseVisionFaceDetector detector;
    private Task<List<FirebaseVisionFace>> result;

    private ImageView img;
    private Looper looper;

    private Handler uiHandler;

    // Face status
    enum FACE_STATUS {
        SMILING,
        LEFT_EYE_OPEN_RIGHT_CLOSE,
        RIGHT_EYE_OPEN_LEFT_CLOSE,
        LEFT_OPEN_RIGHT_OPEN
    }

    public FaceDetector(Context ctx, ImageView img, Looper looper) {
        this.ctx = ctx;
        this.img = img;
        this.looper = looper;
   }

    public void capture() {
        backgroundThread = new HandlerThread("capture");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());

        cachedYuvBytes = new byte[3][];
        output = new int[IMAGE_WIDTH * IMAGE_HEIGHT];

        displayBitmap = Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, Bitmap.Config.ARGB_8888);

        img.setLayerType(View.LAYER_TYPE_SOFTWARE, null);


        // Firebase init
        FirebaseVisionFaceDetectorOptions options =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                        .enableTracking()
                        .build();
        detector = FirebaseVision.getInstance().getVisionFaceDetector(options);


        uiHandler = new Handler(looper);




        // Get Camera manager
        CameraManager cameraManager = (CameraManager) ctx.getSystemService(Context.CAMERA_SERVICE);
        openCamera(cameraManager);

    }

    private void openCamera(CameraManager camManager) {
        try {
            String[] camIds = camManager.getCameraIdList();
            if (camIds.length < 1) {
                Log.e(TAG, "Camera not available");
                listener.onError();
                return;
            }

            //mImageReader = ImageReader.newInstance(IMAGE_WIDTH, IMAGE_HEIGHT, ImageFormat.YUV_420_888, 10);
            //mImageReader.setOnImageAvailableListener(this, backgroundHandler);

            camManager.openCamera(camIds[0],
                    new CameraDevice.StateCallback() {
                        @Override
                        public void onOpened(@NonNull CameraDevice camera) {
                            Log.i(TAG, "Camera opened");
                            startCamera(camera);
                        }

                        @Override
                        public void onDisconnected(@NonNull CameraDevice camera) {

                        }

                        @Override
                        public void onError(@NonNull CameraDevice camera, int error) {
                            Log.e(TAG, "Error ["+error+"]");
                            listener.onError();
                        }
                    },
            backgroundHandler);
        }
        catch(CameraAccessException cae) {
            cae.printStackTrace();
            listener.onError();
        }
    }

    private void startCamera(CameraDevice cameraDevice) {
        try {
            final CaptureRequest.Builder requestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            requestBuilder.addTarget(mImageReader.getSurface());
            cameraDevice.createCaptureSession(Collections.singletonList(mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            Log.i(TAG, "Camera configured..");
                            CaptureRequest request = requestBuilder.build();
                            try {
                                session.setRepeatingRequest(request, null, backgroundHandler);
                            }
                            catch (CameraAccessException cae) {
                                Log.e(TAG, "Camera session error");
                                cae.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                        }
                    },
            backgroundHandler);
        }
        catch (CameraAccessException cae) {
            Log.e(TAG, "Camera Access Error");
            cae.printStackTrace();
            listener.onError();
        }

    }


    public CameraListener getListener() {
        return listener;
    }

    public void setListener(CameraListener listener) {
        this.listener = listener;
    }

    public interface CameraListener {
        public void onError();

        public void onSuccess(FACE_STATUS status);
    }


    // Image Listener
    @Override
    public void onImageAvailable(ImageReader reader) {
        //Log.i(TAG, "Image Ready..");
        Image image = reader.acquireLatestImage();

       // Log.d(TAG, "Image height ["+image.getHeight()+"] - Width ["+image.getWidth()+"]");
        //imageToBitmap(image, displayBitmap);

        if (image == null)
            return ;

        ImageUtils.convertImageToBitmap(image, IMAGE_WIDTH, IMAGE_HEIGHT, output, cachedYuvBytes);
        displayBitmap.setPixels(output, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
        //Log.d(TAG, "Output size ["+output.length+"]");
        //Log.d(TAG, "Display ["+displayBitmap.getWidth()+"x" + displayBitmap.getHeight()+"]");
        image.close();


        firebaseImage = FirebaseVisionImage.fromBitmap(displayBitmap);

        //Log.d(TAG, "Firebase Image ["+firebaseImage+"]");
        result =
                detector
                        .detectInImage(firebaseImage)
                        .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> faces) {
               // Log.d(TAG, "Faces ["+faces.size()+"]");
                for (FirebaseVisionFace face : faces) {
                    Log.d(TAG, "****************************");
                    Log.d(TAG, "face ["+face+"]");
                    Log.d(TAG, "Smiling Prob ["+face.getSmilingProbability()+"]");
                    Log.d(TAG, "Left eye open ["+face.getLeftEyeOpenProbability()+"]");
                    Log.d(TAG, "Right eye open ["+face.getRightEyeOpenProbability()+"]");
                    checkFaceExpression(face);
                }

            }
        });

        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (displayBitmap) {
                    img.setImageBitmap(displayBitmap);
                }
            }
        });
    }

    private void checkFaceExpression(FirebaseVisionFace face) {
        if (face.getSmilingProbability() > 0.5) {
            Log.d(TAG, "**** Smiling ***");
            listener.onSuccess(FACE_STATUS.SMILING);
        }

        if (face.getLeftEyeOpenProbability() < 0.2 &&
                face.getLeftEyeOpenProbability() != -1 &&
                face.getRightEyeOpenProbability() > 0.5) {
            Log.d(TAG, "Right Open..");
            listener.onSuccess(FACE_STATUS.RIGHT_EYE_OPEN_LEFT_CLOSE);
        }

        if (face.getRightEyeOpenProbability() < 0.2 &&
                face.getRightEyeOpenProbability() != -1 &&
                face.getLeftEyeOpenProbability() > 0.5) {
            Log.d(TAG, "Left Open..");
            listener.onSuccess(FACE_STATUS.LEFT_EYE_OPEN_RIGHT_CLOSE);
        }


        listener.onSuccess(FACE_STATUS.LEFT_OPEN_RIGHT_OPEN);
    }

}