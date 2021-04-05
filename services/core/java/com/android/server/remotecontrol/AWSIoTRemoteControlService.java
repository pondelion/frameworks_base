package com.android.server.remotecontrol;

import android.content.Context;
import android.util.Log;

import com.android.server.SystemService;
import com.android.server.remotecontrol.command.RemoteCommand;

import com.amazonaws.mobileconnectors.iot.AWSIotKeystoreHelper;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttNewMessageCallback;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;

public final class AWSIoTRemoteControlService extends SystemService
        implements IRemoteControlService {

    public static final String TAG = "AWSIoTRemoteControlService";
    private AWSIotMqttManager mMQTTManager;
    private final String KEY_STORE_PATH = "***";
    private final String TOPIC_NAME = "***";
    private JsonCommandParser mJCP = new JsonCommandParser();

    public AWSIoTRemoteControlService(Context context) {
        super(context);
        this.mMQTTManager = AWSIotMqttManager(AWSSettings.AWS_IOT_THING_NAME, AWSSettings.AWS_IOT_ENDPOINT);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
    }

    @Override
    public void startRemoteControlService() {
        boolean isPresent = AWSIotKeystoreHelper.isKeystorePresent(KEY_STORE_PATH, AWSSettings.AWS_IOT_KEY_STORE_NAME)
        if (!isPresent) {
            try {
                saveCertificateAndPrivateKey(KEY_STORE_PATH);
            } catch (Exception e) {

            }
        }
        KeyStore keyStore = AWSIotKeystoreHelper.getIotKeystore(
                AWSSettings.AWS_IOT_CERT_ID,
                KEY_STORE_PATH,
                AWSSettings.AWS_IOT_KEY_STORE_NAME,
                AWSSettings.AWS_IOT_KEY_STORE_PASSWORD
        );
        this.mMQTTManager.connect(keyStore, new AWSIotMqttClientStatusCallback() {
            @Override
            public void onStatusChanged(AWSIotMqttClientStatus status, Throwable throwable) {
                Log.d(TAG, "AWSIotMqttClientStatusCallback#onStatusChanged : " + status.toString());
                if (status == AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Connected) {
                    subscribe(TOPIC_NAME);
                }
            }
        });
    }

    @Override
    public void stopRemoteControlService() {
        if (this.mMQTTManager != null) {
            this.mMQTTManager.disconnect();
        }
    }

    private void saveCertificateAndPrivateKey(String keyStorePath) throws Exception {
        final String certFile = AWSSettings.AWS_IOT_CERT_FILEPATH;
        final String certStr = readFile(certFile);
        final String privKeyFile = AWSSettings.AWS_IOT_PRIV_KEY_FILEPATH;
        final String privKeyStr = readFile(privKeyFile);
        AWSIotKeystoreHelper.saveCertificateAndPrivateKey(
            AWSSettings.AWS_IOT_CERT_ID,
            certStr,
            privKeyStr,
            keyStorePath,
            AWSSettings.KEY_STORE_NAME,
            AWSSettings.KEY_STORE_PASSWORD
        );
    }

    private void subscribe(String topic) {
        if (this.mMQTTManager == null) {
            return;
        }
        this.mMQTTManager.subscribeToTopic(topic, AWSIotMqttQos.QOS0, new AWSIotMqttNewMessageCallback() {
            @Override
            public void onMessageArrived(String topic, byte[] data) {
                final String msgStr = new String(data, Charset.forName("UTF-8"));
                RemoteCommand remoteCommand = mJCP.parseCommand(msgStr);
                runCommand(remoteCommand);
            }
        });
    }

    private void publish(String msg, String topic) {
        if (this.mMQTTManager == null) {
            return;
        }
        this.mMQTTManager.publishString(msg, topic, AWSIotMqttQos.QOS0)
    }
    
    private void runCommand(RemoteCommand rc) {
        
    }

    private String readFile(String filepath) throws IOException {
        return Files.lines(Paths.get(filepath))
                .reduce("", (prev, line) ->
                        prev + line + System.getProperty("line.separator"));
    }
}
