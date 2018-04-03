//package com.zhr.cat.interactWindow;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.NonNull;
//import android.view.View;
//import android.view.animation.AlphaAnimation;
//import android.view.animation.Animation;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.zhr.cat.R;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
///**
// * Created by ZHR on 2017/06/08.
// */
//
//public class InteractDialog extends Dialog implements IInteractWindow {
//    private LinearLayout ll_suspend_window;
//
//    private LinearLayout ll_left;//左边的LinerLayout 显示录音，或者说话动画
//    private LinearLayout ll_right;//右边的LinerLayout 显示录音，或者说话动画
//
//    private LinearLayout ll_center;//中间的LinerLayout 显示Cat的表情动作
//    private TextView tv_cat_talk;//Cat说的文字
//
//    private EditText et_cat_listened;//Cat识别用户说的文字
//    private ImageButton ib_close;//手动关闭此对话框
//
//    private static final int HIDE_CLOSE_BUTTON = 0;//隐藏悬浮窗关闭按钮
//    private static final int SHOW_CLOSE_BUTTON = 1;//显示悬浮窗关闭按钮
//    private boolean isShowCloseBtnFlag = true;//当前悬浮窗关闭按钮显示状态 true显示
//    private Timer timer;//用于定时
//    private Handler handler = new Handler() {//处理UI更新
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == HIDE_CLOSE_BUTTON) {
//                hideCloseButton();
//            } else if (msg.what == SHOW_CLOSE_BUTTON) {
//                showCloseButton();
//                sendHideCloseButtonMessage();
//            }
//        }
//    };
//
//    public InteractDialog(@NonNull Context context) {
//        super(context, R.style.MyDialog);
//    }
//        @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.interact_window_layout);
//        //按空白处不能取消动画
//       // setCanceledOnTouchOutside(false);
//        //初始化界面控件
//        initView();
//        setListener();
//        sendHideCloseButtonMessage();
//    }
//    private void initView() {
//        ll_suspend_window = (LinearLayout) findViewById(R.id.ll_suspend_window);
//
//        ll_left = (LinearLayout) findViewById(R.id.ll_left);
//        ll_right = (LinearLayout) findViewById(R.id.ll_right);
//
//        ll_center = (LinearLayout) findViewById(R.id.ll_center);
//
//        tv_cat_talk = (TextView) findViewById(R.id.tv_cat_talk);
//        et_cat_listened = (EditText) findViewById(R.id.et_cat_listened);
//
//        ib_close = (ImageButton) findViewById(R.id.ib_close);
//    }
//
//    private void setListener() {
//        ib_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /**待验证*/
//              //  InteractDialog.this.setVisibility(View.INVISIBLE);
//            }
//        });
//        ll_suspend_window.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isShowCloseBtnFlag) {
//                    sendShowCloseButtonMessage();
//                }
//            }
//        });
//    }
//
//    /**
//     * 隐藏关闭按钮
//     */
//    private void hideCloseButton() {
//        AlphaAnimation aa = new AlphaAnimation(1.0f, 0.0f);
//        aa.setDuration(500);
//        aa.setRepeatCount(0);
//        aa.setRepeatMode(Animation.REVERSE);
//        ib_close.startAnimation(aa);
//        ib_close.setVisibility(View.INVISIBLE);
//        isShowCloseBtnFlag = false;
//    }
//
//    /**
//     * 显示关闭按钮
//     */
//    private void showCloseButton() {
//        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
//        aa.setDuration(500);
//        aa.setRepeatCount(0);
//        aa.setRepeatMode(Animation.REVERSE);
//        ib_close.startAnimation(aa);
//        ib_close.setVisibility(View.VISIBLE);
//        isShowCloseBtnFlag = true;
//    }
//
//    /**
//     * 发送 显示关闭按钮消息
//     */
//    private void sendShowCloseButtonMessage() {
//        Message message = new Message();
//        message.what = SHOW_CLOSE_BUTTON;
//        handler.sendMessage(message);
//    }
//
//    /**
//     * 延时发送 隐藏关闭按钮
//     */
//    private void sendHideCloseButtonMessage() {
//        //  同时倒计时3秒
//        timer = new Timer();/*构造Timer*/
//        final TimerTask task = new TimerTask() {/*每隔1S执行的事件*/
//            @Override
//            public void run() {
//                Message message = new Message();
//                message.what = HIDE_CLOSE_BUTTON;
//                handler.sendMessage(message);
//                if (timer != null)
//                    timer.cancel();
//            }
//        };
//        timer.schedule(task, 2000l);/*xms执行一次task 立即执行*/
//    }
//
//    @Override
//    public View getView() {
//        return ll_center;
//    }
//
//    @Override
//    public void setTalkText(String text) {
//        if (tv_cat_talk != null) {
//            tv_cat_talk.setText(text);
//        }
//
//    }
//
//    @Override
//    public void setListenText(String text) {
//        if (et_cat_listened != null) {
//            et_cat_listened.setText(text);
//        }
//
//    }
//
//    @Override
//    public void setVisiable(int visibility) {
//        switch (visibility) {
//            case View.VISIBLE:
//                show();
//                break;
//            case View.INVISIBLE:
//                hide();
//                break;
//        }
//    }
//}
