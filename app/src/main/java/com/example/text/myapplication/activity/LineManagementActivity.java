package com.example.text.myapplication.activity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.text.myapplication.R;
import com.example.text.myapplication.adapter.LineListViewAdapter;
import com.example.text.myapplication.db.ChannelDao;
import com.example.text.myapplication.db.DBHepler;
import com.example.text.myapplication.domain.HomeLine;
import com.example.text.myapplication.domain.Line;
import com.example.text.myapplication.tool.CommonData;
import com.example.text.myapplication.view.pulltorefresh.PullToRefreshListView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

//具体线路页面 jiangpan 2017.4.10
public class LineManagementActivity extends BaseActivity implements View.OnClickListener {
    private TextView line_name;
    private ImageView line_return, line_add;
    private PullToRefreshListView pullToRefreshListView;//刷新
    private ListView listView;
    private LineListViewAdapter lineListViewAdapter;//首页适配器
    private static String number, number2;
    private List<DeviceCtegoryModel> items = new ArrayList<DeviceCtegoryModel>();
    public static List<Line> lineList = new ArrayList<Line>();//线路数据
    private String category_name;//选择的类别名称
    private Intent intent;
    private Line lines;
    /**
     * 图片标识id
     */
    private int checkedDeviceId = 0;
    /**
     * 图片标识位置
     */
    private int checkedDevicePosition = 0;
    /**
     * 图片标识位置
     */
    public static final String imageFlag_1 = "1";
    public static final String imageFlag_2 = "2";
    public static final String imageFlag_3 = "3";
    public static final String imageFlag_4 = "4";
    public static final String imageFlag_5 = "5";
    public static final String imageFlag_6 = "6";
    public static final String imageFlag_7 = "7";
    private EditText e1;
    private RadioButton r1, r2, r3;
    public static SQLiteDatabase db;
    //数据库
    private ChannelDao dao;
    private String category;//选择的类别
    private int position, dbPosition;//每条线路坐标
    private String dbLineName, dbLineNameOne, dbCategory, dbFileId, dbLineData, dbLineData2;
    private int file_number, user_position;
    private static List<HomeLine> homeLines = new ArrayList<HomeLine>();
    private List<String> dbLineDatalist = new ArrayList<String>();
    private List<String> dbFileIdlist = new ArrayList<String>();
    private List<String> dbLineDatalist2 = new ArrayList<String>();
    private LinearLayout textLoading;
    private boolean isExistName;
    private static final String MYFOLDER = "/sdcard/外采数据/";
    private List<Integer> mypositions = new ArrayList<Integer>();
    private List<Integer> file_numbers = new ArrayList<Integer>();
    private List<Integer> user_positions = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_management);
        //两行代码搞定在下有一个Listview要动态加载一个硕大的SQlite数据库（15M，接近8万行），用作动态查询列表，奈何在使用SQLiteDatabase时总是抛出异常,这个方法就是掩饰了加载的时间
        Timer timer = new Timer(true);
        timer.schedule(task, 100); //第二个参数为延迟时间，1000为1s
        DBHepler dbHelper = new DBHepler(this, "line_data.db", null, 1);
        db = dbHelper.getWritableDatabase();
    }

    //  实现run()方法，发送一条Message给Handler
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            mHandler.sendMessage(mHandler.obtainMessage());
        }
    };
    //handleMessage()方法，用于接收Message，刷新UI，加载ListView
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            init();
            textLoading.setVisibility(View.GONE);
        }
    };

    public void init() {
        intent = getIntent();
        number = intent.getStringExtra("number");
        position = intent.getExtras().getInt("position");
        file_numbers.clear();
        dbLineDatalist.clear();
        user_positions.clear();
        mypositions.clear();
        dbFileIdlist.clear();
        query_user();
        query_line_name();
        textLoading = (LinearLayout) findViewById(R.id.textLoading);
        line_name = (TextView) findViewById(R.id.line_name);
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.line_list);
        line_return = (ImageView) findViewById(R.id.line_return);
        line_add = (ImageView) findViewById(R.id.line_add);
        line_name.setText(number);
        listView = pullToRefreshListView.getRefreshableView();
        line_return.setOnClickListener(this);
        lineListViewAdapter = new LineListViewAdapter(this, lineList,dbFileIdlist);
        listView.setAdapter(lineListViewAdapter);
        line_add.setOnClickListener(this);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int arg, long id) {
                //定义AlertDialog.Builder对象，当长按列表项的时候弹出确认删除对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(LineManagementActivity.this);
                builder.setMessage("确定删除?");
                builder.setTitle("提示");
                //添加AlertDialog.Builder对象的setPositiveButton()方法
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        file_numbers.clear();
                        dbLineDatalist.clear();
                        user_positions.clear();
                        dbFileIdlist.clear();
                        query_user();
                        //删除数据库数据
                        if (lineList.size() > arg - 1) {
                            delFromName(lineList.get(arg - 1).getLine_item_title());
                            delFileNumber(lineList.size() - 1);
                            //删除文件数据
                            delete_photo(lineList.get(arg - 1).getLine_item_title());
                            delete(lineList.get(arg - 1).getLine_item_title());
                        }
                        MainActivity.query_user_data();
                        System.out.println("success");
                        lineList.remove(arg - 1);
                        lineListViewAdapter.notifyDataSetChanged();
                        Toast.makeText(getBaseContext(), "删除列表项", Toast.LENGTH_SHORT).show();
                    }
                });
                //添加AlertDialog.Builder对象的setNegativeButton()方法
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.create().show();
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LineManagementActivity.this, SecondActivity.class);
                intent.putExtra("number", lineList.get(position-1).getView());
//                intent.putExtra("number", number2);
                intent.putExtra("position", position - 1);
                startActivity(intent);//进入具体线路页面

            }
        });
        lineListViewAdapter.setClick(new LineListViewAdapter.Click() {
            @Override
            public void click(Object obj, View view) {
                lines = (Line) obj;
                if (lines != null) {
                    checkedDeviceId = lines.getDeviceCtegoryModel().getDeviceId();
                    CommonData.checkedDeviceId = lines.getDeviceCtegoryModel().getDeviceId();
                    checkedDevicePosition = lines.getDeviceCtegoryModel().getDevicePosition();
                    CommonData.checkedDevicePosition = lines.getDeviceCtegoryModel().getDevicePosition();
                    Log.i("print", "checkedDeviceId:" + checkedDeviceId);
                }
                if (view.getId() == R.id.line_listview_item_img1) {
                    String Line_item_img1 = convertIconToString(lines.getLine_item_img1());
                    if (Line_item_img1 == null) {
                        category_name = lines.getLine_item_title();
                        intent = new Intent();//进入拍照定位页面
                        intent.setClass(LineManagementActivity.this, PhotographLocationActivity.class);
                        intent.putExtra("imageFlag", imageFlag_1);
                        startActivityForResult(intent, 2);
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(LineManagementActivity.this, LargeActivity.class);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        lines.getLine_item_img1().compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] bitmapByte = baos.toByteArray();
                        intent.putExtra("bitmap", bitmapByte);
                        intent.putExtra("myLatitudelist", lines.getLatitude1());
                        intent.putExtra("myLongitudelist", lines.getLongitude1());
                        startActivity(intent);
                    }
                } else if (view.getId() == R.id.line_listview_item_img2) {
                    String Line_item_img2 = convertIconToString(lines.getLine_item_img2());
                    if (Line_item_img2 == null) {
                        category_name = lines.getLine_item_title();
                        intent = new Intent();//进入拍照定位页面
                        intent.setClass(LineManagementActivity.this, PhotographLocationActivity.class);
                        intent.putExtra("imageFlag", imageFlag_2);
                        startActivityForResult(intent, 2);
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(LineManagementActivity.this, LargeActivity.class);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        lines.getLine_item_img2().compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] bitmapByte = baos.toByteArray();
                        intent.putExtra("bitmap", bitmapByte);
                        intent.putExtra("myLatitudelist", lines.getLatitude2());
                        intent.putExtra("myLongitudelist", lines.getLongitude2());
                        startActivity(intent);
                    }
                } else if (view.getId() == R.id.line_listview_item_img3) {
                    String Line_item_img3 = convertIconToString(lines.getLine_item_img3());
                    if (Line_item_img3 == null) {
                        category_name = lines.getLine_item_title();
                        intent = new Intent();//进入拍照定位页面
                        intent.setClass(LineManagementActivity.this, PhotographLocationActivity.class);
                        intent.putExtra("imageFlag", imageFlag_3);
                        startActivityForResult(intent, 2);
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(LineManagementActivity.this, LargeActivity.class);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        lines.getLine_item_img3().compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] bitmapByte = baos.toByteArray();
                        intent.putExtra("bitmap", bitmapByte);
                        intent.putExtra("myLatitudelist", lines.getLatitude3());
                        intent.putExtra("myLongitudelist", lines.getLongitude3());
                        startActivity(intent);
                    }
                } else if (view.getId() == R.id.line_listview_item_img4) {
                    String Line_item_img4 = convertIconToString(lines.getLine_item_img4());
                    if (Line_item_img4 == null) {
                        category_name = lines.getLine_item_title();
                        intent = new Intent();//进入拍照定位页面
                        intent.setClass(LineManagementActivity.this, PhotographLocationActivity.class);
                        intent.putExtra("imageFlag", imageFlag_4);
                        startActivityForResult(intent, 2);
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(LineManagementActivity.this, LargeActivity.class);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        lines.getLine_item_img4().compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] bitmapByte = baos.toByteArray();
                        intent.putExtra("bitmap", bitmapByte);
                        intent.putExtra("myLatitudelist", lines.getLatitude4());
                        intent.putExtra("myLongitudelist", lines.getLongitude4());
                        startActivity(intent);
                    }
                } else if (view.getId() == R.id.line_listview_item_img5) {
                    String Line_item_img5 = convertIconToString(lines.getLine_item_img5());
                    if (Line_item_img5 == null) {
                        category_name = lines.getLine_item_title();
                        intent = new Intent();//进入拍照定位页面
                        intent.setClass(LineManagementActivity.this, PhotographLocationActivity.class);
                        intent.putExtra("imageFlag", imageFlag_5);
                        startActivityForResult(intent, 2);
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(LineManagementActivity.this, LargeActivity.class);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        lines.getLine_item_img5().compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] bitmapByte = baos.toByteArray();
                        intent.putExtra("bitmap", bitmapByte);
                        intent.putExtra("myLatitudelist", lines.getLatitude5());
                        intent.putExtra("myLongitudelist", lines.getLongitude5());
                        startActivity(intent);
                    }
                } else if (view.getId() == R.id.line_listview_item_img6) {
                    String Line_item_img6 = convertIconToString(lines.getLine_item_img6());
                    if (Line_item_img6 == null) {
                        category_name = lines.getLine_item_title();
                        intent = new Intent();//进入拍照定位页面
                        intent.setClass(LineManagementActivity.this, PhotographLocationActivity.class);
                        intent.putExtra("imageFlag", imageFlag_6);
                        startActivityForResult(intent, 2);
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(LineManagementActivity.this, LargeActivity.class);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        lines.getLine_item_img6().compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] bitmapByte = baos.toByteArray();
                        intent.putExtra("bitmap", bitmapByte);
                        intent.putExtra("myLatitudelist", lines.getLatitude6());
                        intent.putExtra("myLongitudelist", lines.getLongitude6());
                        startActivity(intent);
                    }
                } else if (view.getId() == R.id.line_listview_item_img7) {
                    String Line_item_img7 = convertIconToString(lines.getLine_item_img7());
                    if (Line_item_img7 == null) {
                        category_name = lines.getLine_item_title();
                        intent = new Intent();//进入拍照定位页面
                        intent.setClass(LineManagementActivity.this, PhotographLocationActivity.class);
                        intent.putExtra("imageFlag", imageFlag_7);
                        startActivityForResult(intent, 2);
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(LineManagementActivity.this, LargeActivity.class);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        lines.getLine_item_img7().compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] bitmapByte = baos.toByteArray();
                        intent.putExtra("bitmap", bitmapByte);
                        intent.putExtra("myLatitudelist", lines.getLatitude7());
                        intent.putExtra("myLongitudelist", lines.getLongitude7());
                        startActivity(intent);
                    }
                }
//                else if(view.getId()==R.id.l2){
//                    Intent intent = new Intent(LineManagementActivity.this, LineManagementActivity.class);
//                    intent.putExtra("number", lineList.get(2).getView());
//                    intent.putExtra("position", position - 1);
//                    startActivity(intent);//进入具体线路页面
//                }
            }
        });
    }

    //查询数据库user_data
    public void query_user() {
        DBHepler dbHelper = new DBHepler(this, "line_data.db", null, 1);
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("user_data", null, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    int nameColumnIndexFileNumber = cursor.getColumnIndex("file_number");
                    int nameColumnIndexPosition = cursor.getColumnIndex("position");
                    user_position = cursor.getInt(nameColumnIndexPosition);
                    user_positions.add(user_position);
                    file_number = cursor.getInt(nameColumnIndexFileNumber);
                    file_numbers.add(file_number);
                    int nameColumnIndex = cursor.getColumnIndex("line_name");
                    dbLineNameOne = cursor.getString(nameColumnIndex);
                    if (dbLineNameOne != null) {
                        dbLineName = dbLineNameOne.toString();
                        Log.i("数据库line_name：：：：", dbLineName);
                        HomeLine homeLine = new HomeLine();
                        homeLine.setView(dbLineName);
                        homeLines.add(homeLine);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
        }
    }

    //查询数据库line_name
    public void query_line_name() {
        dao = dao == null ? new ChannelDao(LineManagementActivity.this) : dao;
        List<Line> dataList = dao.listCache();
        lineList.clear();
        for (Line line : dataList) {
            if (line != null) {
                if (line.getLine_name() != null) {
                    if (line.getLine_name().equals(number)) {
                        lineList.add(line);
                        dbCategory = line.getCategory();
                        dbPosition = line.getPosition();
                        mypositions.add(dbPosition);
                        dbLineData = line.getLine_item_title();
                        dbLineDatalist.add(dbLineData);
                        dbFileId = line.getView();
                        dbFileIdlist.add(dbFileId);
                    }
                }
            }
        }
    }

    // 删除
    public int delFromName(String dbLineData) {
        int id2 = db.delete("home_data",
                "line_data='" + dbLineData + "'", null);
        Log.e("DelUserInfo", id2 + "");
        return id2;
    }

    // 删除个数
    public void delFileNumber(int file_number) {
        ContentValues cValues = new ContentValues();
        cValues.put("file_number", file_number);
        db.update("user_data", cValues, "line_name='" + number + "'", null);
    }

    //删除图片文件
    public static void delete_photo(String line_name) {
        String path = "/sdcard/外采数据/" + line_name + "_图片";
        File dir = new File(path);
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;

        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                delete_photo(path); // 递规的方式删除文件夹
        }
//        dir.delete();// 删除目录本身
    }

    //删除文件
    public static void delete(String line_name) {
        String path = MYFOLDER + "important2/" + line_name;
        File dir = new File(path);
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;

        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                delete(path); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.line_return:
                Intent intent = new Intent(LineManagementActivity.this, MainActivity.class);
                startActivity(intent);//进入具体线路页面
                finish();
                break;
            case R.id.line_add:
                //定义AlertDialog.Builder对象，当长按列表项的时候弹出确认删除对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(LineManagementActivity.this);
//                builder.setMessage("确定删除?");
                builder.setTitle("提示");
                //添加AlertDialog.Builder对象的setPositiveButton()方法
                builder.setPositiveButton("新建文件夹", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 1. 布局文件转换为View对象
                        LayoutInflater inflater = LineManagementActivity.this.getLayoutInflater();
                        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.edittext, null);
                        // 2. 新建对话框对象
                        final Dialog dialog2 = new AlertDialog.Builder(LineManagementActivity.this).create();
                        dialog2.setCancelable(false);
                        dialog2.show();
                        dialog2.setContentView(layout);
                        WindowManager.LayoutParams params = dialog2.getWindow().getAttributes();
                        params.width = WindowManager.LayoutParams.WRAP_CONTENT/2;
                        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        dialog2.getWindow().setAttributes(params);
                        //弹出软键盘
                        dialog2.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                        dialog2.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                        final EditText e1 = (EditText) layout.findViewById(R.id.e);
                        Button btnOK = (Button) layout.findViewById(R.id.dialog_ok);
                        btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mypositions.clear();
                                dbLineDatalist.clear();
                                query_line_name();//如果数据库中line_data为空则不创建
                                if (dbLineDatalist.size() >0) {
                                    number2 = e1.getText().toString();
                                    Log.i("edittext_content：：：：", number2);
                                    try {
                                        if (e1 == null || e1.getText().toString().trim().length() == 0) {
                                            //不关闭对话框
                                            Field field = dialog2.getClass().getSuperclass().getDeclaredField("mShowing");
                                            field.setAccessible(true);
                                            field.set(dialog2, false);   //设定为false,则不可以关闭对话框
                                            dialog2.dismiss();
                                            Toast.makeText(getApplication(), "内容不能为空", Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (e1 != null || e1.getText().toString().trim().length() != 0) {
//                                                HomeLine homeLine = new HomeLine();
//                                            if (isExistName == false) {
//                                                Toast.makeText(LineManagementActivity.this, "此线路已存在，不能重复添加！", Toast.LENGTH_SHORT).show();
//                                            } else {
                                                Line provinces = new Line();
                                                provinces.setView(number2);
//                                                provinces.setLine_name(number);
                                                if (mypositions.contains(position)) {
                                                    provinces.setPosition(position + 3);
                                                } else {
                                                    provinces.setPosition(position + 2);
                                                }
                                                lineList.add(provinces);
                                                ContentValues cValues = new ContentValues();
                                                cValues.put("file", number2);
                                                cValues.put("line_name", number);
                                                if (mypositions.contains(position)) {
                                                    cValues.put("position",position + 3);
                                                } else {
                                                    cValues.put("position",position+2);
                                                }
                                                db.insert("home_data", null,cValues);

                                                Toast.makeText(getApplication(), "文件夹已保存在/sdcard/外采数据/", Toast.LENGTH_LONG).show();
                                                lineListViewAdapter.notifyDataSetChanged();
                                            }
                                            // 要正常关闭对话框
                                            Field field = dialog2.getClass().getSuperclass().getDeclaredField("mShowing");
                                            field.setAccessible(true);
                                            field.set(dialog2, true);//设定为true,则可以关闭对话框
                                            dialog2.dismiss();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }else {
                                    Toast.makeText(getApplication(), "请新建文件！", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        // 5. 取消按钮
                        Button btnCancel = (Button) layout.findViewById(R.id.dialog_cancel);
                        btnCancel.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                try {
                                    Field field = dialog2.getClass().getSuperclass().getDeclaredField("mShowing");
                                    field.setAccessible(true);
                                    field.set(dialog2, true);//设定为true,则可以关闭对话框
                                    dialog2.dismiss();
                                } catch (Exception e) {

                                }
                            }
                        });
                    }
                });
                //添加AlertDialog.Builder对象的setNegativeButton()方法
                builder.setNegativeButton("新建文件", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 创建数据
                        DeviceCtegoryModel model = new DeviceCtegoryModel();
                        model.setDeviceId(0);
                        model.setDeviceName("杆塔");
                        items.add(model);
                        model = new DeviceCtegoryModel();
                        model.setDeviceId(1);
                        model.setDeviceName("变压器");
                        items.add(model);
                        model = new DeviceCtegoryModel();
                        model.setDeviceId(2);
                        model.setDeviceName("开关");
                        items.add(model);
                        model = null;
                        // 1. 布局文件转换为View对象
                        LayoutInflater inflater = LineManagementActivity.this.getLayoutInflater();
                        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.alert_dialog_001, null);
                        // 2. 新建对话框对象
                        final Dialog dialog1 = new AlertDialog.Builder(LineManagementActivity.this).create();
                        dialog1.setCancelable(false);
                        dialog1.show();
                        dialog1.setContentView(layout);
                        WindowManager.LayoutParams params =
                                dialog1.getWindow().getAttributes();
                        params.width = WindowManager.LayoutParams.WRAP_CONTENT/2;
                        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        dialog1.getWindow().setAttributes(params);
                        //弹出软键盘
                        dialog1.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                        // 3. 类别选择
                        RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.r);
                        r1 = (RadioButton) layout.findViewById(R.id.r1);
                        r1.setId(items.get(0).getDeviceId());
                        r1.setText(items.get(0).getDeviceName());

                        r2 = (RadioButton) layout.findViewById(R.id.r2);
                        r2.setId(items.get(1).getDeviceId());
                        r2.setText(items.get(1).getDeviceName());

                        r3 = (RadioButton) layout.findViewById(R.id.r3);
                        r3.setId(items.get(2).getDeviceId());
                        r3.setText(items.get(2).getDeviceName());
                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                Log.i("print", "checkedId:" + checkedId);
                                checkedDeviceId = checkedId;
                                if (r1.getId() == checkedId) {
                                    category = r1.getText().toString();
                                } else if (r2.getId() == checkedId) {
                                    category = r2.getText().toString();
                                } else if (r3.getId() == checkedId) {
                                    category = r3.getText().toString();
                                }
                            }
                        });
                        //类别名称
                        e1 = (EditText) layout.findViewById(R.id.e);
                        //点击完成
                        e1.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                if (actionId == EditorInfo.IME_ACTION_DONE) {
                                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                    return true;
                                }
                                return false;
                            }
                        });
                        // 4. 确定按钮
                        Button btnOK = (Button) layout.findViewById(R.id.dialog_ok);
                        btnOK.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                category_name = e1.getText().toString();
                                try {
                                    if (e1 == null || e1.getText().toString().trim().length() == 0) {
                                        //不关闭对话框
                                        Field field = dialog1.getClass().getSuperclass().getDeclaredField("mShowing");
                                        field.setAccessible(true);
                                        field.set(dialog1, false);   //设定为false,则不可以关闭对话框
                                        dialog1.dismiss();
                                        Toast.makeText(getApplication(), "名称不能为空", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (e1 != null || e1.getText().toString().trim().length() != 0) {
                                            ExistName(category_name);
                                            if (isExistName == false) {
                                                Toast.makeText(LineManagementActivity.this, "此数据已存在，不能重复添加！", Toast.LENGTH_SHORT).show();
                                            } else {
                                                initDataImportant2();
                                                initDataImportantContent2(category_name);
                                                category_add_data(position + 1);
                                                lineListViewAdapter.notifyDataSetChanged();
                                                MainActivity.query_user_data();
                                            }

                                        }
                                        // 要正常关闭对话框
                                        Field field = dialog1.getClass().getSuperclass().getDeclaredField("mShowing");
                                        field.setAccessible(true);
                                        field.set(dialog1, true);//设定为true,则可以关闭对话框
                                        dialog1.dismiss();

                                    }
                                } catch (Exception e) {

                                }
                            }
                        });

                        // 5. 取消按钮
                        Button btnCancel = (Button) layout.findViewById(R.id.dialog_cancel);
                        btnCancel.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                try {
                                    Field field = dialog1.getClass().getSuperclass().getDeclaredField("mShowing");
                                    field.setAccessible(true);
                                    field.set(dialog1, true);//设定为true,则可以关闭对话框
                                    dialog1.dismiss();
                                } catch (Exception e) {

                                }
                            }
                        });

                    }
                });

                final AlertDialog dialog = builder.create();
                WindowManager.LayoutParams params2 =
                        dialog.getWindow().getAttributes();
                params2.width = WindowManager.LayoutParams.WRAP_CONTENT/2;
                params2.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(params2);
                dialog.show();
                break;
        }
    }
    public static String getSystemProperty(String propName){
        String line;
        BufferedReader input = null;
        try
        {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        }
        catch (IOException ex)
        {
            Log.e(TAG, "Unable to read sysprop " + propName, ex);
            return null;
        }
        finally
        {
            if(input != null)
            {
                try
                {
                    input.close();
                }
                catch (IOException e)
                {
                    Log.e(TAG, "Exception while closing InputStream", e);
                }
            }
        }
        return line;
    }
    //添加基本数据
    public void category_add_data(int position) {
        if (checkedDeviceId == 0) {
            Line provinces = new Line();
            provinces.setDeviceCtegoryModel(items.get(checkedDeviceId));
            provinces.setLine_item_title(category_name);
            provinces.setLine_item_content1("塔杆全景");
            provinces.setLine_item_content2("标识牌");
            provinces.setLine_item_content3("塔杆基础");
            provinces.setLine_item_content4("塔头");
            provinces.setLine_item_content5("大号通道");
            provinces.setLine_item_content6("小号通道");
            provinces.setLine_item_content7("其他");
            lineList.add(provinces);
            provinces.setLine_name(number);
            if (mypositions.contains(position)) {
                provinces.setPosition(position + 1);
            } else {
                provinces.setPosition(position);
            }

            provinces.setCategory(category);
            dao.addCache(provinces);
            ContentValues cValues = new ContentValues();
            cValues.put("file_number", lineList.size());
            db.update("user_data", cValues, "line_name='" + number + "'", null);
        } else if (checkedDeviceId == 1) {
            Line provinces = new Line();
            provinces.setDeviceCtegoryModel(items.get(checkedDeviceId));
            provinces.setLine_item_title(category_name);
            provinces.setLine_item_content1("变压器属性1");
            provinces.setLine_item_content2("变压器属性2");
            provinces.setLine_item_content3("变压器属性3");
            provinces.setLine_item_content4("变压器属性4");
            provinces.setLine_item_content5("变压器属性5");
            provinces.setLine_item_content6("变压器属性6");
            provinces.setLine_item_content7("变压器属性7");
            lineList.add(provinces);
            provinces.setLine_name(number);
            if (mypositions.contains(position)) {
                provinces.setPosition(position + 1);
            } else {
                provinces.setPosition(position);
            }
            provinces.setCategory(category);
            dao.addCache(provinces);
            ContentValues cValues = new ContentValues();
            cValues.put("file_number", lineList.size());
            db.update("user_data", cValues, "line_name='" + number + "'", null);
        } else if (checkedDeviceId == 2) {
            Line provinces = new Line();
            provinces.setDeviceCtegoryModel(items.get(checkedDeviceId));
            provinces.setLine_item_title(category_name);
            provinces.setLine_item_content1("开关属性1");
            provinces.setLine_item_content2("开关属性2");
            provinces.setLine_item_content3("开关属性3");
            provinces.setLine_item_content4("开关属性4");
            provinces.setLine_item_content5("开关属性5");
            provinces.setLine_item_content6("开关属性6");
            provinces.setLine_item_content7("开关属性7");
            lineList.add(provinces);
            provinces.setLine_name(number);
            if (mypositions.contains(position)) {
                provinces.setPosition(position + 1);
            } else {
                provinces.setPosition(position);
            }
            provinces.setCategory(category);
            dao.addCache(provinces);
            ContentValues cValues = new ContentValues();
            cValues.put("file_number", lineList.size());
            cValues.put("position", position);
            db.update("user_data", cValues, "line_name='" + number + "'", null);
        }

    }

    //拍照之后返回的图片和经纬度
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            dao = dao == null ? new ChannelDao(LineManagementActivity.this) : dao;
            List<Line> dataList2 = dao.listCache();
            dbLineDatalist2.clear();
            dbFileIdlist.clear();
            for (Line line2 : dataList2) {
                if (line2 != null) {
                    if (line2.getLine_name().equals(number)) {
                        dbLineData2 = line2.getLine_item_title();
                        dbLineDatalist2.add(dbLineData2);
                        dbFileId=line2.getView();
                        dbFileIdlist.add(dbFileId);
                    }
                }
            }
            category_add();//图片的添加
//            category_add_lat_long();//经纬度添加
            lineListViewAdapter.notifyDataSetChanged();
            CommonData.imageBitmaps.clear();
            CommonData.myLatitudelist.clear();
            CommonData.myLongitudelist.clear();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //图片添加
    public void category_add() {
//        if (checkedDevicePosition == CommonData.checkedDevicePosition) {
        for (int i = 0; i < dbLineDatalist2.size(); i++) {
            if (dbLineDatalist2.get(i) != null) {
                if (dbLineDatalist2.get(i).toString().equals(category_name)) {
                    Line provinces = lineList.get(i);
                    if (CommonData.imageBitmaps.containsKey(imageFlag_1)
                            && CommonData.imageBitmaps.get(imageFlag_1) != null) {
                        provinces.setLine_item_img1(CommonData.imageBitmaps.get(imageFlag_1));
                        provinces.setLatitude1(CommonData.myLatitudelist.get(imageFlag_1));
                        provinces.setLongitude1(CommonData.myLongitudelist.get(imageFlag_1));
                        Log.i("保存myLongitudelist1：：：：", CommonData.myLongitudelist.get(imageFlag_1).toString());
                        if (dbCategory == null) {
                            sava_photo(homeLines.get(position).getView(), CommonData.imageBitmaps.get(imageFlag_1), homeLines.get(position).getView() + "_" + category + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_1) + "_" + CommonData.myLongitudelist.get(imageFlag_1));
                        } else {
                            sava_photo(homeLines.get(position).getView(), CommonData.imageBitmaps.get(imageFlag_1), homeLines.get(position).getView() + "_" + dbCategory + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_1) + "_" + CommonData.myLongitudelist.get(imageFlag_1));
                        }
                        ContentValues cValues = new ContentValues();
                        cValues.put("picture", Bitmap2Bytes(CommonData.imageBitmaps.get(imageFlag_1)));
                        cValues.put("deviceId", CommonData.checkedDeviceId);
                        cValues.put("devicePosition", CommonData.checkedDevicePosition);
                        cValues.put("latitude", CommonData.myLatitudelist.get(imageFlag_1));
                        cValues.put("longitude", CommonData.myLongitudelist.get(imageFlag_1));
                        if (dbCategory == null) {
                            cValues.put("photo", "图片名称：" + homeLines.get(position).getView() + "_" + category + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_1) + "_" + CommonData.myLongitudelist.get(imageFlag_1));
                        } else {
                            cValues.put("photo", "图片名称：" + homeLines.get(position).getView() + "_" + dbCategory + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_1) + "_" + CommonData.myLongitudelist.get(imageFlag_1));
                        }
                        db.update("home_data", cValues, "line_data='" + category_name + "'", null);

                    }
                    if (CommonData.imageBitmaps.containsKey(imageFlag_2)
                            && CommonData.imageBitmaps.get(imageFlag_2) != null) {
                        provinces.setLine_item_img2(CommonData.imageBitmaps.get(imageFlag_2));
                        provinces.setLatitude1(CommonData.myLatitudelist.get(imageFlag_2));
                        provinces.setLongitude1(CommonData.myLongitudelist.get(imageFlag_2));
                        if (dbCategory == null) {
                            sava_photo(homeLines.get(position).getView(), CommonData.imageBitmaps.get(imageFlag_2), homeLines.get(position).getView() + "_" + category + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_2) + "_" + CommonData.myLongitudelist.get(imageFlag_2));
                        } else {
                            sava_photo(homeLines.get(position).getView(), CommonData.imageBitmaps.get(imageFlag_2), homeLines.get(position).getView() + "_" + dbCategory + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_2) + "_" + CommonData.myLongitudelist.get(imageFlag_2));
                        }
                        ContentValues cValues = new ContentValues();
                        cValues.put("picture2", Bitmap2Bytes(CommonData.imageBitmaps.get(imageFlag_2)));
                        cValues.put("deviceId", CommonData.checkedDeviceId);
                        cValues.put("devicePosition", CommonData.checkedDevicePosition);
                        Log.i("保存myLongitudelist1：：：：", CommonData.myLongitudelist.get(imageFlag_2).toString());
                        cValues.put("latitude", CommonData.myLatitudelist.get(imageFlag_2));
                        cValues.put("longitude", CommonData.myLongitudelist.get(imageFlag_2));
                        if (dbCategory == null) {
                            cValues.put("photo", "图片名称：" + homeLines.get(position).getView() + "_" + category + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_2) + "_" + CommonData.myLongitudelist.get(imageFlag_2));
                        } else {
                            cValues.put("photo", "图片名称：" + homeLines.get(position).getView() + "_" + dbCategory + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_2) + "_" + CommonData.myLongitudelist.get(imageFlag_2));
                        }
                        db.update("home_data", cValues, "line_data='" + category_name + "'", null);
                    }
                    if (CommonData.imageBitmaps.containsKey(imageFlag_3)
                            && CommonData.imageBitmaps.get(imageFlag_3) != null) {
                        provinces.setLine_item_img3(CommonData.imageBitmaps.get(imageFlag_3));
                        provinces.setLatitude3(CommonData.myLatitudelist.get(imageFlag_3));
                        provinces.setLongitude3(CommonData.myLongitudelist.get(imageFlag_3));
                        if (dbCategory == null) {
                            sava_photo(homeLines.get(position).getView(), CommonData.imageBitmaps.get(imageFlag_3), homeLines.get(position).getView() + "_" + category + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_3) + "_" + CommonData.myLongitudelist.get(imageFlag_3));
                        } else {
                            sava_photo(homeLines.get(position).getView(), CommonData.imageBitmaps.get(imageFlag_3), homeLines.get(position).getView() + "_" + dbCategory + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_3) + "_" + CommonData.myLongitudelist.get(imageFlag_3));
                        }
                        ContentValues cValues = new ContentValues();
                        cValues.put("picture3", Bitmap2Bytes(CommonData.imageBitmaps.get(imageFlag_3)));
                        cValues.put("deviceId", CommonData.checkedDeviceId);
                        cValues.put("devicePosition", CommonData.checkedDevicePosition);
                        Log.i("保存myLongitudelist3：：：：", CommonData.myLongitudelist.get(imageFlag_3).toString());
                        cValues.put("latitude3", CommonData.myLatitudelist.get(imageFlag_3));
                        cValues.put("longitude3", CommonData.myLongitudelist.get(imageFlag_3));
                        if (dbCategory == null) {
                            cValues.put("photo3", "图片名称：" + homeLines.get(position).getView() + "_" + category + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_3) + "_" + CommonData.myLongitudelist.get(imageFlag_3));
                        } else {
                            cValues.put("photo3", "图片名称：" + homeLines.get(position).getView() + "_" + dbCategory + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_3) + "_" + CommonData.myLongitudelist.get(imageFlag_3));
                        }
                        db.update("home_data", cValues, "line_data='" + category_name + "'", null);
                    }
                    if (CommonData.imageBitmaps.containsKey(imageFlag_4)
                            && CommonData.imageBitmaps.get(imageFlag_4) != null) {
                        provinces.setLine_item_img4(CommonData.imageBitmaps.get(imageFlag_4));
                        provinces.setLatitude4(CommonData.myLatitudelist.get(imageFlag_4));
                        provinces.setLongitude4(CommonData.myLongitudelist.get(imageFlag_4));
                        if (dbCategory == null) {
                            sava_photo(homeLines.get(position).getView(), CommonData.imageBitmaps.get(imageFlag_4), dbLineName + "_" + category + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_4) + "_" + CommonData.myLongitudelist.get(imageFlag_4));
                        } else {
                            sava_photo(homeLines.get(position).getView(), CommonData.imageBitmaps.get(imageFlag_4), dbLineName + "_" + dbCategory + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_4) + "_" + CommonData.myLongitudelist.get(imageFlag_4));
                        }

                        ContentValues cValues = new ContentValues();
                        cValues.put("picture4", Bitmap2Bytes(CommonData.imageBitmaps.get(imageFlag_4)));
                        cValues.put("deviceId", CommonData.checkedDeviceId);
                        cValues.put("devicePosition", CommonData.checkedDevicePosition);
                        Log.i("保存myLongitudelist4：：：：", CommonData.myLongitudelist.get(imageFlag_4).toString());
                        cValues.put("latitude4", CommonData.myLatitudelist.get(imageFlag_4));
                        cValues.put("longitude4", CommonData.myLongitudelist.get(imageFlag_4));
                        if (dbCategory == null) {
                            cValues.put("photo4", "图片名称：" + homeLines.get(position).getView() + "_" + category + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_4) + "_" + CommonData.myLongitudelist.get(imageFlag_4));
                        } else {
                            cValues.put("photo4", "图片名称：" + homeLines.get(position).getView() + "_" + dbCategory + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_4) + "_" + CommonData.myLongitudelist.get(imageFlag_4));
                        }
                        db.update("home_data", cValues, "line_data='" + category_name + "'", null);
                    }
                    if (CommonData.imageBitmaps.containsKey(imageFlag_5)
                            && CommonData.imageBitmaps.get(imageFlag_5) != null) {
                        provinces.setLine_item_img5(CommonData.imageBitmaps.get(imageFlag_5));
                        provinces.setLatitude5(CommonData.myLatitudelist.get(imageFlag_5));
                        provinces.setLongitude5(CommonData.myLongitudelist.get(imageFlag_5));
                        if (dbCategory == null) {
                            sava_photo(homeLines.get(position).getView(), CommonData.imageBitmaps.get(imageFlag_5), homeLines.get(position).getView() + "_" + category + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_5) + "_" + CommonData.myLongitudelist.get(imageFlag_5));
                        } else {
                            sava_photo(homeLines.get(position).getView(), CommonData.imageBitmaps.get(imageFlag_5), homeLines.get(position).getView() + "_" + dbCategory + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_5) + "_" + CommonData.myLongitudelist.get(imageFlag_5));
                        }

                        ContentValues cValues = new ContentValues();
                        cValues.put("picture5", Bitmap2Bytes(CommonData.imageBitmaps.get(imageFlag_5)));
                        cValues.put("deviceId", CommonData.checkedDeviceId);
                        cValues.put("devicePosition", CommonData.checkedDevicePosition);
                        Log.i("保存myLongitudelist5：：：：", CommonData.myLongitudelist.get(imageFlag_5).toString());
                        cValues.put("latitude5", CommonData.myLatitudelist.get(imageFlag_5));
                        cValues.put("longitude5", CommonData.myLongitudelist.get(imageFlag_5));
                        if (dbCategory == null) {
                            cValues.put("photo5", "图片名称：" + homeLines.get(position).getView() + "_" + category + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_5) + "_" + CommonData.myLongitudelist.get(imageFlag_5));
                        } else {
                            cValues.put("photo5", "图片名称：" + homeLines.get(position).getView() + "_" + dbCategory + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_5) + "_" + CommonData.myLongitudelist.get(imageFlag_5));
                        }
                        db.update("home_data", cValues, "line_data='" + category_name + "'", null);
                    }
                    if (CommonData.imageBitmaps.containsKey(imageFlag_6)
                            && CommonData.imageBitmaps.get(imageFlag_6) != null) {
                        provinces.setLine_item_img6(CommonData.imageBitmaps.get(imageFlag_6));
                        provinces.setLatitude6(CommonData.myLatitudelist.get(imageFlag_6));
                        provinces.setLongitude6(CommonData.myLongitudelist.get(imageFlag_6));
                        if (dbCategory == null) {
                            sava_photo(homeLines.get(position).getView(), CommonData.imageBitmaps.get(imageFlag_6), homeLines.get(position).getView() + "_" + category + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_6) + "_" + CommonData.myLongitudelist.get(imageFlag_6));
                        } else {
                            sava_photo(homeLines.get(position).getView(), CommonData.imageBitmaps.get(imageFlag_6), homeLines.get(position).getView() + "_" + dbCategory + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_6) + "_" + CommonData.myLongitudelist.get(imageFlag_6));
                        }
                        ContentValues cValues = new ContentValues();
                        cValues.put("picture6", Bitmap2Bytes(CommonData.imageBitmaps.get(imageFlag_6)));
                        cValues.put("deviceId", CommonData.checkedDeviceId);
                        cValues.put("devicePosition", CommonData.checkedDevicePosition);

                        Log.i("保存myLongitudelist6：：：：", CommonData.myLongitudelist.get(imageFlag_6).toString());
                        cValues.put("latitude6", CommonData.myLatitudelist.get(imageFlag_6));
                        cValues.put("longitude6", CommonData.myLongitudelist.get(imageFlag_6));
                        if (dbCategory == null) {
                            cValues.put("photo6", "图片名称：" + homeLines.get(position).getView() + "_" + category + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_6) + "_" + CommonData.myLongitudelist.get(imageFlag_6));
                        } else {
                            cValues.put("photo6", "图片名称：" + homeLines.get(position).getView() + "_" + dbCategory + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_6) + "_" + CommonData.myLongitudelist.get(imageFlag_6));
                        }
                        db.update("home_data", cValues, "line_data='" + category_name + "'", null);
                    }
                    if (CommonData.imageBitmaps.containsKey(imageFlag_7)
                            && CommonData.imageBitmaps.get(imageFlag_7) != null) {
                        provinces.setLine_item_img7(CommonData.imageBitmaps.get(imageFlag_7));
                        provinces.setLatitude7(CommonData.myLatitudelist.get(imageFlag_7));
                        provinces.setLongitude7(CommonData.myLongitudelist.get(imageFlag_7));
                        if (dbCategory == null) {
                            sava_photo(homeLines.get(position).getView(), CommonData.imageBitmaps.get(imageFlag_7), homeLines.get(position).getView() + "_" + category + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_7) + "_" + CommonData.myLongitudelist.get(imageFlag_7));
                        } else {
                            sava_photo(homeLines.get(position).getView(), CommonData.imageBitmaps.get(imageFlag_7), homeLines.get(position).getView() + "_" + dbCategory + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_7) + "_" + CommonData.myLongitudelist.get(imageFlag_7));
                        }
                        ContentValues cValues = new ContentValues();
                        cValues.put("picture7", Bitmap2Bytes(CommonData.imageBitmaps.get(imageFlag_7)));
                        cValues.put("deviceId", CommonData.checkedDeviceId);
                        cValues.put("devicePosition", CommonData.checkedDevicePosition);
                        Log.i("保存myLongitudelist7：：：：", CommonData.myLongitudelist.get(imageFlag_7).toString());
                        cValues.put("latitude7", CommonData.myLatitudelist.get(imageFlag_7));
                        cValues.put("longitude7", CommonData.myLongitudelist.get(imageFlag_7));
                        if (dbCategory == null) {
                            cValues.put("photo7", "图片名称：" + homeLines.get(position).getView() + "_" + category + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_7) + "_" + CommonData.myLongitudelist.get(imageFlag_7));
                        } else {
                            cValues.put("photo7", "图片名称：" + homeLines.get(position).getView() + "_" + dbCategory + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_7) + "_" + CommonData.myLongitudelist.get(imageFlag_7));
                        }
                        db.update("home_data", cValues, "line_data='" + category_name + "'", null);
                    }

                    provinces.setLine_item_title(category_name);

                    provinces.setLine_item_content1("塔杆全景");
                    provinces.setLine_item_content2("标识牌");
                    provinces.setLine_item_content3("塔杆基础");
                    provinces.setLine_item_content4("塔头");
                    provinces.setLine_item_content5("大号通道");
                    provinces.setLine_item_content6("小号通道");
                    provinces.setLine_item_content7("其他");
                    lineList.set(checkedDevicePosition, provinces);
//                    lineList.add(provinces);
                }
            }
        }
    }


    //经纬度添加
    public void category_add_lat_long() {
        for (int i = 0; i < dbLineDatalist2.size(); i++) {
            if (dbLineDatalist2.get(i) != null) {
                if (dbLineDatalist2.get(i).toString().equals(category_name)) {
                    Line provinces = lineList.get(i);
                    if (CommonData.imageBitmaps.containsKey(imageFlag_1)
                            && CommonData.imageBitmaps.get(imageFlag_1) != null) {
                        provinces.setLatitude1(CommonData.myLatitudelist.get(imageFlag_1));
                        provinces.setLongitude1(CommonData.myLongitudelist.get(imageFlag_1));
                        Log.i("保存myLongitudelist1：：：：", CommonData.myLongitudelist.get(imageFlag_1).toString());
                        ContentValues cValues = new ContentValues();
                        cValues.put("latitude", CommonData.myLatitudelist.get(imageFlag_1));
                        cValues.put("longitude", CommonData.myLongitudelist.get(imageFlag_1));
                        if (dbCategory == null) {
                            cValues.put("photo", "图片名称：" + homeLines.get(position).getView() + "_" + category + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_1) + "_" + CommonData.myLongitudelist.get(imageFlag_1));
                        } else {
                            cValues.put("photo", "图片名称：" + homeLines.get(position).getView() + "_" + dbCategory + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_1) + "_" + CommonData.myLongitudelist.get(imageFlag_1));
                        }

                        db.update("home_data", cValues, "line_data='" + category_name + "'", null);
                    }
                    if (CommonData.imageBitmaps.containsKey(imageFlag_2)
                            && CommonData.imageBitmaps.get(imageFlag_2) != null) {
                        provinces.setLatitude2(CommonData.myLatitudelist.get(imageFlag_2));
                        provinces.setLongitude2(CommonData.myLongitudelist.get(imageFlag_2));
                        Log.i("保存myLongitudelist2：：：：", CommonData.myLongitudelist.get(imageFlag_2).toString());
                        ContentValues cValues = new ContentValues();
                        cValues.put("latitude2", CommonData.myLatitudelist.get(imageFlag_2));
                        cValues.put("longitude2", CommonData.myLongitudelist.get(imageFlag_2));
                        if (dbCategory == null) {
                            cValues.put("photo2", "图片名称：" + homeLines.get(position).getView() + "_" + category + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_2) + "_" + CommonData.myLongitudelist.get(imageFlag_2));
                        } else {
                            cValues.put("photo2", "图片名称：" + homeLines.get(position).getView() + "_" + dbCategory + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_2) + "_" + CommonData.myLongitudelist.get(imageFlag_2));
                        }

                        db.update("home_data", cValues, "line_data='" + category_name + "'", null);
                    }
                    if (CommonData.imageBitmaps.containsKey(imageFlag_3)
                            && CommonData.imageBitmaps.get(imageFlag_3) != null) {
                        provinces.setLatitude3(CommonData.myLatitudelist.get(imageFlag_3));
                        provinces.setLongitude3(CommonData.myLongitudelist.get(imageFlag_3));
                        Log.i("保存myLongitudelist3：：：：", CommonData.myLongitudelist.get(imageFlag_3).toString());
                        ContentValues cValues = new ContentValues();
                        cValues.put("latitude3", CommonData.myLatitudelist.get(imageFlag_3));
                        cValues.put("longitude3", CommonData.myLongitudelist.get(imageFlag_3));
                        if (dbCategory == null) {
                            cValues.put("photo3", "图片名称：" + homeLines.get(position).getView() + "_" + category + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_3) + "_" + CommonData.myLongitudelist.get(imageFlag_3));
                        } else {
                            cValues.put("photo3", "图片名称：" + homeLines.get(position).getView() + "_" + dbCategory + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_3) + "_" + CommonData.myLongitudelist.get(imageFlag_3));
                        }
                        db.update("home_data", cValues, "line_data='" + category_name + "'", null);
                    }
                    if (CommonData.imageBitmaps.containsKey(imageFlag_4)
                            && CommonData.imageBitmaps.get(imageFlag_4) != null) {
                        provinces.setLatitude4(CommonData.myLatitudelist.get(imageFlag_4));
                        provinces.setLongitude4(CommonData.myLongitudelist.get(imageFlag_4));
                        Log.i("保存myLongitudelist4：：：：", CommonData.myLongitudelist.get(imageFlag_4).toString());
                        ContentValues cValues = new ContentValues();
                        cValues.put("latitude4", CommonData.myLatitudelist.get(imageFlag_4));
                        cValues.put("longitude4", CommonData.myLongitudelist.get(imageFlag_4));
                        if (dbCategory == null) {
                            cValues.put("photo4", "图片名称：" + homeLines.get(position).getView() + "_" + category + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_4) + "_" + CommonData.myLongitudelist.get(imageFlag_4));
                        } else {
                            cValues.put("photo4", "图片名称：" + homeLines.get(position).getView() + "_" + dbCategory + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_4) + "_" + CommonData.myLongitudelist.get(imageFlag_4));
                        }

                        db.update("home_data", cValues, "line_data='" + category_name + "'", null);
                    }
                    if (CommonData.imageBitmaps.containsKey(imageFlag_5)
                            && CommonData.imageBitmaps.get(imageFlag_5) != null) {
                        provinces.setLatitude5(CommonData.myLatitudelist.get(imageFlag_5));
                        provinces.setLongitude5(CommonData.myLongitudelist.get(imageFlag_5));
                        Log.i("保存myLongitudelist5：：：：", CommonData.myLongitudelist.get(imageFlag_5).toString());
                        ContentValues cValues = new ContentValues();
                        cValues.put("latitude5", CommonData.myLatitudelist.get(imageFlag_5));
                        cValues.put("longitude5", CommonData.myLongitudelist.get(imageFlag_5));
                        if (dbCategory == null) {
                            cValues.put("photo5", "图片名称：" + homeLines.get(position).getView() + "_" + category + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_5) + "_" + CommonData.myLongitudelist.get(imageFlag_5));
                        } else {
                            cValues.put("photo5", "图片名称：" + homeLines.get(position).getView() + "_" + dbCategory + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_5) + "_" + CommonData.myLongitudelist.get(imageFlag_5));
                        }
                        db.update("home_data", cValues, "line_data='" + category_name + "'", null);
                    }
                    if (CommonData.imageBitmaps.containsKey(imageFlag_6)
                            && CommonData.imageBitmaps.get(imageFlag_6) != null) {
                        provinces.setLatitude6(CommonData.myLatitudelist.get(imageFlag_6));
                        provinces.setLongitude6(CommonData.myLongitudelist.get(imageFlag_6));
                        Log.i("保存myLongitudelist6：：：：", CommonData.myLongitudelist.get(imageFlag_6).toString());
                        ContentValues cValues = new ContentValues();
                        cValues.put("latitude6", CommonData.myLatitudelist.get(imageFlag_6));
                        cValues.put("longitude6", CommonData.myLongitudelist.get(imageFlag_6));
                        if (dbCategory == null) {
                            cValues.put("photo6", "图片名称：" + homeLines.get(position).getView() + "_" + category + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_6) + "_" + CommonData.myLongitudelist.get(imageFlag_6));
                        } else {
                            cValues.put("photo6", "图片名称：" + homeLines.get(position).getView() + "_" + dbCategory + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_6) + "_" + CommonData.myLongitudelist.get(imageFlag_6));
                        }
                        db.update("home_data", cValues, "line_data='" + category_name + "'", null);
                    }
                    if (CommonData.imageBitmaps.containsKey(imageFlag_7)
                            && CommonData.imageBitmaps.get(imageFlag_7) != null) {
                        provinces.setLatitude7(CommonData.myLatitudelist.get(imageFlag_7));
                        provinces.setLongitude7(CommonData.myLongitudelist.get(imageFlag_7));
                        Log.i("保存myLongitudelist7：：：：", CommonData.myLongitudelist.get(imageFlag_7).toString());
                        ContentValues cValues = new ContentValues();
                        cValues.put("latitude7", CommonData.myLatitudelist.get(imageFlag_7));
                        cValues.put("longitude7", CommonData.myLongitudelist.get(imageFlag_7));
                        if (dbCategory == null) {
                            cValues.put("photo7", "图片名称：" + homeLines.get(position).getView() + "_" + category + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_7) + "_" + CommonData.myLongitudelist.get(imageFlag_7));
                        } else {
                            cValues.put("photo7", "图片名称：" + homeLines.get(position).getView() + "_" + dbCategory + "_" + category_name + "_" + CommonData.myLatitudelist.get(imageFlag_7) + "_" + CommonData.myLongitudelist.get(imageFlag_7));
                        }

                        db.update("home_data", cValues, "line_data='" + category_name + "'", null);
                    }
                    ContentValues cValues = new ContentValues();
                    cValues.put("altitude", CommonData.altitude.get("altitude"));
                    db.update("home_data", cValues, "line_data='" + category_name + "'", null);
                    provinces.setLine_item_title(category_name);
                    provinces.setLine_item_content1("塔杆全景");
                    provinces.setLine_item_content2("标识牌");
                    provinces.setLine_item_content3("塔杆基础");
                    provinces.setLine_item_content4("塔头");
                    provinces.setLine_item_content5("大号通道");
                    provinces.setLine_item_content6("小号通道");
                    provinces.setLine_item_content7("其他");
//                    lineList.set(checkedDevicePosition, provinces);
                    lineList.add(provinces);
                }
            }
        }
    }

    /**
     * 把Bitmap转Byte
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 图片转成string
     *
     * @param bitmap
     * @return
     */
    public static String convertIconToString(Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] appicon = baos.toByteArray();// 转为byte数组

            return Base64.encodeToString(appicon, Base64.DEFAULT);
        } catch (Exception e) {

        }
        return null;
    }

    //保存图片
    private void sava_photo(String dbLineName, Bitmap bitmap1, String name) {
        String filePath2 = "/sdcard/外采数据/" + dbLineName + "_图片";
        makeRootDirectory2(filePath2, bitmap1, name);

    }

    //创建文件夹,用来判断如果存在就不添加
    private void initDataImportant2() {
        String filePath2 = MYFOLDER + "important2/";
        makeRootDirectory(filePath2);

    }

    //创建文件夹,用来判断如果存在就不添加
    private void initDataImportantContent2(String content) {
        String filePath2 = MYFOLDER + "important2/" + content + "/";
        makeRootDirectory(filePath2);
    }

    public void ExistName(String key) {
        String savePath = MYFOLDER + "important2/" + key;
        File mediaStorageDir = new File(savePath);
        if (!mediaStorageDir.exists()) {
            isExistName = true;
        } else {
            isExistName = false;
        }
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }

    public static void makeRootDirectory2(String filePath, Bitmap bitmap1, String name) {
        File file = null;
        try {
            File path = new File(filePath);
            // 文件
            String filepath = filePath + "/" + name + ".png";
            File f = new File(filepath);
            if (!path.exists()) {
                path.mkdirs();
            }
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fos = null;
            fos = new FileOutputStream(f);
            if (null != fos) {

                if ("540*960".equals(CommonData.photo_size)) {
                    // 获得图片的宽高
                    int width = bitmap1.getWidth();
                    int height = bitmap1.getHeight();
                    // 计算缩放比例
                    float scaleWidth = ((float) 540) / width;
                    float scaleHeight = ((float) 960) / height;
                    // 取得想要缩放的matrix参数
                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleWidth, scaleHeight);
                    // 得到新的图片
                    Bitmap newbm = Bitmap.createBitmap(bitmap1, 0, 0, width, height, matrix, true);
                    newbm.compress(Bitmap.CompressFormat.PNG, 90, fos);
                } else if ("720*1280".equals(CommonData.photo_size)) {
                    // 获得图片的宽高
                    int width = bitmap1.getWidth();
                    int height = bitmap1.getHeight();
                    // 计算缩放比例
                    float scaleWidth = ((float) 720) / width;
                    float scaleHeight = ((float) 1280) / height;
                    // 取得想要缩放的matrix参数
                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleWidth, scaleHeight);
                    // 得到新的图片
                    Bitmap newbm = Bitmap.createBitmap(bitmap1, 0, 0, width, height, matrix, true);
                    newbm.compress(Bitmap.CompressFormat.PNG, 90, fos);
                } else if ("1080*1920".equals(CommonData.photo_size)) {
                    // 获得图片的宽高
                    int width = bitmap1.getWidth();
                    int height = bitmap1.getHeight();
                    // 计算缩放比例
                    float scaleWidth = ((float) 1080) / width;
                    float scaleHeight = ((float) 1920) / height;
                    // 取得想要缩放的matrix参数
                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleWidth, scaleHeight);
                    // 得到新的图片
                    Bitmap newbm = Bitmap.createBitmap(bitmap1, 0, 0, width, height, matrix, true);
                    newbm.compress(Bitmap.CompressFormat.PNG, 90, fos);
                } else {
                    // 获得图片的宽高
                    int width = bitmap1.getWidth();
                    int height = bitmap1.getHeight();
                    // 计算缩放比例
                    float scaleWidth = ((float) 540) / width;
                    float scaleHeight = ((float) 960) / height;
                    // 取得想要缩放的matrix参数
                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleWidth, scaleHeight);
                    // 得到新的图片
                    Bitmap newbm = Bitmap.createBitmap(bitmap1, 0, 0, width, height, matrix, true);
                    newbm.compress(Bitmap.CompressFormat.PNG, 90, fos);
                }

                fos.flush();
                fos.close();
            }

        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LineManagementActivity.this, MainActivity.class);
        startActivity(intent);//进入具体线路页面
        finish();
    }
}
