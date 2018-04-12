package com.zhr.cat.voice.interfaces;

/**
 * Created by ZHR on 2017/05/25.
 * 语音合成事件监听器
 */

public interface IVoiceTalkEventListener {
    /**
     * 播放完成
     */
    void onCompleted();

    /**
     * 开始播放
     */
    void onSpeakBegin();


    /**
     * 暂停播放
     */
    void onSpeakPaused();
}
