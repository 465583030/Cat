package com.zhr.cat.voice.iFlyTEK;

import android.content.Context;
import android.os.Bundle;

import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.zhr.cat.voice.IVoice;

import java.util.ArrayList;

/**
 * Created by ZHR on 2017/05/25.
 */

public class IFlyTEKVoice implements IVoice{
    private Context context;
    private  SpeechSynthesizer mTts;
    private  RecognizerDialog mDialog;
    boolean finishFlag=false;
    private   String resultString;
    public  IFlyTEKVoice(Context context){
        this.context=context;
        initTalk();
    }
    @Override
    public String listen( ) {
        //1.创建RecognizerDialog对象 第二个参数 初始化的监听器，用不上 设置为NULL
        mDialog = new RecognizerDialog(context, null);
        //2.设置accent、language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");//设置为中文模式
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");//设置普通话
        //若要将UI控件用于语义理解，必须添加以下参数设置，设置之后onResult回调返回将是语义理解
        //结果
        // mDialog.setParameter("asr_sch", "1");
        // mDialog.setParameter("nlp_version", "2.0");
        final StringBuilder stringBuilder = new StringBuilder();
        //3.设置回调接口
        mDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                //识别成功回调  recognizerResult 识别结果，json格式
                //b true会话结束
                //一般情况下 这个方法 多次回调
                String resultStringCut = recognizerResult.getResultString();
                stringBuilder.append(getGosn(resultStringCut));
                if (b) {
                    resultString = stringBuilder.toString();
                    finishFlag = true;
                }
            }

            @Override
            public void onError(SpeechError speechError) {
                //识别失败回调
                System.out.println("错误码： " + speechError);
            }
        });
        //4.显示dialog，接收语音输入
        mDialog.show();
        new Thread() {
            public void run() {
                while (!finishFlag){

                }
                finishFlag=false;
            }
        }.start();
        return resultString;
    }
    @Override
    public void talk(String text) {
        //3.开始合成
        mTts.startSpeaking(text, mSynListener);
    }
    public void initTalk() {
        //1.创建 SpeechSynthesizer 对象, 第二个参数：本地合成时传 InitListener
        mTts = SpeechSynthesizer.createSynthesizer(context, null);
        //2.合成参数设置，详见《MSC Reference Manual》SpeechSynthesizer 类
        //设置发音人（更多在线发音人，用户可参见 附录13.2
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan"); //设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
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
        }

        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在
        //文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        //开始播放
        public void onSpeakBegin() {
        }

        //暂停播放
        public void onSpeakPaused() {
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
