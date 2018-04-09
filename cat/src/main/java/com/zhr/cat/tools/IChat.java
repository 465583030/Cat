package com.zhr.cat.tools;

public interface IChat {
    /** 输入类型:0-文本(默认)、1-图片、2-音频 */
    enum ChatType {
        TYPE_TEXT(0),
        TYPE_IMAGE(1),
        TYPE_MEDIA(2);
        int type;

        ChatType(int type) {
            this.type = type;
        }

        ChatType() {
            this.type = 0;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    enum ResultType {
        /** 文本 */
        TYPE_TEXT(0,"text"),
        /** 连接 */
        TYPE_URL(1,"url"),
        /** 音频 */
        TYPE_VOICE(2,"vioce"),
        /** 视频 */
        TYPE_VIDEO(3,"video"),
        /** 图片 */
        TYPE_IMAGE(4,"image"),
        /** 图文 */
        TYPE_NEWS(5,"news");

        int type;
        String value;

        ResultType(int type, String value) {
            this.type = type;
            this.value=value;
        }

        ResultType() {
            this.type = 0;
            this.value="text";
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
    String chat(ChatType type, String data) throws Exception;
}
