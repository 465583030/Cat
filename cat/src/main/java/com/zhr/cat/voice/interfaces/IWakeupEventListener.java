package com.zhr.cat.voice.interfaces;

/**
 * 语音唤=唤醒接口
 */
public interface IWakeupEventListener {
    /**
     * 唤醒错误枚举
     */
    enum WakeupError {
        NET_TIMEOUT("1", "网络超时"),
        NET_CONNECT_ERROR("2", "网络连接失败"),
        AUDIO_ERROR("3", "音频错误"),
        PROTOCOL_ERROR("4", "协议错误"),
        CALL_ERROR("5", "客户端调用错误"),
        AUDIO_TOO_LONG("6", "语音过长"),
        NET_ERROR("7", "没有识别结果"),
        ENGINE_BUSY("8", "引擎忙"),
        NEED_PERMISSION("9", "缺少权限");

        private String code;
        private String message;

        WakeupError(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    /**
     * 开始唤醒
     */
    void onWakeupStart();

    /**
     * 唤醒成功
     *
     * @param word 唤醒词
     */
    void onWakeupSuccess(String word);

    /**
     * 唤醒结束
     */
    void onWakeupStop();

    /**
     * 唤醒出错
     *
     * @param error 唤醒错误
     */
    void onWakeupError(WakeupError error);

//    void onASrAudio(byte[] data, int offset, int length);
}