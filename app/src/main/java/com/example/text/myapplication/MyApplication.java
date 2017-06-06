package com.example.text.myapplication;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.util.DisplayMetrics;

import com.baidu.mapapi.SDKInitializer;

import java.util.ArrayList;
import java.util.List;

import baidu.location.service.LocationService;

/**
 * Created by jiangpan on 2017/4/28.
 */

public class MyApplication extends Application {
    public static final String ONLY_WIFI_LOAD_IMAGE = "onlyWifiLoadImage";
    /**
     * 当前应用的实例
     */
    private static MyApplication mInstance;
    /**
     * 图片参数数据
     */
    private SharedPreferences imageOptionsSP;
    /**
     * 获取当前应用程序的实例
     *
     * @return 应用程序的实例
     */

    /**
     * 只有WIFI时才加载图片
     */
    public boolean m_bOnlyWifiLoadImage = true;
    /**
     * 当前手机的高度
     */
    public static int height;
    /**
     * 当前手机的宽度
     */
    /**
     * 当前手机的像素密度
     */
    public static float density;
    /**
     * 当前手机的像素密度DPI
     */
    public static int densityDIP;
    public static int width;
    public LocationService locationService;
    public Vibrator mVibrator;
    public static MyApplication getInstance() {
        return mInstance;
    }
    private List<Activity> oList;//用于存放所有启动的Activity的集合
    @Override
    public void onCreate() {
        super.onCreate();
        oList = new ArrayList<Activity>();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        density = displayMetrics.density;
        densityDIP = displayMetrics.densityDpi;
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        try {
            SDKInitializer.initialize(this);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
    }
    /**
     * 添加Activity
     */
    public void addActivity_(Activity activity) {
// 判断当前集合中不存在该Activity
        if (!oList.contains(activity)) {
            oList.add(activity);//把当前Activity添加到集合中
        }
    }

    /**
     * 销毁单个Activity
     */
    public void removeActivity_(Activity activity) {
//判断当前集合中存在该Activity
        if (oList.contains(activity)) {
            oList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    /**
     * 销毁所有的Activity
     */
    public void removeALLActivity_() {
        //通过循环，把集合中的所有Activity销毁
        for (Activity activity : oList) {
            activity.finish();
        }
    }
    /**
     * 返回图片的参数数据
     *
     * @return
     */

    protected SharedPreferences getImageOptionsSharedPreferences() {
        if (imageOptionsSP == null) {
            imageOptionsSP = getSharedPreferences("imageOptions", Context.MODE_PRIVATE);
        }
        return imageOptionsSP;
    }
    public boolean isOnlyWifiLoadImage() {
        if (imageOptionsSP == null) {
            SharedPreferences imagePrefrences = getImageOptionsSharedPreferences();
            m_bOnlyWifiLoadImage = imagePrefrences.getBoolean(ONLY_WIFI_LOAD_IMAGE, false);
        }
        return m_bOnlyWifiLoadImage;
    }
}
