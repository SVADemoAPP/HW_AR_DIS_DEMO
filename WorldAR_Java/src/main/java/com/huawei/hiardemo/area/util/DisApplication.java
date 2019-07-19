package com.huawei.hiardemo.area.util;

import android.os.Environment;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.io.File;

public class DisApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        initPhotoError();
        Constant.FilePath = Environment.getExternalStorageDirectory().getPath() + File.separator + Constant.PROJECT_NAME;
        Constant.AR_PATH=Environment.getExternalStorageDirectory().getPath() + File.separator + Constant.PROJECT_NAME+File.separator+"AR";
        Constant.MAP = Constant.FilePath + File.separator + "map";
        Constant.CSV_FilePath=Constant.FilePath+File.separator+"csv";
        LogUtils.getInstance()
                .setDiskPath(Constant.FilePath + File.separator + "Log")
                .setLevel(LogUtils.VERBOSE_LEVEL)
                .setWriteFlag(true);
        FlowManager.init(this); //初始化数据库问题
        Stetho.initializeWithDefaults(getApplicationContext()); //初始化facebook chrome 持久化数据查看
        LogUtils.getInstance()
                .setDiskPath(Constant.AR_PATH+File.separator+"Log")
                .setLevel(LogUtils.VERBOSE_LEVEL)
                .setWriteFlag(true);  //写入日志文件
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
    }

    private void initPhotoError() {
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

}
