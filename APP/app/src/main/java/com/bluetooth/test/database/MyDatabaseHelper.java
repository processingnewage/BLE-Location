package com.bluetooth.test.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_TABLE = "create table location_info(" +
            "location integer primary key, " +
            "beacon_id_01 integer, " +
            "beacon_mac_01 text, " +
            "beacon_rss_01 integer, " +
            "beacon_id_02 integer, " +
            "beacon_mac_02 text, " +
            "beacon_rss_02 integer, " +
            "beacon_id_03 integer, " +
            "beacon_mac_03 text, " +
            "beacon_rss_03 integer, " +
            "beacon_id_04 integer, " +
            "beacon_mac_04 text, " +
            "beacon_rss_04 integer, " +
            "beacon_id_05 integer, " +
            "beacon_mac_05 text, " +
            "beacon_rss_05 integer, " +
            "beacon_id_06 integer, " +
            "beacon_mac_06 text, " +
            "beacon_rss_06 integer)";

    private final Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 执行建表语句
        db.execSQL(CREATE_TABLE);
        Toast.makeText(mContext, "Create table succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}