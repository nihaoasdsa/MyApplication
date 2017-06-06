package com.example.text.myapplication.domain;

import android.graphics.Bitmap;

import com.example.text.myapplication.activity.DeviceCtegoryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangpan on 2017/4/10.
 */

public  class Line {

    private DeviceCtegoryModel deviceCtegoryModel;
    private String line_name;
    private String line_item_title;
    private String line_item_content1;
    private String line_item_content2;
    private String line_item_content3;
    private String line_item_content4;
    private String line_item_content5;
    private String line_item_content6;
    private String line_item_content7;

    private Bitmap line_item_img1;
    private Bitmap line_item_img2;
    private Bitmap line_item_img3;
    private Bitmap line_item_img4;
    private Bitmap line_item_img5;
    private Bitmap line_item_img6;
    private Bitmap line_item_img7;
    private String latitude1;
    private String longitude1;
    private String latitude2;
    private String longitude2;
    private String latitude3;
    private String longitude3;
    private String latitude4;
    private String longitude4;
    private String latitude5;
    private String longitude5;
    private String latitude6;
    private String longitude6;
    private String latitude7;
    private String longitude7;
    private String photo_name;
    private String photo_name2;
    private String photo_name3;
    private String photo_name4;
    private String photo_name5;
    private String photo_name6;
    private String photo_name7;
    private String altitude;
    private int position;
    private String category;
    private List<Line> childList = new ArrayList<Line>();
    private String view;
    private int file_number;

    public List<Line> getChildList() {
        return childList;
    }

    public void setChildList(List<Line> childList) {
        this.childList = childList;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public int getFile_number() {
        return file_number;
    }

    public void setFile_number(int file_number) {
        this.file_number = file_number;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getLine_name() {
        return line_name;
    }

    public void setLine_name(String line_name) {
        this.line_name = line_name;
    }

    public String getPhoto_name2() {
        return photo_name2;
    }

    public void setPhoto_name2(String photo_name2) {
        this.photo_name2 = photo_name2;
    }

    public String getPhoto_name3() {
        return photo_name3;
    }

    public void setPhoto_name3(String photo_name3) {
        this.photo_name3 = photo_name3;
    }

    public String getPhoto_name4() {
        return photo_name4;
    }

    public void setPhoto_name4(String photo_name4) {
        this.photo_name4 = photo_name4;
    }

    public String getPhoto_name5() {
        return photo_name5;
    }

    public void setPhoto_name5(String photo_name5) {
        this.photo_name5 = photo_name5;
    }

    public String getPhoto_name6() {
        return photo_name6;
    }

    public void setPhoto_name6(String photo_name6) {
        this.photo_name6 = photo_name6;
    }

    public String getPhoto_name7() {
        return photo_name7;
    }

    public void setPhoto_name7(String photo_name7) {
        this.photo_name7 = photo_name7;
    }

    public String getPhoto_name() {
        return photo_name;
    }

    public void setPhoto_name(String photo_name) {
        this.photo_name = photo_name;
    }

    public DeviceCtegoryModel getDeviceCtegoryModel() {
        return deviceCtegoryModel;
    }

    public String getLatitude2() {
        return latitude2;
    }

    public void setLatitude2(String latitude2) {
        this.latitude2 = latitude2;
    }

    public String getLongitude2() {
        return longitude2;
    }

    public void setLongitude2(String longitude2) {
        this.longitude2 = longitude2;
    }

    public String getLatitude3() {
        return latitude3;
    }

    public void setLatitude3(String latitude3) {
        this.latitude3 = latitude3;
    }

    public String getLongitude3() {
        return longitude3;
    }

    public void setLongitude3(String longitude3) {
        this.longitude3 = longitude3;
    }

    public String getLatitude4() {
        return latitude4;
    }

    public void setLatitude4(String latitude4) {
        this.latitude4 = latitude4;
    }

    public String getLongitude4() {
        return longitude4;
    }

    public void setLongitude4(String longitude4) {
        this.longitude4 = longitude4;
    }

    public String getLatitude5() {
        return latitude5;
    }

    public void setLatitude5(String latitude5) {
        this.latitude5 = latitude5;
    }

    public String getLongitude5() {
        return longitude5;
    }

    public void setLongitude5(String longitude5) {
        this.longitude5 = longitude5;
    }

    public String getLatitude6() {
        return latitude6;
    }

    public void setLatitude6(String latitude6) {
        this.latitude6 = latitude6;
    }

    public String getLongitude6() {
        return longitude6;
    }

    public void setLongitude6(String longitude6) {
        this.longitude6 = longitude6;
    }

    public String getLatitude7() {
        return latitude7;
    }

    public void setLatitude7(String latitude7) {
        this.latitude7 = latitude7;
    }

    public String getLongitude7() {
        return longitude7;
    }

    public void setLongitude7(String longitude7) {
        this.longitude7 = longitude7;
    }

    public String getLatitude1() {
        return latitude1;
    }

    public void setLatitude1(String latitude1) {
        this.latitude1 = latitude1;
    }

    public String getLongitude1() {
        return longitude1;
    }

    public void setLongitude1(String longitude1) {
        this.longitude1 = longitude1;
    }

    public void setDeviceCtegoryModel(DeviceCtegoryModel deviceCtegoryModel) {
        this.deviceCtegoryModel = deviceCtegoryModel;
    }

    public String getLine_item_title() {
        return line_item_title;
    }

    public void setLine_item_title(String line_item_title) {
        this.line_item_title = line_item_title;
    }

    public String getLine_item_content1() {
        return line_item_content1;
    }

    public void setLine_item_content1(String line_item_content1) {
        this.line_item_content1 = line_item_content1;
    }

    public String getLine_item_content2() {
        return line_item_content2;
    }

    public void setLine_item_content2(String line_item_content2) {
        this.line_item_content2 = line_item_content2;
    }

    public String getLine_item_content3() {
        return line_item_content3;
    }

    public void setLine_item_content3(String line_item_content3) {
        this.line_item_content3 = line_item_content3;
    }

    public String getLine_item_content4() {
        return line_item_content4;
    }

    public void setLine_item_content4(String line_item_content4) {
        this.line_item_content4 = line_item_content4;
    }

    public String getLine_item_content5() {
        return line_item_content5;
    }

    public void setLine_item_content5(String line_item_content5) {
        this.line_item_content5 = line_item_content5;
    }

    public String getLine_item_content6() {
        return line_item_content6;
    }

    public void setLine_item_content6(String line_item_content6) {
        this.line_item_content6 = line_item_content6;
    }

    public String getLine_item_content7() {
        return line_item_content7;
    }

    public void setLine_item_content7(String line_item_content7) {
        this.line_item_content7 = line_item_content7;
    }

    public Bitmap getLine_item_img1() {
        return line_item_img1;
    }

    public void setLine_item_img1(Bitmap line_item_img1) {
        this.line_item_img1 = line_item_img1;
    }

    public Bitmap getLine_item_img2() {
        return line_item_img2;
    }

    public void setLine_item_img2(Bitmap line_item_img2) {
        this.line_item_img2 = line_item_img2;
    }

    public Bitmap getLine_item_img3() {
        return line_item_img3;
    }

    public void setLine_item_img3(Bitmap line_item_img3) {
        this.line_item_img3 = line_item_img3;
    }

    public Bitmap getLine_item_img4() {
        return line_item_img4;
    }

    public void setLine_item_img4(Bitmap line_item_img4) {
        this.line_item_img4 = line_item_img4;
    }

    public Bitmap getLine_item_img5() {
        return line_item_img5;
    }

    public void setLine_item_img5(Bitmap line_item_img5) {
        this.line_item_img5 = line_item_img5;
    }

    public Bitmap getLine_item_img6() {
        return line_item_img6;
    }

    public void setLine_item_img6(Bitmap line_item_img6) {
        this.line_item_img6 = line_item_img6;
    }

    public Bitmap getLine_item_img7() {
        return line_item_img7;
    }

    public void setLine_item_img7(Bitmap line_item_img7) {
        this.line_item_img7 = line_item_img7;
    }
}
