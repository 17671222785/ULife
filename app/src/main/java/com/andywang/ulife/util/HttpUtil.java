package com.andywang.ulife.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by andyWang on 2017/11/29 0029.
 * 邮箱：393656489@qq.com
 */

/**
 * 与服务器交互的工具类
 */
public class HttpUtil {

    /**
     * 发送HTTP请求
     * @param address 请求地址
     * @param callback 注册的回调来处理服务器响应
     */
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }


}

