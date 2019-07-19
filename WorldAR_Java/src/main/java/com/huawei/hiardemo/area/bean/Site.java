package com.huawei.hiardemo.area.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 站点
 */
public class Site implements Serializable {
    String id;
    String siteName;
    List<MapInfo> mapInfos;
    String svaList;

    public Site() {

    }

    public Site(String id, String siteName, List<MapInfo> mapInfos, String svaList) {
        this.id = id;
        this.siteName = siteName;
        this.mapInfos = mapInfos;
        this.svaList = svaList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public List<MapInfo> getMapInfos() {
        return mapInfos;
    }

    public void setMapInfos(List<MapInfo> mapInfos) {
        this.mapInfos = mapInfos;
    }

    public String getSvaList() {
        return svaList;
    }

    public void setSvaList(String svaList) {
        this.svaList = svaList;
    }

    @Override
    public String toString() {
        return "Site{" +
                "id='" + id + '\'' +
                ", siteName='" + siteName + '\'' +
                ", mapInfos=" + mapInfos +
                ", svaList='" + svaList + '\'' +
                '}';
    }
}
