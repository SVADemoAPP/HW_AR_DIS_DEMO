package com.huawei.hiardemo.area.util;

import android.bluetooth.BluetoothAdapter;

public class BlueUntils {
    private static BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
    public static BluetoothAdapter getBluetoothAdapter(){
        return mAdapter;
    }

    public static boolean isBluetoothAvaliable(){
        return mAdapter != null;
    }


}
