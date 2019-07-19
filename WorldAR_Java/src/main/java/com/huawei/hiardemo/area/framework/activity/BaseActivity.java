package com.huawei.hiardemo.area.framework.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.huawei.hiardemo.area.activity.SettingActivity;
import com.huawei.hiardemo.area.util.SharePref;
import com.tbruyelle.rxpermissions2.RxPermissions;


import io.reactivex.functions.Consumer;

import static com.huawei.hiardemo.area.util.Constant.ADRESS;


public abstract class BaseActivity extends AppCompatActivity {
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setLayout();
        getRxPermission();

    }

    public abstract void setLayout();

    public abstract void initView();

    public abstract void initData();

    /***
     * 动态获取权限
     */
    private void getRxPermission() {
        RxPermissions rxPermissions = new RxPermissions(this); // where this is an Activity instance
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {  //当所有权限都允许之后，返回true
                            initView();
                            initData();
                        } else { //没有给权限
                            Toast.makeText(BaseActivity.this, "未授权权限，部分功能不能使用", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 判断是否没有填写地址信息
     */
    public void judgeAdress() {
        String adress = (String) SharePref.get(mContext, ADRESS, "");
        if (adress.equals("")) { //如果地址为空则进入设置页面
            startActivity(new Intent(this, SettingActivity.class));
        }
    }

    public void getOtherRxPermission(String[] permission, final PerMissonListener listener) {
        RxPermissions rxPermissions = new RxPermissions(this); // where this is an Activity instance
        rxPermissions.request(permission)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {  //当所有权限都允许之后，返回true
                            listener.havePermission();
                        } else { //没有给权限
                            listener.missPermission();
                        }
                    }
                });

    }

    /***
     *  权限回调接口
     */
    public interface PerMissonListener {

        void havePermission();

        void missPermission();
    }
}
