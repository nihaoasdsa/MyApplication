package com.example.text.myapplication.db;

import android.database.SQLException;
import android.util.Log;

import com.example.text.myapplication.domain.Line;

import java.util.ArrayList;
import java.util.List;

public class ChannelManage {
    private List<Line> lineList = new ArrayList<Line>();
    public static ChannelManage channelManage;
    public static List<Line> lines;
    private ChannelDao channelDao;

    static {
        lines = new ArrayList<Line>();
    }

    private ChannelManage(DBHepler paramDBHelper) throws SQLException {
        if (channelDao == null)
            channelDao = new ChannelDao(paramDBHelper.getContext());
        return;
    }

    /**
     * 初始化频道管理类
     *
     * @param
     * @throws SQLException
     */
    public static ChannelManage getManage(DBHepler dbHelper) throws SQLException {
        if (channelManage == null)
            channelManage = new ChannelManage(dbHelper);
        return channelManage;
    }

    /**
     * 清除所有的频道
     */
    public void deleteAllChannel() {
//        channelDao.clearFeedTable();
    }

    public void saveUserChannel(List<Line> userList) {
        for (int i = 0; i < userList.size(); i++) {
            Line line = (Line) userList.get(i);
            line.getLine_item_title();
            line.getLine_item_content1();
            line.getLine_item_content2();
            line.getLine_item_content3();
            line.getLine_item_content4();
            line.getLine_item_content5();
            line.getLine_item_content6();
            line.getLine_item_content7();

            channelDao.addCache(line);
        }
    }

}
