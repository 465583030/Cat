package com.zhr.cat.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.zhr.cat.R;
import com.zhr.cat.domain.MyListViewAdapter;
import com.zhr.cat.domain.TextInfo;
import com.zhr.cat.domain.TextInfoDAO;
import com.zhr.cat.services.MyService;
import com.zhr.cat.tools.CircleImageView;
import com.zhr.cat.tools.camera.CameraHelper;

import java.util.ArrayList;
import java.util.List;

import static com.zhr.cat.tools.Utils.transBar;

/**
 * 聊天交互界面
 *
 * @author 赵浩燃
 */
public class ChatActivity extends Activity implements OnClickListener, ServiceConnection {
    /**
     * 保存应用启动有关的配置文件的名称
     */
    private static SharedPreferences sp;
    private static String openconfig = "openconfig";
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
    private TextInfoDAO dao;// TextInfo Database Access Object
    /**
     * 来自 客户的消息
     */
    private String textFromClient;
    /**
     * 相机SurfaceView
     */
    private SurfaceView cameraSurfaceView;
    private CameraHelper cameraHelper;
    private MyService myService;
//    IInteractWindow interactWindow;
    private Handler myHandler = new Handler() {// 处理消息的handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case CAT_PROCESS_FINISH:
                    currentTextInfo = new TextInfo();
                    currentTextInfo.setContent((String) msg.obj);
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

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        transBar(this);
        // 加载控件
        civ_user = (CircleImageView) findViewById(R.id.civ_user);
        ib_camera = (ImageButton) findViewById(R.id.ib_camera);
        ib_face = (ImageButton) findViewById(R.id.ib_face);
        ib_microphone = (ImageButton) findViewById(R.id.ib_microphone);
        ib_image = (ImageButton) findViewById(R.id.ib_image);
        ib_add = (ImageButton) findViewById(R.id.ib_add);
        ll_microphone = (LinearLayout) findViewById(R.id.ll_microphone);
        ll_image = (LinearLayout) findViewById(R.id.ll_image);
        ll_add = (LinearLayout) findViewById(R.id.ll_add);
        ll_camera_and_face = (LinearLayout) findViewById(R.id.ll_camera_and_face);
        ll_camera = (LinearLayout) findViewById(R.id.ll_camera);
        ll_face = (LinearLayout) findViewById(R.id.ll_face);
        et_content = (EditText) findViewById(R.id.et_content);
        bt_send = (Button) findViewById(R.id.bt_send);
        lv_content = (ListView) findViewById(R.id.lv_content);
        cameraSurfaceView = findViewById(R.id.svCamera);
        cameraHelper = new CameraHelper(cameraSurfaceView, this);
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
                if (s.length() > 0) {
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
                if (s.length() > 0) {
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
        textInfos = new ArrayList<TextInfo>();
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
        sp = getSharedPreferences(openconfig, Context.MODE_PRIVATE);
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
        sp = getSharedPreferences(openconfig, Context.MODE_PRIVATE);
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
        textFromClient = et_content.getText().toString().trim();
        et_content.setText(null);
        currentTextInfo = new TextInfo();
        currentTextInfo.setContent(textFromClient);
        currentTextInfo.setTime(System.currentTimeMillis());
        currentTextInfo.setType(1);
        textInfos.add(currentTextInfo);
        myAdapter.setList(textInfos);
        myAdapter.notifyDataSetChanged();
        dao.add(currentTextInfo.getContent(), currentTextInfo.getType(), currentTextInfo.getTime());
        currentTextInfo = null;
        new Thread() {
            @Override
            public synchronized void start() {
                super.start();
            }

            public void run() {
                Message msg = new Message();
                msg.what = CAT_PROCESS_FINISH;
                msg.obj = processContent(textFromClient);
                myHandler.sendMessage(msg);
            }

            private String processContent(String textFromClient) {
                return "接收到：" + textFromClient;
            }
        }.start();
    }

    /**
     * 点击 相机 按钮时执行
     */
    private void onClickCamera() {
        if (selectState[SELECT_STATE_CAMERA]) {
            selectState[SELECT_STATE_CAMERA] = false;
            ib_camera.setImageResource(R.drawable.pic_main_camera_off);
            cameraSurfaceView.setVisibility(View.GONE);
            ll_camera.setVisibility(View.GONE);
            if (ll_face.getVisibility() == View.GONE) {
                ll_camera_and_face.setVisibility(View.GONE);
            }
            cameraHelper.releaseCamera();
        } else {
            ll_camera_and_face.setVisibility(View.VISIBLE);
            selectState[SELECT_STATE_CAMERA] = true;
            ib_camera.setImageResource(R.drawable.pic_main_camera_on);
            cameraSurfaceView.setVisibility(View.VISIBLE);
            ll_camera.setVisibility(View.VISIBLE);
            cameraHelper.onResume();
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

    /**
     * 响应 点击设置键
     */
    private void onClickSetting() {
        startActivity(new Intent(this, SettingActivity.class));
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        myService = ((MyService.EchoServiceBinder) binder).getMyService();
        //   myService.talk("欢迎！");
        System.out.println("ChatActivity onService Connected. ");
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    @Override
    protected void onDestroy() {
        saveState();
        super.onDestroy();
        unbindService(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraHelper != null) {
            cameraHelper.releaseCamera();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cameraHelper != null) {
            cameraHelper.onResume();
        }
    }

}
