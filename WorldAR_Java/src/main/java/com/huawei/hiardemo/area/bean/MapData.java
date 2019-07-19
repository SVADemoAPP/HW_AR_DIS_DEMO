package com.huawei.hiardemo.area.bean;

public class MapData {
    private String name;

    public MapData() {

    }

    public MapData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MapData{" +
                "name='" + name + '\'' +
                '}';
    }
}
