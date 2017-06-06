package com.example.text.myapplication.tool;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.text.myapplication.activity.DeviceCtegoryModel;
import com.example.text.myapplication.domain.Line;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangpan on 2017/4/7.
 */

public class CSVUtils {
    /**
     * 导出
     *
     * @param file     csv文件(路径+文件名)，csv文件不存在会自动创建
     * @param dataList 数据
     * @return
     */
//    List<Line>lineList=new ArrayList<Line>();
    String name2;

    public static boolean exportCsv(File file, List<Line> dataList, String number) {
        boolean isSucess = false;

        FileOutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out);
            bw = new BufferedWriter(osw);
            if (dataList != null && !dataList.isEmpty()) {
                bw.append("线路名称")
                        .append(",").append("杆塔类别")
                        .append(",").append("附属设备")
                        .append(",").append("属性1")
                        .append(",").append("属性2")
                        .append(",").append("属性3")
                        .append(",").append("属性4")
                        .append(",").append("属性5")
                        .append(",").append("属性6")
                        .append(",").append("属性7").append(",")
                        .append("经度1").append(",").append("纬度2")
                        .append("经度2").append(",").append("纬度2")
                        .append("经度3").append(",").append("纬度3")
                        .append("经度4").append(",").append("纬度4")
                        .append("经度5").append(",").append("纬度5")
                        .append("经度6").append(",").append("纬度6")
                        .append("经度7").append(",").append("纬度7")
                        .append(",").append("图片1").append(",")
                        .append(",").append("图片2").append(",")
                        .append(",").append("图片3").append(",")
                        .append(",").append("图片4").append(",")
                        .append(",").append("图片5").append(",")
                        .append(",").append("图片6").append(",")
                        .append(",").append("图片7").append("\r\n");
                for (Line data : dataList) {
                    if (data.getLine_name().equals(number)) {
                        bw.append(number)
                                .append(",").append(data.getCategory())
                                .append(",").append(data.getLine_item_title())
                                .append(",").append(data.getLine_item_content1())
                                .append(",").append(data.getLine_item_content2())
                                .append(",").append(data.getLine_item_content3())
                                .append(",").append(data.getLine_item_content4())
                                .append(",").append(data.getLine_item_content5())
                                .append(",").append(data.getLine_item_content6())
                                .append(",").append(data.getLine_item_content7())
                                .append(",").append(data.getLatitude1()).append(",").append(data.getLongitude1())
                                .append(",").append(data.getLatitude2()).append(",").append(data.getLongitude2())
                                .append(",").append(data.getLatitude3()).append(",").append(data.getLongitude3())
                                .append(",").append(data.getLatitude4()).append(",").append(data.getLongitude4())
                                .append(",").append(data.getLatitude5()).append(",").append(data.getLongitude5())
                                .append(",").append(data.getLatitude6()).append(",").append(data.getLongitude6())
                                .append(",").append(data.getLatitude7()).append(",").append(data.getLongitude7())
                                .append(",").append(data.getPhoto_name())
                                .append(",").append(data.getPhoto_name2())
                                .append(",").append(data.getPhoto_name3())
                                .append(",").append(data.getPhoto_name4())
                                .append(",").append(data.getPhoto_name5())
                                .append(",").append(data.getPhoto_name6())
                                .append(",").append(data.getPhoto_name7()).append("\r\n");
                    }
                }
            }
            isSucess = true;
        } catch (Exception e) {
            isSucess = false;
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                    bw = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                    osw = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                    out = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return isSucess;
    }

    /**
     * 导入
     *
     * @param file csv文件(路径+文件)
     * @return
     */
    public static List<Line> importCsv(File file,int maxPosition) {
        List<Line> dataList = new ArrayList<Line>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = "";
            String line_name = "";


            while ((line = br.readLine()) != null) {
                Log.i("导入line：：：：", line);
                String[] strs = line.split(",");
                Line line1 = new Line();
                DeviceCtegoryModel deviceCtegoryModel=new DeviceCtegoryModel();
                for (int i = 0, len = strs.length; i < len; i++) {
                    System.out.println(strs[i].toString());
                    Log.i("导入strs：：：：", strs[i].toString());
                    line1.setLine_name(strs[0]);
                    line1.setCategory(strs[1]);//选择的杆塔类别
                    line1.setLine_item_title(strs[2]);
                    line1.setLine_item_content1(strs[3]);
                    line1.setLine_item_content2(strs[4]);
                    line1.setLine_item_content3(strs[5]);
                    line1.setLine_item_content4(strs[6]);
                    line1.setLine_item_content5(strs[7]);
                    line1.setLine_item_content6(strs[8]);
                    line1.setLine_item_content7(strs[9]);
                    line1.setLatitude1(strs[10]);
                    line1.setLongitude1(strs[11]);

                    line1.setLatitude2(strs[12]);
                    line1.setLongitude2(strs[13]);

                    line1.setLatitude3(strs[14]);
                    line1.setLongitude3(strs[15]);

                    line1.setLatitude4(strs[16]);
                    line1.setLongitude4(strs[17]);

                    line1.setLatitude5(strs[18]);
                    line1.setLongitude5(strs[19]);

                    line1.setLatitude6(strs[20]);
                    line1.setLongitude6(strs[21]);

                    line1.setLatitude7(strs[22]);
                    line1.setLongitude7(strs[23]);
                    line1.setAltitude("");
//                    line1.setPosition(1);
                    deviceCtegoryModel.setDeviceId(0);
                    deviceCtegoryModel.setDevicePosition(0);
                    line1.setDeviceCtegoryModel(deviceCtegoryModel);

                    line_name = line1.getLine_name();
//                    for(int a=0;a<line_name.length();a++){
//                        List<Integer>list_number=new ArrayList<Integer>();
//                        list_number.add(a);
//                        for(int b=0;b<list_number.size();b++){
                            line1.setPosition(maxPosition+1);
//                        }

//                    }
                }
                if (!line_name.equals("线路名称"))
                    dataList.add(line1);

//                if (!line_name.equals("线路名称"))
//                dataList.add(line1);
            }

        } catch (Exception e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                    br = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return dataList;
    }
}
