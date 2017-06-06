package com.example.text.myapplication.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.text.myapplication.R;
import com.example.text.myapplication.domain.HomeLine;
import com.example.text.myapplication.domain.Line;

import java.util.ArrayList;
import java.util.List;


/**
 * 2017.4.6jiangpan
 * 首页适配器
 */
public class HomeListViewAdapter extends BaseAdapter {
//    private List<String> views = new ArrayList<String>();
    private List<HomeLine>homeLines=new ArrayList<HomeLine>();
    private Context context;
    private ImageView home_img, home_img2;
    private TextView home_item_title, home_item_content, home_item_date;
    private String number;
    public HomeListViewAdapter(Context context, List<HomeLine>homeLines) {
        this.context = context;
        this.homeLines = homeLines;
    }

    @Override
    public int getCount() {
        return homeLines.size();
    }

    private LayoutInflater mInflater;

    @Override
    public Object getItem(int position) {
        if (homeLines != null) {
            return homeLines.get(position);
        }
        return null;

    }

    private View.OnClickListener listener;


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final HomeLine homeLine = (HomeLine) getItem(position);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.home_llistview_item, null);
        home_img = (ImageView) convertView.findViewById(R.id.home_img);
        home_img2 = (ImageView) convertView.findViewById(R.id.home_img2);
        home_item_title = (TextView) convertView.findViewById(R.id.home_item_title);
        home_item_content = (TextView) convertView.findViewById(R.id.home_item_content);
        home_item_date = (TextView) convertView.findViewById(R.id.home_item_date);
        if (homeLines.get(position) != null)
            home_item_title.setText(homeLine.getView());
        home_item_content.setText(homeLine.getFile_number()+"个文件");
        if(homeLine.getFile_number()==0){
            home_img.setVisibility(View.VISIBLE);
            home_img2.setVisibility(View.GONE);
        }else {
            home_img.setVisibility(View.GONE);
            home_img2.setVisibility(View.VISIBLE);
        }
        return convertView;
    }


}