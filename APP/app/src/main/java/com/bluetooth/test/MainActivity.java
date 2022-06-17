package com.bluetooth.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;

import com.bluetooth.test.database.BeaconData;
import com.bluetooth.test.database.LocationInfo;
import com.bluetooth.test.database.MyDatabaseHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<LocationInfo> mLocationInfoList;       // 保存信标节点信息的数组
    private MyDatabaseHelper mDbHelper;                     // SQLite数据库帮助对象
    private SQLiteDatabase mLocationInfoDb;                 // SQLite数据库对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 实例化数据库帮助对象
        mDbHelper = new MyDatabaseHelper(this, "LocationInfo.db", null, 1);

        // 为 button设置点击监听事件，实现点击跳转视图展示界面并初始化数据库
        Button mBtnEnter = findViewById(R.id.btn_ent);
        mBtnEnter.setOnClickListener(v -> {
            // 通过数据库帮助对象实例化数据库对象：存在则打开，不存在则创建
            mLocationInfoDb = mDbHelper.getWritableDatabase();

            // 判断当前数据库是否为空（若为空则初始化数据，避免多次插入数据）
            Cursor c = mLocationInfoDb.rawQuery("select * from location_info", null);
            int rawCounts = c.getCount();
            c.close();              // 关闭当前cursor

            // if(0 != rawCounts) {
            // 初始化数据库
            initDataBase();
            mLocationInfoDb.beginTransaction();       // 开始事务（不可分割的操作集合）
            try {
                for (int i = 0; i < mLocationInfoList.size(); i++) {
                    LocationInfo temp = mLocationInfoList.get(i);
                    ContentValues contentValues = new ContentValues();
                    // 获取位置
                    contentValues.put("location", temp.getLocationId());
                    // 获取信标节点 01 的信息
                    contentValues.put("beacon_id_01", temp.getBeaconData01().getBeaconId());
                    contentValues.put("beacon_mac_01", temp.getBeaconData01().getBeaconMac());
                    contentValues.put("beacon_rss_01", temp.getBeaconData01().getBeaconRss());
                    // 获取信标节点 02 的信息
                    contentValues.put("beacon_id_02", temp.getBeaconData02().getBeaconId());
                    contentValues.put("beacon_mac_02", temp.getBeaconData02().getBeaconMac());
                    contentValues.put("beacon_rss_02", temp.getBeaconData02().getBeaconRss());
                    // 获取信标节点 03 的信息
                    contentValues.put("beacon_id_03", temp.getBeaconData03().getBeaconId());
                    contentValues.put("beacon_mac_03", temp.getBeaconData03().getBeaconMac());
                    contentValues.put("beacon_rss_03", temp.getBeaconData03().getBeaconRss());
                    // 获取信标节点 04 的信息
                    contentValues.put("beacon_id_04", temp.getBeaconData04().getBeaconId());
                    contentValues.put("beacon_mac_04", temp.getBeaconData04().getBeaconMac());
                    contentValues.put("beacon_rss_04", temp.getBeaconData04().getBeaconRss());
                    // 获取信标节点 05 的信息
                    contentValues.put("beacon_id_05", temp.getBeaconData05().getBeaconId());
                    contentValues.put("beacon_mac_05", temp.getBeaconData05().getBeaconMac());
                    contentValues.put("beacon_rss_05", temp.getBeaconData05().getBeaconRss());
                    // 获取信标节点 06 的信息
                    contentValues.put("beacon_id_06", temp.getBeaconData06().getBeaconId());
                    contentValues.put("beacon_mac_06", temp.getBeaconData06().getBeaconMac());
                    contentValues.put("beacon_rss_06", temp.getBeaconData06().getBeaconRss());
                    // 将以上数据插入对应元组
                    mLocationInfoDb.insert("location_info", null, contentValues);
                }
                mLocationInfoDb.setTransactionSuccessful();       // 事务已经执行成功
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mLocationInfoDb.endTransaction();     // 结束事务
            }

            // 跳转到展示界面
            Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
            startActivity(intent);
        });
    }

    // 初始化数据库函数：组装原始数据到数组中
    private void initDataBase() {
        mLocationInfoList = new ArrayList<>(28);
        // 将采集好的指纹信息组装成数组
        // 1. 分别创建 64个位置对应的 LocationInfo对象
        LocationInfo localLocationInfo01 = new LocationInfo(1,
                new BeaconData(1, "D10D835390A2", -51),
                new BeaconData(2, "FEF5C4FE3DE7", -62),
                new BeaconData(3, "F7CA277A3602", -55),
                new BeaconData(4, "C4E497072BEF", -54),
                new BeaconData(5, "E0FC523BDC4C", -67),
                new BeaconData(6, "EC9C1499D5F3", -57));
        LocationInfo localLocationInfo02 = new LocationInfo(2,
                new BeaconData(1, "D10D835390A2", -56),
                new BeaconData(2, "FEF5C4FE3DE7", -43),
                new BeaconData(3, "F7CA277A3602", -49),
                new BeaconData(4, "C4E497072BEF", -35),
                new BeaconData(5, "E0FC523BDC4C", -71),
                new BeaconData(6, "EC9C1499D5F3", -68));
        LocationInfo localLocationInfo03 = new LocationInfo(3,
                new BeaconData(1, "D10D835390A2", -52),
                new BeaconData(2, "FEF5C4FE3DE7", -63),
                new BeaconData(3, "F7CA277A3602", -64),
                new BeaconData(4, "C4E497072BEF", -60),
                new BeaconData(5, "E0FC523BDC4C", -71),
                new BeaconData(6, "EC9C1499D5F3", -68));
        LocationInfo localLocationInfo04 = new LocationInfo(4,
                new BeaconData(1, "D10D835390A2", -50),
                new BeaconData(2, "FEF5C4FE3DE7", -60),
                new BeaconData(3, "F7CA277A3602", -65),
                new BeaconData(4, "C4E497072BEF", -64),
                new BeaconData(5, "E0FC523BDC4C", -67),
                new BeaconData(6, "EC9C1499D5F3", -68));
        LocationInfo localLocationInfo05 = new LocationInfo(5,
                new BeaconData(1, "D10D835390A2", -56),
                new BeaconData(2, "FEF5C4FE3DE7", -67),
                new BeaconData(3, "F7CA277A3602", -65),
                new BeaconData(4, "C4E497072BEF", -76),
                new BeaconData(5, "E0FC523BDC4C", -67),
                new BeaconData(6, "EC9C1499D5F3", -58));
        LocationInfo localLocationInfo06 = new LocationInfo(6,
                new BeaconData(1, "D10D835390A2", -50),
                new BeaconData(2, "FEF5C4FE3DE7", -66),
                new BeaconData(3, "F7CA277A3602", -55),
                new BeaconData(4, "C4E497072BEF", -44),
                new BeaconData(5, "E0FC523BDC4C", -47),
                new BeaconData(6, "EC9C1499D5F3", -58));
        LocationInfo localLocationInfo07 = new LocationInfo(7,
                new BeaconData(1, "D10D835390A2", -60),
                new BeaconData(2, "FEF5C4FE3DE7", -67),
                new BeaconData(3, "F7CA277A3602", -65),
                new BeaconData(4, "C4E497072BEF", -64),
                new BeaconData(5, "E0FC523BDC4C", -67),
                new BeaconData(6, "EC9C1499D5F3", -68));
        LocationInfo localLocationInfo08 = new LocationInfo(8,
                new BeaconData(1, "D10D835390A2", -60),
                new BeaconData(2, "FEF5C4FE3DE7", -66),
                new BeaconData(3, "F7CA277A3602", -55),
                new BeaconData(4, "C4E497072BEF", -64),
                new BeaconData(5, "E0FC523BDC4C", -47),
                new BeaconData(6, "EC9C1499D5F3", -58));
        LocationInfo localLocationInfo09 = new LocationInfo(9,
                new BeaconData(1, "D10D835390A2", -70),
                new BeaconData(2, "FEF5C4FE3DE7", -60),
                new BeaconData(3, "F7CA277A3602", -75),
                new BeaconData(4, "C4E497072BEF", -74),
                new BeaconData(5, "E0FC523BDC4C", -77),
                new BeaconData(6, "EC9C1499D5F3", -78));
        LocationInfo localLocationInfo10 = new LocationInfo(10,
                new BeaconData(1, "D10D835390A2", -70),
                new BeaconData(2, "FEF5C4FE3DE7", -70),
                new BeaconData(3, "F7CA277A3602", -75),
                new BeaconData(4, "C4E497072BEF", -74),
                new BeaconData(5, "E0FC523BDC4C", -77),
                new BeaconData(6, "EC9C1499D5F3", -78));
        LocationInfo localLocationInfo11 = new LocationInfo(11,
                new BeaconData(1, "D10D835390A2", -70),
                new BeaconData(2, "FEF5C4FE3DE7", -70),
                new BeaconData(3, "F7CA277A3602", -75),
                new BeaconData(4, "C4E497072BEF", -74),
                new BeaconData(5, "E0FC523BDC4C", -87),
                new BeaconData(6, "EC9C1499D5F3", -78));
        LocationInfo localLocationInfo12 = new LocationInfo(12,
                new BeaconData(1, "D10D835390A2", -60),
                new BeaconData(2, "FEF5C4FE3DE7", -50),
                new BeaconData(3, "F7CA277A3602", -45),
                new BeaconData(4, "C4E497072BEF", -34),
                new BeaconData(5, "E0FC523BDC4C", -67),
                new BeaconData(6, "EC9C1499D5F3", -88));
        LocationInfo localLocationInfo13 = new LocationInfo(13,
                new BeaconData(1, "D10D835390A2", -70),
                new BeaconData(2, "FEF5C4FE3DE7", -60),
                new BeaconData(3, "F7CA277A3602", -55),
                new BeaconData(4, "C4E497072BEF", -44),
                new BeaconData(5, "E0FC523BDC4C", -37),
                new BeaconData(6, "EC9C1499D5F3", -78));
        LocationInfo localLocationInfo14 = new LocationInfo(14,
                new BeaconData(1, "D10D835390A2", -70),
                new BeaconData(2, "FEF5C4FE3DE7", 60),
                new BeaconData(3, "F7CA277A3602", -75),
                new BeaconData(4, "C4E497072BEF", -84),
                new BeaconData(5, "E0FC523BDC4C", -67),
                new BeaconData(6, "EC9C1499D5F3", -68));
        LocationInfo localLocationInfo15 = new LocationInfo(15,
                new BeaconData(1, "D10D835390A2", -50),
                new BeaconData(2, "FEF5C4FE3DE7", -63),
                new BeaconData(3, "F7CA277A3602", -55),
                new BeaconData(4, "C4E497072BEF", -51),
                new BeaconData(5, "E0FC523BDC4C", -67),
                new BeaconData(6, "EC9C1499D5F3", -58));
        LocationInfo localLocationInfo16 = new LocationInfo(16,
                new BeaconData(1, "D10D835390A2", -49),
                new BeaconData(2, "FEF5C4FE3DE7", -64),
                new BeaconData(3, "F7CA277A3602", -55),
                new BeaconData(4, "C4E497072BEF", -58),
                new BeaconData(5, "E0FC523BDC4C", -62),
                new BeaconData(6, "EC9C1499D5F3", -53));
        LocationInfo localLocationInfo17 = new LocationInfo(17,
                new BeaconData(1, "D10D835390A2", -45),
                new BeaconData(2, "FEF5C4FE3DE7", -46),
                new BeaconData(3, "F7CA277A3602", -56),
                new BeaconData(4, "C4E497072BEF", -54),
                new BeaconData(5, "E0FC523BDC4C", -67),
                new BeaconData(6, "EC9C1499D5F3", -58));
        LocationInfo localLocationInfo18 = new LocationInfo(18,
                new BeaconData(1, "D10D835390A2", -57),
                new BeaconData(2, "FEF5C4FE3DE7", -60),
                new BeaconData(3, "F7CA277A3602", -53),
                new BeaconData(4, "C4E497072BEF", -52),
                new BeaconData(5, "E0FC523BDC4C", -64),
                new BeaconData(6, "EC9C1499D5F3", -55));
        LocationInfo localLocationInfo19 = new LocationInfo(19,
                new BeaconData(1, "D10D835390A2", -54),
                new BeaconData(2, "FEF5C4FE3DE7", -63),
                new BeaconData(3, "F7CA277A3602", -55),
                new BeaconData(4, "C4E497072BEF", -54),
                new BeaconData(5, "E0FC523BDC4C", -67),
                new BeaconData(6, "EC9C1499D5F3", -58));
        LocationInfo localLocationInfo20 = new LocationInfo(20,
                new BeaconData(1, "D10D835390A2", -70),
                new BeaconData(2, "FEF5C4FE3DE7", -54),
                new BeaconData(3, "F7CA277A3602", -53),
                new BeaconData(4, "C4E497072BEF", -51),
                new BeaconData(5, "E0FC523BDC4C", -62),
                new BeaconData(6, "EC9C1499D5F3", -54));
        LocationInfo localLocationInfo21 = new LocationInfo(21,
                new BeaconData(1, "D10D835390A2", -65),
                new BeaconData(2, "FEF5C4FE3DE7", -64),
                new BeaconData(3, "F7CA277A3602", -54),
                new BeaconData(4, "C4E497072BEF", -53),
                new BeaconData(5, "E0FC523BDC4C", -62),
                new BeaconData(6, "EC9C1499D5F3", -54));
        LocationInfo localLocationInfo22 = new LocationInfo(22,
                new BeaconData(1, "D10D835390A2", -70),
                new BeaconData(2, "FEF5C4FE3DE7", -64),
                new BeaconData(3, "F7CA277A3602", -54),
                new BeaconData(4, "C4E497072BEF", -56),
                new BeaconData(5, "E0FC523BDC4C", -64),
                new BeaconData(6, "EC9C1499D5F3", -54));
        LocationInfo localLocationInfo23 = new LocationInfo(23,
                new BeaconData(1, "D10D835390A2", -60),
                new BeaconData(2, "FEF5C4FE3DE7", -64),
                new BeaconData(3, "F7CA277A3602", -55),
                new BeaconData(4, "C4E497072BEF", -54),
                new BeaconData(5, "E0FC523BDC4C", -67),
                new BeaconData(6, "EC9C1499D5F3", -58));
        LocationInfo localLocationInfo24 = new LocationInfo(24,
                new BeaconData(1, "D10D835390A2", -73),
                new BeaconData(2, "FEF5C4FE3DE7", -60),
                new BeaconData(3, "F7CA277A3602", -55),
                new BeaconData(4, "C4E497072BEF", -54),
                new BeaconData(5, "E0FC523BDC4C", -67),
                new BeaconData(6, "EC9C1499D5F3", -58));
        LocationInfo localLocationInfo25 = new LocationInfo(25,
                new BeaconData(1, "D10D835390A2", -70),
                new BeaconData(2, "FEF5C4FE3DE7", -60),
                new BeaconData(3, "F7CA277A3602", -55),
                new BeaconData(4, "C4E497072BEF", -54),
                new BeaconData(5, "E0FC523BDC4C", -67),
                new BeaconData(6, "EC9C1499D5F3", -58));
        LocationInfo localLocationInfo26 = new LocationInfo(26,
                new BeaconData(1, "D10D835390A2", -72),
                new BeaconData(2, "FEF5C4FE3DE7", -70),
                new BeaconData(3, "F7CA277A3602", -64),
                new BeaconData(4, "C4E497072BEF", -59),
                new BeaconData(5, "E0FC523BDC4C", -55),
                new BeaconData(6, "EC9C1499D5F3", -58));
        LocationInfo localLocationInfo27 = new LocationInfo(27,
                new BeaconData(1, "D10D835390A2", -72),
                new BeaconData(2, "FEF5C4FE3DE7", -69),
                new BeaconData(3, "F7CA277A3602", -63),
                new BeaconData(4, "C4E497072BEF", -54),
                new BeaconData(5, "E0FC523BDC4C", -55),
                new BeaconData(6, "EC9C1499D5F3", -58));
        LocationInfo localLocationInfo28 = new LocationInfo(28,
                new BeaconData(1, "D10D835390A2", -72),
                new BeaconData(2, "FEF5C4FE3DE7", -70),
                new BeaconData(3, "F7CA277A3602", -65),
                new BeaconData(4, "C4E497072BEF", -64),
                new BeaconData(5, "E0FC523BDC4C", -67),
                new BeaconData(6, "EC9C1499D5F3", -58));
        // 2. 将 64个对象分别添加到数组中
        mLocationInfoList.add(localLocationInfo01);
        mLocationInfoList.add(localLocationInfo02);
        mLocationInfoList.add(localLocationInfo03);
        mLocationInfoList.add(localLocationInfo04);
        mLocationInfoList.add(localLocationInfo05);
        mLocationInfoList.add(localLocationInfo06);
        mLocationInfoList.add(localLocationInfo07);
        mLocationInfoList.add(localLocationInfo08);
        mLocationInfoList.add(localLocationInfo09);
        mLocationInfoList.add(localLocationInfo10);
        mLocationInfoList.add(localLocationInfo11);
        mLocationInfoList.add(localLocationInfo12);
        mLocationInfoList.add(localLocationInfo13);
        mLocationInfoList.add(localLocationInfo14);
        mLocationInfoList.add(localLocationInfo15);
        mLocationInfoList.add(localLocationInfo16);
        mLocationInfoList.add(localLocationInfo17);
        mLocationInfoList.add(localLocationInfo18);
        mLocationInfoList.add(localLocationInfo19);
        mLocationInfoList.add(localLocationInfo20);
        mLocationInfoList.add(localLocationInfo21);
        mLocationInfoList.add(localLocationInfo22);
        mLocationInfoList.add(localLocationInfo23);
        mLocationInfoList.add(localLocationInfo24);
        mLocationInfoList.add(localLocationInfo25);
        mLocationInfoList.add(localLocationInfo26);
        mLocationInfoList.add(localLocationInfo27);
        mLocationInfoList.add(localLocationInfo28);
    }
}