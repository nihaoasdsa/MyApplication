package com.example.text.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.text.myapplication.R;
import com.example.text.myapplication.tool.CommonData;

//大图显示页面jiangpan 2017.4.12
public class LargeActivity extends BaseActivity {
    private ImageView large_iv,large_return;
    private TextView latitude1,longitude1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large);
        large_iv=(ImageView) findViewById(R.id.large_iv);
        latitude1=(TextView)findViewById(R.id.latitude1);
        longitude1=(TextView)findViewById(R.id.longitude1);
        byte [] bis=getIntent().getByteArrayExtra("bitmap");
        String myLatitudelist=getIntent().getStringExtra("myLatitudelist");
        String myLongitudelist=getIntent().getStringExtra("myLongitudelist");
        Bitmap bitmap=BitmapFactory.decodeByteArray(bis, 0, bis.length);
        large_iv.setImageBitmap(bitmap);
        latitude1.setText("经度："+myLatitudelist);
        longitude1.setText("纬度："+myLongitudelist);
        large_return=(ImageView)findViewById(R.id.large_return);
        large_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
