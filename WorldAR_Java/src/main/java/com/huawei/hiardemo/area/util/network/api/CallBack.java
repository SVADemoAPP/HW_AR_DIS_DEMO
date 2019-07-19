package com.huawei.hiardemo.area.util.network.api;

public interface CallBack<T> {
    void onSucCallBack(T data);

    void onFailCallBack();
}
