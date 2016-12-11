package com.survivingwithandroid.finger;

/*
 * Copyright (C) 2016 Surviving with Android (http://www.survivingwithandroid.com)
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

import android.app.KeyguardManager;
import android.hardware.fingerprint.FingerprintManager;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyGenParameterSpec.Builder;
import android.security.keystore.KeyProperties;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {

    private TextView message;
    private static final String KEY_NAME = "SwA";

    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private FingerprintManager.CryptoObject cryptoObject;

    private FingerprintManager fingerprintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        message = (TextView) findViewById(R.id.fingerStatus);
        Button btn = (Button) findViewById(R.id.authBtn);

        final FingerprintHandler fph = new FingerprintHandler(message);

        if (!checkFinger()) {
            btn.setEnabled(false);
        }
        else {
            // We are ready to set up the cipher and the key
            try {
                generateKey();
                Cipher cipher = generateCipher();
                cryptoObject =
                        new FingerprintManager.CryptoObject(cipher);

            }
            catch(FingerprintException fpe) {
                // Handle exception
                btn.setEnabled(false);
            }
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message.setText("Swipe your finger");
                fph.doAuth(fingerprintManager, cryptoObject);
            }
        });
    }


    private boolean checkFinger() {

        // Keyguard Manager
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        // Fingerprint Manager
        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        try {
            // Check if the fingerprint sensor is present
            if (!fingerprintManager.isHardwareDetected()) {
                message.setText("Fingerprint authentication not supported");
                return false;
            }

            if (!fingerprintManager.hasEnrolledFingerprints()) {
                message.setText("No fingerprint configured.");
                return false;
            }

            if (!keyguardManager.isKeyguardSecure()) {
                message.setText("Secure lock screen not enabled");
                return false;
            }

        }
        catch(SecurityException se) {
            se.printStackTrace();
        }


        return true;

    }

    private void generateKey() throws FingerprintException {
        try {
            // Get the reference to the key store
            keyStore = KeyStore.getInstance("AndroidKeyStore");

            // Key generator to generate the key
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            keyStore.load(null);
            keyGenerator.init( new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            keyGenerator.generateKey();

        }
        catch(KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            exc.printStackTrace();
            throw new FingerprintException(exc);
        }


    }

    private Cipher generateCipher() throws FingerprintException {
        try {
            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher;
        }
        catch (NoSuchAlgorithmException
                | NoSuchPaddingException
                | InvalidKeyException
                | UnrecoverableKeyException
                | KeyStoreException exc) {
            exc.printStackTrace();
            throw new FingerprintException(exc);
        }
    }

    private class FingerprintException extends Exception {

        public FingerprintException(Exception e) {
            super(e);
        }
    }
}
