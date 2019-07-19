package com.huawei.hiardemo.area.bean;

import java.io.Serializable;

/**
 * 获取到的地图信息
 */
public class MapInfo implements Serializable {
    String id;
    String mapId;
    String imagePath;
    String mapName;
    float x;
    float y;
    int scale;
    String siteId;
    Site site;

    public MapInfo() {

    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public MapInfo(String id, String mapId, String imagePath, String mapName, float x, float y, int scale, String siteId, Site site) {
        this.id = id;
        this.mapId = mapId;
        this.imagePath = imagePath;
        this.mapName = mapName;
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.siteId = siteId;
        this.site = site;
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

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
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

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @Override
    public String toString() {
        return "MapInfo{" +
                "id='" + id + '\'' +
                ", mapId='" + mapId + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", mapName='" + mapName + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", scale=" + scale +
                ", siteId='" + siteId + '\'' +
                ", site=" + site +
                '}';
    }
}
