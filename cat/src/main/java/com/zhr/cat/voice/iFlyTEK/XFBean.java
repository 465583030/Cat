package com.zhr.cat.voice.iFlyTEK;

import java.util.ArrayList;

/**
 * Created by ZHR on 2017/05/25.
 */

public class XFBean {
    public ArrayList<WS> ws;
    public class WS{
        public ArrayList<CW> cw;
    }
    public class CW {
        public String w;
    }
}
