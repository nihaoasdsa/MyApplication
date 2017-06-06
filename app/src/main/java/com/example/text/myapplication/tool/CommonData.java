package com.example.text.myapplication.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.example.text.myapplication.domain.BitmapList;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jiangpan on 2017/4/10.
 */

public class CommonData {
    public static HashMap<String, Bitmap> imageBitmaps = new HashMap<String, Bitmap>();//拍照图片
    public static HashMap<String, String> myLatitudelist = new HashMap<String, String>();//经度
    public static int checkedDevicePosition;
    public static int checkedDeviceId;
    public static HashMap<String, String> altitude = new HashMap<String, String>();//海拔
    public static HashMap<String, String> myLongitudelist = new HashMap<String, String>();//
    public static String mPhotoPath;//图片地址
    public static String name;//用户姓名
    public static String photo_size;//图片保存在本地的尺寸
    public static String line_name;//线路名称
    public static int id;//新建文件夹还是文件的标识
    public static Bitmap convertToBitmapNew(String defaultImagePath,int w, int h) {//设置图片的大小

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeFile(defaultImagePath, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;

        scaleWidth = ((float) w) / width;
        scaleHeight = ((float) h) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(
                BitmapFactory.decodeFile(defaultImagePath, null));

        Bitmap bitmap = Bitmap.createBitmap(weak.get(), 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }
}
