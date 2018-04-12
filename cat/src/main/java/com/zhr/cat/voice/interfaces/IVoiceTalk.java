package com.zhr.cat.voice.interfaces;

/**
 * 语音合成帮助类接口
 * Created by ZHR on 2017/05/25.
 */

public interface IVoiceTalk {
    /**
     * 说话
     *
     * @param text 要说的内容
     */
    void talk(String text);

    /**
     * 是否在说话
     *
     * @return 在说话返回true
     */
    boolean isTalking();
}
