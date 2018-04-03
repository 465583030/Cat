package com.zhr.cat.tools.camera;

import android.support.annotation.NonNull;

/**
 * Created by ZL on 2018/4/3.
 */

class WrapCameraSize implements Comparable<WrapCameraSize>{
    private int height;
    private int width;
    private int d;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    @Override
    public int compareTo(@NonNull WrapCameraSize wrapCameraSize) {
        return d-wrapCameraSize.getD();
    }
}
