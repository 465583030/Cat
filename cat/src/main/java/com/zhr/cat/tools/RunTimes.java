package com.zhr.cat.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ZHR on 2017/05/26.
 */

public class RunTimes {
    /**
     * 保存应用启动有关的配置文件的名称
     */
    private SharedPreferences sp;
    private final static String OPEN_CONFIG_FILE_NAME = "openConfig";
    private final static String RUN_TIMES = "runTimes";
    private int runTimes = 0;

    public RunTimes(Context context) {
        if (context==null){
            System.out.println(" RunTimes  Context == null ");
        }
        sp = context.getSharedPreferences(OPEN_CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        if (sp==null){
            System.out.println("RunTimes.RunTimes  sp==null");
        }

        runTimes = sp.getInt(RUN_TIMES, 0);
    }

    /**
     * 返回应用运行的次数
     *
     * @return 返回应用运行的次数
     */
    public int getRunTimes() {
        return runTimes;
    }

    /**
     * 设置软件运行的次数，调用一次，自动加一
     */
    public void setRunTimes() {
        runTimes++;
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(RUN_TIMES, runTimes);
        editor.commit();
    }

    /**
     * 判断应用是否是第一次启动
     *
     * @return 第一次启动返回true
     */
    public boolean isFirstOpen() {
        if (runTimes == 0)
            return true;
        return false;
    }
}
