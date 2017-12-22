package com.andywang.ulife.callback;

import java.util.List;

/**
 * Created by parting_soul on 2016/10/31.
 */

public interface CollectionCallBack<T> {
    public void getResult(List<T> lists);

    public void isSuccess(boolean isSuccess);
}
