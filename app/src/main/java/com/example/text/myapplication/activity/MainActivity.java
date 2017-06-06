package com.example.text.myapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.text.myapplication.R;
import com.example.text.myapplication.adapter.HomeListViewAdapter;
import com.example.text.myapplication.db.ChannelDao;
import com.example.text.myapplication.db.DBHepler;
import com.example.text.myapplication.domain.HomeLine;
import com.example.text.myapplication.domain.Line;
import com.example.text.myapplication.tool.CommonData;
import com.example.text.myapplication.view.pulltorefresh.PullToRefreshListView;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


//首页 jiangpan 2017.4.6
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshListView pullToRefreshListView;//刷新
    private ListView listView;
    private LinearLayout add;//添加按钮
    private ImageView set;//设置按钮
    public static String number, number_one;
    private static HomeListViewAdapter homeListViewAdapter;//首页适配器
    public static SQLiteDatabase db;
    private static String name, name_one, line_name;
    //数据库
    private ChannelDao dao;
    private static int myposition, user_position;
    public static List<Line> lineList1 = new ArrayList<Line>();//线路数据1
    private static List<HomeLine> homeLines = new ArrayList<HomeLine>();
    private List<Integer> mypositions = new ArrayList<Integer>();
    private static List<Integer> positions = new ArrayList<Integer>();
    private List<String> line_names = new ArrayList<String>();
    /**
     * 判断是否退出程序
     */
    private long mExitTime;
    private static final String MYFOLDER = "/sdcard/外采数据/";
    private boolean isExistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        query_home_data();
        query_user_data();
        initData();
        initDataCsvExport();
        initDataCsvImport();

    }

    private void init() {
        DBHepler dbHelper = new DBHepler(this, "line_data.db", null, 1);
        db = dbHelper.getWritableDatabase();
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pcontent_list);
        listView = pullToRefreshListView.getRefreshableView();
        add = (LinearLayout) findViewById(R.id.add);
        add.setOnClickListener(this);
        set = (ImageView) findViewById(R.id.set);
        set.setOnClickListener(this);
        homeListViewAdapter = new HomeListViewAdapter(this, homeLines);
        listView.setAdapter(homeListViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, LineManagementActivity.class);
                intent.putExtra("number", homeLines.get(position - 1).getView());
                intent.putExtra("position", position - 1);
                startActivity(intent);//进入具体线路页面

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //定义AlertDialog.Builder对象，当长按列表项的时候弹出确认删除对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("确定删除?");
                builder.setTitle("提示");
                //添加AlertDialog.Builder对象的setPositiveButton()方法
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        query_home_data();
                        delfromName(homeLines.get(position - 1).getView());
                        if (mypositions.size() > 0) {
                            if (lineList1.size() > position - 1) {
                                delfromPosition(position);
                            }
                        }
                        delete_photo(homeLines.get(position - 1).getView());
                        delete(homeLines.get(position - 1).getView());
                        homeLines.remove(position - 1).getView();
                        homeListViewAdapter.notifyDataSetChanged();
                        System.out.println("success");
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
    }

    //查询数据库home_data
    public void query_home_data() {
        mypositions.clear();
        dao = dao == null ? new ChannelDao(MainActivity.this) : dao;
        List<Line> dataList2 = dao.listCache();
        for (Line line2 : dataList2) {
            if (line2 != null) {
                myposition = line2.getPosition();
                mypositions.add(myposition);
                line_name = line2.getLine_name();
                lineList1.add(line2);
                line_names.add(line_name);
            }

        }
    }

    //查询数据库user_data
    public static void query_user_data() {
        homeLines.clear();
        positions.clear();
        Cursor cursor = db.query("user_data", null, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    int nameColumnIndex = cursor.getColumnIndex("line_name");
                    int nameColumnIndex2 = cursor.getColumnIndex("file_number");
                    int nameColumnIndex3 = cursor.getColumnIndex("position");
                    user_position = cursor.getInt(nameColumnIndex3);
                    positions.add(user_position);
                    number_one = cursor.getString(nameColumnIndex);
                    int file_number = cursor.getInt(nameColumnIndex2);
                    HomeLine homeLine = new HomeLine();
                    if (number_one != null) {
                        number = number_one.toString();
                        Log.i("数据库line_name：：：：", number);
                        homeLine.setView(number);
                        homeLine.setFile_number(file_number);
                        homeLines.add(homeLine);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            homeListViewAdapter.notifyDataSetChanged();
            cursor.close();
        }
    }

    //创建1级文件夹
    private void initData() {
        String filePath = MYFOLDER;
        makeRootDirectory(filePath);
    }

    //创建csv导出文件夹
    private void initDataCsvExport() {
        String filePath = MYFOLDER + "CSV导出/";
        makeRootDirectory(filePath);
    }

    //创建csv导入文件夹
    private void initDataCsvImport() {
        String filePath = MYFOLDER + "CSV导入/";
        makeRootDirectory(filePath);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set://进入设置页面
                Intent intent = new Intent(MainActivity.this, SetActivity.class);
                startActivity(intent);
                break;
            case R.id.add://输入编号
                if (name == null) {
                    Toast.makeText(getApplication(), "请先在设置里输入姓名！", Toast.LENGTH_SHORT).show();
                } else {
                    // 1. 布局文件转换为View对象
                    LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                    LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.edittext, null);
                    // 2. 新建对话框对象
                    final Dialog dialog = new AlertDialog.Builder(MainActivity.this).create();
                    dialog.setCancelable(false);
                    dialog.show();
                    dialog.setContentView(layout);
                    WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                    params.width = WindowManager.LayoutParams.WRAP_CONTENT / 2;
                    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    dialog.getWindow().setAttributes(params);
                    //弹出软键盘
                    dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    final EditText e1 = (EditText) layout.findViewById(R.id.e);
                    Button btnOK = (Button) layout.findViewById(R.id.dialog_ok);
                    btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            number = e1.getText().toString();
                            Log.i("edittext_content：：：：", number);
                            homeListViewAdapter.notifyDataSetChanged();
                            try {
                                if (e1 == null || e1.getText().toString().trim().length() == 0) {
                                    //不关闭对话框
                                    Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                                    field.setAccessible(true);
                                    field.set(dialog, false);   //设定为false,则不可以关闭对话框
                                    dialog.dismiss();
                                    Toast.makeText(getApplication(), "内容不能为空", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (e1 != null || e1.getText().toString().trim().length() != 0) {
                                        HomeLine homeLine = new HomeLine();
                                        ExistName(number);
                                        if (isExistName == false) {
                                            Toast.makeText(MainActivity.this, "此线路已存在，不能重复添加！", Toast.LENGTH_SHORT).show();
                                        } else {
                                            homeLine.setView(number);
                                            homeLines.add(homeLine);
                                            initDataImportant();
                                            initDataImportantContent(number);
                                            Toast.makeText(getApplication(), "文件夹已保存在/sdcard/外采数据/", Toast.LENGTH_LONG).show();
                                            Data(number);//添加到数据库
                                            query_user_data();
                                            if (positions.size() > 0) {
                                                if (!positions.contains(homeLines.size())) {
                                                    DataPosition(homeLines.size(), homeLines.get(homeLines.size() - 1).getView());
                                                } else {
                                                    DataPosition(homeLines.size() + 1, homeLines.get(homeLines.size() - 1).getView());
                                                }
                                            } else {
                                                DataPosition(homeLines.size(), homeLines.get(homeLines.size() - 1).getView());
                                            }
                                            CommonData.line_name = number;
                                            homeListViewAdapter.notifyDataSetChanged();
                                        }

                                    }

                                    // 要正常关闭对话框
                                    Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                                    field.setAccessible(true);
                                    field.set(dialog, true);//设定为true,则可以关闭对话框
                                    dialog.dismiss();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    // 5. 取消按钮
                    Button btnCancel = (Button) layout.findViewById(R.id.dialog_cancel);
                    btnCancel.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            try {
                                Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                                field.setAccessible(true);
                                field.set(dialog, true);//设定为true,则可以关闭对话框
                                dialog.dismiss();
                            } catch (Exception e) {

                            }
                        }
                    });
                }
                break;
        }
    }


    //创建文件夹,用来判断如果存在就不添加
    private void initDataImportant() {
        String filePath2 = MYFOLDER + "important/";
        makeRootDirectory(filePath2);

    }

    //创建文件夹,用来判断如果存在就不添加
    private void initDataImportantContent(String content) {
        String filePath2 = MYFOLDER + "important/" + content;
        makeRootDirectory(filePath2);
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

    //保存到数据库
    public void Data(String line_name) {
        ContentValues values = new ContentValues();
        values.put("line_name", line_name);
        values.put("file_number", 0);
        db.insert("user_data", null, values);
    }

    //保存到数据库position
    public void DataPosition(int position, String user_number) {
        ContentValues cValues = new ContentValues();
        cValues.put("position", position);
        db.update("user_data", cValues, "line_name='" + user_number + "'", null);
    }


    // 删除users表的记录
    public int delfromName(String number) {
        int id = db.delete("user_data",
                "line_name='" + number + "'", null);
        Log.e("DelUserInfo", id + "");
        return id;
    }

    // 删除position
    public int delfromPosition(int position) {
        int id2 = db.delete("home_data",
                "position='" + position + "'", null);
        Log.e("DelUserInfo", id2 + "");
        return id2;
    }

    //删除文件夹和文件夹里面的文件
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
        dir.delete();// 删除目录本身
    }

    //删除文件夹和文件夹里面的文件
    public static void delete(String line_name) {
        String path = MYFOLDER + "important/" + line_name;
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

    public void ExistName(String key) {
        String savePath = MYFOLDER + "important/" + key;
        File mediaStorageDir = new File(savePath);
        if (!mediaStorageDir.exists()) {
            isExistName = true;
        } else {
            isExistName = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Cursor cursor2 = db.query("user_data", null, null, null, null, null, null, null);
        if (cursor2 != null) {
            while (cursor2.moveToNext()) {
                try {
                    int nameColumnIndex = cursor2.getColumnIndex("name");
                    name_one = cursor2.getString(nameColumnIndex);
                    if (name_one != null) {
                        name = name_one.toString();
                        Log.i("name：：：：", name);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cursor2.close();
        }

    }

    //    // 回退键
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            exit();
//            return false;
//        } else {
//            return super.onKeyDown(keyCode, event);
//        }
//    }
//
//    // 双击退出
//    private void exit() {
//        if ((System.currentTimeMillis() - mExitTime) > 2000) {
//            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//            mExitTime = System.currentTimeMillis();
//        } else {
//            finish();
//        }
//    }
//重写onKeyDown方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断当点击的是返回键
        if (keyCode == event.KEYCODE_BACK) {
            exit();//退出方法
        }
        return true;
    }

    private long time = 0;


//退出方法

    private void exit() {

//如果在两秒大于2秒

        if (System.currentTimeMillis() - time > 2000) {

//获得当前的时间

            time = System.currentTimeMillis();

            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();

        } else {

//点击在两秒以内

            removeALLActivity();//执行移除所以Activity方法

        }

    }
}
