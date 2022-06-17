package com.bluetooth.test;

import static com.bluetooth.test.service.MyMqttService.SEND_MQTT_PAYLOAD;
import static com.bluetooth.test.utils.UtilsHelper.WKNNAlgorithm;
import static com.bluetooth.test.utils.UtilsHelper.getRssByDataBase;
import static com.bluetooth.test.utils.UtilsHelper.getRssByMsgReceiver;
import static com.bluetooth.test.utils.UtilsHelper.oneDiDivideToTwoDi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bluetooth.test.database.MyDatabaseHelper;
import com.bluetooth.test.javabean.ReturnResult;
import com.bluetooth.test.service.MyMqttService;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class DisplayActivity extends AppCompatActivity {

    private final String TAG = "DisplayActivity";       // 调试用
    private ImageView mIvDisplay;                       // 人员感知结果图展示
    private MqttBroadcastReceiver mMqttBroadcastReceiver;   // 广播接收者
    private Intent serviceIntent;                       // 启动和停止 Service
    private ArrayList<ArrayList<Integer>> rssListOfLocationList;    // 根据位置组装为二维数组
    private final Timer timer = new Timer();    // 定时器：定时执行计算匹配结果并更新 UI
    private final ArrayList<String> msgReceiveTempList = new ArrayList<>();   // 存储广播接收到的数据的临时数组
    private final Handler uiHandler = new Handler();      // uiHandler在主线程中创建，所以自动绑定主线程

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        // 启动定时器
        timer.schedule(timerTask, 1000, 1000);

        // 绑定 Iv控件并设置展示默认图
        mIvDisplay = findViewById(R.id.iv_display);
        setImageByMinIndex(new ReturnResult(0.0, 0.0, 0));

        // 启用 service
        serviceIntent = new Intent(this, MyMqttService.class);
        // 开启 service实际上由当前 Activity执行
        startService(serviceIntent);
        Log.d(TAG, "start service");

        // 动态注册广播
        mMqttBroadcastReceiver = new MqttBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SEND_MQTT_PAYLOAD);    // 设置该页面接收哪些广播
        LocalBroadcastManager.getInstance(this).registerReceiver(mMqttBroadcastReceiver, intentFilter);

        // 实例化 SQLite数据库帮助对象
        MyDatabaseHelper mDbHelper = new MyDatabaseHelper(this, "LocationInfo.db", null, 1);
        SQLiteDatabase mLocationInfoDb = mDbHelper.getWritableDatabase();

        // 2. 调用提取函数从数据库中分别获取所有位置的 Rss数组
        ArrayList<Integer> rssListFromDataBase = getRssByDataBase(mLocationInfoDb);
        Log.d(TAG, rssListFromDataBase.toString());

        // 3. 将上述所有位置的 Rss数组拆分并重新组装为根据位置划分的二维数组
        rssListOfLocationList = oneDiDivideToTwoDi(rssListFromDataBase);
        Log.d(TAG, rssListOfLocationList.toString());
    }

    // 广播接收者：处理广播发送的信息（实现动态更新 UI）
    private class MqttBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SEND_MQTT_PAYLOAD.equals(intent.getAction())) {
                // 一旦后台 service更新数据，广播接收者会随之更新，接收到的数据也会随之变化
                Bundle bundle = intent.getExtras();
                String msgReceiver;     // 接受广播消息(String)
                if (bundle != null) {
                    msgReceiver = bundle.getString("MqttRevMsg");   // 获取接收到的数据
                    Log.d(TAG, msgReceiver);      // 打印接收到的数据
                    msgReceiveTempList.add(msgReceiver);
                }
            }
        }
    }

    // 根据传入的最小位置选择展示图片并弹出定位提示信息
    private void setImageByMinIndex(ReturnResult returnResult) {
        int minDistanceIndex = returnResult.getReturnID();
        if(minDistanceIndex != 0) {
            Toast.makeText(DisplayActivity.this,
                    "定位坐标: (" + returnResult.getX() + ", " + returnResult.getY() + ")->" + minDistanceIndex,
                    Toast.LENGTH_SHORT).show();
        }
        if (0 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_00);
        } else if (1 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_01);
        } else if (2 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_02);
        } else if (3 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_03);
        } else if (4 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_04);
        } else if (5 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_05);
        } else if (6 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_06);
        } else if (7 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_07);
        } else if (8 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_08);
        } else if (9 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_09);
        } else if (10 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_10);
        } else if (11 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_11);
        } else if (12 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_12);
        } else if (13 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_13);
        } else if (14 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_14);
        } else if (15 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_15);
        } else if (16 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_16);
        } else if (17 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_17);
        } else if (18 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_18);
        } else if (19 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_19);
        } else if (20 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_20);
        } else if (21 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_21);
        } else if (22 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_22);
        } else if (23 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_23);
        } else if (24 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_24);
        } else if (25 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_25);
        } else if (26 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_26);
        } else if (27 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_27);
        } else if (28 == minDistanceIndex) {
            mIvDisplay.setImageResource(R.drawable.location_28);
        }
    }

    // 按下 BACK键时注销广播接收者并终止服务
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMqttBroadcastReceiver); // 注销广播接收者
        stopService(serviceIntent);   // 停止服务
        timer.cancel();     // 取消定时器
        Log.d(TAG, "stop service");
    }

    // 定时器任务
    private final TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            // 当接收到的数据不为空时：更新 UI
            if (msgReceiveTempList.size() != 0) {
                // 1. 调用解析 Json函数得到 Rss数组
                ArrayList<Integer> rssListFromServer = getRssByMsgReceiver(msgReceiveTempList.get(0));
                Log.d(TAG, "收集到的数据: " + rssListFromServer);
                msgReceiveTempList.remove(0);   // 解析一个数据后即移除，节约内存空间

                // 2. 3. 为了减少代码执行次数，在 onCreate()中组装 Rss数值数组

                // 4.1 NN算法：计算欧氏距离得到最优定位点
                //  ReturnResult returnResult = NNAlgorithm(rssListFromServer, rssListOfLocationList);

                // 4.2 KNN算法：分别讨论 K = 3, 4, 5时的定位精度
                // ReturnResult returnResult = KNNAlgorithm(3, rssListFromServer, rssListOfLocationList);
                // ReturnResult returnResult = KNNAlgorithm(4, rssListFromServer, rssListOfLocationList);
                // ReturnResult returnResult = KNNAlgorithm(5, rssListFromServer, rssListOfLocationList);

                // 4.3 WKNN算法：分别讨论 K = 3, 4, 5时的定位精度
                ReturnResult returnResult = WKNNAlgorithm(3, rssListFromServer, rssListOfLocationList);
                // ReturnResult returnResult = WKNNAlgorithm(4, rssListFromServer, rssListOfLocationList);
                // ReturnResult returnResult = WKNNAlgorithm(5, rssListFromServer, rssListOfLocationList);

                // 5. 更新 UI：在子线程中无法更新，使用 handler
                Runnable runnable = () -> DisplayActivity.this.setImageByMinIndex(returnResult);
                uiHandler.post(runnable);
            }
        }
    };
}