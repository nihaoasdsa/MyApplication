package com.example.text.myapplication.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ypgt-007 on 2017/4/26.
 */

public class HomeLine {
    private List<Line> childList = new ArrayList<Line>();
    private String view;
    private int file_number;

    public int getFile_number() {
        return file_number;
    }

    public void setFile_number(int file_number) {
        this.file_number = file_number;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }




    public List<Line> getChildList() {
        return childList;
    }

    public void setChildList(List<Line> childList) {
        this.childList = childList;
    }
}
