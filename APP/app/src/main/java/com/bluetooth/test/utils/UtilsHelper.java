package com.bluetooth.test.utils;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bluetooth.test.javabean.BeaconJsonBean;
import com.bluetooth.test.javabean.CoordinateBean;
import com.bluetooth.test.javabean.CoordinateRecord;
import com.bluetooth.test.javabean.ReturnResult;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class UtilsHelper {
    private static final String TAG = "UtilsHelper";

    // 将所有位置的 Rss数组拆分并重新组装为根据位置划分的二维数组
    public static ArrayList<ArrayList<Integer>> oneDiDivideToTwoDi(ArrayList<Integer> rssListFromDataBase) {
        ArrayList<ArrayList<Integer>> rssListOfLocationList = new ArrayList<>();
        // 两层循环给二维数组赋值
        for (int i = 0; i < 28; i++) {
            ArrayList<Integer> rssTemp = new ArrayList<>();
            for (int j = 0; j < 6; j++) {
                rssTemp.add(rssListFromDataBase.get(i * 6 + j));
            }
            rssListOfLocationList.add(rssTemp);
        }
        return rssListOfLocationList;
    }

    // 提取数据：从数据库中分别获取 4个位置的 Rss信息数组
    @SuppressLint("Range")
    public static ArrayList<Integer> getRssByDataBase(SQLiteDatabase locationInfoDb) {
        ArrayList<Integer> rssList = new ArrayList<>(); // 存储查询结果的（每个位置的 Rss数值数组）
        Cursor cursor = locationInfoDb.query("location_info", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            rssList.add(cursor.getInt(cursor.getColumnIndex("beacon_rss_01")));
            rssList.add(cursor.getInt(cursor.getColumnIndex("beacon_rss_02")));
            rssList.add(cursor.getInt(cursor.getColumnIndex("beacon_rss_03")));
            rssList.add(cursor.getInt(cursor.getColumnIndex("beacon_rss_04")));
            rssList.add(cursor.getInt(cursor.getColumnIndex("beacon_rss_05")));
            rssList.add(cursor.getInt(cursor.getColumnIndex("beacon_rss_06")));
        }
        cursor.close();
        return rssList;
    }

    // 解析数据：安卓解析 Json格式（Gson类库），接受到的服务端的数据应该是字符串（网关一次性收集周边信标数据发送到服务端）
    // 解析数据应该得到数组，按顺序（6个信标的顺序）标识每个信标的 Rss值
    public static ArrayList<Integer> getRssByMsgReceiver(String msgReceiver) {
        // Gson解析 Json字符串，由 msgReceiver字符串转化为 beaconJsonBean对象
        Gson gson = new Gson();
        BeaconJsonBean beaconJsonBean = gson.fromJson(msgReceiver, BeaconJsonBean.class);
        // Log.d(TAG, beaconJsonBean.toString());

        // 解析 beaconJsonBean对象得到 Rss数值数组
        ArrayList<Integer> rssList = new ArrayList<>(
                Arrays.asList(-100, -100, -100, -100, -100, -100));     // 解析得到的 Rss数值存储数组
        ArrayList<ArrayList<String>> devices = beaconJsonBean.getDevices();  // 解析得到的 devices数组
        // 两层循环遍历 devices数组
        // 外层循环找到 6个 beacon的 Rss数据
        for (int i = 0; i < 6; i++) {
            for (ArrayList<String> array : devices) {
                // 内层循环找到每一个 beacon广播数组
                if (0 == i) {
                    if (array.get(1).equals("D10D835390A2")) {
                        rssList.set(0, Integer.valueOf(array.get(2)));
                        break;
                    }
                } else if (1 == i) {
                    if (array.get(1).equals("FEF5C4FE3DE7")) {
                        rssList.set(1, Integer.valueOf(array.get(2)));
                        break;
                    }
                } else if (2 == i) {
                    if (array.get(1).equals("F7CA277A3602")) {
                        rssList.set(2, Integer.valueOf(array.get(2)));
                        break;
                    }
                } else if (3 == i) {
                    if (array.get(1).equals("C4E497072BEF")) {
                        rssList.set(3, Integer.valueOf(array.get(2)));
                        break;
                    }
                } else if (4 == i) {
                    if (array.get(1).equals("E0FC523BDC4C")) {
                        rssList.set(4, Integer.valueOf(array.get(2)));
                        break;
                    }
                } else {
                    if (array.get(1).equals("EC9C1499D5F3")) {
                        rssList.set(5, Integer.valueOf(array.get(2)));
                        break;
                    }
                }
            }
        }
        return rssList;
    }

    // 欧式距离的计算：计算得到距离值，并保留两位小数
    private static Double calculateEuclideanDistance(ArrayList<Integer> rssListFromLocation, ArrayList<Integer> rssListFromServer) {
        double euclideanDistanceTemp;           // 累加的中间值
        double euclideanDistanceSum = 0.0;      // 每个坐标差的平方和
        double euclideanDistance;               // 欧氏距离
        for (int i = 0; i < 6; i++) {
            euclideanDistanceTemp = Math.pow(rssListFromLocation.get(i) - rssListFromServer.get(i), 2);
            euclideanDistanceSum += euclideanDistanceTemp;
        }
        euclideanDistance = Math.sqrt(euclideanDistanceSum);
        // 保留两位小数
        BigDecimal formatHelper = new BigDecimal(euclideanDistance);
        return formatHelper.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    // NN算法
    public static ReturnResult NNAlgorithm(ArrayList<Integer> rssListFromServer,
                                           ArrayList<ArrayList<Integer>> rssListOfLocationList) {
        // 1. 计算最小距离
        ArrayList<Double> distanceList = new ArrayList<>();
        for (int i = 0; i < 28; i++) {
            Double distance = calculateEuclideanDistance(rssListOfLocationList.get(i), rssListFromServer);
            distanceList.add(distance);
        }
        Log.d(TAG, distanceList.toString());

        // 2. 返回最优匹配坐标
        int returnID = distanceList.indexOf(Collections.min(distanceList)) + 1;
        double x = CoordinateRecord.coordinateRecord.get(returnID).getX();
        double y = CoordinateRecord.coordinateRecord.get(returnID).getY();
        Log.d(TAG, String.valueOf(returnID));

        return new ReturnResult(x, y, returnID);
    }

    // KNN算法：K = 3/4/5
    public static ReturnResult KNNAlgorithm(int K, ArrayList<Integer> rssListFromServer,
                                            ArrayList<ArrayList<Integer>> rssListOfLocationList) {
        // 1. 计算最小距离
        ArrayList<Double> distanceList = new ArrayList<>();
        for (int i = 0; i < 28; i++) {
            Double distance = calculateEuclideanDistance(rssListOfLocationList.get(i), rssListFromServer);
            distanceList.add(distance);
        }
        Log.d(TAG, distanceList.toString());

        // 2. 找到 distanceList数组中最小的 K个值及其编号（index + 1）
        HashMap<Integer, Double> minDistanceMap = new HashMap<>();
        for (int i = 0; i < K; i++) {
            Double minElement = Collections.min(distanceList);
            int indexOfMinElement = distanceList.indexOf(Collections.min(distanceList));
            minDistanceMap.put(indexOfMinElement + 1, minElement);
            distanceList.set(indexOfMinElement, 1000.0);    // 将 distanceList中已找到的最小值设为一个相对无穷大
        }
        Log.d(TAG, minDistanceMap.toString());

        // 3. 取到最小的 K个点及其定位坐标
        ArrayList<CoordinateBean> minCoordinateList = new ArrayList<>();
        for (Integer key : minDistanceMap.keySet()) {
            minCoordinateList.add(CoordinateRecord.coordinateRecord.get(key - 1));
        }
        Log.d(TAG, String.valueOf(minCoordinateList.size()));

        // 4. 计算最终匹配的坐标
        double sumX = 0.0;
        double sumY = 0.0;
        for (int i = 0; i < K; i++) {
            sumX += minCoordinateList.get(i).getX();
            sumY += minCoordinateList.get(i).getY();
        }
        double x = sumX / K;
        double y = sumY / K;
        Log.d(TAG, x + ", " + y);

        // 5. 根据得到的最小坐标计算最终的定位点编号
        double[] minDistanceForId = new double[28];
        for (int i = 0; i < 28; i++) {
            minDistanceForId[i] = calculateDistance(x, y, CoordinateRecord.coordinateRecord.get(i).getX(), CoordinateRecord.coordinateRecord.get(i).getY());
        }
        Log.d(TAG, Arrays.toString(minDistanceForId));
        Log.d(TAG, String.valueOf(getMinIndex(minDistanceForId) + 1));
        return new ReturnResult(x, y, getMinIndex(minDistanceForId) + 1);
    }

    // WKNN算法：取 KNN中的最优值
    public static ReturnResult WKNNAlgorithm(int K, ArrayList<Integer> rssListFromServer,
                                             ArrayList<ArrayList<Integer>> rssListOfLocationList) {
        // 1. 计算最小距离
        ArrayList<Double> distanceList = new ArrayList<>();
        for (int i = 0; i < 28; i++) {
            Double distance = calculateEuclideanDistance(rssListOfLocationList.get(i), rssListFromServer);
            distanceList.add(distance);
        }
        Log.d(TAG, distanceList.toString());

        // 2. 找到 distanceList数组中最小的 K个值及其编号（index + 1）
        HashMap<Integer, Double> minDistanceMap = new HashMap<>();
        for (int i = 0; i < K; i++) {
            Double minElement = Collections.min(distanceList);
            int indexOfMinElement = distanceList.indexOf(Collections.min(distanceList));
            minDistanceMap.put(indexOfMinElement + 1, minElement);
            distanceList.set(indexOfMinElement, 1000.0);    // 将 distanceList中已找到的最小值设为一个相对无穷大
        }
        Log.d(TAG, minDistanceMap.toString());

        // 3. 取到最小的 K个点及其定位坐标和 NN距离
        ArrayList<CoordinateBean> minCoordinateList = new ArrayList<>();
        for (Integer key : minDistanceMap.keySet()) {
            minCoordinateList.add(CoordinateRecord.coordinateRecord.get(key - 1));
        }
        Log.d(TAG, String.valueOf(minCoordinateList.size()));

        ArrayList<Double> minValues = new ArrayList<>(minDistanceMap.values());
        Log.d(TAG, minValues.toString());

        // 4. WKNN的计算：得到最小坐标（欧氏距离的倒数加权）
        double x = 0.0;
        double y = 0.0;
        if (3 == K) {
            x = calculateWKNN3(minValues.get(0), minValues.get(1), minValues.get(2),
                    minCoordinateList.get(0).getX(), minCoordinateList.get(1).getX(), minCoordinateList.get(2).getX());
            y = calculateWKNN3(minValues.get(0), minValues.get(1), minValues.get(2),
                    minCoordinateList.get(0).getY(), minCoordinateList.get(1).getY(), minCoordinateList.get(2).getY());
        } else if (4 == K) {
            x = calculateWKNN4(minValues.get(0), minValues.get(1), minValues.get(2), minValues.get(3),
                    minCoordinateList.get(0).getX(), minCoordinateList.get(1).getX(), minCoordinateList.get(2).getX(), minCoordinateList.get(3).getX());
            y = calculateWKNN4(minValues.get(0), minValues.get(1), minValues.get(2), minValues.get(3),
                    minCoordinateList.get(0).getY(), minCoordinateList.get(1).getY(), minCoordinateList.get(2).getY(), minCoordinateList.get(3).getY());
        } else if (5 == K) {
            x = calculateWKNN5(minValues.get(0), minValues.get(1), minValues.get(2), minValues.get(3), minValues.get(4),
                    minCoordinateList.get(0).getX(), minCoordinateList.get(1).getX(), minCoordinateList.get(2).getX(), minCoordinateList.get(3).getX(), minCoordinateList.get(4).getX());
            y = calculateWKNN5(minValues.get(0), minValues.get(1), minValues.get(2), minValues.get(3), minValues.get(4),
                    minCoordinateList.get(0).getY(), minCoordinateList.get(1).getY(), minCoordinateList.get(2).getY(), minCoordinateList.get(3).getY(), minCoordinateList.get(4).getY());
        }
        Log.d(TAG, x + ", " + y);

        // 5. 根据得到的最小坐标计算最终的定位点编号
        double[] minDistanceForId = new double[28];
        for (int i = 0; i < 28; i++) {
            minDistanceForId[i] = calculateDistance(x, y, CoordinateRecord.coordinateRecord.get(i).getX(), CoordinateRecord.coordinateRecord.get(i).getY());
        }
        Log.d(TAG, Arrays.toString(minDistanceForId));
        Log.d(TAG, String.valueOf(getMinIndex(minDistanceForId) + 1));
        return new ReturnResult(x, y, getMinIndex(minDistanceForId) + 1);
    }

    // WKNN K = 3的计算
    private static double calculateWKNN3(double minValue01, double minValue02, double minValue03,
                                         float p01, float p02, float p03) {
        double temp = (1 / (1 / minValue01 + 1 / minValue02 + 1 / minValue03));
        // Log.d(TAG, String.valueOf(temp));
        double result = ((1 / minValue01) * p01 + (1 / minValue02) * p02 + (1 / minValue03) * p03) * temp;
        // Log.d(TAG, String.valueOf(result));
        BigDecimal formatHelper = new BigDecimal(result);
        return formatHelper.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    // WKNN K = 4的计算
    private static double calculateWKNN4(double minValue01, double minValue02, double minValue03, double minValue04,
                                         float p01, float p02, float p03, float p04) {
        double temp = (1 / (1 / minValue01 + 1 / minValue02 + 1 / minValue03 + 1 / minValue04));
        // Log.d(TAG, String.valueOf(temp));
        double result = ((1 / minValue01) * p01 + (1 / minValue02) * p02 + (1 / minValue03) * p03 +
                (1 / minValue04) * p04) * temp;
        // Log.d(TAG, String.valueOf(result));
        BigDecimal formatHelper = new BigDecimal(result);
        return formatHelper.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    // WKNN K = 5的计算
    private static double calculateWKNN5(double minValue01, double minValue02, double minValue03, double minValue04, double minValue05,
                                         float p01, float p02, float p03, float p04, float p05) {
        double temp = (1 / (1 / minValue01 + 1 / minValue02 + 1 / minValue03 + 1 / minValue04 + 1 / minValue05));
        // Log.d(TAG, String.valueOf(temp));
        double result = ((1 / minValue01) * p01 + (1 / minValue02) * p02 + (1 / minValue03) * p03 +
                (1 / minValue04) * p04 + (1 / minValue05) * p05) * temp;
        // Log.d(TAG, String.valueOf(result));
        BigDecimal formatHelper = new BigDecimal(result);
        return formatHelper.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    // 定位点编号计算：欧氏距离
    private static double calculateDistance(double minX, double minY, float coordinateX, float coordinateY) {
        double distance = Math.sqrt(Math.pow(minX - coordinateX, 2) + Math.pow(minY - coordinateY, 2));
        // 保留两位小数
        BigDecimal formatHelper = new BigDecimal(distance);
        return formatHelper.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    // 得到数组最小值索引
    private static int getMinIndex(double[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int minIndex = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[minIndex] > arr[i + 1]) {
                minIndex = i + 1;
            }
        }
        return minIndex;
    }
}
