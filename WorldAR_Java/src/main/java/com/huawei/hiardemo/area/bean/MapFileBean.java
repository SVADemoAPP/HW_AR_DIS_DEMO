package com.huawei.hiardemo.area.bean;

import java.io.Serializable;

/**
 * Create by 91569
 * Time 2019/7/14
 * Describe :
 */
public class MapFileBean implements Serializable {
    private String filePath;
    private String fileName;

    public MapFileBean(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
