package com.example.text.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.text.myapplication.activity.DeviceCtegoryModel;
import com.example.text.myapplication.domain.Line;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChannelDao implements ChannelDaoInface {
    private DBHepler helper;
    private Bitmap bitmap1 = null;
    private Bitmap bitmap2 = null;
    private Bitmap bitmap3 = null;
    private Bitmap bitmap4 = null;
    private Bitmap bitmap5 = null;
    private Bitmap bitmap6 = null;
    private Bitmap bitmap7 = null;

    public ChannelDao(Context context) {
        helper = null;
        helper = new DBHepler(context);
    }

    public boolean addCache(Line item) {
        boolean flag = false;
        SQLiteDatabase database = null;
        long id = -1;
        try {
            database = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            if (item.getLine_name() != null && !"".equals(item.getLine_name()))
                values.put(DBHepler.LINE_NAME, item.getLine_name());
            if (item.getLine_item_title() != null && !"".equals(item.getLine_item_title()))
                values.put(DBHepler.LINE_DATA, item.getLine_item_title());
            if (item.getLine_item_content1() != null && !"".equals(item.getLine_item_content1()))
                values.put(DBHepler.ATTRIBUTE1, item.getLine_item_content1());
            if (item.getLine_item_content2() != null && !"".equals(item.getLine_item_content2()))
                values.put(DBHepler.ATTRIBUTE2, item.getLine_item_content2());
            if (item.getLine_item_content3() != null && !"".equals(item.getLine_item_content3()))
                values.put(DBHepler.ATTRIBUTE3, item.getLine_item_content3());
            if (item.getLine_item_content4() != null && !"".equals(item.getLine_item_content4()))
                values.put(DBHepler.ATTRIBUTE4, item.getLine_item_content4());
            if (item.getLine_item_content5() != null && !"".equals(item.getLine_item_content5()))
                values.put(DBHepler.ATTRIBUTE5, item.getLine_item_content5());
            if (item.getLine_item_content6() != null && !"".equals(item.getLine_item_content6()))
                values.put(DBHepler.ATTRIBUTE6, item.getLine_item_content6());
            if (item.getLine_item_content7() != null && !"".equals(item.getLine_item_content7()))
                values.put(DBHepler.ATTRIBUTE7, item.getLine_item_content7());
            if (item.getLatitude1() != null && !"".equals(item.getLatitude1()))
                values.put(DBHepler.LATITUDE, item.getLatitude1());
            if (item.getLongitude1() != null && !"".equals(item.getLongitude1()))
                values.put(DBHepler.LONGITUDE, item.getLongitude1());
            if (item.getLatitude2() != null && !"".equals(item.getLatitude2()))
                values.put(DBHepler.LATITUDE2, item.getLatitude2());
            if (item.getLongitude2() != null && !"".equals(item.getLongitude2()))
                values.put(DBHepler.LONGITUDE2, item.getLongitude2());
            if (item.getLatitude3() != null && !"".equals(item.getLatitude3()))
                values.put(DBHepler.LATITUDE3, item.getLatitude3());
            if (item.getLongitude3() != null && !"".equals(item.getLongitude3()))
                values.put(DBHepler.LONGITUDE3, item.getLongitude3());
            if (item.getLatitude4() != null && !"".equals(item.getLatitude4()))
                values.put(DBHepler.LATITUDE4, item.getLatitude4());
            if (item.getLongitude4() != null && !"".equals(item.getLongitude4()))
                values.put(DBHepler.LONGITUDE4, item.getLongitude4());
            if (item.getLatitude5() != null && !"".equals(item.getLatitude5()))
                values.put(DBHepler.LATITUDE5, item.getLatitude5());
            if (item.getLongitude5() != null && !"".equals(item.getLongitude5()))
                values.put(DBHepler.LONGITUDE5, item.getLongitude5());
            if (item.getLatitude6() != null && !"".equals(item.getLatitude6()))
                values.put(DBHepler.LATITUDE6, item.getLatitude6());
            if (item.getLongitude6() != null && !"".equals(item.getLongitude6()))
                values.put(DBHepler.LONGITUDE6, item.getLongitude6());
            if (item.getLatitude7() != null && !"".equals(item.getLatitude7()))
                values.put(DBHepler.LATITUDE7, item.getLatitude7());
            if (item.getLongitude7() != null && !"".equals(item.getLongitude7()))
                values.put(DBHepler.LONGITUDE7, item.getLongitude7());
            if (item.getView() != null && !"".equals(item.getView()))
                values.put(DBHepler.FILE, item.getView());
            if (item.getLine_item_img1() != null && !"".equals(item.getLine_item_img1()))
                values.put(DBHepler.PICTURE, convertIconToString(item.getLine_item_img1()));
            if (item.getLine_item_img2() != null && !"".equals(item.getLine_item_img2()))
                values.put(DBHepler.PICTURE2, convertIconToString(item.getLine_item_img2()));
            if (item.getLine_item_img3() != null && !"".equals(item.getLine_item_img3()))
                values.put(DBHepler.PICTURE3, convertIconToString(item.getLine_item_img3()));
            if (item.getLine_item_img4() != null && !"".equals(item.getLine_item_img4()))
                values.put(DBHepler.PICTURE4, convertIconToString(item.getLine_item_img4()));
            if (item.getLine_item_img5() != null && !"".equals(item.getLine_item_img5()))
                values.put(DBHepler.PICTURE5, convertIconToString(item.getLine_item_img5()));
            if (item.getLine_item_img6() != null && !"".equals(item.getLine_item_img6()))
                values.put(DBHepler.PICTURE6, convertIconToString(item.getLine_item_img6()));
            if (item.getLine_item_img7() != null && !"".equals(item.getLine_item_img7()))
                values.put(DBHepler.PICTURE7, convertIconToString(item.getLine_item_img7()));
            if (item.getPhoto_name() != null && !"".equals(item.getPhoto_name()))
                values.put(DBHepler.PHOTO, item.getPhoto_name());
            if (item.getPhoto_name2() != null && !"".equals(item.getPhoto_name2()))
                values.put(DBHepler.PHOTO2, item.getPhoto_name2());
            if (item.getPhoto_name3() != null && !"".equals(item.getPhoto_name3()))
                values.put(DBHepler.PHOTO3, item.getPhoto_name3());
            if (item.getPhoto_name4() != null && !"".equals(item.getPhoto_name4()))
                values.put(DBHepler.PHOTO4, item.getPhoto_name4());
            if (item.getPhoto_name5() != null && !"".equals(item.getPhoto_name5()))
                values.put(DBHepler.PHOTO5, item.getPhoto_name5());
            if (item.getPhoto_name6() != null && !"".equals(item.getPhoto_name6()))
                values.put(DBHepler.PHOTO6, item.getPhoto_name6());
            if (item.getPhoto_name7() != null && !"".equals(item.getPhoto_name7()))
                values.put(DBHepler.PHOTO7, item.getPhoto_name7());
            if (item.getAltitude() != null && !"".equals(item.getAltitude()))
                values.put(DBHepler.ALTITUDE, item.getAltitude());
            if ("0".equals(String.valueOf(item.getDeviceCtegoryModel().getDeviceId()))&&"null".equals(String.valueOf(item.getDeviceCtegoryModel().getDeviceId()))&&String.valueOf(item.getDeviceCtegoryModel().getDeviceId())!=null&&!"".equals(item.getDeviceCtegoryModel().getDeviceId()))
                values.put(DBHepler.DEVICEID, item.getDeviceCtegoryModel().getDeviceId());
            if ("0".equals(String.valueOf(item.getDeviceCtegoryModel().getDevicePosition()))&&"null".equals(String.valueOf(item.getDeviceCtegoryModel().getDevicePosition()))&&String.valueOf(item.getDeviceCtegoryModel().getDevicePosition())!=null&&!"".equals(item.getDeviceCtegoryModel().getDevicePosition()))
                values.put(DBHepler.DEVICEPOSITION, item.getDeviceCtegoryModel().getDevicePosition());
            if (!"".equals(item.getCategory()))
                values.put(DBHepler.CATEGORY, item.getCategory());
            if (!"".equals(item.getPosition()))
                values.put(DBHepler.POSITION, item.getPosition());
            id = database.insert(DBHepler.TABLE_CHANNEL, null, values);
            flag = (id != -1 ? true : false);
        } catch (Exception e) {
            // TODO: handle exception
            Log.i("数据库报错", e.getStackTrace().toString());
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return flag;
    }

    @Override
    public boolean updateCache(ContentValues values, String whereClause, String[] whereArgs) {
        boolean flag = false;
        SQLiteDatabase database = null;
        int count = 0;
        try {
            database = helper.getWritableDatabase();

            count = database.update(DBHepler.TABLE_CHANNEL, values, whereClause, whereArgs);
            flag = (count > 0 ? true : false);
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return flag;
    }

    @Override
    public boolean deleteCache(String whereClause, String[] whereArgs) {
        boolean flag = false;
        SQLiteDatabase database = null;
        try {
            database = helper.getWritableDatabase();
            database.beginTransaction();
            database.delete(DBHepler.TABLE_CHANNEL, null, null);
            database.setTransactionSuccessful();
            flag = true;
        } catch (Exception e) {

        } finally {
            database.endTransaction();
            if (database != null) {
                database.close();
            }
        }
        return flag;
    }

    public boolean deleteAllCache() {
        boolean flag = false;
        SQLiteDatabase database = null;
        try {
            database = helper.getWritableDatabase();
            database.beginTransaction();
            database.delete(DBHepler.TABLE_CHANNEL, null, null);
            database.setTransactionSuccessful();
            flag = true;
        } catch (Exception e) {

        } finally {
            database.endTransaction();
            if (database != null) {
                database.close();
            }
        }
        return flag;
    }

    public ArrayList<Line> listCache() {
        ArrayList<Line> list = null;
        SQLiteDatabase database = null;
        try {
            database = helper.getReadableDatabase();
            Cursor cursor = database.rawQuery("select * from " + DBHepler.TABLE_CHANNEL, null);
//            Cursor cursor=database.query(SQLHelper.TABLE_CHANNEL,null,null,null,null,null,null);
            list = new ArrayList<Line>();

            while (cursor.moveToNext()) {

                String line_name = cursor.getString(cursor.getColumnIndex(DBHepler.LINE_NAME));
                String line_data = cursor.getString(cursor.getColumnIndex(DBHepler.LINE_DATA));
                String attribute1 = cursor.getString(cursor.getColumnIndex(DBHepler.ATTRIBUTE1));
                String attribute2 = cursor.getString(cursor.getColumnIndex(DBHepler.ATTRIBUTE2));
                String attribute3 = cursor.getString(cursor.getColumnIndex(DBHepler.ATTRIBUTE3));
                String attribute4 = cursor.getString(cursor.getColumnIndex(DBHepler.ATTRIBUTE4));
                String attribute5 = cursor.getString(cursor.getColumnIndex(DBHepler.ATTRIBUTE5));
                String attribute6 = cursor.getString(cursor.getColumnIndex(DBHepler.ATTRIBUTE6));
                String attribute7 = cursor.getString(cursor.getColumnIndex(DBHepler.ATTRIBUTE7));
                String latitude1 = cursor.getString(cursor.getColumnIndex(DBHepler.LATITUDE));
                String longitude1 = cursor.getString(cursor.getColumnIndex(DBHepler.LONGITUDE));
                String latitude2 = cursor.getString(cursor.getColumnIndex(DBHepler.LATITUDE2));
                String longitude2 = cursor.getString(cursor.getColumnIndex(DBHepler.LONGITUDE2));

                String latitude3 = cursor.getString(cursor.getColumnIndex(DBHepler.LATITUDE3));
                String longitude3 = cursor.getString(cursor.getColumnIndex(DBHepler.LONGITUDE3));

                String latitude4 = cursor.getString(cursor.getColumnIndex(DBHepler.LATITUDE4));
                String longitude4 = cursor.getString(cursor.getColumnIndex(DBHepler.LONGITUDE4));

                String latitude5 = cursor.getString(cursor.getColumnIndex(DBHepler.LATITUDE5));
                String longitude5 = cursor.getString(cursor.getColumnIndex(DBHepler.LONGITUDE5));

                String latitude6 = cursor.getString(cursor.getColumnIndex(DBHepler.LATITUDE6));
                String longitude6 = cursor.getString(cursor.getColumnIndex(DBHepler.LONGITUDE6));

                String latitude7 = cursor.getString(cursor.getColumnIndex(DBHepler.LATITUDE7));
                String longitude7 = cursor.getString(cursor.getColumnIndex(DBHepler.LONGITUDE7));
                int deviceId = cursor.getInt(cursor.getColumnIndex(DBHepler.DEVICEID));
                int devicePosition = cursor.getInt(cursor.getColumnIndex(DBHepler.DEVICEPOSITION));
                int position = cursor.getInt(cursor.getColumnIndex(DBHepler.POSITION));
                String category = cursor.getString(cursor.getColumnIndex(DBHepler.CATEGORY));
                String file = cursor.getString(cursor.getColumnIndex(DBHepler.FILE));
                if (cursor.getBlob(cursor.getColumnIndex(DBHepler.PICTURE)) != null) {
                    bitmap1 = Bytes2Bimap(cursor.getBlob(cursor.getColumnIndex(DBHepler.PICTURE)));
                } else {
                    bitmap1 = null;
                }
                if (cursor.getBlob(cursor.getColumnIndex(DBHepler.PICTURE2)) != null) {
                    bitmap2 = Bytes2Bimap(cursor.getBlob(cursor.getColumnIndex(DBHepler.PICTURE2)));
                } else {
                    bitmap2 = null;
                }
                if (cursor.getBlob(cursor.getColumnIndex(DBHepler.PICTURE3)) != null) {
                    bitmap3 = Bytes2Bimap(cursor.getBlob(cursor.getColumnIndex(DBHepler.PICTURE3)));
                } else {
                    bitmap3 = null;
                }
                if (cursor.getBlob(cursor.getColumnIndex(DBHepler.PICTURE4)) != null) {
                    bitmap4 = Bytes2Bimap(cursor.getBlob(cursor.getColumnIndex(DBHepler.PICTURE4)));
                } else {
                    bitmap4 = null;
                }
                if (cursor.getBlob(cursor.getColumnIndex(DBHepler.PICTURE5)) != null) {
                    bitmap5 = Bytes2Bimap(cursor.getBlob(cursor.getColumnIndex(DBHepler.PICTURE5)));
                } else {
                    bitmap5 = null;
                }
                if (cursor.getBlob(cursor.getColumnIndex(DBHepler.PICTURE6)) != null) {
                    bitmap6 = Bytes2Bimap(cursor.getBlob(cursor.getColumnIndex(DBHepler.PICTURE6)));
                } else {
                    bitmap6 = null;
                }
                if (cursor.getBlob(cursor.getColumnIndex(DBHepler.PICTURE7)) != null) {
                    bitmap7 = Bytes2Bimap(cursor.getBlob(cursor.getColumnIndex(DBHepler.PICTURE7)));
                } else {
                    bitmap7 = null;
                }
                String photo_name = cursor.getString(cursor.getColumnIndex(DBHepler.PHOTO));
                String photo_name2 = cursor.getString(cursor.getColumnIndex(DBHepler.PHOTO2));
                String photo_name3 = cursor.getString(cursor.getColumnIndex(DBHepler.PHOTO3));
                String photo_name4 = cursor.getString(cursor.getColumnIndex(DBHepler.PHOTO4));
                String photo_name5 = cursor.getString(cursor.getColumnIndex(DBHepler.PHOTO5));
                String photo_name6 = cursor.getString(cursor.getColumnIndex(DBHepler.PHOTO6));
                String photo_name7 = cursor.getString(cursor.getColumnIndex(DBHepler.PHOTO7));
                if (attribute7 != null||file!=null) {
                    Line item = new Line();
                    DeviceCtegoryModel deviceCtegoryModel = new DeviceCtegoryModel();
                    item.setLine_item_title(line_data);
                    item.setLine_item_content1(attribute1);
                    item.setLine_item_content2(attribute2);
                    item.setLine_item_content3(attribute3);
                    item.setLine_item_content4(attribute4);
                    item.setLine_item_content5(attribute5);
                    item.setLine_item_content6(attribute6);
                    item.setLine_item_content7(attribute7);
                    item.setLatitude1(latitude1);
                    item.setLongitude1(longitude1);

                    item.setLatitude2(latitude2);
                    item.setLongitude2(longitude2);

                    item.setLatitude3(latitude3);
                    item.setLongitude3(longitude3);

                    item.setLatitude4(latitude4);
                    item.setLongitude4(longitude4);

                    item.setLatitude5(latitude5);
                    item.setLongitude5(longitude5);

                    item.setLatitude6(latitude6);
                    item.setLongitude6(longitude6);

                    item.setLatitude7(latitude7);
                    item.setLongitude7(longitude7);
                    item.setView(file);
                    item.setLine_item_img1(bitmap1);
                    item.setLine_item_img2(bitmap2);
                    item.setLine_item_img3(bitmap3);
                    item.setLine_item_img4(bitmap4);
                    item.setLine_item_img5(bitmap5);
                    item.setLine_item_img6(bitmap6);
                    item.setLine_item_img7(bitmap7);
                    item.setPhoto_name(photo_name);
                    item.setPhoto_name2(photo_name2);
                    item.setPhoto_name3(photo_name3);
                    item.setPhoto_name4(photo_name4);
                    item.setPhoto_name5(photo_name5);
                    item.setPhoto_name6(photo_name6);
                    item.setPhoto_name7(photo_name7);
                    item.setLine_name(line_name);
                    deviceCtegoryModel.setDeviceId(deviceId);
                    deviceCtegoryModel.setDevicePosition(devicePosition);
                    item.setDeviceCtegoryModel(deviceCtegoryModel);
                    item.setPosition(position);
                    item.setCategory(category);
                    list.add(item);
                }
            }
        } catch (Exception e) {
            Log.i("数据库报错", e.getStackTrace().toString());
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return list;
    }

    Bitmap Bytes2Bimap(byte[] b) {
        try {
            if (b.length != 0) {
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmapOptions.inSampleSize = 3;
//                return BitmapFactory.decodeByteArray(b, 0, b.length);
                return BitmapFactory.decodeByteArray(b, 0, b.length, bitmapOptions);
            } else {
                return null;
            }
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
