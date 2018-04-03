package com.zhr.cat.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.zhr.cat.R;
import com.zhr.cat.interactWindow.IInteractWindow;
import com.zhr.cat.tools.RunTimes;
import com.zhr.cat.voice.IVoice;
import com.zhr.cat.voice.iFlyTEK.IFlyTEKVoice;

public class MyService extends Service {
    private IInteractWindow interactWindow;
    private IVoice voice;
    private final EchoServiceBinder echoServiceBinder = new EchoServiceBinder();    //单例

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

    public String listen() {
        return voice.listen();
    }

    public String getString() {
        return "你好...我是服务里的方法，你调用了我....";
    }

    @Override
    public void onCreate() {
        System.out.println("onCreate...");
        super.onCreate();
        voice = new IFlyTEKVoice(this);

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
