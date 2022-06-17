package com.bluetooth.test.javabean;

import java.util.ArrayList;

public class BeaconJsonBean {

    private int v;          // 版本号
    private int mid;        // message id
    private int time;       // 启动时长，以秒计算
    private String ip;      // 网关的 IP
    private String mac;     // 网关的 MAC地址
    // 将混合数组中的所用类型都认为是字符串将变得简单
    private ArrayList<ArrayList<String>> devices;    // 由 BLE广播包组成的数组


    public void setV(int v) {
        this.v = v;
    }
    public int getV() {
        return v;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }
    public int getMid() {
        return mid;
    }

    public void setTime(int time) {
        this.time = time;
    }
    public int getTime() {
        return time;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getIp() {
        return ip;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
    public String getMac() {
        return mac;
    }

    public void setDevices(ArrayList<ArrayList<String>> devices) {
        this.devices = devices;
    }

    public ArrayList<ArrayList<String>> getDevices() {
        return devices;
    }
}