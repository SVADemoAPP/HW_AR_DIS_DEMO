package com.huawei.hiardemo.area.util;

public class InterRequestUtils {
//    private static Context context;
//
//    public InterRequestUtils(Context context) {
//        context = context;
//    }
//
//    public static InterRequestUtils getInstance(Context mContext) {
//        context = mContext;
//        if (Constant.interRequestUtils == null) {
//            Constant.interRequestUtils = new InterRequestUtils(mContext);
//        }
//        return Constant.interRequestUtils;
//    }
//
//    public void hasLogin(int method, String url, Listener<JSONObject> listener, ErrorListener errorListener) {
//        JsonObjectPostRequest jRequest = new JsonObjectPostRequest(method, url, listener, errorListener);
//        jRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 1, 1.0f));
//        jRequest.setSendCookie(SharedPrefHelper.getString(context, "Cookie"));
//        Constant.mRequestQueue.add(jRequest);
//    }
//
//    public void login(int method, String url, Listener<JSONObject> listener, ErrorListener errorListener, Map<String, String> map) {
//        Constant.mRequestQueue.add(new JsonObjectPostRequest(method, url, listener, errorListener, map));
//    }
//
//    public void exitLogin(int method, String url, Listener<JSONObject> listener, ErrorListener errorListener) {
//        JsonObjectPostRequest jRequest = new JsonObjectPostRequest(method, url, listener, errorListener);
//        jRequest.setSendCookie(SharedPrefHelper.getString(context, "Cookie"));
//        Constant.mRequestQueue.add(jRequest);
//    }
//
//    public void getMapData(String cookie, int method, String url, Listener<JSONObject> listener, ErrorListener errorListener) {
//        JsonObjectPostRequest jRequest = new JsonObjectPostRequest(method, url, listener, errorListener);
//        jRequest.setSendCookie(cookie);
//        jRequest.setRetryPolicy(new DefaultRetryPolicy(5000, 1, 1.0f));
//        Constant.mRequestQueue.add(jRequest);
//    }
//
//    public void getBookRRUData(int method, String url, Listener<JSONObject> listener, ErrorListener errorListener, Map map) {
//        JsonObjectPostRequest jRequest = new JsonObjectPostRequest(method, url, listener, errorListener, map);
//        jRequest.setSendCookie(SharedPrefHelper.getString(context, "Cookie"));
//        Constant.mRequestQueue.add(jRequest);
//    }
//
//    public void getLocAndPrruInfo(int method, String url, Listener<String> listener, ErrorListener errorListener) {
//        StringPostRequest sPostRequest = new StringPostRequest(method, url, listener, errorListener);
//        sPostRequest.setSendCookie(SharedPrefHelper.getString(context, "Cookie"));
//        Constant.mRequestQueue.add(sPostRequest);
//    }
//
//    public void getAllPrruInfo(int method, String url, Listener<String> listener, ErrorListener errorListener) {
//        StringPostRequest sPostRequest = new StringPostRequest(method, url, listener, errorListener);
//        sPostRequest.setSendCookie(SharedPrefHelper.getString(context, "Cookie"));
//        Constant.mRequestQueue.add(sPostRequest);
//    }
//
//    public void getPhonePrru(int method, String url, Listener<String> listener, ErrorListener errorListener) {
//        StringPostRequest sPostRequest = new StringPostRequest(method, url, listener, errorListener);
//        sPostRequest.setSendCookie(SharedPrefHelper.getString(context, "Cookie"));
//        Constant.mRequestQueue.add(sPostRequest);
//    }
}
