package com.zhr.cat.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.zhr.cat.R;
import com.zhr.cat.interactWindow.IInteractWindow;
import com.zhr.cat.tools.RunTimes;
import com.zhr.cat.voice.baidu.BaiDuVoiceRecognizeHelper;
import com.zhr.cat.voice.baidu.BaiDuWakeupHelper;
import com.zhr.cat.voice.iFlyTEK.IFlyTEKVoiceTalk;
import com.zhr.cat.voice.interfaces.IVoiceTalk;
import com.zhr.cat.voice.interfaces.IVoiceRecognizeEventListener;
import com.zhr.cat.voice.interfaces.IVoiceRecognizeHelper;
import com.zhr.cat.voice.interfaces.IWakeupEventListener;
import com.zhr.cat.voice.interfaces.IWakeupHelper;

public class MyService extends Service {
    private IInteractWindow interactWindow;
    private IVoiceTalk voice;
    private IVoiceRecognizeHelper voiceRecognizeHelper;
    private IWakeupHelper wakeupHelper;
    private final EchoServiceBinder echoServiceBinder = new EchoServiceBinder();    //单例
    private IWakeupEventListener wakeupEventListener;
    private IVoiceRecognizeEventListener voiceRecognizeEventListener;

    public IVoiceRecognizeHelper getVoiceRecognizeHelper() {
        return voiceRecognizeHelper;
    }

    public IWakeupHelper getWakeupHelper() {
        return wakeupHelper;
    }

    public IWakeupEventListener getWakeupEventListener() {
        return wakeupEventListener;
    }

    public void setWakeupEventListener(IWakeupEventListener wakeupEventListener) {
        this.wakeupEventListener = wakeupEventListener;
        if (wakeupHelper != null && this.wakeupEventListener != null) {
            wakeupHelper.releaseWakeup();
            wakeupHelper = new BaiDuWakeupHelper(this, wakeupEventListener);
        } else if (wakeupHelper == null && this.wakeupEventListener != null) {
            wakeupHelper = new BaiDuWakeupHelper(this, wakeupEventListener);
        }
    }

    public IVoiceRecognizeEventListener getVoiceRecognizeEventListener() {
        return voiceRecognizeEventListener;
    }

    public void setVoiceRecognizeEventListener(IVoiceRecognizeEventListener voiceRecognizeEventListener) {
        this.voiceRecognizeEventListener = voiceRecognizeEventListener;
        if (voiceRecognizeHelper != null && this.voiceRecognizeEventListener != null) {
            voiceRecognizeHelper.releaseRecognize();
            voiceRecognizeHelper = new BaiDuVoiceRecognizeHelper(this, true, voiceRecognizeEventListener);
        } else if (voiceRecognizeHelper == null && this.voiceRecognizeEventListener != null) {
            voiceRecognizeHelper = new BaiDuVoiceRecognizeHelper(this, true, voiceRecognizeEventListener);
        }
    }

    public class EchoServiceBinder extends Binder {
        public MyService getMyService() {
            return MyService.this;
        }
    }

    public MyService() {
        super();
    }

    public void talk(String text) {
        voice.talk(text);
    }

    public boolean isTalking(){
        return voice.isTalking();
    }
    public String getString() {
        return "你好...我是服务里的方法，你调用了我....";
    }

    @Override
    public void onCreate() {
        System.out.println("onCreate...");
        super.onCreate();
        voice = new IFlyTEKVoiceTalk(this);

        // interactWindow = new SuspendIW(this);
        if (new RunTimes(this).getRunTimes() < 3) {
            talk(getString(R.string.welcomeTalkString));
        }
        //interactWindow.setTalkText("你好！欢迎使用Cat手机助手！");
        // interactWindow.setListenText("在这里输入文字或者直接语音输入！");
    }

    public IInteractWindow getInteractWindow() {
        return interactWindow;
    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("OnBind....");
        return echoServiceBinder;
    }


    @Override
    public void onDestroy() {
        System.out.println("onDestroy...");
        super.onDestroy();
    }
}
