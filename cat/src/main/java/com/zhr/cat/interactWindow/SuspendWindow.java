package com.zhr.cat.interactWindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhr.cat.R;

/**
 * 悬浮窗
 */
public class SuspendWindow extends LinearLayout {

    private LinearLayout ll_left;//左边的LinerLayout 显示录音，或者说话动画
    private LinearLayout ll_right;//右边的LinerLayout 显示录音，或者说话动画
    private LinearLayout ll_center;//中间的LinerLayout 显示Cat的表情动作
    private TextView tv_cat_talk;//Cat说的文字
    private EditText et_cat_listened;//Cat识别用户说的文字
    private ImageButton ib_close;//手动关闭此对话框
    private OnCloseClickListener closeListener;
    private LinearLayout ll_suspend_window;
    private OnLinerLayoutClickListener linerLayoutClickListener;

    public SuspendWindow(Context context) {
        super(context);
        //setOrientation(LinearLayout.VERTICAL);// 竖直排列
        this.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams
                .WRAP_CONTENT));        //设置宽高
        View view = LayoutInflater.from(context).inflate(R.layout.suspend_window_layout, null);
        this.addView(view);

        tv_cat_talk = (TextView) findViewById(R.id.tv_cat_talk);
        et_cat_listened = (EditText) findViewById(R.id.et_cat_listened);

        ll_center = (LinearLayout) findViewById(R.id.ll_center);
        ll_left = (LinearLayout) findViewById(R.id.ll_left);
        ll_right = (LinearLayout) findViewById(R.id.ll_right);
        ll_suspend_window = (LinearLayout) findViewById(R.id.ll_suspend_window);
        ib_close = (ImageButton) findViewById(R.id.ib_close);

        //设置关闭按钮被点击后，向外界提供监听
        ib_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closeListener != null) {
                    closeListener.onCloseClick();
                }
            }
        });
        ll_suspend_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linerLayoutClickListener != null) {
                    linerLayoutClickListener.onLinerLayoutClick();
                }
            }
        });
    }


    /**
     * 设置关闭按钮的监听
     *
     * @param closeListener
     */
    public void setOnCloseClickListener(OnCloseClickListener closeListener) {
        this.closeListener = closeListener;
    }

    /**
     * 设置关闭按钮的监听
     *
     * @param linerLayoutClickListener
     */
    public void setOnLinerlayoutClickListener(OnLinerLayoutClickListener linerLayoutClickListener) {
        this.linerLayoutClickListener = linerLayoutClickListener;
    }

    /**
     * @return ib_close
     */
    public ImageButton getImageButton() {
        return ib_close;
    }

    /**
     * @return 悬浮窗口左边的LinearLayout
     */
    public LinearLayout getLeftLinearLayout() {
        return ll_left;
    }

    /**
     * @return 悬浮窗口右边的LinearLayout
     */
    public LinearLayout getRightLinearLayout() {
        return ll_right;
    }

    /**
     * @return 悬浮窗口中间的LinearLayout
     */
    public LinearLayout getCenterLinearLayout() {
        return ll_center;
    }

    /**
     * 给悬浮窗设置 Cat所说的文本
     *
     * @param text
     */
    public void setTalkText(String text) {
        if (tv_cat_talk != null) {
            tv_cat_talk.setText(text);
        }
    }

    /**
     * * 给悬浮窗设置 Cat所说听到的文本
     *
     * @param text
     */
    public void setListenText(String text) {
        if (et_cat_listened != null) {
            et_cat_listened.setText(text);
        }
    }

    public interface OnCloseClickListener {
        void onCloseClick();
    }

    public interface OnLinerLayoutClickListener {
        void onLinerLayoutClick();
    }
}
