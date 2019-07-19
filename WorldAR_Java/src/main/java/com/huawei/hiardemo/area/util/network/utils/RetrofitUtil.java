package com.huawei.hiardemo.area.util.network.utils;

import android.content.Context;
import android.text.TextUtils;

import com.huawei.hiardemo.area.util.Constant;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {
    private static Retrofit mRetrofit;
    private Context mContext;
    private static OkHttpClient mOkHttpClient;

    public static Retrofit getInstance() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(Constant.REQUEST_ADRESS)   //设置基地址
                    .client(getOKHttpClientInstance())    //设置
                    // 添加生成bean的工厂
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }

    public static void destory() {
        mRetrofit = null;
    }

    private static OkHttpClient getOKHttpClientInstance() {
        if (mOkHttpClient == null) {
            initOkHttpClient();
        }
        return mOkHttpClient;
    }

    /**
     * 初始化okhttpClient客户端
     */
    private static void initOkHttpClient() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)  // 设置超时时间
                .readTimeout(10000L, TimeUnit.MILLISECONDS)  // 设置读写时间
                .build();
    }

    public static String URLEncoded(String paramString) {
        if (TextUtils.isEmpty(paramString)) {
            return "";
        }
        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
