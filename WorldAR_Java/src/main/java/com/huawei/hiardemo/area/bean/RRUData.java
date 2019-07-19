package com.huawei.hiardemo.area.bean;

public class RRUData {
    private int pRRUNumber;
    private int radius;

    public RRUData() {
    }

    public RRUData(int pRRUNumber, int radius) {
        this.pRRUNumber = pRRUNumber;
        this.radius = radius;
    }

    public int getpRRUNumber() {
        return pRRUNumber;
    }

    public void setpRRUNumber(int pRRUNumber) {
        this.pRRUNumber = pRRUNumber;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
