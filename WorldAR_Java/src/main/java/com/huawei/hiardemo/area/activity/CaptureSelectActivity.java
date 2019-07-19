package com.huawei.hiardemo.area.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hiardemo.area.R;
import com.huawei.hiardemo.area.bean.MapFileBean;
import com.huawei.hiardemo.area.db.table.ARLoctionModel;
import com.huawei.hiardemo.area.db.table.AreaMapData;
import com.huawei.hiardemo.area.db.utils.DBUtil;
import com.huawei.hiardemo.area.framework.activity.BaseActivity;
import com.huawei.hiardemo.area.util.Constant;
import com.huawei.hiardemo.area.util.XMlUtils;
import com.huawei.hiardemo.area.view.ScaleImageView;
import com.huawei.hiardemo.area.view.popup.AreaMapSelectPopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by 91569
 * Time 2019/7/13
 * Describe :
 */
public class CaptureSelectActivity extends BaseActivity implements View.OnClickListener {
    private Context mContext;
    private LinearLayout mLlMapGroup;
    private TextView mTvMapItem;
    private AreaMapSelectPopupWindow mAreaMapSelectPopupWindow;
    private ScaleImageView mIvCapture;
    private ScaleImageView mIvLoc;
    private String mMapName;
    private String mPath = "";
    private List<MapFileBean> mapFileBeans;

    @Override
    public void setLayout() {
        mContext = this;
        setContentView(R.layout.activity_capture);
    }

    @Override
    public void initView() {
        mLlMapGroup = findViewById(R.id.map_group);
        mTvMapItem = findViewById(R.id.map_item);
        mIvCapture = findViewById(R.id.capture);
        mIvLoc = findViewById(R.id.loc);
        mLlMapGroup.setOnClickListener(this);
        mIvCapture.setOnClickListener(this);
        mIvLoc.setOnClickListener(this);
        //遍历获取数据源
        mapFileBeans = new ArrayList<>();
        mAreaMapSelectPopupWindow = new AreaMapSelectPopupWindow(mContext, mapFileBeans);//加载popupWindow
        mAreaMapSelectPopupWindow.setOnItemClick(new AreaMapSelectPopupWindow.OnItemClick() {
            @Override
            public void onItemClick(String mapName, String path) {
                mTvMapItem.setText(mapName);
                mMapName=mapName;
                mPath = path;
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onDestroy() {
        if (mAreaMapSelectPopupWindow.isShowing()) //如果pop还在显示关闭窗体 避免window泄漏
        {
            mAreaMapSelectPopupWindow.hide();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_group: //弹出选择地图框
                mAreaMapSelectPopupWindow.changeData(searchFileFormSavePath(Constant.AR_PATH)); //再遍历一次每次打开都刷新
                mAreaMapSelectPopupWindow.show();
                break;
            case R.id.capture:
                Intent captureIntent = new Intent(this, SelectActivity.class);
                startActivity(captureIntent);
                break;
            case R.id.loc:
                if (mPath.equals("")) {
                    Toast.makeText(mContext, "请选择所需加载的地图数据，然后进行采样", Toast.LENGTH_SHORT).show();
                    return;
                }
//                DBUtil.getSearchData(mPath, new DBUtil.DBListener() {  //   使用数据库查询，查询成功后跳转
//                    @Override
//                    public void asyncQueryData(List<ARLoctionModel> data) {
//
//                    }
//
//                    @Override
//                    public void asyncQueryMapData(AreaMapData data) {
//                        if (data != null) {
//                            gotoFloorMapActivity(data.getInitX(), data.getInitY(), data.getAngle());
//                        }
//                    }
//                });

//                float x = (float) SharePref.get(mContext, "select_pointF_x", 0f);   //获取之前选中的pointF
//                float y = (float) SharePref.get(mContext, "select_pointF_y", 0f);
//                float angle = (float) SharePref.get(mContext, "select_angleF", 0f);    //获取之前选中的angle
//                gotoFloorMapActivity(x, y, angle);


//                mPath="/storage/emulated/0/HUAWEI_AR/AR/map1563239701494.data";
//                gotoFloorMapActivity(243.77841186523438f, 1104.9271240234375f, 0.0f);

                AreaMapData areaMapData = XMlUtils.parseXml(mMapName);
//                areaMapData.getAngle();
                gotoFloorMapActivity(areaMapData.getInitX(), areaMapData.getInitY(), areaMapData.getAngle());
                break;
        }
    }

    private void gotoFloorMapActivity(float x, float y, float angle) {
        Intent locIntent = new Intent(this, FloorMapActivity.class);
        locIntent.putExtra("arpath", mPath);
        locIntent.putExtra(Constant.SELECT_POINT, new PointF(x, y));
        locIntent.putExtra(Constant.SELECT_ANGLE, angle);
        startActivity(locIntent);
    }

    /**
     * 在目标文件下遍历数据
     *
     * @param path
     * @return
     */
    public List<MapFileBean> searchFileFormSavePath(String path) {
        List<MapFileBean> mapFileBeans = new ArrayList<>();
        File file = new File(path);
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                String filePath = f.getAbsolutePath();
                if (filePath.endsWith(".data")) {
                    mapFileBeans.add(new MapFileBean(filePath, f.getName()));
                }
            }
        }
        return mapFileBeans;
    }
}
