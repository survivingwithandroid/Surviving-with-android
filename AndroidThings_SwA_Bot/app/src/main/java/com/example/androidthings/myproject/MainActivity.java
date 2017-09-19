/*
 * Copyright 2016, The Android Open Source Project
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

package com.example.androidthings.myproject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioDriver;
import com.google.android.things.pio.PeripheralManagerService;
import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetUpdatesResponse;

import java.io.IOException;
import java.util.List;

/**
 * Skeleton of the main Android Things activity. Implement your device's logic
 * in this class.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 *
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 */
public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String BOT_TOKEN = "385044937:AAH61a_8AofgYGAXOku_05tpiANtkZehDWk";
    TelegramBot bot;

    // Android Things GPIO
    private Gpio bluePin;
    private Gpio yellowPin;
    private PeripheralManagerService pmService = new PeripheralManagerService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        bot = TelegramBotAdapter.build(BOT_TOKEN);

        // start listening for incoming messages
        GetUpdates getUpdates = new GetUpdates().limit(100).offset(0).timeout(10);
        // async
        /*
        bot.execute(getUpdates, new Callback<GetUpdates, GetUpdatesResponse>() {
            @Override
            public void onResponse(GetUpdates request, GetUpdatesResponse response) {
                Log.d(TAG, "Resposnse...");
                List<Update> updates = response.updates();
                // List for messages
                for (Update update : updates) {
                    Message msg = update.message();
                    Log.d(TAG, "Message ["+msg+"]");
                }
            }

            @Override
            public void onFailure(GetUpdates request, IOException e) {
                Log.e(TAG, "ERROR", e);
            }
        });
        */

        // Get pin reference

        initPins();

        bot.setUpdatesListener(new UpdatesListener() {
            @Override
            public int process(List<Update> updates) {
                for (Update update : updates) {
                    Message msg = update.message();

                    if (msg != null) {
                        String txt = msg.text();
                        if (txt.trim().startsWith("LED")) {
                            Log.d(TAG, "LED COMMAND");
                            String[] data = txt.split(" ");
                            if (data.length < 3) {
                                Log.d(TAG, "Command format error");
                                return UpdatesListener.CONFIRMED_UPDATES_ALL;
                            }

                            String value = data[2];
                            if (data[1].equalsIgnoreCase("b")) {
                                Log.d(TAG, "Blue pin ["+value+"]");
                               setPin(bluePin, value);
                            }
                            else if (data[1].equalsIgnoreCase("y")) {
                                setPin(yellowPin, value);
                            }
                        }
                    }
                }
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    private void setPin(Gpio pin, String state) {
        try {
            pin.setValue(Boolean.parseBoolean(state));
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void initPins() {

        try {
            bluePin = pmService.openGpio("BCM22");
            yellowPin = pmService.openGpio("BCM27");

            bluePin.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            yellowPin.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
        }
        catch(IOException ioe) {
                ioe.printStackTrace();
        }

    }
}
