package com.zhr.cat.voice.interfaces;

/**
 * 语音识别接口
 * Created by ZL on 2018/4/11.
 */

public interface IVoiceRecognizeEventListener {
    /**
     * 语音识别错误枚举
     */
    enum VoiceRecognizeError {
        NET_TIMEOUT("1", "网络超时"),
        NET_CONNECT_ERROR("2", "网络连接失败"),
        AUDIO_ERROR("3", "音频错误"),
        PROTOCOL_ERROR("4", "协议错误"),
        CALL_ERROR("5", "客户端调用错误"),
        AUDIO_TOO_LONG("6", "语音过长"),
        NET_ERROR("7", "没有识别结果"),
        ENGINE_BUSY("8", "引擎忙"),
        NEED_PERMISSION("9", "缺少权限");
        /**
         * 错误码
         */
        private String code;
        /**
         * 错误信息
         */
        private String message;

        VoiceRecognizeError(String code, String message) {
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
     * 开始识别
     */
    void onStartRecognize();

    /**
     * 结束识别
     */
    void onStopRecognize();

    /**
     * 识别完成
     *
     * @param result 识别结果
     */
    void onFinishRecognize(String result);

    /**
     * 识别中
     *
     * @param result 识别中间结果
     */
    void onRecognize(String result);

    /**
     * 取消识别
     */
    void onCancelRecognize();

    /**
     * 识别出错
     *
     * @param error 识别错误
     */
    void onError(VoiceRecognizeError error);
}
