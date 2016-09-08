package com.survivingwithandroid.nfc.model;

import android.util.Log;

/*
 * Copyright (C) 2015 Francesco Azzola
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
public class RDTTextRecord extends BaseRecord {

    public String language;
    public String encoding;

    public RDTTextRecord() {
        this.tnf = 0x01;
    }

    public String toString() {
        Log.d("Nfc","Here to string");
        StringBuffer buffer = new StringBuffer(super.toString());
        buffer.append(" Encoding:" + encoding);
        buffer.append(" Content: " + payload);

        return buffer.toString();
    }

    public static RDTTextRecord createRecord(byte[] payload) {
        Log.d("Nfc", "Text record");
        String txtContent = "";
        // Handle text

        RDTTextRecord record = new RDTTextRecord();

        byte status = payload[0];
        int enc = status & 0x80; // Bit mask 7th bit 1
        String encString = null;
        if (enc == 0)
            encString = "UTF-8";
        else
            encString = "UTF-16";

        record.encoding = encString;
        int ianaLength = status & 0x3F; // Bit mask bit 5..0
        Log.d("Nfc", "IANA Len [" + ianaLength + "]");

        try {
            String content = new String(payload, ianaLength + 1, payload.length - 1 - ianaLength, encString);
            record.payload = content;
            // txtContent = "Enc:" + encString + " Content:" + content;

        }
        catch(Throwable t) {
            t.printStackTrace();
        }

        return record;
    }
}
