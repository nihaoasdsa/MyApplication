package com.example.text.myapplication.activity;

import java.io.Serializable;

/**
 * 设备信息model
 * @author 
 */
@SuppressWarnings("serial")
public class DeviceCtegoryModel implements Serializable {
	
	/** 设备id */
    private int deviceId;
    /** 设备name */
    private String deviceName;
    /** 选中位置 */
    private int devicePosition;

    /** 获取设备id */
	public int getDeviceId() {
		return deviceId;
	}

	/** 设置设备id */
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	/** 获取设备name */
	public String getDeviceName() {
		return deviceName;
	}

	/** 设置设备name */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	/** 获取设备选中位置 */
	public int getDevicePosition() {
		return devicePosition;
	}
	
	/** 设置设备选中位置 */
	public void setDevicePosition(int devicePosition) {
		this.devicePosition = devicePosition;
	}
    
}
