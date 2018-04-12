package com.zhr.cat.voice.interfaces;

/**
 * 语音识别帮助类
 * Created by ZL on 2018/4/11.
 */

public interface IVoiceRecognizeHelper {
    /**
     * 开始识别
     */
    void startRecognize();

    /**
     * 停止识别
     */
    void stopRecognize();

    /**
     * 取消识别
     */
    void cancelRecognize();

    /**
     * 释放资源
     */
    void releaseRecognize();
}
