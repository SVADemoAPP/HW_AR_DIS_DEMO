package com.huawei.hiardemo.area.util.network.api;
import com.huawei.hiardemo.area.bean.DataBean;

import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 下载文件接口
 * crate by xhf
 * 2019-5-20
 */
public interface ApiInterface {
    /**
     * 下载文件
     *
     * @param fileUrl 文件全路径
     * @return
     */
    @Streaming //大文件时要加不然会OOM
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    /***
     * 请求站点-地图信息接口
     * @return
     */
    @GET("/newsva/phone/getSiteAndMapInfos")
    Call<DataBean> getMapInfo();

    /**
     * 上传采样rsrp + 地图定位点接口
     *
     * @return
     */
    @POST("/newsva/phone/setNetWorkInfos")
    Call<DataBean> uploadRsRpInfo(@Body RequestBody body);

//    /**
//     * @param file 上传图片
//     * @return
//     */
//    @Multipart
//    @POST("/phone/uploadImage")
//    Call<DataBean> uploadImage(@QueryMap Map<String, String> map, @Part MultipartBody.Part file);

    /**
     * @param file 上传图片
     * @return
     */
//    @Headers("Content-Type:multipart/form-data; charset=utf-8")
    @Multipart
    @POST("/newsva/phone/uploadImage")
    Call<DataBean> uploadImage(@QueryMap Map<String, String> map, @Part MultipartBody.Part file);

    /**
     * 获取新地图
     *
     * @param path
     * @return
     */
    @GET("/newsva/mapInfo/getImage")
    Call<DataBean> getImageByApi(@Query(value = "path", encoded = true) String path);

    /**
     * 更新prruInfo信息
     *
     * @return
     */
    @POST("/newsva/phone/changePrruLocation")
    Call<DataBean> updatePrruInfo(@Query("prruId") String prruId, @Query("x") float x, @Query("y") float y);

    /**
     * 更新机房信息
     *
     * @return
     */
    @POST("/newsva/phone/uploadMachineRoom")
    Call<DataBean> updateMachineRoom(@QueryMap Map<String, String> map);

    /**
     * 下载Vins_Prru 附件图片
     */
    @POST("/newsva/mapInfo/findImage")
    Call<DataBean> downLoadPrruPicture(@Query("mapId") String mapId, @Query("x") float x, @Query("y") float y);
}
