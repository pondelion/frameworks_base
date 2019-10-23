LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_SRC_FILES += \
        ../../../native/cmds/dumpstate/binder/android/os/IDumpstate.aidl \
        ../../../native/cmds/dumpstate/binder/android/os/IDumpstateListener.aidl \
        ../../../native/cmds/dumpstate/binder/android/os/IDumpstateToken.aidl

LOCAL_AIDL_INCLUDES = frameworks/native/cmds/dumpstate/binder

LOCAL_STATIC_JAVA_LIBRARIES := android-support-v4

LOCAL_STATIC_JAVA_LIBRARIES += aws-android-sdk-core
LOCAL_STATIC_JAVA_LIBRARIES += aws-android-sdk-s3

LOCAL_PACKAGE_NAME := Shell
LOCAL_CERTIFICATE := platform
LOCAL_PRIVILEGED_MODULE := true

LOCAL_JACK_COVERAGE_INCLUDE_FILTER := com.android.shell.*

include $(BUILD_PACKAGE)

include $(CLEAR_VARS)

LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := \
    aws-android-sdk-core:../../../../prebuilts/misc/common/aws/aws-android-sdk-core.jar \
    aws-android-sdk-s3:../../../../prebuilts/misc/common/aws/aws-android-sdk-s3.jar \

include $(BUILD_MULTI_PREBUILT)

include $(LOCAL_PATH)/tests/Android.mk
