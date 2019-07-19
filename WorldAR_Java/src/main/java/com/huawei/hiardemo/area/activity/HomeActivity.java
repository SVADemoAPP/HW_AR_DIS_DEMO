package com.huawei.hiardemo.area.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.huawei.hiardemo.area.R;
import com.huawei.hiardemo.area.bean.DataBean;
import com.huawei.hiardemo.area.bean.DataListTree;
import com.huawei.hiardemo.area.bean.MapInfo;
import com.huawei.hiardemo.area.bean.Site;
import com.huawei.hiardemo.area.framework.activity.BaseActivity;
import com.huawei.hiardemo.area.util.BitmapUtil;
import com.huawei.hiardemo.area.util.Constant;
import com.huawei.hiardemo.area.util.SharePref;
import com.huawei.hiardemo.area.util.network.ApiManager;
import com.huawei.hiardemo.area.util.network.api.CallBack;
import com.huawei.hiardemo.area.util.network.utils.RetrofitUtil;
import com.huawei.hiardemo.area.view.MarqueeTextView;
import com.huawei.hiardemo.area.view.popup.MapSelectPopupWindow;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.huawei.hiardemo.area.util.Constant.ADRESS;
import static com.huawei.hiardemo.area.util.Constant.HTTP_TYPE;


public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private Context mContext;
    private MarqueeTextView mTvMq;
    private ImageView mIvBefore;
    private ImageView mIvAfter;
    private ImageView mIvLoc;
    private ImageView mIvSettings;
    private MapSelectPopupWindow mMapSelectPopupWindow;
    private LinearLayout mLlMap;
    private List<DataListTree<Site, MapInfo>> mMapData = new ArrayList<>();
    private boolean isFirst = false;
    private boolean isDownload = false;

    @Override
    public void setLayout() {
        mContext = HomeActivity.this;
        setContentView(R.layout.activity_home);
    }

    private void initMapPopupWindow() {
        initSelectPopupWindow();
        getMapInfoData();
    }

    private void initSelectPopupWindow() {
        mMapSelectPopupWindow = new MapSelectPopupWindow(mContext, mMapData);
        mMapSelectPopupWindow.setOnItemClick(new MapSelectPopupWindow.OnItemClick() {
            @Override
            public void onItemClick(final MapInfo mapInfo) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTvMq.setText(mapInfo.getMapName());
                            }
                        });
                        Constant.Scale = mapInfo.getScale();
                        Constant.MAP_ID = mapInfo.getId();
                        downLoadMap(mapInfo.getImagePath());
                    }
                });
            }
        });
    }

    @Override
    public void initView() {
        mTvMq = findViewById(R.id.marque);
        mLlMap = findViewById(R.id.ll_map);
        mIvBefore = findViewById(R.id.before);
        mIvAfter = findViewById(R.id.after);
        mIvLoc = findViewById(R.id.loc);
        mIvSettings = findViewById(R.id.settings);
        mIvBefore.setOnClickListener(this);
        mIvAfter.setOnClickListener(this);
        mIvLoc.setOnClickListener(this);
        mIvSettings.setOnClickListener(this);
        mTvMq.setOnClickListener(this);
        mLlMap.setOnClickListener(this);
    }

    @Override
    public void initData() {
        saveTempAdress();
        initMapPopupWindow();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTvMq != null) {
            mTvMq.resumeScroll();

        }
        getMapInfoData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTvMq != null) {
            mTvMq.pauseScroll();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mTvMq != null) {
            mTvMq.stopScroll();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mTvMq != null) {
            mTvMq.startScroll();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapSelectPopupWindow.hide();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.before:
                Intent intent = new Intent(mContext, FloorMapActivity.class);
                intent.putExtra("download" ,isDownload);
                startActivity(intent);
                SharePref.put(mContext, "fuc", 0);
                break;
            case R.id.after:
                Intent intent2 = new Intent(mContext, FloorMapActivity.class);
                intent2.putExtra("download" ,isDownload);
                startActivity(intent2);
                SharePref.put(mContext, "fuc", 1);
                break;
            case R.id.loc:
                getMapInfoData();
                Toast.makeText(mContext, "3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
            case R.id.ll_map:
                mMapSelectPopupWindow.show(); //查看下载列表
                break;
            case R.id.marque:
                mMapSelectPopupWindow.show(); //查看下载列表
                break;
        }
    }

    /**
     * 获取全局地址
     */
    private void saveTempAdress() {
        String isHttp = (String) SharePref.get(mContext, HTTP_TYPE, "");//保存http||https(请求方式)
        String adress = (String) SharePref.get(mContext, ADRESS, "");//保存请求地址
        if (!isHttp.equals("") && !adress.equals("")) {
            Constant.REQUEST_ADRESS = isHttp + adress;
        } else {
            judgeAdress();
        }
    }

    private void getMapInfoData() {

        ApiManager.getMapData(new CallBack() {

            @Override
            public void onSucCallBack(Object data) {
                List<Site> sites;
                if (data instanceof DataBean) {
                    List<Site> siteList = new ArrayList<>();
                    Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                    sites = (List<Site>) ((DataBean) data).getData();
                    for (int i = 0; i < sites.size(); i++) {
                        String jsonString = gson.toJson(sites.get(i));
                        Site bean = gson.fromJson(jsonString, Site.class);
                        siteList.add(bean);
                    }
                    mMapData = writeData(siteList);
                    mMapSelectPopupWindow.setData(mMapData);
                    if (mMapData != null) {
                        if (!isFirst) {
                            if (siteList.get(0).getMapInfos() != null) {
                                if (siteList.get(0).getMapInfos().size() == 0) {
                                    return;
                                }
                                final MapInfo mapInfo = siteList.get(0).getMapInfos().get(0);
                                initFirstMap(mapInfo);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mTvMq.setText(mapInfo.getMapName());
                                    }
                                });
                                Log.e("SVA_MAP_FIRST", "onSuc");
                            }
                            isFirst = true;
                        }


                    }
                    Log.e("SVA_MAP", "onSuc");
                }
            }

            @Override
            public void onFailCallBack() {
                Log.e("SVA_MAP", "onFail");
            }
        });

    }

    /**
     * 默认初始化第一个地图
     */
    private void initFirstMap(MapInfo mapInfo) {
        downLoadMap(mapInfo.getImagePath());
        Constant.MAP_ID = mapInfo.getId();
    }


    private List<DataListTree<Site, MapInfo>> writeData(List<Site> sites) {
        List<DataListTree<Site, MapInfo>> dataList = new ArrayList<>();
        for (int i = 0; i < sites.size(); i++) {
            Site site = sites.get(i);
            dataList.add(new DataListTree<Site, MapInfo>(site, site.getMapInfos()));
        }
        return dataList;
    }

    /**
     * 下载图片
     *
     * @param url 服务器需要的地址
     */
    private void downLoadMap(String url) {
        String path = url.replaceAll("\\\\", "-");
        String[] split = path.split("-");
        final String fileName = split[split.length - 1];
        ApiManager.getImageByApi(RetrofitUtil.URLEncoded(url), new CallBack() {
            @Override
            public void onSucCallBack(Object data) {
                Log.e("SVA_DOWNLOAD_MAP", "onSuc");

                if (data == null) {
                    Toast.makeText(mContext, "data is null", Toast.LENGTH_SHORT).show();
                }
                if (data instanceof DataBean) {
                    final String temp = (String) ((DataBean) data).getData();
                    if (temp == null) {
                        return;
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            BitmapUtil.getEncode64Map(temp.getBytes(), fileName);
                            isDownload = true;
                        }
                    }).start();
                    Constant.MAP_NAME = fileName;
                    Toast.makeText(mContext, "地图下载成功", Toast.LENGTH_SHORT).show();
                }

                SharePref.put(mContext, "mapName", fileName);   //存储本地map
            }

            @Override
            public void onFailCallBack() {
                Log.e("SVA_DOWNLOAD_MAP", "onFail");
                Toast.makeText(mContext, "地图下载失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 加载默认地图
     */
    private void initDefaultMap() {
        Constant.MAP_NAME = (String) SharePref.get(mContext, "mapName", "");//获取地图图片
        if (!Constant.MAP_NAME.equals("")) {
            String path = Constant.MAP + File.separator + Constant.MAP_NAME;
            Constant.mSelectBitmap = BitmapFactory.decodeFile(path);
        }
    }
}
