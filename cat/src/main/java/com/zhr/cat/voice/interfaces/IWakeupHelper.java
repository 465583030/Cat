package com.zhr.cat.voice.interfaces;

/**
 * 语音唤醒帮助类接口
 */
public interface IWakeupHelper {
    /**
     * 开始唤醒
     */
    void startWakeup();

    /**
     * 停止唤醒
     */
    void stopWakeup();

    /**
     * 释放资源
     */
    void releaseWakeup();
}