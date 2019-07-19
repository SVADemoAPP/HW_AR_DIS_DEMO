package com.huawei.hiardemo.area.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.huawei.hiardemo.area.R;
import com.huawei.hiardemo.area.framework.activity.BaseActivity;
import com.huawei.hiardemo.area.view.popup.RePortPopupWindow;

import junit.framework.Test;

import java.util.HashMap;

public class TestActivity extends BaseActivity {

    private RePortPopupWindow rePortPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        rePortPopupWindow = new RePortPopupWindow(TestActivity.this);
        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                rePortPopupWindow.show();
            }
        });
        HashMap<String, String> map = new HashMap<>();
        map.put("time","2019/7/10  15:45-15:50");
        map.put("ip","10.149.4.12");
        map.put("area","U5-2F");
        map.put("data","/scard/0/Vins/data");
        map.put("value1","30%"); //-75 - 0
        map.put("value2","20%"); //-95 -  -75
        map.put("value3","40%"); //-105 - -95
        map.put("value4","10%"); //-120 - -105

        rePortPopupWindow.setDetails(map);
    }

    @Override
    public void setLayout() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
