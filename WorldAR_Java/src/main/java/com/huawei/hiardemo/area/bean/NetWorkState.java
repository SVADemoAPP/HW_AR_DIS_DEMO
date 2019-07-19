package com.huawei.hiardemo.area.bean;

public class NetWorkState {
    private String rsRp;
    private String sinr;

    public NetWorkState() {
    }

    public NetWorkState(String rsRp, String sinr) {
        this.rsRp = rsRp;
        this.sinr = sinr;
    }

    public String getRsRp() {
        return rsRp;
    }

    public void setRsRp(String rsRp) {
        this.rsRp = rsRp;
    }

    public String getSinr() {
        return sinr;
    }

    public void setSinr(String sinr) {
        this.sinr = sinr;
    }

    @Override
    public String toString() {
        return "NetWorkState{" +
                "rsRp=" + rsRp +
                ", sinr=" + sinr +
                '}';
    }
}
