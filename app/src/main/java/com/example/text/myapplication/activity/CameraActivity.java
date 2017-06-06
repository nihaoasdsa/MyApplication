package com.example.text.myapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.text.myapplication.R;
import com.example.text.myapplication.tool.HelpUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

//自定义相机页面
public class CameraActivity extends BaseActivity implements View.OnClickListener,
        SurfaceHolder.Callback {
    private static final String TAG = CameraActivity.class.getSimpleName();
    private static final int MEDIA_TYPE_IMAGE = 1;
    private Button switchCameraBtn, captureBtn, captureBtn2;
    private SurfaceView surfaceSv;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    // 0表示后置，1表示前置,暂时不需要
    private int cameraPosition = 1;
    private TextView camera_ok;
    private File pictureFile;
    private ImageView camera_back;
    private LinearLayout l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        findById();
        initData();
    }
    /**
     * 初始化view
     */
    private void findById() {
        switchCameraBtn = (Button) this.findViewById(R.id.id_switch_camera_btn);
        captureBtn = (Button) this.findViewById(R.id.id_capture_btn);
        captureBtn2 = (Button) findViewById(R.id.id_capture_btn2);
        captureBtn.setVisibility(View.VISIBLE);
        captureBtn2.setVisibility(View.GONE);
        captureBtn2.setOnClickListener(this);
        surfaceSv = (SurfaceView) this.findViewById(R.id.id_area_sv);
        camera_ok = (TextView) findViewById(R.id.camera_ok);
        camera_back = (ImageView) findViewById(R.id.camera_back);
        l = (LinearLayout) findViewById(R.id.l);
        l.setOnClickListener(this);
        camera_back.setOnClickListener(this);
        camera_ok.setOnClickListener(this);
        switchCameraBtn.setOnClickListener(this);
        captureBtn.setOnClickListener(this);
    }

    /**
     * 初始化相关data
     */
    private void initData() {
        // 获得句柄
        mHolder = surfaceSv.getHolder();
        // 添加回调
        mHolder.addCallback(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.checkCameraHardware(this) && (mCamera == null)) {
            // 打开camera
            mCamera = getCamera();
            if (mHolder != null) {
                setStartPreview(mCamera, mHolder);
            }
        }
    }
    //横竖屏切换

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        //暂时不需要
//        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
//            //横屏
//            mCamera.setDisplayOrientation(0);
//        }else{
        //竖屏
        mCamera.setDisplayOrientation(90);
//        }
    }

    private Camera getCamera() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            camera = null;
            Log.e(TAG, "Camera is not available (in use or does not exist)");
        }
        return camera;
    }

    @Override
    public void onPause() {
        super.onPause();
        /**
         * 记得释放camera，方便其他应用调用
         */
        releaseCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 释放mCamera
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();// 停掉原来摄像头的预览
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_switch_camera_btn:
                // 切换前后摄像头
                int cameraCount = 0;
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                cameraCount = Camera.getNumberOfCameras();// 得到摄像头的个数
                for (int i = 0; i < cameraCount; i++) {
                    Camera.getCameraInfo(i, cameraInfo);// 得到每一个摄像头的信息
                    if (cameraPosition == 1) {
                        // 现在是后置，变更为前置
                        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                            /**
                             * 记得释放camera，方便其他应用调用
                             */
                            releaseCamera();
                            // 打开当前选中的摄像头
                            mCamera = Camera.open(i);
                            // 通过surfaceview显示取景画面
                            setStartPreview(mCamera, mHolder);
                            cameraPosition = 0;
                            break;
                        }
                    } else {
                        // 现在是前置， 变更为后置
                        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                            /**
                             * 记得释放camera，方便其他应用调用
                             */
                            releaseCamera();
                            mCamera = Camera.open(i);
                            setStartPreview(mCamera, mHolder);
                            cameraPosition = 1;
                            break;
                        }
                    }

                }
                break;
            case R.id.id_capture_btn:
                try {
                    int PreviewWidth = 0;
                    int PreviewHeight = 0;
                    WindowManager wm=(WindowManager)getSystemService(Context.WINDOW_SERVICE);//获取窗口管理器
                    Display display=wm.getDefaultDisplay();//获得窗口里面的屏幕
                    Camera.Parameters parameters=mCamera.getParameters();
                    //选择合适的预览尺寸
                    List<Camera.Size>sizeList=parameters.getSupportedPreviewSizes();
                    //如果sizeList只有一个就不需要做什么了
                    if(sizeList.size()>1){
                        Iterator<Camera.Size>itor=sizeList.iterator();
                        while ((itor.hasNext())){
                            Camera.Size cur=itor.next();
                            if(cur.width>=PreviewWidth&&cur.height>=PreviewHeight){
                                PreviewHeight=cur.height;
                                PreviewWidth=cur.width;
                                break;
                            }
                        }
                    }
                    parameters.setPreviewSize(PreviewWidth,PreviewHeight);//获得摄像区域的大小
                    parameters.setPictureFormat(ImageFormat.JPEG);//设置照片输出的格式
                    parameters.set("jpeg-quality",85);//设置照片质量
                    parameters.setPictureSize(PreviewWidth,PreviewHeight);//设置拍出来的屏幕大小
                    mCamera.setParameters(parameters);//把上面的设置赋给摄像头
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);// 自动对焦
                    mCamera.takePicture(null, null, picture);
                    captureBtn.setVisibility(View.GONE);
                    captureBtn2.setVisibility(View.VISIBLE);
                    l.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    Log.d(TAG, "Error accessing file: " + e.getMessage());
                }
                break;
            case R.id.camera_ok:
                if (pictureFile != null) {
                    returnResult(pictureFile);
                }
                break;
            case R.id.l:
                if (pictureFile != null) {
                    returnResult(pictureFile);
                }
                break;
            case R.id.id_capture_btn2:
                l.setVisibility(View.GONE);
                captureBtn.setVisibility(View.VISIBLE);
                captureBtn2.setVisibility(View.GONE);
                releaseCamera();
                initData();
                if (this.checkCameraHardware(this) && (mCamera == null)) {
                    // 打开camera
                    mCamera = getCamera();
                    if (mHolder != null) {
                        setStartPreview(mCamera, mHolder);
                    }
                }
                break;
            case R.id.camera_back:
                finish();
                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setStartPreview(mCamera, mHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
// If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        setStartPreview(mCamera, mHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
// 当surfaceview关闭时，关闭预览并释放资源
        /**
         * 记得释放camera，方便其他应用调用
         */
        releaseCamera();
        holder = null;
        surfaceSv = null;
    }

    /**
     * 创建png图片回调数据对象
     */
    Camera.PictureCallback picture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                Log.d(TAG,
                        "Error creating media file, check storage permissions: ");
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
//                returnResult(pictureFile);
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = HelpUtil.getDateFormatString(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".png");
        } else {
            return null;
        }
        return mediaFile;
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * activity返回式返回拍照图片路径
     *
     * @param mediaFile
     */
    private void returnResult(File mediaFile) {
        Intent intent = new Intent();
        if (Uri.fromFile(mediaFile) != null)
            intent.setData(Uri.fromFile(mediaFile));
        this.setResult(RESULT_OK, intent);
        this.finish();
    }

    /**
     * 设置camera显示取景画面,并预览
     *
     * @param camera
     */
    private void setStartPreview(Camera camera, SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
            mCamera.setDisplayOrientation(90);
        } catch (IOException e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
}
