package com.zhr.cat.voice.interfaces;

/**
 * 语音识别接口
 * Created by ZL on 2018/4/11.
 */

public interface IVoiceRecognizeHelper {
    void startRecognize();
    void stopRecognize();
    void cancelRecognize();
    void releaseRecognize();
}
