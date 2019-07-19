package com.huawei.hiardemo.area.util.network;

import com.google.gson.Gson;
import com.huawei.hiardemo.area.bean.DataBean;
import com.huawei.hiardemo.area.bean.UploadInfo;
import com.huawei.hiardemo.area.util.LogUtils;
import com.huawei.hiardemo.area.util.network.api.ApiInterface;
import com.huawei.hiardemo.area.util.network.api.CallBack;
import com.huawei.hiardemo.area.util.network.utils.RetrofitUtil;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiManager {

    /***
     * 请求地图接口
     */
    public static void getMapData(final CallBack callBack) {
        ApiInterface apiInterface = RetrofitUtil.getInstance().create(ApiInterface.class);
        Call<DataBean> mapInfo = apiInterface.getMapInfo();
        mapInfo.enqueue(new Callback<DataBean>() {


            @Override
            public void onResponse(Call<DataBean> call, Response<DataBean> response) {
                callBack.onSucCallBack(response.body());
            }

            @Override
            public void onFailure(Call<DataBean> call, Throwable t) {
                callBack.onFailCallBack();
            }
        });
    }

    public static void getImageByApi(String path, final CallBack callBack) {
        ApiInterface apiInterface = RetrofitUtil.getInstance().create(ApiInterface.class);
        Call<DataBean> imageByApiCall = apiInterface.getImageByApi(path);
        imageByApiCall.enqueue(new Callback<DataBean>() {
            @Override
            public void onResponse(Call<DataBean> call, Response<DataBean> response) {
                callBack.onSucCallBack(response.body());

            }

            @Override
            public void onFailure(Call<DataBean> call, Throwable t) {
                callBack.onFailCallBack();
            }
        });
    }

    /**
     * 上传rsrp接口
     */
    public static void updateRsRpInfo(List<UploadInfo> data, final CallBack callBack) {
        Gson gson = new Gson();
        String uploadInfo = gson.toJson(data);
        LogUtils.e("XHF", "UploadInfos = " + uploadInfo);
        ApiInterface apiInterface = RetrofitUtil.getInstance().create(ApiInterface.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), uploadInfo);
        Call<DataBean> dataBeanCall = apiInterface.uploadRsRpInfo(body);
        dataBeanCall.enqueue(new Callback<DataBean>() {
            @Override
            public void onResponse(Call<DataBean> call, Response<DataBean> response) {
                callBack.onSucCallBack(response.body());
            }

            @Override
            public void onFailure(Call<DataBean> call, Throwable t) {
                callBack.onFailCallBack();
            }
        });
    }


    /**
     * 上传文件
     */
    public static void upLoadImage(String mapId, float x, float y, File file, final CallBack callBack) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("mapId", mapId);
        hashMap.put("x", String.valueOf(x));
        hashMap.put("y", String.valueOf(y));
        ApiInterface apiInterface = RetrofitUtil.getInstance().create(ApiInterface.class);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Call<DataBean> responseBodyCall = apiInterface.uploadImage(hashMap, body);
        responseBodyCall.enqueue(new Callback<DataBean>() {
            @Override
            public void onResponse(Call<DataBean> call, Response<DataBean> response) {
                callBack.onSucCallBack(response.body());
                LogUtils.e("SVA", "上传成功");
            }

            @Override
            public void onFailure(Call<DataBean> call, Throwable t) {
                callBack.onFailCallBack();
                LogUtils.e("SVA", "上传失败");
            }
        });
    }

    /**
     * 更新prruInfo接口
     */
    public static void upLoadPrruInfo(String prruId, float x, float y, final CallBack callBack) {
        ApiInterface apiInterface = RetrofitUtil.getInstance().create(ApiInterface.class);
        Call<DataBean> dataBeanCall = apiInterface.updatePrruInfo(prruId, x, y);
        dataBeanCall.enqueue(new Callback<DataBean>() {
            @Override
            public void onResponse(Call<DataBean> call, Response<DataBean> response) {
                callBack.onSucCallBack(response.body());
                LogUtils.e("SVA", "PrruInfo更新成功");
            }

            @Override
            public void onFailure(Call<DataBean> call, Throwable t) {
                callBack.onFailCallBack();
                LogUtils.e("SVA", "PrruInfo更新失败");
            }
        });
    }

    /**
     * 更新机房接口
     */
    public static void updateMachineRoom(String mapId, float x, float y, final CallBack callBack) {
        Map<String, String> map = new HashMap<>();
        map.put("mapId", mapId);
        map.put("x", String.valueOf(x));
        map.put("y", String.valueOf(y));
        ApiInterface apiInterface = RetrofitUtil.getInstance().create(ApiInterface.class);
        Call<DataBean> dataBeanCall = apiInterface.updateMachineRoom(map);
        dataBeanCall.enqueue(new Callback<DataBean>() {
            @Override
            public void onResponse(Call<DataBean> call, Response<DataBean> response) {
                callBack.onSucCallBack(response.body());
                LogUtils.e("SVA", "PrruInfo更新成功");
            }

            @Override
            public void onFailure(Call<DataBean> call, Throwable t) {
                callBack.onFailCallBack();
                LogUtils.e("SVA", "PrruInfo更新失败");
            }
        });
    }


    public static void downloadPrruPic(String mapId, float x, float y, final CallBack callBack) {
        ApiInterface apiInterface = RetrofitUtil.getInstance().create(ApiInterface.class);
        Call<DataBean> dataBeanCall = apiInterface.downLoadPrruPicture(mapId, x, y);
        dataBeanCall.enqueue(new Callback<DataBean>() {
            @Override
            public void onResponse(Call<DataBean> call, Response<DataBean> response) {
                callBack.onSucCallBack(response.body());
            }

            @Override
            public void onFailure(Call<DataBean> call, Throwable t) {
                callBack.onFailCallBack();
            }
        });
    }
}
