package com.zhr.cat.tools.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.zhr.cat.services.ServicesHelper;
import com.zhr.cat.tools.RunTimes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ZL on 2018/4/4.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera camera;
    private int cameraId;
    private static String TAG = "CameraPreview";

    public CameraPreview(Context context) {
        super(context);
        camera = getFrontCamera();
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
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.autoFocus(null);
            }
        });
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(90);
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            setCameraSize(parameters);

            camera.startPreview();

        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            camera.setPreviewDisplay(mHolder);
            camera.startPreview();

        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }


    /**
     * 获取系统相机
     */
    private static Camera getCamera(int direction) {
        Camera camera;
        try {
            camera = Camera.open(direction);
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


    public static void setCameraSize(Camera.Parameters parameters) {
        //preview
        List<Camera.Size> previewSizes=parameters.getSupportedPreviewSizes();
        List<WrapCameraSize> wrapCameraPreviewSizes = new ArrayList<>(previewSizes.size());
        for (Camera.Size size:previewSizes             ) {
            WrapCameraSize wrapCameraSize = new WrapCameraSize();
            wrapCameraSize.setWidth(size.width);
            wrapCameraSize.setHeight(size.height);
            wrapCameraSize.setD(size.width+size.height);
            wrapCameraPreviewSizes.add(wrapCameraSize);
        }
        WrapCameraSize minCameraPreviewSize = Collections.min(wrapCameraPreviewSizes);
        parameters.setPreviewSize(minCameraPreviewSize.getWidth(), minCameraPreviewSize.getHeight());
        //picture
        List<Camera.Size> pictureSizes=parameters.getSupportedPictureSizes();
        List<WrapCameraSize> wrapCameraPicSizes = new ArrayList<>(pictureSizes.size());
        for (Camera.Size size:pictureSizes             ) {
            WrapCameraSize wrapCameraSize = new WrapCameraSize();
            wrapCameraSize.setWidth(size.width);
            wrapCameraSize.setHeight(size.height);
            wrapCameraSize.setD(size.width+size.height);
            wrapCameraPicSizes.add(wrapCameraSize);
        }
        WrapCameraSize minCameraPictureSize = Collections.min(wrapCameraPicSizes);
        //设置参数
        parameters.setPreviewSize( minCameraPreviewSize.getHeight(),minCameraPreviewSize.getWidth());
        parameters.setPictureSize( minCameraPictureSize.getHeight(),minCameraPictureSize.getWidth());
        Log.e(TAG, "setCameraSize: width:"+minCameraPreviewSize.getWidth()+"  height:"+minCameraPreviewSize.getHeight() );
    }
}
