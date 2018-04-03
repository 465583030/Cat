package com.zhr.cat.voice.iFlyTEK;


import android.app.Application;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by ZHR on 2017/05/25.
 */

public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
            SpeechUtility.createUtility(this, SpeechConstant.APPID +"=58087162");
    }
}
