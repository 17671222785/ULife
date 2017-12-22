package com.andywang.ulife.util.network;

import android.content.Context;
import android.net.ConnectivityManager;

import com.andywang.ulife.util.support.NewsApplication;


/**
 * Created by parting_soul on 2016/10/23.
 */

public class NetworkInfo {
    /**
     * 检测当的网络（WLAN、4G/3G/2G）状态
     *
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) NewsApplication.getContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getState() == android.net.NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前是否处于wifi状态
     *
     * @return boolean
     */
    public static boolean isWifiAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) NewsApplication.getContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }
}
