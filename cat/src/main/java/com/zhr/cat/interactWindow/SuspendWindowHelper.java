package com.zhr.cat.interactWindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zhr.cat.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ZHR on 2017/05/22.
 */

public class SuspendWindowHelper implements View.OnFocusChangeListener{
    private Context context;//用于生成View
    private WindowManager mWindowManager;//用于创建悬浮窗
    private WindowManager.LayoutParams mLayout;//悬浮窗布局参数
    private SuspendWindow mSuspendWindow;//悬浮窗
    private static SuspendWindowHelper suspendWindowHelper;
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

  private  SuspendWindowHelper(Context context) {
        this.context = context;
        createWindowManager();
        createSuspendWindowLayout();
        sendHideCloseButtonMessage();
      mSuspendWindow.setOnFocusChangeListener(this);
    }
   public static  SuspendWindowHelper getSuspendWindowHelper(Context context){
       if (suspendWindowHelper==null){
           suspendWindowHelper=new SuspendWindowHelper(context);
       }
       return suspendWindowHelper;
   }
    /**
     * 隐藏关闭按钮
     */
    private void hideCloseButton() {
        isShowCloseBtnFlag = false;
        AlphaAnimation aa = new AlphaAnimation(1.0f, 0.0f);
        aa.setDuration(500);
        aa.setRepeatCount(0);
        aa.setRepeatMode(Animation.REVERSE);
        mSuspendWindow.getImageButton().startAnimation(aa);
        mSuspendWindow.getImageButton().setVisibility(View.INVISIBLE);
    }

    /**
     * 显示关闭按钮
     */
    private void showCloseButton() {
        isShowCloseBtnFlag = true;
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
        aa.setDuration(500);
        aa.setRepeatCount(0);
        aa.setRepeatMode(Animation.REVERSE);
        mSuspendWindow.getImageButton().startAnimation(aa);
        mSuspendWindow.getImageButton().setVisibility(View.VISIBLE);
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
        timer.schedule(task, 3000l);/*1000ms执行一次task 立即执行*/
    }

    /**
     * 设置WindowManager
     */
    private void createWindowManager() {
        // 取得系统窗体
        mWindowManager = (WindowManager) context.getApplicationContext().getSystemService(Context
                .WINDOW_SERVICE);
        // 窗体的布局样式
        mLayout = new WindowManager.LayoutParams();
        // 设置窗体显示类型——TYPE_SYSTEM_ALERT(系统提示)
        mLayout.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        // 设置窗体焦点及触摸：
        // FLAG_NOT_FOCUSABLE(不能获得按键输入焦点)
        mLayout.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 设置显示的模式
        mLayout.format = PixelFormat.RGBA_8888;
        //RGBA_8888为android的一种32位颜色格式，R、G、B、A分别用八位表示，Android默认的图像格式是PixelFormat.OPAQUE，其是不带Alpha值的
        // 设置对齐的方法
        mLayout.gravity = Gravity.CENTER;
        // 设置窗体宽度和高度
        mLayout.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayout.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    /**
     * 显示DesktopLayout
     */
    public void showSuspendWindow() {
        mWindowManager.addView(mSuspendWindow, mLayout);
    }

    /**
     * 隐藏DesktopLayout
     */
    public void hideSuspendWindow() {
        mWindowManager.removeView(mSuspendWindow);
    }

    /**
     * 创建悬浮窗体
     */
    private void createSuspendWindowLayout() {
        mSuspendWindow = new SuspendWindow(context);
        mSuspendWindow.setOnCloseClickListener(new SuspendWindow.OnCloseClickListener() {
            @Override
            public void onCloseClick() {
                hideSuspendWindow();
            }
        });
        mSuspendWindow.setOnLinerlayoutClickListener(new SuspendWindow.OnLinerLayoutClickListener
                () {
            @Override
            public void onLinerLayoutClick() {
                if (isShowCloseBtnFlag) {
                    sendHideCloseButtonMessage();
                } else {
                    sendShowCloseButtonMessage();
                }
            }
        });
    }

    /**
     * @return 悬浮窗口左边的LinearLayout
     */
    public LinearLayout getLeftLinearLayout() {
        return mSuspendWindow.getLeftLinearLayout();
    }

    /**
     * @return 悬浮窗口右边的LinearLayout
     */
    public LinearLayout getRightLinearLayout() {
        return mSuspendWindow.getRightLinearLayout();
    }

    /**
     * @return 悬浮窗口中间的LinearLayout
     */
    public LinearLayout getCenterLinearLayout() {
        return mSuspendWindow.getCenterLinearLayout();
    }

    /**
     * 给悬浮窗设置 Cat所说的文本
     *
     * @param text
     */
    public void setTalkText(String text) {
        mSuspendWindow.setTalkText(text);
    }

    /**
     * * 给悬浮窗设置 Cat所说听到的文本
     *
     * @param text
     */
    public void setListenText(String text) {
        mSuspendWindow.setListenText(text);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Toast.makeText(context,"ifif",Toast.LENGTH_SHORT).show();
        switch (v.getId()){
            case R.id.ll_suspend_window :
                if(!hasFocus){
                    hideSuspendWindow();
                }

        }
    }
}
