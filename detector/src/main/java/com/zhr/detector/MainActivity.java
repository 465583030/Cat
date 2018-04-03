package com.zhr.detector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    private Camera mCamera;
    private SurfaceView svCamera;
    private SurfaceHolder surfaceHolder;

    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            File tempImg = new File("/sdcard/iiitemp.jpg");
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                //压缩格式
                Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
                //压缩
                int quality = 100;
                OutputStream outputStream = new FileOutputStream(tempImg);
                Matrix matrix = new Matrix();
                matrix.setRotate(90);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.compress(format, quality, outputStream);
                outputStream.close();

               // Intent intent = new Intent(MainActivity.this, CaptureResultActivity.class);
              //  intent.putExtra("picPath", tempImg.getAbsolutePath());
               // startActivity(intent);
                //    MainActivity.this.finish();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
