package com.android.server.remotecontrol;

import com.amazonaws.regions.Regions;


public class AWSSettings {
    public static final String KEY_STORE_NAME = "android_test1.jks";
    public static final String KEY_STORE_PASSWORD = "android_test_pass";
    public static final String CERT_ID = "android_test_certid";
    public static final String AWS_IOT_THING_NAME = "android-test1";
    public static final String AWS_IOT_ENDPOINT = "a1j46spshhpsvk-ats.iot.ap-northeast-1.amazonaws.com";
    public static final String AWS_IOT_CERT_FILEPATH = "5a0d4948c4-certificate.pem.crt";
    public static final String AWS_IOT_PRIV_KEY_FILEPATH = "5a0d4948c4-private.pem.key";
    public static final String AWS_IOT_ROOT_CA_FILE = "AmazonRootCA1.pem";
    public static final Regions REGION = Regions.AP_NORTHEAST_1;
}
