package com.survivingwithandroid.ml;


/*
 * Copyright (C) 2019 Surviving with Android (https://www.survivingwithandroid.com)
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

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import ai.fritz.core.Fritz;
import ai.fritz.core.FritzManagedModel;
import ai.fritz.vision.FritzVision;
import ai.fritz.vision.FritzVisionImage;
import ai.fritz.vision.FritzVisionLabel;
import ai.fritz.vision.PredictorStatusListener;
import ai.fritz.vision.base.FritzVisionPredictor;
import ai.fritz.vision.imagelabeling.FritzVisionLabelPredictor;
import ai.fritz.vision.imagelabeling.FritzVisionLabelResult;
import ai.fritz.vision.imagelabeling.ImageLabelManagedModel;

public class MainActivity extends AppCompatActivity {

    private FritzVisionLabelPredictor predictor;
    private TextureView preview;
    private Button labelBtn;

    private String cameraId;
    private Size imageDimension;
    private CameraDevice cameraDevice;
    private CaptureRequest.Builder previewRequestBuilder;
    private CameraCaptureSession cameraSession;

    private static final int PERMISION_REQUEST = 1410;

    private Handler backgroundHandler;
    private HandlerThread handlerThread;

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private static final String TAG = MainActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preview = (TextureView) findViewById(R.id.preview);
        labelBtn = (Button) findViewById(R.id.btnLabel);

        labelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        preview.setSurfaceTextureListener(surfaceListener);
        // Check Permission
        if (!checkPermission())
            requestPermission();

        Fritz.configure(this, "7a6bfc58497e4436860176a4f6f720bc");
        downloadModel();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public synchronized void onResume() {
        super.onResume();

        handlerThread = new HandlerThread("machinelearning");
        handlerThread.start();
        backgroundHandler = new Handler(handlerThread.getLooper());
    }

    private void downloadModel() {
        FritzManagedModel model = new ImageLabelManagedModel();
        Log.d(TAG, "Download model...");
        FritzVision.ImageLabeling.loadPredictor(model, new PredictorStatusListener<FritzVisionLabelPredictor>() {
            @Override
            public void onPredictorReady(FritzVisionLabelPredictor predictor) {
                Log.d(TAG, "Model downloaded");
                MainActivity.this.predictor = predictor;
            }
        });
    }


    TextureView.SurfaceTextureListener surfaceListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.d(TAG, "Surface available");
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    // Handling the camera
    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        // We get the first available camera
        try {
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap streamConfMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            imageDimension = streamConfMap.getOutputSizes(SurfaceTexture.class)[0];

            // We can open the camera now
            manager.openCamera(cameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    Log.i(TAG, "Camera opened");
                    cameraDevice = camera;
                    createPreview();
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {

                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {
                    Log.e(TAG, "Error opening the camera");
                }
            }, null);
        }
        catch(CameraAccessException cae) {
            // Let us handle the error
        }
        catch(SecurityException se) {

        }
    }

    private void createPreview() {
        SurfaceTexture surfaceTexture = preview.getSurfaceTexture();
        surfaceTexture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
        Surface surface = new Surface(surfaceTexture);


        try {
            previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            previewRequestBuilder.addTarget(surface);

            cameraDevice.createCaptureSession(Arrays.asList(surface),
                new CameraCaptureSession.StateCallback() {
                   public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                       cameraSession = cameraCaptureSession;
                       previewRequestBuilder.set(
                              CaptureRequest.CONTROL_AF_MODE,
                               CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                       // Flash is automatically enabled when necessary.
                       previewRequestBuilder.set(
                               CaptureRequest.CONTROL_AE_MODE,
                               CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);

                       // Finally, we start displaying the camera preview.
                       CaptureRequest previewRequest = previewRequestBuilder.build();
                       try {
                           cameraSession.setRepeatingRequest(
                                   previewRequest, new CameraCaptureSession.CaptureCallback() {
                                       @Override
                                       public void onCaptureProgressed(
                                               final CameraCaptureSession session,
                                               final CaptureRequest request,
                                               final CaptureResult partialResult) {
                                       }

                                       @Override
                                       public void onCaptureCompleted(
                                               final CameraCaptureSession session,
                                               final CaptureRequest request,
                                               final TotalCaptureResult result) {
                                       }
                                   }, backgroundHandler);
                       }
                       catch(CameraAccessException cae) {}
                  }

                 @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Log.e(TAG, "Configuration failed");
                   }
               }, null);
        }
        catch (CameraAccessException cae) {}
    }

    private void updatePreview() {

    }

    // Handle permission
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        else
            return true;
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // let's request permissions
            requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISION_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                finish();
        }
    }


    private void labelIt() {

    }

    private void takePicture() {
        Log.d(TAG, "Taking picture...");
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        ImageReader imgReder = ImageReader.newInstance(640, 480, ImageFormat.JPEG, 1);

        try {
            final CaptureRequest.Builder reqBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            reqBuilder.addTarget(imgReder.getSurface());
            reqBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            reqBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {

                @Override
                public void onImageAvailable(ImageReader reader) {
                    Log.d(TAG, "Image ready");
                    Image image = reader.acquireLatestImage();
                    ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                    byte[] bytes = new byte[buffer.capacity()];
                    buffer.get(bytes);
                    Bitmap bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);

                    FritzVisionImage fvi = FritzVisionImage.fromBitmap(bitmapImage);

                    FritzVisionLabelResult labels = predictor.predict(fvi);

                   List<FritzVisionLabel> labelList = labels.getVisionLabels();

                   if (labelList != null && labelList.size() > 0) {
                       FritzVisionLabel label = labels.getVisionLabels().get(0);

                       System.out.println("Label [" + label.getText() + "]");
                       Toast.makeText(MainActivity.this, "Label:" + label.getText(), Toast.LENGTH_LONG).show();

                   }
                }
            };

            imgReder.setOnImageAvailableListener(readerListener, backgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    createPreview();
                }
            };

            cameraDevice.createCaptureSession(Arrays.asList(imgReder.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        session.capture(reqBuilder.build(), captureListener, backgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, backgroundHandler);
        }
        catch (CameraAccessException cae) {}
    }
}
