package com.survivingwithandroid.androidthings.androidthings_facedetector;

/*
 * Copyright 2017 The Android Open Source Project.
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


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.Image;
import android.os.Environment;
import android.util.Log;

import junit.framework.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

/**
 * Utility class for manipulating images.
 * This class is derived from the class:
 * https://github.com/androidthings/photobooth/blob/master/app/src/main/java/com/example/androidthings/photobooth/ImageUtils.java
 **/
public class ImageUtils {

    static final String TAG = "ImageUtils";

    // This value is 2 ^ 18 - 1, and is used to clamp the RGB values before their ranges
    // are normalized to eight bits.
    static final int kMaxChannelValue = 262143;



    public static void convertImageToBitmap(Image image, int width, int height, int[] output,
                                            byte[][] cachedYuvBytes) {

        if (image == null)
            return ;

        if (cachedYuvBytes == null || cachedYuvBytes.length != 3) {
            cachedYuvBytes = new byte[3][];
        }

        Image.Plane[] planes = image.getPlanes();
        fillBytes(planes, cachedYuvBytes);
        final int yRowStride = planes[0].getRowStride();
        final int uvRowStride = planes[1].getRowStride();
        final int uvPixelStride = planes[1].getPixelStride();
        convertYUV420ToARGB8888(cachedYuvBytes[0], cachedYuvBytes[1], cachedYuvBytes[2], width,
                height, yRowStride, uvRowStride, uvPixelStride, output);
    }

    private static void convertYUV420ToARGB8888(byte[] yData, byte[] uData, byte[] vData, int width,
                                                int height, int yRowStride, int uvRowStride, int uvPixelStride, int[] out) {
        int i = 0;
        for (int y = 0; y < height; y++) {
            int pY = yRowStride * y;
            int uv_row_start = uvRowStride * (y >> 1);
            int pU = uv_row_start;
            int pV = uv_row_start;
            for (int x = 0; x < width; x++) {
                int uv_offset = (x >> 1) * uvPixelStride;
                out[i++] = YUV2RGB(
                        convertByteToInt(yData, pY + x),
                        convertByteToInt(uData, pU + uv_offset),
                        convertByteToInt(vData, pV + uv_offset));
            }
        }
    }

    private static int convertByteToInt(byte[] arr, int pos) {
        return arr[pos] & 0xFF;
    }

    private static int YUV2RGB(int nY, int nU, int nV) {
        nY -= 16;
        nU -= 128;
        nV -= 128;
        if (nY < 0) nY = 0;
        // This is the floating point equivalent. We do the conversion in integer
        // because some Android devices do not have floating point in hardware.
        // nR = (int)(1.164 * nY + 2.018 * nU);
        // nG = (int)(1.164 * nY - 0.813 * nV - 0.391 * nU);
        // nB = (int)(1.164 * nY + 1.596 * nV);
        int nR = (int) (1192 * nY + 1634 * nV);
        int nG = (int) (1192 * nY - 833 * nV - 400 * nU);
        int nB = (int) (1192 * nY + 2066 * nU);
        nR = Math.min(kMaxChannelValue, Math.max(0, nR));
        nG = Math.min(kMaxChannelValue, Math.max(0, nG));
        nB = Math.min(kMaxChannelValue, Math.max(0, nB));
        nR = (nR >> 10) & 0xff;
        nG = (nG >> 10) & 0xff;
        nB = (nB >> 10) & 0xff;
        return 0xff000000 | (nR << 16) | (nG << 8) | nB;
    }

    private static void fillBytes(final Image.Plane[] planes, final byte[][] yuvBytes) {
        // Because of the variable row stride it's not possible to know in
        // advance the actual necessary dimensions of the yuv planes.
        for (int i = 0; i < planes.length; ++i) {
            final ByteBuffer buffer = planes[i].getBuffer();
            if (yuvBytes[i] == null || yuvBytes[i].length != buffer.capacity()) {
                yuvBytes[i] = new byte[buffer.capacity()];
            }
            buffer.get(yuvBytes[i]);
        }
    }



}