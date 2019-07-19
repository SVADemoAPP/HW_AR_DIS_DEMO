package com.huawei.hiardemo.area.bean;

public class MapImage {
    private String mapId;
    private float x;
    private float y;

    public MapImage() {
    }

    public MapImage(String mapId, float x, float y) {
        this.mapId = mapId;
        this.x = x;
        this.y = y;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
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
        return "MapImage{" +
                "mapId='" + mapId + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
