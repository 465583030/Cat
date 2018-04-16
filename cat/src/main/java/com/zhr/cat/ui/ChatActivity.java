package com.zhr.cat.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhr.cat.R;
import com.zhr.cat.domain.MyListViewAdapter;
import com.zhr.cat.domain.TextInfo;
import com.zhr.cat.domain.TextInfoDAO;
import com.zhr.cat.services.MyService;
import com.zhr.cat.tools.ChatImpl;
import com.zhr.cat.tools.CircleImageView;
import com.zhr.cat.tools.IChat;
import com.zhr.cat.tools.StringUtils;
import com.zhr.cat.tools.camera.CameraPreview;
import com.zhr.cat.voice.interfaces.IVoiceRecognizeEventListener;
import com.zhr.cat.voice.interfaces.IWakeupEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天交互界面
 *
 * @author 赵浩燃
 */
public class ChatActivity extends Activity implements OnClickListener, ServiceConnection, IVoiceRecognizeEventListener, IWakeupEventListener {
    /**
     * 保存应用启动有关的配置文件的名称
     */
    private static SharedPreferences sp;
    private static String OPEN_CONFIG = "OPEN_CONFIG";
    /**
     * 设置 点击跳转设置界面
     */
    private CircleImageView civ_user;
    /**
     * 相机 打开或者关闭相机
     */
    private ImageButton ib_camera;
    /**
     * 机器人表情 打开或者关闭机器人表情
     */
    private ImageButton ib_face;
    /**
     * 麦克风 打开或者关闭麦克风
     */
    private ImageButton ib_microphone;
    /**
     * 发送图片 点击选择图片
     */
    private ImageButton ib_image;
    /**
     * 其他功能
     */
    private ImageButton ib_add;
    /**
     * 麦克风打开时的布局
     */
    private LinearLayout ll_microphone;
    /**
     * 选择图片的布局
     */
    private LinearLayout ll_image;
    /**
     * 更多选项 的布局
     */
    private LinearLayout ll_add;
    /**
     * 显示相机和表情布局的父容器
     */
    private LinearLayout ll_camera_and_face;
    /**
     * 显示相机画面的布局
     */
    private LinearLayout ll_camera;
    /**
     * 显示机器人表情的布局
     */
    private LinearLayout ll_face;
    /**
     * 标记按钮的选中状态 0 camera 1 face 2 microphone 3 image 4 add
     */
    private static final int SELECT_STATE_CAMERA = 0;
    private static final int SELECT_STATE_FACE = 1;
    private static final int SELECT_STATE_MICROPHONE = 2;
    private static final int SELECT_STATE_IMAGE = 3;
    private static final int SELECT_STATE_ADD = 4;
    private boolean selectState[];
    /**
     * 底部文本框
     */
    private EditText et_content;
    /**
     * 底部发送文本的按钮
     */
    private Button bt_send;
    /**
     * 显示聊天内容
     */
    private ListView lv_content;

    /**
     * 点击麦克风弹出的TextView
     */
    private TextView tv_microphone_tip;
    /**
     * 存储所有的聊天信息
     */
    private List<TextInfo> textInfos;
    /**
     * 当前的聊天记录
     */
    private TextInfo currentTextInfo;
    /**
     * 机器人 处理完成
     */
    protected static final int CAT_PROCESS_FINISH = 1;
    private MyListViewAdapter myAdapter;
    /**
     * TextInfo Database Access Object
     */
    private TextInfoDAO dao;

    /**
     * 相机FrameLayout
     */
    private FrameLayout flCamera;
    private MyService myService;
    //    IInteractWindow interactWindow;
    /**
     * 识别到的唤醒词
     */
    private String wakeupWord = "";
    /**
     * 是否用了唤醒
     */
    private boolean useWakeup = false;
    /**
     * 聊天机器人
     */

    private IChat chater = new ChatImpl();
    @SuppressWarnings("all")
    private Handler myHandler = new Handler() {// 处理消息的handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case CAT_PROCESS_FINISH:
                    String content = (String) msg.obj;
                    currentTextInfo = new TextInfo();
                    currentTextInfo.setContent(content);
                    currentTextInfo.setTime(System.currentTimeMillis());
                    currentTextInfo.setType(0);
                    textInfos.add(currentTextInfo);
                    myAdapter.setList(textInfos);
                    myAdapter.notifyDataSetChanged();
                    lv_content.setSelection(textInfos.size() - 1);
                    dao.add(currentTextInfo.getContent(), currentTextInfo.getType(),
                            currentTextInfo.getTime());
                    currentTextInfo = null;
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        // 加载控件
        civ_user = findViewById(R.id.civ_user);
        ib_camera = findViewById(R.id.ib_camera);
        ib_face = findViewById(R.id.ib_face);
        ib_microphone = findViewById(R.id.ib_microphone);
        ib_image = findViewById(R.id.ib_image);
        ib_add = findViewById(R.id.ib_add);
        ll_microphone = findViewById(R.id.ll_microphone);
        ll_image = findViewById(R.id.ll_image);
        ll_add = findViewById(R.id.ll_add);
        ll_camera_and_face = findViewById(R.id.ll_camera_and_face);
        ll_camera = findViewById(R.id.ll_camera);
        ll_face = findViewById(R.id.ll_face);
        et_content = findViewById(R.id.et_content);
        bt_send = findViewById(R.id.bt_send);
        lv_content = findViewById(R.id.lv_content);
        tv_microphone_tip = findViewById(R.id.tv_microphone_tip);

        flCamera = findViewById(R.id.flCamera);
        CameraPreview mPreview = new CameraPreview(this);
        flCamera.addView(mPreview);
        /** 设置监听*/
        civ_user.setOnClickListener(this);
        ib_camera.setOnClickListener(this);
        ib_face.setOnClickListener(this);
        ib_microphone.setOnClickListener(this);
        ib_image.setOnClickListener(this);
        ib_add.setOnClickListener(this);
        ll_microphone.setOnClickListener(this);
        ll_image.setOnClickListener(this);
        ll_add.setOnClickListener(this);
        ll_camera_and_face.setOnClickListener(this);
        ll_camera.setOnClickListener(this);
        ll_face.setOnClickListener(this);
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    bt_send.setEnabled(true);
                    bt_send.setBackgroundResource(R.drawable.button_shape2);
                    bt_send.setTextColor(getResources().getColor(R.color.send_on));
                } else {
                    bt_send.setEnabled(false);
                    bt_send.setBackgroundResource(R.drawable.button_shape);
                    bt_send.setTextColor(getResources().getColor(R.color.send_off));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0) {
                    bt_send.setBackgroundResource(R.drawable.button_shape2);
                    bt_send.setTextColor(getResources().getColor(R.color.send_on));
                } else {
                    bt_send.setBackgroundResource(R.drawable.button_shape);
                    bt_send.setTextColor(getResources().getColor(R.color.send_off));
                }
            }
        });
        bt_send.setOnClickListener(this);
        /** 读取并设置布局 */
        readState();
        /** 给ListView准备数据 */
        textInfos = new ArrayList<>();
        dao = new TextInfoDAO(this, "textinfo.db");
        textInfos = dao.findAll();
        myAdapter = new MyListViewAdapter(this, textInfos);
        lv_content.setAdapter(myAdapter);
        lv_content.setSelection(textInfos.size() - 1);
        bindService(new Intent(this, MyService.class), this, Context.BIND_AUTO_CREATE);
    }

    /**
     * 读取并设置布局
     */
    private void readState() {
        selectState = new boolean[5];
        for (int i = 0; i < selectState.length; i++) {
            selectState[i] = false;
        }
        sp = getSharedPreferences(OPEN_CONFIG, Context.MODE_PRIVATE);
        selectState[SELECT_STATE_CAMERA] = sp.getBoolean("camera", false);
        selectState[SELECT_STATE_FACE] = sp.getBoolean("face", false);
        selectState[SELECT_STATE_MICROPHONE] = sp.getBoolean("microphone", false);
        if (!selectState[SELECT_STATE_CAMERA] && !selectState[SELECT_STATE_FACE]) {
            ll_camera_and_face.setVisibility(View.GONE);
        } else {
            ll_camera_and_face.setVisibility(View.VISIBLE);
        }
        if (selectState[SELECT_STATE_CAMERA]) {/** 上次状态是打开 所以打开相机 */
            ib_camera.setImageResource(R.drawable.pic_main_camera_on);
            ll_camera.setVisibility(View.VISIBLE);
        } else {
            ll_camera.setVisibility(View.GONE);
        }
        if (selectState[SELECT_STATE_FACE]) {/** 上次状态是打开 所以打开机器人表情视窗 */
            ib_face.setImageResource(R.drawable.pic_main_face_on);
            ll_face.setVisibility(View.VISIBLE);
        } else {
            ll_face.setVisibility(View.GONE);
        }
        if (selectState[SELECT_STATE_MICROPHONE]) {/** 上次状态是打开 所以打开麦克风 */
            ib_microphone.setImageResource(R.drawable.pic_main_microphone_on);
            ll_microphone.setVisibility(View.VISIBLE);
        } else {
            ll_microphone.setVisibility(View.GONE);
        }
    }

    /**
     * 保存布局状态
     */
    private void saveState() {
        sp = getSharedPreferences(OPEN_CONFIG, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean("camera", selectState[SELECT_STATE_CAMERA]);
        editor.putBoolean("face", selectState[SELECT_STATE_FACE]);
        editor.putBoolean("microphone", selectState[SELECT_STATE_MICROPHONE]);
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.civ_user:
                onClickSetting();
                break;
            case R.id.ib_camera:
                onClickCamera();
                break;
            case R.id.ib_face:
                onClickFace();
                break;
            case R.id.ib_microphone:
                onClickMicrophone();
                break;
            case R.id.ib_image:
                onClickImage();
                break;
            case R.id.ib_add:
                onClickAdd();
                break;
            case R.id.ll_camera:
                break;
            case R.id.ll_face:
                break;
            case R.id.ll_camera_and_face:
                break;
            case R.id.ll_microphone:
                break;
            case R.id.ll_image:
                break;
            case R.id.ll_add:
                break;
            case R.id.bt_send:
                onClickSend();
                break;
            default:
                break;
        }
    }

    private void onClickSend() {
        /**获取内容*/
        String textFromClient = et_content.getText().toString().trim();
        /**置空输入框*/
        et_content.setText("");
        /**处理用户发来的消息*/
        sendMessage(textFromClient);
    }

    private void sendMessage(final String content) {
        currentTextInfo = new TextInfo();
        currentTextInfo.setContent(content);
        currentTextInfo.setTime(System.currentTimeMillis());
        currentTextInfo.setType(1);
        /**存储消息到消息列表*/
        textInfos.add(currentTextInfo);
        /**添加数据到适配器*/
        myAdapter.setList(textInfos);
        myAdapter.notifyDataSetChanged();
        /**添加数据到数据库*/
        dao.add(currentTextInfo.getContent(), currentTextInfo.getType(), currentTextInfo.getTime());
        currentTextInfo = null;
        /**处理来自客户端的字符串*/
        new Thread() {
            @Override
            public synchronized void start() {
                super.start();
            }

            public void run() {
                Message msg = new Message();
                msg.what = CAT_PROCESS_FINISH;
                /**处理得到的字符串*/
                String processResult = processContent(content);
                msg.obj = processResult;
                /**说出回应的话*/
                myService.talk(processResult);
                /**更新到UI*/
                myHandler.sendMessage(msg);
            }

            private String processContent(String textFromClient) {
                if (StringUtils.isNotEmpty(getAppName(textFromClient))) {
                    return getText(R.string.action_open_app) + getAppName(textFromClient);
                } else if (StringUtils.isNotEmpty(getCallName(textFromClient))) {
                    return getText(R.string.action_call_start) + getAppName(textFromClient) + getText(R.string.action_call_end);
                } else {
                    try {
                        return chater.chat(IChat.ChatType.TYPE_TEXT, textFromClient);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return getText(R.string.tip_exception).toString();
                    }
                }
            }
        }.start();
    }

    /**
     * 获取要打电话的人 同时拨打电话
     *
     * @param textFromClient 来自客户的文本
     * @return 有人名返回人名 没有返回尾号 其他返回null
     */
    private String getCallName(String textFromClient) {
        //TODO
        String callName = null;

        return callName;
    }

    /**
     * 返回App的名称 同时打开App
     *
     * @param textFromClient 来自客户的文本
     * @return 有则返回 没有则返回null
     */
    private String getAppName(String textFromClient) {
        //TODO
        String appName = null;
        PackageManager packageManager = getPackageManager();
        packageManager.getInstalledApplications(0);
        return appName;
    }

    /**
     * 点击 相机 按钮时执行
     */
    private void onClickCamera() {
        if (selectState[SELECT_STATE_CAMERA]) {
            selectState[SELECT_STATE_CAMERA] = false;
            ib_camera.setImageResource(R.drawable.pic_main_camera_off);
            ll_camera.setVisibility(View.GONE);
            if (ll_face.getVisibility() == View.GONE) {
                ll_camera_and_face.setVisibility(View.GONE);
            }
        } else {
            ll_camera_and_face.setVisibility(View.VISIBLE);
            selectState[SELECT_STATE_CAMERA] = true;
            ib_camera.setImageResource(R.drawable.pic_main_camera_on);
            ll_camera.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 点击 表情 按钮时执行
     */
    private void onClickFace() {
        if (selectState[SELECT_STATE_FACE]) {
            selectState[SELECT_STATE_FACE] = false;
            ib_face.setImageResource(R.drawable.pic_main_face_off);
            ll_face.setVisibility(View.GONE);
            if (ll_camera.getVisibility() == View.GONE) {
                ll_camera_and_face.setVisibility(View.GONE);
            }
        } else {
            ll_camera_and_face.setVisibility(View.VISIBLE);
            selectState[SELECT_STATE_FACE] = true;
            ib_face.setImageResource(R.drawable.pic_main_face_on);
            ll_face.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 点击 麦克风 按钮时执行
     */
    private void onClickMicrophone() {
        if (selectState[SELECT_STATE_MICROPHONE]) {/** 判断对应的底部布局是否显示 */
            if (ll_microphone.getVisibility() == View.GONE) {
                ll_microphone.setVisibility(View.VISIBLE);
                Toast.makeText(this, "再次点击关闭麦克风", Toast.LENGTH_SHORT).show();
                /** 处理Image相关事件 */
                ll_image.setVisibility(View.GONE);
                ib_image.setImageResource(R.drawable.pic_main_image_off);
                selectState[SELECT_STATE_IMAGE] = false;
                /** 处理add相关事件 */
                ll_add.setVisibility(View.GONE);
                ib_add.setImageResource(R.drawable.pic_main_add_off);
                selectState[SELECT_STATE_ADD] = false;
            } else {
                selectState[SELECT_STATE_MICROPHONE] = false;
                ib_microphone.setImageResource(R.drawable.pic_main_microphone_off);
                ll_microphone.setVisibility(View.GONE);
                /**关闭语音识别  同时开启语音唤醒*/
                myService.getVoiceRecognizeHelper().stopRecognize();
                myService.getWakeupHelper().startWakeup();
            }
        } else {
            selectState[SELECT_STATE_MICROPHONE] = true;
            ib_microphone.setImageResource(R.drawable.pic_main_microphone_on);
            ll_microphone.setVisibility(View.VISIBLE);
            /** 处理Image相关事件 */
            ll_image.setVisibility(View.GONE);
            ib_image.setImageResource(R.drawable.pic_main_image_off);
            selectState[SELECT_STATE_IMAGE] = false;
            /** 处理add相关事件 */
            ll_add.setVisibility(View.GONE);
            ib_add.setImageResource(R.drawable.pic_main_add_off);
            selectState[SELECT_STATE_ADD] = false;
            /**开启麦克风进行语音识别  同时关闭语音唤醒*/
            useWakeup = false;
            myService.getWakeupHelper().stopWakeup();
            myService.getVoiceRecognizeHelper().startRecognize();
        }
    }

    /**
     * 点击 图片 按钮时执行
     */
    private void onClickImage() {
        if (selectState[SELECT_STATE_IMAGE]) {
            selectState[SELECT_STATE_IMAGE] = false;
            ib_image.setImageResource(R.drawable.pic_main_image_off);
            ll_image.setVisibility(View.GONE);
        } else {
            selectState[SELECT_STATE_IMAGE] = true;
            ib_image.setImageResource(R.drawable.pic_main_image_on);
            ll_image.setVisibility(View.VISIBLE);
            /** 处理add相关事件 */
            ll_add.setVisibility(View.GONE);
            ib_add.setImageResource(R.drawable.pic_main_add_off);
            selectState[SELECT_STATE_ADD] = false;
            /** 处理microphone相关事件 */
            ll_microphone.setVisibility(View.GONE);
        }
    }

    /**
     * 点击 更多 按钮时执行
     */
    private void onClickAdd() {
        if (selectState[SELECT_STATE_ADD]) {
            selectState[SELECT_STATE_ADD] = false;
            ib_add.setImageResource(R.drawable.pic_main_add_off);
            ll_add.setVisibility(View.GONE);
        } else {
            selectState[SELECT_STATE_ADD] = true;
            ib_add.setImageResource(R.drawable.pic_main_add_on);
            ll_add.setVisibility(View.VISIBLE);
            /** 处理Image相关事件 */
            ll_image.setVisibility(View.GONE);
            ib_image.setImageResource(R.drawable.pic_main_image_off);
            selectState[SELECT_STATE_IMAGE] = false;
            /** 处理microphone相关事件 */
            ll_microphone.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 响应 点击设置键
     */
    private void onClickSetting() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        saveState();
        unbindService(this);
        super.onDestroy();
    }


    /**
     * 服务相关开始
     */
    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        /**获取服务*/
        myService = ((MyService.EchoServiceBinder) binder).getMyService();
        /**设置监听*/
        myService.setVoiceRecognizeEventListener(this);
        myService.setWakeupEventListener(this);
        /**开启语音唤醒*/
        myService.getWakeupHelper().startWakeup();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    /**
     * 语音识别接口开始
     */
    @Override
    public void onStartRecognize() {
        if (!useWakeup) {
            /**设置View 识别开始*/
            tv_microphone_tip.setText(R.string.recognize_start);
        }
    }

    @Override
    public void onStopRecognize() {

    }

    @Override
    public void onFinishRecognize(String result) {
        et_content.setText(result);
        Selection.setSelection(et_content.getText(), et_content.getText().length());
        if (useWakeup) {
            sendMessage(wakeupWord + result);
            et_content.setText("");
        } else {
            /**关闭语音识别  同时关闭View */
            myService.getVoiceRecognizeHelper().stopRecognize();
            /**隐藏麦克风提示  设置图片*/
            selectState[SELECT_STATE_MICROPHONE] = false;
            ib_microphone.setImageResource(R.drawable.pic_main_microphone_off);
            ll_microphone.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRecognize(String result) {
        if (useWakeup) {
            et_content.setText(wakeupWord + result);
            Selection.setSelection(et_content.getText(), et_content.getText().length());
        } else {
            et_content.setText(result);
            Selection.setSelection(et_content.getText(), et_content.getText().length());
            /**设置View  识别中*/
            tv_microphone_tip.setText(R.string.recognize_on);
        }
    }

    @Override
    public void onCancelRecognize() {

    }

    @Override
    public void onError(VoiceRecognizeError error) {
        if (!useWakeup) {
            /**设置View 识别异常*/
            tv_microphone_tip.setText(error.getMessage());
        }
    }

    /**
     * 语音识别接口结束
     */

    /**
     * 语音唤醒接口开始
     */
    @Override
    public void onWakeupStart() {

    }

    @Override
    public void onWakeupSuccess(String word) {
        /**保存关键词*/
        wakeupWord = word;
        /**设置语音唤醒成功标志*/
        useWakeup = true;
        /**开启语音识别*/
        myService.getVoiceRecognizeHelper().startRecognize();
    }

    @Override
    public void onWakeupStop() {

    }

    @Override
    public void onWakeupError(WakeupError error) {

    }
    /**
     * 语音唤醒接口关闭
     */
}
