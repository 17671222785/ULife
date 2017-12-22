package com.andywang.ulife.callback;

/**
 * Created by parting_soul on 2016/10/6.
 * http工具类的回调函数
 */

public interface HttpCallBack {
    /**
     * 下载成功时调用,该方法在子线程调用
     *
     * @param result 字符串形式的下载的结果
     */
    public void onResult(String result);

    /**
     * 下载成功时调用,该方法在子线程调用
     *
     * @param result 字符数组形式的下载结果
     */
    public void onResult(byte[] result);

    /**
     * 产生异常时调用,该方法在子线程调用
     *
     * @param e 异常类型
     */
    public void onError(Exception e);
}
