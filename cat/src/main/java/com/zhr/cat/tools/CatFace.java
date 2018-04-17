package com.zhr.cat.tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ZL on 2018/4/17.
 */

public class CatFace extends View {
    Paint paint;

    public CatFace(Context context) {
        super(context);
    }

    public CatFace(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(); //
        paint.setColor(Color.RED);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
    }

    public CatFace(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //...自定义绘制
    }
}
