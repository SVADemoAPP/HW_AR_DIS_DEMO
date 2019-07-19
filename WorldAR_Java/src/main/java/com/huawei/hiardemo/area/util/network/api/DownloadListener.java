package com.huawei.hiardemo.area.util.network.api;

public interface DownloadListener {
    /***
     * 监听下载开始的时候
     */
    void onStart();

    /***
     * 当前下载进度
     * @param currentLength
     */
    void onProgress(int currentLength);

    /**
     * 是否完成完成大小
     *
     * @param localPath
     */
    void onFinish(String localPath);

    /**
     * 下载失败
     */
    void onFailure();
}
