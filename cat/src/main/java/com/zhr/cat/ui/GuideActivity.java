package com.zhr.cat.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhr.cat.R;
import com.zhr.cat.services.MyService;
import com.zhr.cat.services.ServicesHelper;
import com.zhr.cat.tools.Constant;
import com.zhr.cat.tools.RunTimes;
import com.zhr.cat.tools.Utils;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity implements OnClickListener, ServiceConnection {
    private MyService myService;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == Constant.SERVICE_CONNECTED_GUIDE_ACTIVITY) {
                if (!runTimes.isFirstOpen()) {
                    openChatActivity();
                    finish();
                }
            }
        }
    };
    /* 引导界面 */
    private ViewPager viewpager;//
    private List<View> viewList;// 存储引导布局的容器
    /**
     * 引导布局的数量
     */
    private int guide_images_ids[] = {R.drawable.pic_welcome_first, R.drawable
            .pic_welcome_second, R.drawable.pic_welcome_third};
    private int dot_ids[] = {R.id.iv_dot1, R.id.iv_dot2, R.id.iv_dot3, R.id.iv_dot4};
    private ImageView iv_cur_dot;
    private int offset; // 位移量
    private int curPos = 0; // 记录当前的位置
    /* 开启界面 */
    private TextView tv_version_name;
    private RunTimes runTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runTimes = new RunTimes(this);
        if (runTimes.isFirstOpen()) {/** 判断是否是第一次启动,第一次启动 */
            System.out.println("First Open....");
            setContentView(R.layout.activity_guide);
            /**开启服务*/
            startMyService();
            /**继续引导*/
            continueGuide();

        } else {/**不是第一次启动*/
            setContentView(R.layout.activity_open);
            System.out.println("Not First Open....");
            /**运行次数加一*/
            runTimes.setRunTimes();

            tv_version_name = (TextView) findViewById(R.id.tv_version_name);
            tv_version_name.setText(Utils.getVersionName(this));
            /**开启服务*/
            startMyService();
        }
    }

    private void startMyService() {
        Intent intent = new Intent(getApplicationContext(), MyService.class);
        startService(intent);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        /**获取Services*/
        myService = ((MyService.EchoServiceBinder) binder).getMyService();
        /**保存Services引用*/
        ServicesHelper.setMyService(myService);
        Message message = new Message();
        message.what = Constant.SERVICE_CONNECTED_GUIDE_ACTIVITY;
        handler.sendMessage(message);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    private void continueGuide() {
        //初始化控件
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        //初始化引导界面布局
        viewList = new ArrayList<View>();
        LayoutInflater inflater = LayoutInflater.from(this);
        ImageView iv_guide = null;
        //初始化length-1个布局
        for (int i = 0; i < guide_images_ids.length; i++) {
            iv_guide = (ImageView) View.inflate(this, R.layout.guide_iv, null);
            iv_guide.setImageResource(guide_images_ids[i]);
            viewList.add(iv_guide);
        }
        //填充最后一个引导布局
        viewList.add(inflater.inflate(R.layout.guide_4, null));
        /**
         * ViewPager适配器，用来绑定数据和view
         */
        viewpager.setAdapter(new PagerAdapter() {

            /**
             * 初始化position位置的界面
             */
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(viewList.get(position));
                return viewList.get(position);
            }

            /**
             * 销毁position位置的界面
             */
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ((ViewPager) container).removeView(viewList.get(position));
            }

            /**
             * 判断是否由对象生成界面
             */
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            /**
             * 获得当前界面数
             */
            @Override
            public int getCount() {
                return viewList.size();
            }
        });
        /**设置监听器*/
        viewpager.addOnPageChangeListener(new OnPageChangeListener() {
            /**
             * 滑动状态改变时调用
             */
            @Override
            public void onPageSelected(int position) {
                int pos = position % viewList.size();

                moveCursorTo(pos);

                if (pos == viewList.size() - 1) {// 到最后一张了
                    runTimes.setRunTimes();/* 设置软件运行的次数 */
                } else if (curPos == viewList.size() - 1) {
                }
                curPos = pos;
            }

            /**
             * 当前页面滑动时调用
             *
             * offset Value from [0, 1) indicating the offset from the page
             * at position.
             */
            @Override
            public void onPageScrolled(int position, float offset, int arg2) {
            }

            /**
             * 新的页面被选中时调用
             */
            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        iv_cur_dot = (ImageView) findViewById(R.id.iv_cur_dot);
        iv_cur_dot.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            public boolean onPreDraw() {
                // 获取ImageView的宽度也就是点图片的宽度
                offset = iv_cur_dot.getWidth();
                return true;
            }
        });
        ImageView imageView[] = new ImageView[dot_ids.length];
        for (int i = 0; i < dot_ids.length; i++) {
            imageView[i] = (ImageView) findViewById(dot_ids[i]);
            imageView[i].setOnClickListener(this); // 给每个小点设置监听
            imageView[i].setTag(i); // 设置位置tag，方便取出与当前位置对应
        }
    }

    /**
     * 打开聊天交流界面
     */
    private void openChatActivity() {
        Intent intent = new Intent(GuideActivity.this, ChatActivity.class);
        startActivity(intent);
    }

    /**
     * 移动指针到相邻的位置 动画
     *
     * @param position 指针的索引值
     */
    private void moveCursorTo(int position) {
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation tAnim = new TranslateAnimation(offset * curPos, offset * position, 0, 0);
        animationSet.addAnimation(tAnim);
        animationSet.setDuration(300);
        animationSet.setFillAfter(true);
        iv_cur_dot.startAnimation(animationSet);
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag(); // 对应ImageView位置的tag，与当前位置对应
        if (position >= 0 && position < dot_ids.length) {
            viewpager.setCurrentItem(position);
            moveCursorTo(position);

            if (position == viewList.size() - 1) {// 到最后一张了
                runTimes.setRunTimes();/** 设置软件运行的次数 */
            } else if (curPos == viewList.size() - 1) {
            }
            curPos = position;
        }
    }

    /**
     * 引导页面中，最后一个页面，响应点击开启按钮时间
     *
     * @param view
     */
    public void OnClickOpenChatActivity(View view) {
        openChatActivity();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
