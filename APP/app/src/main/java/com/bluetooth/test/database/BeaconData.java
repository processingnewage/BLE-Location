package com.bluetooth.test.database;

// 信标节点信息
/*
id              mac             Rss
1           D10D835390A2        -50
2           FEF5C4FE3DE7        -50
3           F7CA277A3602        -50
4           C4E497072BEF        -50
5           E0FC523BDC4C        -50
6           EC9C1499D5F3        -50
*/

public class BeaconData {
    private int beaconId;       // 信标节点编号：1 - 6
    private String beaconMac;   // 信标节点的Mac地址
    private int beaconRss;      // 信标节点的Rss值（信号强度）

    public BeaconData(int beaconId, String beaconMac, int beaconRss) {
        this.beaconId = beaconId;
        this.beaconMac = beaconMac;
        this.beaconRss = beaconRss;
    }

    public int getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(int beaconId) {
        this.beaconId = beaconId;
    }

    public String getBeaconMac() {
        return beaconMac;
    }

    public void setBeaconMac(String beaconMac) {
        this.beaconMac =beaconMac;
    }

    public int getBeaconRss() {
        return beaconRss;
    }

    public void setBeaconRss(int beaconRss) {
        this.beaconRss = beaconRss;
    }
}
