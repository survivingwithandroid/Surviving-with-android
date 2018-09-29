package com.survivingwithandroid.androidthings;

/*
 * Copyright (C) 2018 Francesco Azzola
 *  Surviving with Android (https://www.survivingwithandroid.com)
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

import java.io.IOException;

import rocks.androidthings.driver.max72xx.MAX72XX;


public class Max7219Display {

    private MAX72XX display;

    private static Max7219Display me;

    private byte ROW = (byte) 0b11111111;

    private byte[] table = {(byte) 0b10101010,
                            (byte) 0b01010101,
                            (byte) 0b10101010,
                            (byte) 0b01010101,
                            (byte) 0b10101010,
                            (byte) 0b01010101,
                            (byte) 0b10101010,
                            (byte) 0b01010101};

    private Max7219Display() {
        init();
    }

    public static Max7219Display getInstance() {
        if (me == null)
            me = new Max7219Display();

        return me;
    }


    private void init() {
        try {
            display = new MAX72XX("SPI0.0", 1);
           //
            display.setIntensity(0, 13);
            display.shutdown(0, false);
            display.clearDisplay(0);
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }

    }

    public void drawBorder() {
        try {
           display.setRow(0, 0, ROW);
           display.setRow(0, 7, ROW);
           display.setColumn(0, 0, ROW);
           display.setColumn(0, 7, ROW);
           display.close();
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void drawTable() {
        try {
           for (int i=0 ; i < table.length; i++)
               display.setRow(0, i, table[i]);

           display.close();
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
