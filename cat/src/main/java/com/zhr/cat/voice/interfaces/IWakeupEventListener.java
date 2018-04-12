package com.zhr.cat.voice.interfaces;

public interface IWakeupEventListener {
    enum WakeupError {
        NET_TIMEOUT("1","网络超时"),
        NET_CONNECT_ERROR("2","网络连接失败"),
        AUDIO_ERROR("3","音频错误"),
        PROTOCOL_ERROR("4","协议错误"),
        CALL_ERROR("5","客户端调用错误"),
        AUDIO_TOO_LONG("6","语音过长"),
        NET_ERROR("7","没有识别结果"),
        ENGINE_BUSY("8","引擎忙"),
        NEED_PERMISSION("9","缺少权限");

        private String code;
        private  String message;

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
    void onWakeupStart();
    void onWakeupSuccess(String word);

    void onWakeupStop();

    void onWakeupError(WakeupError error);

//    void onASrAudio(byte[] data, int offset, int length);
}