package com.zhr.cat.tools.camera;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.zhr.cat.services.ServicesHelper;
import com.zhr.cat.tools.RunTimes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ZHR on 2017/11/23.
 * 相机帮助类
 */

public class CameraHelper implements SurfaceHolder.Callback {
    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private static final String TAG = "CameraHelper";
    private Activity activity;
    private int cameraId;

    public CameraHelper(SurfaceView surfaceView, Activity activity) {
        this.surfaceView = surfaceView;
        this.activity = activity;
        this.camera = getFrontCamera();
        cameraId = 0;
        if (camera == null) {
            camera = getBackCamera();
            cameraId = 1;
            if (camera == null) {
                if (new RunTimes(ServicesHelper.getService()).isFirstOpen()) {
                    ServicesHelper.getService().talk("相机被占用或者设备无相机");
                }
            }
        }
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        this.surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.autoFocus(null);
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        detectFace();
        setCameraPreview(camera, activity, surfaceView);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        camera.stopPreview();
        detectFace();
        setCameraPreview(camera, activity, surfaceView);
    }

    private void detectFace() {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        releaseCamera();
    }

    public void onResume() {
        if (camera == null) {
            this.camera = getFrontCamera();
            if (camera == null) {
                camera = getBackCamera();
            }
            if (surfaceHolder != null) {
                detectFace();
                setCameraPreview(camera, activity, surfaceView);
            }
        }
    }

    /**
     * 获取系统相机
     */
    private static Camera getCamera(int direction) {
        if (direction != 0 && direction != 1) {
            return null;
        }
        Camera camera;
        try {
            camera = Camera.open(direction);
            //输出相机的长宽比
            Camera.Parameters parameters = camera.getParameters();
            List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
            Log.e("Camera", "Camera.Size" + sizes.size());
            for (Camera.Size s : sizes) {
                Log.e("Camera", "width:" + s.width + "  height:" + s.height + "   w/h:" + s.width * 1.0 / s.height + "   h/w:" + s.height * 1.0 / s.width);
            }
        } catch (Exception e) {
            camera = null;
            e.printStackTrace();
        }
        return camera;
    }

    /**
     * 获取后置摄像头
     */
    private static Camera getBackCamera() {
        return getCamera(0);
    }

    /***
     * 获取前置摄像头
     *
     */
    private static Camera getFrontCamera() {
        return getCamera(1);
    }

    /**
     * 设置相机预览界面
     */
    private static void setCameraPreview(Camera camera, Activity activity, SurfaceView surfaceView) {
        try {
            System.err.println("CameraHelper.setCameraPreview");
            Log.e(TAG, "setCameraPreview:设置相机预览界面 ");
            camera.setPreviewDisplay(surfaceView.getHolder());
            /**调整系统Camera预览角度*/
            setCameraDisplayOrientation(activity, 0, camera, surfaceView);
            /**设置自动聚焦*/
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            /**开始预览*/
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放相机
     */
    public void releaseCamera() {
        if (camera != null) {
            surfaceHolder.removeCallback(this);
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.lock();
            camera.release();
            camera = null;
        }
    }

    public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera, SurfaceView surfaceView) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Log.e(TAG, "setCameraDisplayOrientation: rotation" + rotation);
        int degree = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degree) % 360;
            // 就是摄像头需要顺时针转过多少度才能恢复自然方向
            result = (360 - result) % 360;
        } else { // back-facing
            result = (info.orientation - degree + 360) % 360;
        }
        camera.setDisplayOrientation(result);
        switch (result) {
            case 0:
            case 180:
                setCameraSize(camera.getParameters(), surfaceView.getWidth(), surfaceView.getHeight());
                break;
            case 90:
            case 270:
                setCameraSize(camera.getParameters(), surfaceView.getHeight(), surfaceView.getWidth());
                break;
        }
    }

    public static void setCameraSize(Camera.Parameters parameters, int width, int height) {
        Map<String, List<Size>> allSizes = new HashMap<>();
        String typePreview = "typePreview";
        String typePicture = "typePicture";
        allSizes.put(typePreview, parameters.getSupportedPreviewSizes());
        allSizes.put(typePicture, parameters.getSupportedPictureSizes());
        Iterator iterator = allSizes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<Size>> entry = (Map.Entry<String, List<Size>>) iterator.next();
            List<Size> sizes = entry.getValue();
            if (sizes == null || sizes.isEmpty()) continue;
            ArrayList<WrapCameraSize> wrapCameraSizes = new ArrayList<>(sizes.size());
            for (Size size : sizes) {
                WrapCameraSize wrapCameraSize = new WrapCameraSize();
                wrapCameraSize.setWidth(size.width);
                wrapCameraSize.setHeight(size.height);
                wrapCameraSize.setD(Math.abs(size.width / size.height - width / height));
                if (size.width / size.height == width / height) {
                    if (typePreview.equals(entry.getKey())) {
                        parameters.setPreviewSize(size.width, size.height);
                    } else if (typePicture.equals(entry.getKey())) {
                        parameters.setPictureSize(size.width, size.height);
                    }
                    Log.d(TAG, "best size: width=" + size.width + ";height=" + size.height);
                    break;
                }
                wrapCameraSizes.add(wrapCameraSize);
            }
            // Log.d(TAG, "wrapCameraSizes.size()=" + wrapCameraSizes.size());
            Size resultSize = null;
            if (typePreview.equals(entry.getKey())) {
                resultSize = parameters.getPreviewSize();
            } else if (typePicture.equals(entry.getKey())) {
                resultSize = parameters.getPictureSize();
            }
            if (resultSize == null || (resultSize.width != width && resultSize.height != height)) {
                //找到相机Preview Size 和 Picture Size中最适合的大小
                if (wrapCameraSizes.isEmpty()) continue;
                WrapCameraSize minCameraSize = Collections.min(wrapCameraSizes);
                while (!(minCameraSize.getWidth() >= width && minCameraSize.getHeight() >= height)) {
                    wrapCameraSizes.remove(minCameraSize);
                    if (wrapCameraSizes.isEmpty()) break;
                    minCameraSize = null;
                    minCameraSize = Collections.min(wrapCameraSizes);
                }
                Log.d(TAG, "surfaceView: width=" + width + ";height=" + height);
                Log.d(TAG, "best min size: width=" + minCameraSize.getWidth() + ";height=" + minCameraSize.getHeight());
                if (typePreview.equals(entry.getKey())) {
                    parameters.setPreviewSize(minCameraSize.getWidth(), minCameraSize.getHeight());
                } else if (typePicture.equals(entry.getKey())) {
                    parameters.setPictureSize(minCameraSize.getWidth(), minCameraSize.getHeight());
                }
            }

            iterator.remove();
        }
        parameters.setPreviewSize(176,144);
        parameters.setPictureSize(176,144);
    }

}
