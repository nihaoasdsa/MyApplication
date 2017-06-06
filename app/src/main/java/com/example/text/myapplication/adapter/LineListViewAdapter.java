package com.example.text.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.text.myapplication.R;
import com.example.text.myapplication.domain.HomeLine;
import com.example.text.myapplication.domain.Line;
import com.example.text.myapplication.tool.CommonData;
import com.example.text.myapplication.tool.ImageUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 2017.4.10jiangpan
 * 具体线路适配器
 */
public class LineListViewAdapter extends BaseAdapter {
    private List<Line> lineList = new ArrayList<Line>();//线路数据
    private Context context;
    private ImageView line_listview_item_img1, line_listview_item_img2, line_listview_item_img3, line_listview_item_img4, line_listview_item_img5, line_listview_item_img6, line_listview_item_img7;
    private TextView line_item_title, line_listview_item_name1, line_listview_item_name2, line_listview_item_name3, line_listview_item_name4, line_listview_item_name5, line_listview_item_name6, line_listview_item_name7, latitude1, longitude1, latitude2, longitude2, latitude3, longitude3, latitude4, longitude4, latitude5, longitude5, latitude6, longitude6, latitude7, longitude7;
    private Click click;
    private String file_id;
    private ImageView home_img, home_img2;
    private TextView home_item_title, home_item_content, home_item_date;
    private LinearLayout l1, l2;
    private List<String> dbFileIdlist = new ArrayList<String>();
    private String dbFileId;
    public LineListViewAdapter(Context context, List<Line> lineList,List<String> dbFileIdlist) {
        this.context = context;
        this.lineList = lineList;
        this.dbFileIdlist = dbFileIdlist;
    }

    public static interface Click {
        void click(Object obj, View view);
    }

    @Override
    public int getCount() {
        return lineList.size();
        //        return 6;
    }

    private LayoutInflater mInflater;

    @Override
    public Object getItem(int position) {
        if (lineList != null) {
            return lineList.get(position);
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
        final Line line = (Line) getItem(position);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.line_llistview_item, null);//文件页面
        l1 = (LinearLayout) convertView.findViewById(R.id.l1);
        l2 = (LinearLayout) convertView.findViewById(R.id.l2);
        home_img = (ImageView) convertView.findViewById(R.id.home_img);
        home_img2 = (ImageView) convertView.findViewById(R.id.home_img2);
        home_item_title = (TextView) convertView.findViewById(R.id.home_item_title);
        home_item_content = (TextView) convertView.findViewById(R.id.home_item_content);
        home_item_date = (TextView) convertView.findViewById(R.id.home_item_date);
//        if (lineList.get(position) != null) {
//            for(int i=0;i<dbFileIdlist.size();i++){
//                dbFileId=dbFileIdlist.get(i);
                if (line.getView()!= null) {
                    l2.setVisibility(View.VISIBLE);
                    home_item_title.setText(line.getView());
                }
//            }

//        }
        line_listview_item_img1 = (ImageView) convertView.findViewById(R.id.line_listview_item_img1);
        line_listview_item_img2 = (ImageView) convertView.findViewById(R.id.line_listview_item_img2);
        line_listview_item_img3 = (ImageView) convertView.findViewById(R.id.line_listview_item_img3);
        line_listview_item_img4 = (ImageView) convertView.findViewById(R.id.line_listview_item_img4);
        line_listview_item_img5 = (ImageView) convertView.findViewById(R.id.line_listview_item_img5);
        line_listview_item_img6 = (ImageView) convertView.findViewById(R.id.line_listview_item_img6);
        line_listview_item_img7 = (ImageView) convertView.findViewById(R.id.line_listview_item_img7);
        line_item_title = (TextView) convertView.findViewById(R.id.line_item_title);
        line_listview_item_name1 = (TextView) convertView.findViewById(R.id.line_listview_item_name1);
        line_listview_item_name2 = (TextView) convertView.findViewById(R.id.line_listview_item_name2);
        line_listview_item_name3 = (TextView) convertView.findViewById(R.id.line_listview_item_name3);
        line_listview_item_name4 = (TextView) convertView.findViewById(R.id.line_listview_item_name4);
        line_listview_item_name5 = (TextView) convertView.findViewById(R.id.line_listview_item_name5);
        line_listview_item_name6 = (TextView) convertView.findViewById(R.id.line_listview_item_name6);
        line_listview_item_name7 = (TextView) convertView.findViewById(R.id.line_listview_item_name7);
        try {
            if (line != null) {
                if(!"".equals(position)){
                    if(line.getDeviceCtegoryModel()!=null){
                        line.getDeviceCtegoryModel().setDevicePosition(position);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (line.getLine_item_title() != null) {
            l1.setVisibility(View.VISIBLE);
            line_item_title.setText(line.getLine_item_title());
            line_listview_item_name1.setText(line.getLine_item_content1());
            line_listview_item_name2.setText(line.getLine_item_content2());
            line_listview_item_name3.setText(line.getLine_item_content3());
            line_listview_item_name4.setText(line.getLine_item_content4());
            line_listview_item_name5.setText(line.getLine_item_content5());
            line_listview_item_name6.setText(line.getLine_item_content6());
            line_listview_item_name7.setText(line.getLine_item_content7());
            if (line.getLine_item_img1() != null) {
//            ImageUtils.displayImage(convertIconToString(line.getLine_item_img1()), line_listview_item_img1, ImageUtils.COMPRESS.LQUALITY, 10);
                line_listview_item_img1.setImageBitmap(comp(line.getLine_item_img1()));
            }
            if (line.getLine_item_img2() != null) {
                line_listview_item_img2.setImageBitmap(comp(line.getLine_item_img2()));
            }
            if (line.getLine_item_img3() != null) {
                line_listview_item_img3.setImageBitmap(comp(line.getLine_item_img3()));
            }
            if (line.getLine_item_img4() != null) {
                line_listview_item_img4.setImageBitmap(comp(line.getLine_item_img4()));
            }
            if (line.getLine_item_img5() != null) {
                line_listview_item_img5.setImageBitmap(comp(line.getLine_item_img5()));
            }
            if (line.getLine_item_img6() != null) {
                line_listview_item_img6.setImageBitmap(comp(line.getLine_item_img6()));
            }
            if (line.getLine_item_img7() != null) {
                line_listview_item_img7.setImageBitmap(comp(line.getLine_item_img7()));
            }
        }

        line_listview_item_img1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (click != null)
                    click.click(line, v);
            }
        });
        line_listview_item_img2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (click != null)
                    click.click(line, v);
            }
        });
        line_listview_item_img3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (click != null)
                    click.click(line, v);
            }
        });
        line_listview_item_img4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (click != null)
                    click.click(line, v);
            }
        });
        line_listview_item_img5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (click != null)
                    click.click(line, v);
            }
        });
        line_listview_item_img6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (click != null)
                    click.click(line, v);
            }
        });
        line_listview_item_img7.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (click != null)
                    click.click(line, v);
            }
        });
//        l2.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (click != null)
//                    click.click(line, v);
//            }
//        });
        return convertView;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public void setClick(Click click) {
        this.click = click;
    }


    private Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;//降低图片从ARGB888到RGB565
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

}