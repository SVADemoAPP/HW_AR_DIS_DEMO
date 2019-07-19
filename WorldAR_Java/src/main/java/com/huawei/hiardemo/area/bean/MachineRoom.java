package com.huawei.hiardemo.area.bean;

public class MachineRoom {

    /**
     * 机房id
     */
    private String id;

    /**
     * mapID
     */
    private String mapId;

    /**
     * 机房X坐标
     */
    private float x;

    /**
     * 机房Y坐标
     */
    private float y;

    public MachineRoom() {
    }

    public MachineRoom(String id, String mapId, float x, float y) {
        this.id = id;
        this.mapId = mapId;
        this.x = x;
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return "MachineRoom{" +
                "id='" + id + '\'' +
                ", mapId='" + mapId + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
