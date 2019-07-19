package com.huawei.hiardemo.area.bean;

public class UploadInfo {
    private String mapId;
    private int rsrp;
    private float x;
    private float y;

    public UploadInfo() {
    }

    public UploadInfo(String mapId, int rsrp, float x, float y) {
        this.mapId = mapId;
        this.rsrp = rsrp;
        this.x = x;
        this.y = y;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public int getRsrp() {
        return rsrp;
    }

    public void setRsrp(int rsrp) {
        this.rsrp = rsrp;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "UploadInfo{" +
                "rsRp=" + rsrp +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
