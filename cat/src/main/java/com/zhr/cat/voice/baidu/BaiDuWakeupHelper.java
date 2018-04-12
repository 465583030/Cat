package com.zhr.cat.voice.baidu;

import android.content.Context;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.zhr.cat.voice.interfaces.IWakeupEventListener;
import com.zhr.cat.voice.interfaces.IWakeupHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 百度唤醒帮助类
 * Created by ZHR on 2018/4/12.
 */

public class BaiDuWakeupHelper implements EventListener, IWakeupHelper {

    private static boolean isInited = false;
    private EventManager wp;
    private IWakeupEventListener wakeupListener;

    private static final String TAG = "MyWakeup";

    public BaiDuWakeupHelper(Context context, IWakeupEventListener wakeupListener) {
        if (isInited) {
            System.out.println("还未调用release()，请勿新建一个新类");
            throw new RuntimeException("还未调用release()，请勿新建一个新类");
        }
        isInited = true;
        this.wakeupListener = wakeupListener;
        wp = EventManagerFactory.create(context, "wp");
        wp.registerListener(this);
    }

    @Override
    public void startWakeup() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin");
        // "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下
        // params.put(SpeechConstant.ACCEPT_AUDIO_DATA,true);
        // params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME,true);
        // params.put(SpeechConstant.IN_FILE,"res:///com/baidu/android/voicedemo/wakeup.pcm");
        // params里 "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下
        String json = new JSONObject(params).toString();
        System.out.println("wakeup params(反馈请带上此行日志):" + json);
        wp.send(SpeechConstant.WAKEUP_START, json, null, 0, 0);
    }

    @Override
    public void stopWakeup() {
        System.out.println("唤醒结束");
        wp.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0);
    }

    @Override
    public void releaseWakeup() {
        stopWakeup();
        wp.unregisterListener(this);
        wp = null;
        isInited = false;
    }

    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        // android studio日志Monitor 中搜索 WakeupEventAdapter即可看见下面一行的日志
        System.out.println("wakeup name:" + name + "; params:" + params);
        if (SpeechConstant.CALLBACK_EVENT_WAKEUP_SUCCESS.equals(name)) { // 识别唤醒词成功
            try {
                JSONObject jsonObject = new JSONObject(params);
                String word = jsonObject.getString("word");
                wakeupListener.onWakeupSuccess(word);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (SpeechConstant.CALLBACK_EVENT_WAKEUP_ERROR.equals(name)) { // 识别唤醒词报错
            try {
                JSONObject jsonObject = new JSONObject(params);
                String word = jsonObject.getString("errorCode");
                int code = Integer.parseInt(word.substring(0, 1));
                IWakeupEventListener.WakeupError error = IWakeupEventListener.WakeupError.values()[code];
                wakeupListener.onWakeupError(error);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (SpeechConstant.CALLBACK_EVENT_WAKEUP_STOPED.equals(name)) { // 关闭唤醒词
            wakeupListener.onWakeupStop();
        } else if (SpeechConstant.CALLBACK_EVENT_WAKEUP_AUDIO.equals(name)) { // 音频回调
            //  wakeupListener.onASrAudio(data, offset, length);
        }
    }

}
