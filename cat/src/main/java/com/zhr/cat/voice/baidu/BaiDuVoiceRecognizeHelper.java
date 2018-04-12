package com.zhr.cat.voice.baidu;


import android.content.Context;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.zhr.cat.voice.interfaces.IVoiceRecognizeEventListener;
import com.zhr.cat.voice.interfaces.IVoiceRecognizeHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 百度语音识别帮助类
 * Created by ZL on 2018/4/11.
 */

public class BaiDuVoiceRecognizeHelper implements EventListener, IVoiceRecognizeHelper {
    private EventManager asr;
    private Context context;
    private boolean enableOffline = false; // 测试离线命令词，需要改成true
    private IVoiceRecognizeEventListener voiceRecognizeEventListener;
    private String lastResult = "";
    private static boolean isInited = false;

    public  BaiDuVoiceRecognizeHelper(Context context, IVoiceRecognizeEventListener voiceRecognizeEventListener) {
        this(context, false, voiceRecognizeEventListener);
    }

    public BaiDuVoiceRecognizeHelper(Context context, boolean enableOffline, IVoiceRecognizeEventListener voiceRecognizeEventListener) {
        if (isInited) {
            throw new RuntimeException("还未调用release()，请勿新建一个新类");
        }
        isInited = true;
        this.voiceRecognizeEventListener = voiceRecognizeEventListener;
        this.context = context;
        this.enableOffline = enableOffline;
        asr = EventManagerFactory.create(context, "asr");
        asr.registerListener(this); //  EventListener 中 onEvent方法
        if (this.enableOffline) {
            loadOfflineEngine(); // 测试离线命令词请开启, 测试 ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH 参数时开启
        }
    }

    public IVoiceRecognizeEventListener getVoiceRecognizeEventListener() {
        return voiceRecognizeEventListener;
    }

    public void setVoiceRecognizeEventListener(IVoiceRecognizeEventListener voiceRecognizeEventListener) {
        this.voiceRecognizeEventListener = voiceRecognizeEventListener;
    }

    public boolean isEnableOffline() {
        return enableOffline;
    }

    public void setEnableOffline(boolean enableOffline) {
        this.enableOffline = enableOffline;
    }


    private void loadOfflineEngine() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put(SpeechConstant.DECODER, 2);
        params.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, "assets://baidu_speech_grammar.bsg");
        asr.send(SpeechConstant.ASR_KWS_LOAD_ENGINE, new JSONObject(params).toString(), null, 0, 0);
    }

    private void unloadOfflineEngine() {
        asr.send(SpeechConstant.ASR_KWS_UNLOAD_ENGINE, null, null, 0, 0); //
    }

    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
            try {
                JSONObject jsonObject = new JSONObject(params);
                String result = jsonObject.getString("best_result");
                lastResult = result;
                if (voiceRecognizeEventListener != null) {
                    voiceRecognizeEventListener.onRecognize(result);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_FINISH)) {
            if (voiceRecognizeEventListener != null) {
                voiceRecognizeEventListener.onFinishRecognize(lastResult);
            }
            try {
                JSONObject jsonObject = new JSONObject(params);
                String result = jsonObject.getString("sub_error");
                int code = Integer.parseInt(result.substring(0, 1));
                IVoiceRecognizeEventListener.VoiceRecognizeError voiceRecognizeError = IVoiceRecognizeEventListener.VoiceRecognizeError.values()[code];
                if (voiceRecognizeEventListener != null) {
                    voiceRecognizeEventListener.onError(voiceRecognizeError);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void startRecognize() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        String event = SpeechConstant.ASR_START; // 替换成测试的event

        if (enableOffline) {
            params.put(SpeechConstant.DECODER, 2);
        }
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);

        String json = new JSONObject(params).toString(); // 这里可以替换成你需要测试的json
        asr.send(event, json, null, 0, 0);
        if (voiceRecognizeEventListener != null) {
            voiceRecognizeEventListener.onStartRecognize();
        }
    }

    @Override
    public void stopRecognize() {
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0); //
        if (voiceRecognizeEventListener != null) {
            voiceRecognizeEventListener.onStopRecognize();
        }
    }

    @Override
    public void cancelRecognize() {
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        if (enableOffline) {
            unloadOfflineEngine(); // 测试离线命令词请开启, 测试 ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH 参数时开启
        }
        if (voiceRecognizeEventListener != null) {
            voiceRecognizeEventListener.onCancelRecognize();
        }
    }

    @Override
    public void releaseRecognize() {
        if (asr == null) {
            return;
        }
        cancelRecognize();
        if (enableOffline) {
            asr.send(SpeechConstant.ASR_KWS_UNLOAD_ENGINE, null, null, 0, 0);
            enableOffline = false;
        }
        asr.unregisterListener(this);
        asr = null;
        isInited = false;
    }
}
