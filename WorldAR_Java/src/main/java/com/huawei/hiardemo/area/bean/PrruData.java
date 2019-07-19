package com.huawei.hiardemo.area.bean;

public class PrruData {
    String mapid;
    String id;
    float rsrp;
    float x;
    float y;

    public PrruData() {
    }

    public PrruData(String mapid, String id, float rsrp, float x, float y) {
        this.mapid = mapid;
        this.id = id;
        this.rsrp = rsrp;
        this.x = x;
        this.y = y;
    }

    public float getRsrp() {
        return rsrp;
    }

    public void setRsrp(float rsrp) {
        this.rsrp = rsrp;
    }

    public String getMapid() {
        return mapid;
    }

    public void setMapid(String mapid) {
        this.mapid = mapid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return "PrruData{" +
                "mapid='" + mapid + '\'' +
                ", id='" + id + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
