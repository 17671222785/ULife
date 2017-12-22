package com.andywang.ulife.util.network;

import android.os.Handler;
import android.os.Message;

/**
 * Created by parting_soul on 2016/10/6.
 * 抽象类 消息处理类
 */

public abstract class AbstractDownLoadHandler extends Handler {
    /**
     * 下载错误时要处理的逻辑代码段
     */
    protected abstract void showError();

    /**
     * 下载正确则更新相应的UI
     *
     * @param msg
     */
    protected abstract void updateUI(Message msg);

}
