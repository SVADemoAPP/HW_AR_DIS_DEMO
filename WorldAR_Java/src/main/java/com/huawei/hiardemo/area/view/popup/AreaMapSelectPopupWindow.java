package com.huawei.hiardemo.area.view.popup;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.huawei.hiardemo.area.R;
import com.huawei.hiardemo.area.adapter.AreaMapAdapter;
import com.huawei.hiardemo.area.bean.MapFileBean;

import java.util.List;

/**
 * Create by 91569
 * Time 2019/7/13
 * Describe :
 */
public class AreaMapSelectPopupWindow {
    private SuperPopupWindow mSelectPopupWindow;
    private Context mContext;
    private OnItemClick onItemClickListener;
    private RecyclerView mapRyl;
    private ImageView mIvNoData;
    private RelativeLayout relativeLayout;
    private AreaMapAdapter areaMapAdapter;
    private List<MapFileBean> mData;
    private String selectFilePath;


    public String getSelectPath() {
        return selectFilePath;
    }

    public void setOnItemClick(AreaMapSelectPopupWindow.OnItemClick listener) {
        onItemClickListener = listener;
    }

    public AreaMapSelectPopupWindow(Context context, List<MapFileBean> data) {
        mContext = context;
        mData = data;
        mSelectPopupWindow = new SuperPopupWindow(context, R.layout.layout_area_map_popup);
        mSelectPopupWindow.setWarpContent();
        mSelectPopupWindow.setChangFocusable(true);
        View popupView = mSelectPopupWindow.getPopupView();
        initPopupWindow(popupView);
    }

    public void changeData(List<MapFileBean> data) {
        if (data.size() == 0) {
            mIvNoData.setVisibility(View.VISIBLE);
        } else {
            mIvNoData.setVisibility(View.GONE);
        }
        areaMapAdapter.setData(data);
        areaMapAdapter.notifyDataSetChanged();
    }

    private void initPopupWindow(View popupView) {
        areaMapAdapter = new AreaMapAdapter(mContext, mData, new AreaMapAdapter.OnItemClick() {
            @Override
            public void onItemClick(String path, String fileName) {
                onItemClickListener.onItemClick(fileName, path);
                selectFilePath = path;
                hide();
            }

        });
        relativeLayout = popupView.findViewById(R.id.rl);
        mapRyl = popupView.findViewById(R.id.area_map_ryl);
        mIvNoData = popupView.findViewById(R.id.iv_no_data);
        mapRyl.setLayoutManager(new LinearLayoutManager(mContext));
        mapRyl.setAdapter(areaMapAdapter);
        if (mData.size() == 0) {
            mIvNoData.setVisibility(View.VISIBLE);
        } else {
            mIvNoData.setVisibility(View.GONE);
        }
        areaMapAdapter.notifyDataSetChanged();
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide(); //点击空白处隐藏
            }
        });
    }

    public void show() {
        mSelectPopupWindow.showPopupWindow();
    }

    public void hide() {
        if (mSelectPopupWindow.isShowing()) {
            mSelectPopupWindow.hidePopupWindow();
        }
    }

    public boolean isShowing() {
        return mSelectPopupWindow.isShowing();
    }

    public interface OnItemClick {
        void onItemClick(String mapName, String path);
    }
}
