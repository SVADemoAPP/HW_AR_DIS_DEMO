package com.huawei.hiardemo.area.activity;

import android.os.Bundle;

import com.huawei.hiardemo.area.util.LogUtils;
import com.journeyapps.barcodescanner.CaptureActivity;

public class PortraitZxingActivity extends CaptureActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        test();
    }

    private void test() {
        LogUtils.e("XHF","test-------------------dsd-sd---------------------------dsds-d-----------------");
        LogUtils.i("XHF","test-------------------dsd-sd---------------------------dsds-d-----info-----");
//        DBUtil.addARLocation("测试U5", "U7", new PointF(1, 7));
//        DBUtil.asyncQueryARLocation("测试U5", "U7", new DBUtil.DBListener() {
//            @Override
//            public void asyncQueryData(List<ARLoctionModel> data) {
//                List<ARLoctionModel> loctionModels = data;
//            }
//        });
//        List<ARLoctionModel> arLoctionModels = DBUtil.syncQueryARLocation("测试U5", "U7");
    }
}

