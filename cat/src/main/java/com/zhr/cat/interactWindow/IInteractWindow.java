package com.zhr.cat.interactWindow;

import android.view.View;

/**
 * Created by ZHR on 2017/06/08.
 * 悬浮窗接口
 */

public interface IInteractWindow {
    /***/
    View getView();

    void setTalkText(String text);

    void setListenText(String text);

    void setVisible(int visibility);

}
