package com.huawei.hiardemo.area.bean;

import java.util.List;

public class DataListTree<K, V> {
    private K mGroupItem;
    private List<V> mSubItem;

    public DataListTree(K groupItem, List<V> subItem) {
        mGroupItem = groupItem;
        mSubItem = subItem;
    }

    public K getGroupItem() {
        return mGroupItem;
    }

    public List<V> getSubItem() {
        return mSubItem;
    }
}
