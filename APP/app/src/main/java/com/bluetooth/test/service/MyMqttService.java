package com.bluetooth.test.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MyMqttService extends Service {

    private final String TAG = "MyMqttService";     // 调试用

    private MqttAndroidClient mMqttAndroidClient;     // Android MQTT Client
    private MqttConnectOptions mMqttConnectOptions;     // MQTT 连接选项配置
    private final String subscribeTopic = "RssUpload";         // 订阅的主题

    public static final String SEND_MQTT_PAYLOAD = "com.bluetooth.test.SEND_MQTT_PAYLOAD";    // 广播消息

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init();     // 初始化连接服务器的配置信息
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // 初始化链接服务器的参数并开始连接服务器
    private void init() {
        String serverURI = "tcp://broker-cn.emqx.io:1883";   //服务器地址（协议+地址+端口号）
        String clientId = "ANDROID_BLE_DEVICE_01";     // 由于只有一个设备，故设置固定设备号
        mMqttAndroidClient = new MqttAndroidClient(this, serverURI, clientId);
        mMqttAndroidClient.setCallback(mqttCallback);       // 设置监听订阅消息的回调
        mMqttConnectOptions = new MqttConnectOptions();     // 参数配置对象
        mMqttConnectOptions.setCleanSession(true);          // 设置是否清除缓存
        mMqttConnectOptions.setConnectionTimeout(10);    // 设置超时时间，单位：秒
        mMqttConnectOptions.setKeepAliveInterval(120);    // 设置心跳包发送间隔，单位：秒
        // last will message
        boolean doConnect = true;
        String message = "{\"terminal_uid\":\"" + clientId + "\",\"msg\":\"Client offline\"}";
        try {
            // 遗嘱发布主题
            String endWillMsgPublishTopic = "EndWillMsg";
            mMqttConnectOptions.setWill(endWillMsgPublishTopic, message.getBytes(), 0, false);
        } catch (Exception e) {
            Log.d(TAG, "Exception Occurred", e);
            doConnect = false;
            iMqttActionListener.onFailure(null, e);
        }

        // 调用连接方法：开始服务器的连接
        if (doConnect) {
            doClientConnection();
        }
    }

    // 连接MQTT服务器
    private void doClientConnection() {
        if(null != mMqttAndroidClient) {
            if (!mMqttAndroidClient.isConnected() && isConnectIsNormal()) {
                try {
                    mMqttAndroidClient.connect(mMqttConnectOptions, null, iMqttActionListener);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 判断网络是否连接
    private boolean isConnectIsNormal() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            Log.d(TAG, "当前网络名称: " + name);
            return true;
        } else {
            Log.d(TAG, "没有可用网络");
            // 没有可用网络的时候，延迟 3秒再尝试重连
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doClientConnection();
                }
            }, 3000);
            return false;
        }
    }

    // MQTT是否连接成功的监听：连接成功后则订阅指定主题
    private final IMqttActionListener iMqttActionListener = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken arg0) {
            Log.d(TAG, "MQTT服务器连接成功！");
            try {
                // 订阅主题，参数：主题、服务质量
                IMqttToken token = mMqttAndroidClient.subscribe(subscribeTopic, 0);
                token.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken iMqttToken) {
                        Log.d(TAG, "Subscribe Successfully " + subscribeTopic);
                    }
                    @Override
                    public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                        Log.e(TAG, "Subscribe Failed " + subscribeTopic);
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            arg1.printStackTrace();
            Log.d(TAG, "MQTT服务器连接失败！");
            doClientConnection();       // 连接失败，重连（可关闭服务器进行模拟）
        }
    };

    // 订阅主题的回调：收到服务端发送的消息时自动执行
    private final MqttCallback mqttCallback = new MqttCallback() {
        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            // Log.d(TAG, "收到" + topic + "发来的消息: " + new String(message.getPayload()));
            // 收到消息，这里弹出 Toast表示，如果需要更新 UI，可以使用广播或者 EventBus 进行发送
            // Toast.makeText(getApplicationContext(), "messageArrived: " + new String(message.getPayload()), Toast.LENGTH_LONG).show();
            String msgForTransfer = new String(message.getPayload());

            // intent 包装 bundle 对象并通过 broadcast 发送
            Intent intent = new Intent(SEND_MQTT_PAYLOAD);
            Bundle bundle = new Bundle();
            bundle.putString("MqttRevMsg", msgForTransfer);       // 键值对
            intent.putExtras(bundle);
            LocalBroadcastManager.getInstance(MyMqttService.this).sendBroadcast(intent); // 发送广播
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {
        }

        @Override
        public void connectionLost(Throwable arg0) {
            Log.d(TAG, "MQTT服务器连接断开 ");
            doClientConnection();       // 连接断开，重连
        }
    };

    @Override
    public void onDestroy() {
        // MqttAndroidClient的源码，发现它在里面通过context绑定了一个服务，并且通过广播与绑定的服务通信。
        // 所以在断开连接前需要调用unregisterResources()注销广播，并且广播属于异步操作，
        // 在调用unregisterResources()后需要等待一小会才能调用disconnect()
        try {
            // mMqttAndroidClient.unsubscribe(subscribeTopic); // 取消主题订阅
            mMqttAndroidClient.unregisterResources();     // 注销 intentReceiver
            mMqttAndroidClient.close();                 // 关闭客户端连接
            mMqttAndroidClient.disconnect();     // 断开连接（从 HashMap中删除 Client）
            mMqttAndroidClient.setCallback(null);
            mMqttAndroidClient = null;
        } catch (MqttException e) {
            e.printStackTrace();
        }
        super.onDestroy();
        Log.d(TAG, "MqttService OnDestroy()");
    }
}