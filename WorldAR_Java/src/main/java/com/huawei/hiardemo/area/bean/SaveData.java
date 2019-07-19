package com.huawei.hiardemo.area.bean;

public class SaveData {
    private long time;
    private int rsRp;
    private float x;
    private float y;
    private float z;
    public SaveData() {
    }

    public SaveData(long time, int rsRp, float x, float y, float z) {
        this.time = time;
        this.rsRp = rsRp;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public SaveData(long time, int rsRp, float x, float y) {
        this.time = time;
        this.rsRp = rsRp;
        this.x = x;
        this.y = y;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getRsRp() {
        return rsRp;
    }

    public void setRsRp(int rsRp) {
        this.rsRp = rsRp;
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

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "SaveData{" +
                "time=" + time +
                ", rsRp=" + rsRp +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
