package com.survivingwithandroid.nfc.model;

/*
 * Copyright (C) 2014 Francesco Azzola
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
public class BaseRecord {


    public  int MB;
    public  int ME;
    public  int SR;
    public  int tnf;
    public  byte[] type;


    public String payload;

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("MB:" + MB);
        buffer.append(" ME:" + ME);
        buffer.append(" SR:" + SR);
        buffer.append(" TNF:" + tnf);

        return buffer.toString();
    }

    static int[] getHeader(byte[] payload) {
        byte header = payload[0];
        int[] result = new int[3];

        // Mask MB
        result[0] = (header & 0x80) >> 7;
        result[1] = (header & 0x40) >> 6;
        result[2] = (header & 0x10) >> 4;

        return result;
    }
}
