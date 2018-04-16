package com.zhr.cat.voice.iFlyTEK;

import android.content.Context;
import android.os.Bundle;

import com.google.gson.Gson;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.zhr.cat.voice.interfaces.IVoiceTalk;
import com.zhr.cat.voice.interfaces.IVoiceTalkEventListener;

import java.util.ArrayList;

/**
 * 讯飞语音合成帮助类
 * Created by ZHR on 2017/05/25.
 */

public class IFlyTEKVoiceTalk implements IVoiceTalk {
    private Context context;
    private SpeechSynthesizer mTts;
    private boolean talking = false;
    IVoiceTalkEventListener voiceTalkEventListener;

    public IFlyTEKVoiceTalk(Context context) {
        this.context = context;
        initTalk();
    }

    public IVoiceTalkEventListener getVoiceTalkEventListener() {
        return voiceTalkEventListener;
    }

    public void setVoiceTalkEventListener(IVoiceTalkEventListener voiceTalkEventListener) {
        this.voiceTalkEventListener = voiceTalkEventListener;
    }

    @Override
    public void talk(String text) {
        //3.开始合成
        mTts.startSpeaking(text, mSynListener);
        talking = true;
    }

    @Override
    public boolean isTalking() {
        return talking;
    }

    public void initTalk() {
        //1.创建 SpeechSynthesizer 对象, 第二个参数：本地合成时传 InitListener
        mTts = SpeechSynthesizer.createSynthesizer(context, null);
        //2.合成参数设置，详见《MSC Reference Manual》SpeechSynthesizer 类
        //设置发音人（更多在线发音人，用户可参见 附录13.2
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan"); //设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "65");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围 0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端,用到讯飞服务器
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在 SD 卡需要在 AndroidManifest.xml 添加写 SD 卡权限
        //仅支持保存为 pcm 和 wav 格式，如果不需要保存合成音频，注释该行代码
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");

    }

    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener() {
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {
            talking = false;
            if (voiceTalkEventListener != null) {
                voiceTalkEventListener.onCompleted();
            }
        }

        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在
        //文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        //开始播放
        public void onSpeakBegin() {
            talking = true;
            if (voiceTalkEventListener != null) {
                voiceTalkEventListener.onSpeakBegin();
            }
        }

        //暂停播放
        public void onSpeakPaused() {
            if (voiceTalkEventListener != null) {
                voiceTalkEventListener.onSpeakPaused();
            }
        }

        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文
        //本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        //恢复播放回调接口
        public void onSpeakResumed() {
        }

        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }
    };

    private String getGosn(String resultString) {
        //创建GSON对象，记得要关联库 快捷键Ctrl Shift Alt S
        //GSON Google解析json器 更好的为阿里的 fastJson
        Gson gson = new Gson();
        //参数一 String类型的json数据 参数2 json类对应的Bean类
        XFBean xfBean = gson.fromJson(resultString, XFBean.class);
        //创建一个集合，用来存放bean类里的集合
        ArrayList<XFBean.WS> ws = xfBean.ws;
        //创建一个容器，用来存放从集合里面拿到的数据 用StringBuilder
        StringBuilder stringBuilder = new StringBuilder();
        //使用for循环 取出特定数据 放入容器
        for (XFBean.WS w : ws) {
            stringBuilder.append(w.cw.get(0).w);
        }
        return stringBuilder.toString();
    }
}
