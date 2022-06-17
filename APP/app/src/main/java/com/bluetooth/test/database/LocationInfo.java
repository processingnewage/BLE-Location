package com.bluetooth.test.database;

// 每个定位位置的数据集：一共五个位置

public class LocationInfo {
    private int locationId;       // 定位位置
    private BeaconData beaconData01;  // 信标节点1的数据
    private BeaconData beaconData02;  // 信标节点2的数据
    private BeaconData beaconData03;  // 信标节点3的数据
    private BeaconData beaconData04;  // 信标节点4的数据
    private BeaconData beaconData05;  // 信标节点5的数据
    private BeaconData beaconData06;  // 信标节点6的数据

    public LocationInfo(int locationId, BeaconData beaconData01, BeaconData beaconData02, BeaconData beaconData03, BeaconData beaconData04, BeaconData beaconData05, BeaconData beaconData06) {
        this.locationId = locationId;
        this.beaconData01 = beaconData01;
        this.beaconData02 = beaconData02;
        this.beaconData03 = beaconData03;
        this.beaconData04 = beaconData04;
        this.beaconData05 = beaconData05;
        this.beaconData06 = beaconData06;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public BeaconData getBeaconData01() {
        return beaconData01;
    }

    public void setBeaconData01(BeaconData beaconData01) {
        this.beaconData01 = beaconData01;
    }

    public BeaconData getBeaconData02() {
        return beaconData02;
    }

    public void setBeaconData02(BeaconData beaconData02) {
        this.beaconData02 = beaconData02;
    }

    public BeaconData getBeaconData03() {
        return beaconData03;
    }

    public void setBeaconData03(BeaconData beaconData03) {
        this.beaconData03 = beaconData03;
    }

    public BeaconData getBeaconData04() {
        return beaconData04;
    }

    public void setBeaconData04(BeaconData beaconData04) {
        this.beaconData04 = beaconData04;
    }

    public BeaconData getBeaconData05() {
        return beaconData05;
    }

    public void setBeaconData05(BeaconData beaconData05) {
        this.beaconData05 = beaconData05;
    }

    public BeaconData getBeaconData06() {
        return beaconData06;
    }

    public void setBeaconData06(BeaconData beaconData06) {
        this.beaconData06 = beaconData06;
    }
}
