// filename: OpenFileDialog.java
package com.example.text.myapplication.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.text.myapplication.R;
import com.example.text.myapplication.activity.MainActivity;
import com.example.text.myapplication.db.ChannelDao;
import com.example.text.myapplication.db.DBHepler;
import com.example.text.myapplication.domain.Line;
import com.example.text.myapplication.tool.CSVUtils;

public class OpenFileDialog {
    public static String tag = "OpenFileDialog";
    static final public String sRoot = "/";
    static final public String sParent = "..";
    static final public String sFolder = ".";
    static final public String sEmpty = "";
    static final private String sOnErrorMsg = "No rights to access!";
    //数据库
    public static ChannelDao dao;
    public static String line_name2;
    public static String line_data;
    public static SQLiteDatabase db;
    // 参数说明
    // context:上下文
    // dialogid:对话框ID
    // title:对话框标题
    // callback:一个传递Bundle参数的回调接口
    // suffix:需要选择的文件后缀，比如需要选择wav、mp3文件的时候设置为".wav;.mp3;"，注意最后需要一个分号(;)
    // images:用来根据后缀显示的图标资源ID。
    //  根目录图标的索引为sRoot;
    //  父目录的索引为sParent;
    //  文件夹的索引为sFolder;
    //  默认图标的索引为sEmpty;
    //  其他的直接根据后缀进行索引，比如.wav文件图标的索引为"wav"
    public static Context context;
    public static int max_position;

    public static Dialog createDialog(int id, Context context, String title, CallbackBundle callback, String suffix, Map<String, Integer> images) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(new FileSelectView(context, id, callback, suffix, images));
        Dialog dialog = builder.create();
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle(title);
        dao = dao == null ? new ChannelDao(context) : dao;
        DBHepler dbHelper = new DBHepler(context, "line_data.db", null, 1);
        db = dbHelper.getWritableDatabase();
        OpenFileDialog.context = context;
        return dialog;
    }

    static class FileSelectView extends ListView implements OnItemClickListener {
        private CallbackBundle callback = null;
        //        private String path = sRoot;
        private String path = "/sdcard/外采数据/CSV导入/";
        private List<Map<String, Object>> list = null;
        private int dialogid = 0;

        private String suffix = null;

        private Map<String, Integer> imagemap = null;

        public FileSelectView(Context context, int dialogid, CallbackBundle callback, String suffix, Map<String, Integer> images) {
            super(context);
            this.imagemap = images;
            this.suffix = suffix == null ? "" : suffix.toLowerCase();
            this.callback = callback;
            this.dialogid = dialogid;
            this.setOnItemClickListener(this);
            refreshFileList();
        }

        private String getSuffix(String filename) {
            int dix = filename.lastIndexOf('.');
            if (dix < 0) {
                return "";
            } else {
                return filename.substring(dix + 1);
            }
        }

        private int getImageId(String s) {
            if (imagemap == null) {
                return 0;
            } else if (imagemap.containsKey(s)) {
                return imagemap.get(s);
            } else if (imagemap.containsKey(sEmpty)) {
                return imagemap.get(sEmpty);
            } else {
                return 0;
            }
        }

        private int refreshFileList() {
            // 刷新文件列表
            File[] files = null;
            try {
                files = new File(path).listFiles();
            } catch (Exception e) {
                files = null;
            }
            if (files == null) {
                // 访问出错
                Toast.makeText(getContext(), sOnErrorMsg, Toast.LENGTH_SHORT).show();
                return -1;
            }
            if (list != null) {
                list.clear();
            } else {
                list = new ArrayList<Map<String, Object>>(files.length);
            }

            // 用来先保存文件夹和文件夹的两个列表
            ArrayList<Map<String, Object>> lfolders = new ArrayList<Map<String, Object>>();
            ArrayList<Map<String, Object>> lfiles = new ArrayList<Map<String, Object>>();

            if (!this.path.equals(sRoot)) {
                // 添加根目录 和 上一层目录
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", sRoot);
                map.put("path", sRoot);
                map.put("img", getImageId(sRoot));
                list.add(map);

                map = new HashMap<String, Object>();
                map.put("name", sParent);
                map.put("path", path);
                map.put("img", getImageId(sParent));
                list.add(map);
            }

            for (File file : files) {
                if (file.isDirectory() && file.listFiles() != null) {
                    // 添加文件夹
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("name", file.getName());
                    map.put("path", file.getPath());
                    map.put("img", getImageId(sFolder));
                    lfolders.add(map);
                } else if (file.isFile()) {
                    // 添加文件
                    String sf = getSuffix(file.getName()).toLowerCase();
                    if (suffix == null || suffix.length() == 0 || (sf.length() > 0 && suffix.indexOf("." + sf + ";") >= 0)) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("name", file.getName());
                        map.put("path", file.getPath());
                        map.put("img", getImageId(sf));
                        lfiles.add(map);
                    }
                }
            }
            list.addAll(lfolders); // 先添加文件夹，确保文件夹显示在上面
            list.addAll(lfiles);    //再添加文件
            SimpleAdapter adapter = new SimpleAdapter(getContext(), list, R.layout.filedialogitem, new String[]{"img", "name", "path"}, new int[]{R.id.filedialogitem_img, R.id.filedialogitem_name, R.id.filedialogitem_path});
            this.setAdapter(adapter);
            return files.length;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            // 条目选择
            String pt = (String) list.get(position).get("path");
            String fn = (String) list.get(position).get("name");
            if (fn.equals(sRoot) || fn.equals(sParent)) {
                // 如果是更目录或者上一层
                File fl = new File(pt);
                String ppt = fl.getParent();
                if (ppt != null) {
                    // 返回上一层
                    path = ppt;
                } else {
                    // 返回更目录
                    path = sRoot;
                }
            } else {
                File fl = new File(pt);
                if (fl.isFile()) {

                    //当存在数据库时，有最大值，否则没有

                    List<Line> dataList2 = dao.listCache();
                    List<Integer> list_position = new ArrayList<Integer>();
                    for (Line line2 : dataList2) {
                        if (line2 != null) {
                            list_position.add(line2.getPosition());
                            max_position = Collections.max(list_position);
                        }
                    }
                    List<Line> lines = CSVUtils.importCsv(new File("/sdcard/外采数据/CSV导入/" + fn + "/"), max_position);

                    if (lines != null && !lines.isEmpty()) {
                        for (Line data2 : lines) {
                            if (data2 != null) {
                                line_name2 = data2.getLine_name();
                                line_data=data2.getLine_item_title();
                            }
                            boolean result = query_data(line_name2);
                            if (!result) {//不能添加重复数据
                                Data(line_name2,lines.size());
                            }
                            boolean result2 = query_data2(line_data);
                            if (!result2) {//不能添加重复数据
                                dao.addCache(data2);
                            }
                        }
                        Toast.makeText(context, "导入成功，请到首页查看！", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, "文件为空，不能添加！", Toast.LENGTH_SHORT).show();
                    }
                    MainActivity.query_user_data();
                    // 如果是文件
                    ((Activity) getContext()).dismissDialog(this.dialogid); // 让文件夹对话框消失

                    // 设置回调的返回值
                    Bundle bundle = new Bundle();
                    bundle.putString("path", pt);
                    bundle.putString("name", fn);
                    // 调用事先设置的回调函数
                    this.callback.callback(bundle);
                    return;
                } else if (fl.isDirectory()) {
                    // 如果是文件夹
                    // 那么进入选中的文件夹
                    path = pt;
                }
            }
            this.refreshFileList();
        }
    }

    //保存到数据库
    public static void Data(String line_name,int file_number) {
        ContentValues values = new ContentValues();
        values.put("line_name", line_name);
        values.put("file_number", file_number);
        db.insert("user_data", null, values);
    }

    public static boolean query_data(String line_name) {
        boolean flag = false;
        DBHepler dbHelper = new DBHepler(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("user_data", null, "line_name=?  ",
                new String[]{line_name}, null, null, null);
        if (cursor.getCount() > 0) {
            flag = true;
        }
        return flag;
    }

    public static boolean query_data2(String line_data) {
        boolean flag = false;
        DBHepler dbHelper = new DBHepler(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("home_data", null, "line_data=?  ",
                new String[]{line_data}, null, null, null);
        if (cursor.getCount() > 0) {
            flag = true;
        }
        return flag;
    }
}
