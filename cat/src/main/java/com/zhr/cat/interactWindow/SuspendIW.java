package com.zhr.cat.interactWindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhr.cat.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * SuspendInteractWindow 悬浮交互窗口
 * Created by ZHR on 2017/06/08.
 */

public class SuspendIW extends LinearLayout implements IInteractWindow, View.OnClickListener {
    private Context context;//用于生成View
    private WindowManager windowManager;//用于创建悬浮窗
    private WindowManager.LayoutParams mLayout;//悬浮窗布局参数

    private LinearLayout ll_suspend_window;
    private boolean isVisible = false;//当前窗口是否显示
    private LinearLayout ll_left;//左边的LinerLayout 显示录音，或者说话动画
    private LinearLayout ll_right;//右边的LinerLayout 显示录音，或者说话动画

    private LinearLayout ll_center;//中间的LinerLayout 显示Cat的表情动作

    private TextView tv_cat_talk;//Cat说的文字
    private EditText et_cat_listened;//Cat识别用户说的文字
    private ImageButton ib_close;//手动关闭此对话框

    private static final int HIDE_CLOSE_BUTTON = 0;//隐藏悬浮窗关闭按钮
    private static final int SHOW_CLOSE_BUTTON = 1;//显示悬浮窗关闭按钮
    private boolean isShowCloseBtnFlag = true;//当前悬浮窗关闭按钮显示状态 true显示
    private Timer timer;//用于定时
    private Handler handler = new Handler() {//处理UI更新
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HIDE_CLOSE_BUTTON) {
                hideCloseButton();
            } else if (msg.what == SHOW_CLOSE_BUTTON) {
                showCloseButton();
                sendHideCloseButtonMessage();
            }
        }
    };

    public SuspendIW(Context context) {
        super(context);
        this.context = context;
        createWindowManager();//
        initView();
        setListener();
        sendHideCloseButtonMessage();//隐藏关闭按钮
    }

    /**
     * 设置WindowManager
     */
    private void createWindowManager() {
        // 取得系统窗体
        windowManager = (WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        // 窗体的布局样式
        mLayout = new WindowManager.LayoutParams();
        // 设置窗体显示类型——TYPE_SYSTEM_ALERT(系统提示)
        mLayout.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        // 设置窗体焦点及触摸：
        // FLAG_NOT_FOCUSABLE(不能获得按键输入焦点)
        mLayout.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        // mLayout.flags = WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        // 设置显示的模式
        mLayout.format = PixelFormat.RGBA_8888;
        //RGBA_8888为android的一种32位颜色格式，R、G、B、A分别用八位表示，Android默认的图像格式是PixelFormat.OPAQUE，其是不带Alpha值的
        // 设置对齐的方法
        mLayout.gravity = Gravity.CENTER;
        //输入法
        mLayout.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
        mLayout.dimAmount = 0.7f;
        // 设置窗体宽度和高度
        mLayout.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayout.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    private void initView() {
        //设置宽高
        this.setLayoutParams(
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        View view = LayoutInflater.from(context).inflate(R.layout.interact_window_layout, null);
        this.addView(view);
        ll_suspend_window = (LinearLayout) findViewById(R.id.ll_suspend_window);

        ll_left = (LinearLayout) findViewById(R.id.ll_left);
        ll_right = (LinearLayout) findViewById(R.id.ll_right);

        ll_center = (LinearLayout) findViewById(R.id.ll_center);

        tv_cat_talk = (TextView) findViewById(R.id.tv_cat_talk);
        et_cat_listened = (EditText) findViewById(R.id.et_cat_listened);

        ib_close = (ImageButton) findViewById(R.id.ib_close);
    }

    private void setListener() {
        if (ll_suspend_window != null) {
            ll_suspend_window.setOnClickListener(this);
        }
        if (ib_close != null) {
            ib_close.setOnClickListener(this);
        }
    }

    /**
     * 隐藏关闭按钮
     */
    private void hideCloseButton() {
        AlphaAnimation aa = new AlphaAnimation(1.0f, 0.0f);
        aa.setDuration(500);
        aa.setRepeatCount(0);
        aa.setRepeatMode(Animation.REVERSE);
        ib_close.startAnimation(aa);
        ib_close.setVisibility(View.INVISIBLE);
        isShowCloseBtnFlag = false;
    }

    /**
     * 显示关闭按钮
     */
    private void showCloseButton() {
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
        aa.setDuration(500);
        aa.setRepeatCount(0);
        aa.setRepeatMode(Animation.REVERSE);
        ib_close.startAnimation(aa);
        ib_close.setVisibility(View.VISIBLE);
        isShowCloseBtnFlag = true;
    }

    /**
     * 发送 显示关闭按钮消息
     */
    private void sendShowCloseButtonMessage() {
        Message message = new Message();
        message.what = SHOW_CLOSE_BUTTON;
        handler.sendMessage(message);
    }

    /**
     * 延时发送 隐藏关闭按钮
     */
    private void sendHideCloseButtonMessage() {
        //  同时倒计时3秒
        timer = new Timer();/*构造Timer*/
        final TimerTask task = new TimerTask() {/*每隔1S执行的事件*/
            @Override
            public void run() {
                Message message = new Message();
                message.what = HIDE_CLOSE_BUTTON;
                handler.sendMessage(message);
                if (timer != null)
                    timer.cancel();
            }
        };
        timer.schedule(task, 2000l);/*xms执行一次task 立即执行*/
    }

    @Override
    public View getView() {
        return ll_center;
    }

    @Override
    public void setTalkText(String text) {
        if (tv_cat_talk != null) {
            tv_cat_talk.setText(text);
        }
    }

    @Override
    public void setListenText(String text) {
        if (et_cat_listened != null) {
            et_cat_listened.setText(text);
        }

    }

    @Override
    public void setVisible(int visibility) {
        switch (visibility) {
            case View.VISIBLE:
                if (!isVisible) {
                    isVisible = true;
                    windowManager.addView(this, mLayout);
                }
                break;
            case View.INVISIBLE:
                if (isVisible) {
                    isVisible = false;
                    windowManager.removeView(this);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_close:
                if (isVisible) {
                    isVisible = false;
                    windowManager.removeView(this);
                }
                break;
            case R.id.ll_suspend_window:
                if (!isShowCloseBtnFlag) {
                    sendShowCloseButtonMessage();
                }
                break;
        }
    }
}
