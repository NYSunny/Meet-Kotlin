//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.calllib;

import android.content.Context;
import android.view.SurfaceView;

public interface IRongCallEngine {
    void create(Context var1, String var2, IRongCallEngineListener var3);

    void destroy();

    SurfaceView createRendererView(Context var1);

    void setupLocalVideo(SurfaceView var1);

    void setupRemoteVideo(SurfaceView var1, String var2);

    int enableVideo();

    int disableVideo();

    int startPreview();

    int stopPreview();

    int joinChannel(String var1, String var2, String var3, String var4);

    int leaveChannel();

    int setChannelProfile(int var1);

    int startEchoTest();

    int stopEchoTest();

    int muteLocalAudioStream(boolean var1);

    int muteAllRemoteAudioStreams(boolean var1);

    int muteRemoteAudioStream(String var1, boolean var2);

    int setEnableSpeakerphone(boolean var1);

    int startAudioRecording(String var1);

    int stopAudioRecording();

    String getCallId();

    int rate(String var1, int var2, String var3);

    int complain(String var1, String var2);

    void monitorHeadsetEvent(boolean var1);

    void monitorBluetoothHeadsetEvent(boolean var1);

    void monitorConnectionEvent(boolean var1);

    boolean isSpeakerphoneEnabled();

    int setSpeakerphoneVolume(int var1);

    int enableAudioVolumeIndication(int var1, int var2);

    int setVideoProfile(int var1);

    int setLocalRenderMode(int var1);

    int setRemoteRenderMode(String var1, int var2);

    void switchView(String var1, String var2);

    int switchCamera();

    int requestNormalUser();

    int requestWhiteBoard();

    int muteLocalVideoStream(boolean var1);

    int muteAllRemoteVideoStreams(boolean var1);

    int muteRemoteVideoStream(String var1, boolean var2);

    int setLogFile(String var1);

    int setLogFilter(int var1);

    int startServerRecording(String var1);

    int stopServerRecording(String var1);

    int getServerRecordingStatus();

    void setUserType(int var1);

    void answerDegradeNormalUserToObserver(String var1);

    int answerUpgradeObserverToNormalUser(String var1, boolean var2);

    int answerHostControlUserDevice(String var1, int var2, boolean var3, boolean var4);
}
