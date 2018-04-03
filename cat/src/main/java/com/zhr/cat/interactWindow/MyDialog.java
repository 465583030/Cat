//package com.zhr.cat.interactWindow;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.StyleRes;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.zhr.cat.R;
//
///**
// * Created by ZHR on 2017/05/26.
// */
//
//public class MyDialog extends Dialog {
//
//    private Button closeButton;//确定按钮
//    private EditText etListen;//内容文本控件
//    private TextView tvTalk;//内容文本控件
//    private EditText etLongSend;//内容文本控件
//
//    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
//    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器
//
//    private onSendTextChangedListener longSendTextChangedListener;
//    private onSendTextChangedListener shortSendTextChangedListener;
//    private onSendTextChangedListener btnShowTextChangedListener;
//
//    public MyDialog(Context context) {
//        super(context, R.style.MyDialog);
//
//    }
//
//    /**
//     * 设置取消按钮的显示内容和监听
//     *
//     * @param onNoOnclickListener
//     */
//    public void setNoOnclickListener(onNoOnclickListener onNoOnclickListener) {
//        this.noOnclickListener = onNoOnclickListener;
//    }
//
//    /**
//     * 设置确定按钮的显示内容和监听
//     *
//     * @param onYesOnclickListener
//     */
//    public void setYesOnclickListener(onYesOnclickListener onYesOnclickListener) {
//        this.yesOnclickListener = onYesOnclickListener;
//    }
//
//    public void setShortSendTextChangedLisener(onSendTextChangedListener sendTextChangedListener) {
//        this.shortSendTextChangedListener = sendTextChangedListener;
//    }
//
//    public void setLongSendTextChangedLisener(onSendTextChangedListener sendTextChangedListener) {
//        this.longSendTextChangedListener = sendTextChangedListener;
//    }
//
//    public void setBtnShowTextChangedLisener(onSendTextChangedListener sendTextChangedListener) {
//        this.btnShowTextChangedListener = sendTextChangedListener;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.mydialog_layout);
//        //按空白处不能取消动画
//        setCanceledOnTouchOutside(false);
//        //初始化界面控件
//        initView();
//        //初始化界面控件的事件
//        initEvent();
//    }
//
//    /**
//     * 初始化界面的确定和取消监听器
//     */
//    private void initEvent() {
//        //设置确定按钮被点击后，向外界提供监听
//        btnYes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (yesOnclickListener != null) {
//                    yesOnclickListener.onYesClick();
//                }
//            }
//        });
//        //设置取消按钮被点击后，向外界提供监听
//        btnNo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (noOnclickListener != null) {
//                    noOnclickListener.onNoClick();
//                }
//            }
//        });
//
//        etLongSend.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (longSendTextChangedListener != null) {
//                    longSendTextChangedListener.onTextChanged(s, start, before, count);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (longSendTextChangedListener != null) {
//                    longSendTextChangedListener.afterTextChanged(s);
//                }
//            }
//        });
//        etShortSend.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (shortSendTextChangedListener != null) {
//                    shortSendTextChangedListener.onTextChanged(s, start, before, count);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (shortSendTextChangedListener != null) {
//                    shortSendTextChangedListener.afterTextChanged(s);
//                }
//            }
//        });
//        etBtnShow.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (btnShowTextChangedListener != null) {
//                    btnShowTextChangedListener.onTextChanged(s, start, before, count);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (btnShowTextChangedListener != null) {
//                    btnShowTextChangedListener.afterTextChanged(s);
//                }
//            }
//        });
//    }
//
//    /**
//     * 初始化界面控件
//     */
//    private void initView() {
//        btnYes = (Button) findViewById(R.id.yes);
//        btnNo = (Button) findViewById(R.id.no);
//        etShortSend = (EditText) findViewById(R.id.etShortSend);
//        etLongSend = (EditText) findViewById(R.id.etLongSend);
//        etBtnShow = (EditText) findViewById(R.id.etBtnShow);
//    }
//
//    /**
//     * 设置确定按钮和取消被点击的接口
//     */
//    public interface onYesOnclickListener {
//        void onYesClick();
//    }
//
//    public interface onNoOnclickListener {
//        void onNoClick();
//    }
//
//    /**
//     * 设置编辑框文本发生变化的接口
//     */
//    public interface onSendTextChangedListener {
//        void onTextChanged(CharSequence s, int start, int before, int count);
//
//        void afterTextChanged(Editable s);
//    }
//
//    public void setBtnShowText(String strBtnShow) {
//        if (etBtnShow != null)
//            etBtnShow.setText(strBtnShow);
//    }
//
//    public void setLongSendText(String strLongSend) {
//        if (strLongSend != null) {
//            etLongSend.setText(strLongSend);
//        }
//    }
//
//    public void setShortSendText(String strShortSend) {
//        if (strShortSend != null) {
//            etShortSend.setText(strShortSend);
//        }
//    }
//}
//
