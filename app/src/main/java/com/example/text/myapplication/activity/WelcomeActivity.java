package com.example.text.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.text.myapplication.R;

//2017.4欢迎页
public class WelcomeActivity extends BaseActivity {
    //想做一个动画效果的欢迎页，但是没有网络，资料都查不了，很难受呀
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
        new Handler().postDelayed(new Runnable() { //这里相当于Handler的第一个参数
            @Override
            public void run() {
                Intent mainIntent = new Intent(WelcomeActivity.this, MainActivity.class);
                WelcomeActivity.this.startActivity(mainIntent);
                WelcomeActivity.this.finish();
            }
        }, 2000); //这里的1000指1000毫秒即1秒
    }
}
