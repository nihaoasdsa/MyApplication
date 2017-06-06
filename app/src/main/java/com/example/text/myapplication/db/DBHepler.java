package com.example.text.myapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jiangpan on 2017/4/7.
 * 数据库
 */

public class DBHepler extends SQLiteOpenHelper {
    public static final String DB_NAME = "line_data.db";
    public static final int VERSION = 1;
    public static final String TABLE_CHANNEL = "home_data";
    public static final String USERDATA = "user_data";
    public static final String LINE_NAME="line_name";
    public static final String LINE_DATA="line_data";
    public static final String ATTRIBUTE1="attribute1";
    public static final String ATTRIBUTE2="attribute2";
    public static final String ATTRIBUTE3="attribute3";
    public static final String ATTRIBUTE4="attribute4";
    public static final String ATTRIBUTE5="attribute5";
    public static final String ATTRIBUTE6="attribute6";
    public static final String ATTRIBUTE7="attribute7";
    public static final String PHOTO="photo";
    public static final String PHOTO2="photo2";
    public static final String PHOTO3="photo3";
    public static final String PHOTO4="photo4";
    public static final String PHOTO5="photo5";
    public static final String PHOTO6="photo6";
    public static final String PHOTO7="photo7";
    public static final String NAME="name";
    public static final String LATITUDE="latitude";
    public static final String LONGITUDE="longitude";

    public static final String LATITUDE2="latitude2";
    public static final String LONGITUDE2="longitude2";

    public static final String LATITUDE3="latitude3";
    public static final String LONGITUDE3="longitude3";

    public static final String LATITUDE4="latitude4";
    public static final String LONGITUDE4="longitude4";

    public static final String LATITUDE5="latitude5";
    public static final String LONGITUDE5="longitude5";

    public static final String LATITUDE6="latitude6";
    public static final String LONGITUDE6="longitude6";

    public static final String LATITUDE7="latitude7";
    public static final String LONGITUDE7="longitude7";
    private Context context;
    public static final String PICTURE = "picture";
    public static final String PICTURE2 = "picture2";
    public static final String PICTURE3 = "picture3";
    public static final String PICTURE4 = "picture4";
    public static final String PICTURE5 = "picture5";
    public static final String PICTURE6 = "picture6";
    public static final String PICTURE7 = "picture7";
    public static final String ALTITUDE="altitude";
    public static final String DEVICEID="deviceId";
    public static final String DEVICEPOSITION="devicePosition";
    public static final String POSITION="position";
    public static final String FILENUMBER="file_number";
    public static final String CATEGORY="category";
    public static final String FILE="file";
    public DBHepler(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;
    }
    public DBHepler(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub

    }
    public Context getContext(){
        return context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String data = "create table if not exists "+TABLE_CHANNEL+
                "(_id integer primary key autoincrement," +
                LINE_NAME+" TEXT , " +
                LINE_DATA+" TEXT , " +
                FILE+" TEXT , "+
                ATTRIBUTE1+" TEXT , " +
                ATTRIBUTE2+" TEXT , " +
                ATTRIBUTE3+" TEXT , " +
                ATTRIBUTE4+" TEXT , " +
                ATTRIBUTE5+" TEXT , " +
                ATTRIBUTE6+" TEXT , " +
                ATTRIBUTE7+" TEXT , " +
                PICTURE+" BLOB , " +
                PICTURE2+" BLOB , " +
                PICTURE3+" BLOB , " +
                PICTURE4+" BLOB , " +
                PICTURE5+" BLOB , " +
                PICTURE6+" BLOB , " +
                PICTURE7+" BLOB , " +
                LATITUDE+" TEXT , " +
                LONGITUDE+ " TEXT , "+
                LATITUDE2+" TEXT , "+
                LONGITUDE2+" TEXT , "+
                LATITUDE3+" TEXT , "+
                LONGITUDE3+" TEXT , "+
                LATITUDE4+" TEXT , "+
                LONGITUDE4+" TEXT , "+
                LATITUDE5+" TEXT , "+
                LONGITUDE5+" TEXT , "+
                LATITUDE6+" TEXT , "+
                LONGITUDE6+" TEXT , "+
                LATITUDE7+" TEXT , "+
                LONGITUDE7+" TEXT , "+
                PHOTO+" TEXT , "+
                PHOTO2+" TEXT , "+
                PHOTO3+" TEXT , "+
                PHOTO4+" TEXT , "+
                PHOTO5+" TEXT , "+
                PHOTO6+" TEXT , "+
                PHOTO7+" TEXT , "+
                DEVICEID+" INTEGER , "+
                DEVICEPOSITION+" INTEGER , "+
                POSITION+" INTEGER , "+
                CATEGORY+" TEXT , "+
                ALTITUDE+" TEXT)";
        db.execSQL(data);
        //设备信息
        String data2 = "create table if not exists equipment("
                + "_id integer primary key autoincrement," + "deviceId integer,"
                + "devicePosition integer,"
                + "deviceName text)";
        db.execSQL(data2);

        String data3 = "create table if not exists "+USERDATA+
                "(_id integer primary key autoincrement," +
                NAME+" TEXT , "+
                POSITION+" INTEGER , "+
                FILENUMBER+" INTEGER , "+
                LINE_NAME+" TEXT )";
        db.execSQL(data3);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
