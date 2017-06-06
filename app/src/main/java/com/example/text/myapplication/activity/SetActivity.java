package com.example.text.myapplication.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.text.myapplication.R;
import com.example.text.myapplication.db.ChannelDao;
import com.example.text.myapplication.db.DBHepler;
import com.example.text.myapplication.domain.Line;
import com.example.text.myapplication.tool.CSVUtils;
import com.example.text.myapplication.tool.CommonData;
import com.example.text.myapplication.view.CallbackBundle;
import com.example.text.myapplication.view.OpenFileDialog;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//设置页面 jiangpan 2017.4.6
public class SetActivity extends BaseActivity {
    private ImageView i_return;
    private RelativeLayout csv,csv2;
    private TextView name2;
    private List<Line> lines = new ArrayList<Line>();
    private ChannelDao dao;
    private ArrayList<String> views = new ArrayList<String>();
    private EditText editText;
    private LinearLayout complete;
    private String name, size;
    private SQLiteDatabase db;
    private Spinner photo_size;
    private ArrayList<String> size_list = new ArrayList<String>();
    static private int openfileDialogId = 0;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        editText = (EditText) findViewById(R.id.name);
        name2=(TextView)findViewById(R.id.name2);
        DBHepler dbHelper = new DBHepler(this, "line_data.db", null, 1);
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("user_data", null, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    int nameColumnIndex = cursor.getColumnIndex("name");
                    name = cursor.getString(nameColumnIndex).toString();
                    Log.i("name：：：：", name);
                    editText.setText(name);
                    name2.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
        }
        Cursor cursor2 = db.query("user_data", null, null, null, null, null, null, null);
        if (cursor2 != null) {
            while (cursor2.moveToNext()) {
                try {
                    int nameColumnIndex = cursor2.getColumnIndex("line_name");
                    number = cursor2.getString(nameColumnIndex).toString();
                    views.add(number);
                    Log.i("数据库line_name：：：：", number);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cursor2.close();
        }
        dao = new ChannelDao(SetActivity.this);
        i_return = (ImageView) findViewById(R.id.i_return);
        csv = (RelativeLayout) findViewById(R.id.csv);
        photo_size = (Spinner) findViewById(R.id.photo_size);
        //保存在手机文件夹里的图片尺寸
        size_list.add("540*960");
        size_list.add("720*1280");
        size_list.add("1080*1920");
        ArrayAdapter<String> a1 = new ArrayAdapter<String>(SetActivity.this,
                android.R.layout.simple_spinner_item, size_list);
        photo_size.setAdapter(a1);
        photo_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                size = photo_size.getSelectedItem().toString();
                CommonData.photo_size = size;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        i_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        csv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportCsv(views);
                Toast.makeText(getApplication(), "CVS文件已经保存在/sdcard/外采数据/", Toast.LENGTH_SHORT).show();

            }
        });

        complete = (LinearLayout) findViewById(R.id.complete);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editText.getText().toString();
                name2.setVisibility(View.VISIBLE);
                CommonData.name = name;
                Data(name);
                Toast.makeText(getApplication(), "保存成功！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SetActivity.this, MainActivity.class);
                startActivity(intent);//进入具体线路页面
                finish();
            }
        });
        csv2 = (RelativeLayout) findViewById(R.id.csv2);
        csv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importCsv();
            }
        });
    }

    //导入
    public void importCsv() {

        showDialog(openfileDialogId);

    }

    //导出
    public void exportCsv(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            List<Line> dataList = dao.listCache();
            lines.clear();
            for (Line line : dataList) {
                if (line != null)
                    lines.add(line);
            }
            boolean isSuccess = CSVUtils.exportCsv(new File("/sdcard/外采数据/CSV导出/" + list.get(i) + ".csv"), lines, list.get(i));
            System.out.println(isSuccess);
        }

    }

    //保存到数据库
    public void Data(String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        db.insert("user_data", null, values);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == openfileDialogId) {
            Map<String, Integer> images = new HashMap<String, Integer>();
            // 下面几句设置各文件类型的图标， 需要你先把图标添加到资源文件夹
            images.put(OpenFileDialog.sRoot, R.drawable.filedialog_root);    // 根目录图标
            images.put(OpenFileDialog.sParent, R.drawable.filedialog_folder_up);    //返回上一层的图标
            images.put(OpenFileDialog.sFolder, R.drawable.filedialog_folder);    //文件夹图标
            images.put("wav", R.drawable.filedialog_wavfile);    //wav文件图标
            images.put(OpenFileDialog.sEmpty, R.drawable.filedialog_root);
            Dialog dialog = OpenFileDialog.createDialog(id, this, "打开文件", new CallbackBundle() {
                        @Override
                        public void callback(Bundle bundle) {
                            String filepath = bundle.getString("path");
                            setTitle(filepath); // 把文件路径显示在标题上
                        }
                    },
                    ".csv;",
                    images);
            return dialog;
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
