package com.zhr.cat.services;

/**
 * Created by ZHR on 2017/05/24.
 * 用于获取服务
 */

public class ServicesHelper {
    private static MyService myService;
    public static MyService getService() {
        return myService;
    }
    public static void setMyService(MyService service) {
        if (myService != null) {
            myService = service;
        }
    }
}
