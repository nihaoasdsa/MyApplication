package com.example.text.myapplication.db;

import android.content.ContentValues;

import com.example.text.myapplication.domain.Line;

import java.util.List;
import java.util.Map;


public interface ChannelDaoInface {
	public boolean addCache(Line item);

	public boolean updateCache(ContentValues values, String whereClause,
							   String[] whereArgs);
	public boolean deleteCache(String whereClause, String[] whereArgs);

}
