package com.zhr.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ZL on 2018/4/17.
 */

public class CatFace extends View {
    Paint paint;
    int color = Color.rgb(20, 200, 226);
    public CatFace(Context context) {
        super(context);

    }

    public CatFace(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(); //
        this.paint.setColor(color);
        this.paint.setAntiAlias(true); //消除锯齿
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
        color = Color.rgb(20, 200, 226);
        paint.setColor(color);
        int padding = 30;//px
        int center = getHeight() / 2 > getWidth() / 2 ? getWidth() / 2 : getHeight() / 2;
        int faceWidth = (int) (center * 0.9) - padding;
        int faceHeight = (int) (center * 0.815) - padding;
        int ringWidth = dip2px(getContext(), 10);   //圆环宽度
        RectF rect = new RectF();
        int eyeMarginCenterH = dip2px(getContext(), 35);   //两眼中心距离布局中心的水平距离
        int eyeMarginCenterV = dip2px(getContext(), 10);   //两眼中心距离布局中心的垂直距离 上正下负
        int eyeWidth = dip2px(getContext(), 16);//眼睛宽度
        int eyeHeight = dip2px(getContext(), 28);//眼睛高度
        int mouthWidth = dip2px(getContext(), 30);//眼睛宽度
        int mouthHeight = dip2px(getContext(), 18);//眼睛高度
        int mouthMarginCenterV = dip2px(getContext(), 30);   //两眼中心距离布局中心的垂直距离
        int earWidth = dip2px(getContext(), 83);
        int earHeight = dip2px(getContext(), 46);
        float earDegrees = 55;
        int beardLength = dip2px(getContext(), 40);
        double intervalDegrees = 15;
        double startDegree = 20;
        int beardNumbers = 3;
        /**左眼*/
        rect.set(center - eyeMarginCenterH - eyeWidth / 2, center - eyeHeight / 2 - eyeMarginCenterV, center - eyeMarginCenterH + eyeWidth / 2, center + eyeHeight / 2 - eyeMarginCenterV);
        canvas.drawOval(rect, paint);
        /**右眼*/
        rect.set(center + eyeMarginCenterH - eyeWidth / 2, center - eyeHeight / 2 - eyeMarginCenterV, center + eyeMarginCenterH + eyeWidth / 2, center + eyeHeight / 2 - eyeMarginCenterV);
        canvas.drawOval(rect, paint);
        /**嘴*/
        rect.set(center - mouthWidth / 2, center - mouthHeight / 2 + mouthMarginCenterV, center + mouthWidth / 2, center + mouthHeight / 2 + mouthMarginCenterV);
        canvas.drawOval(rect, paint);
        /**画耳朵*/
        float xLeft = (float) Math.abs(faceHeight * faceWidth / Math.sqrt(faceHeight * faceHeight + faceWidth * faceWidth) - center);
        float yLeft = xLeft;
        float xRight = 2 * center - xLeft;
        float yRight = yLeft;
        float yBottom = (float) Math.abs(faceHeight * faceWidth / Math.sqrt(faceHeight * faceHeight + faceWidth * faceWidth) + center);
        this.paint.setStyle(Paint.Style.STROKE);  //绘制空心圆或 空心矩形
        this.paint.setStrokeWidth(ringWidth);
        /**左耳*/
        rect.set(xLeft - earWidth / 2, yLeft - earHeight / 2, xLeft + earWidth / 2, yLeft + earHeight / 2);// 设置个新的长方形，扫描测量
        canvas.rotate(earDegrees, xLeft, yLeft);
        canvas.drawArc(rect, 90, 180, false, paint);
        canvas.rotate(-earDegrees, xLeft, yLeft);
        /**右耳*/
        rect.set(xRight - earWidth / 2, yRight - earHeight / 2, xRight + earWidth / 2, yRight + earHeight / 2);// 设置个新的长方形，扫描测量
        canvas.rotate(-earDegrees, xRight, yRight);
        canvas.drawArc(rect, 270, 180, false, paint);
        canvas.rotate(earDegrees, xRight, yRight);
        /**脸*/
        rect.set(center - faceWidth, center - faceHeight, center + faceWidth, center + faceHeight);// 设置个新的长方形，扫描测量
        canvas.drawOval(rect, paint);
        /**绘制胡须*/

        double[] k = new double[beardNumbers];
        float[] bottomLeftX = new float[beardNumbers];
        float[] bottomLeftY = new float[beardNumbers];
        float[] bottomRightX = new float[beardNumbers];
        float[] bottomRightY = new float[beardNumbers];
        for (int i = 0; i < beardNumbers; i++) {
            double deg = startDegree + i * intervalDegrees;
            k[i] = Math.tan(Math.toRadians(deg));
            bottomLeftX[i] = (float) (center - faceWidth * faceHeight / Math.sqrt(faceHeight * faceHeight + k[i] * k[i] * faceWidth * faceWidth));
            bottomRightX[i] = (float) (center + faceWidth * faceHeight / Math.sqrt(faceHeight * faceHeight + k[i] * k[i] * faceWidth * faceWidth));
            bottomRightY[i] = bottomLeftY[i] = -(float) ((bottomLeftX[i] - center) * k[i] - center);
            canvas.drawPoint(bottomLeftX[i], bottomLeftY[i], paint);
            canvas.drawPoint(bottomRightX[i], bottomRightY[i], paint);
            drawBreads(canvas, paint, beardLength, bottomLeftX[i], bottomLeftY[i], deg);
            drawBreads(canvas, paint, beardLength, bottomRightX[i], bottomRightY[i], -deg);
        }


        this.paint.setStyle(Paint.Style.FILL);
    }

    public void drawBreads(Canvas canvas, Paint paint, int beardLength, float x, float y, double deg) {
        float sin = (float) Math.sin(Math.toRadians(deg));
        float cos = (float) Math.cos(Math.toRadians(deg));
        int half=beardLength/2;
        canvas.drawLine(x - half* cos, y + half * sin, x +half * cos, y - half * sin, paint);
    }

    /* 根据手机的分辨率从 dp 的单位 转成为 px(像素) */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
