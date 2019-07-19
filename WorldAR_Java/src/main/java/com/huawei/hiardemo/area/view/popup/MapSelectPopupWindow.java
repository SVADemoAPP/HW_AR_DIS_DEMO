package com.huawei.hiardemo.area.view.popup;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.huawei.hiardemo.area.R;
import com.huawei.hiardemo.area.adapter.ExpandableRecyclerViewAdapter;
import com.huawei.hiardemo.area.bean.DataListTree;
import com.huawei.hiardemo.area.bean.MapInfo;
import com.huawei.hiardemo.area.bean.Site;
import com.huawei.hiardemo.area.util.SharePref;


import java.util.ArrayList;
import java.util.List;

public class MapSelectPopupWindow {

    private SuperPopupWindow mSelectPopupWindow;
    private Context mContext;
    private List<DataListTree<Site, MapInfo>> mDataListTrees = new ArrayList<>();
    private OnItemClick onItemClickListener;
    private ExpandableRecyclerViewAdapter mExpandableRecyclerViewAdapter;
    private ImageView mIvNoData;
    private RecyclerView mapRyl;

    public void setOnItemClick(OnItemClick listener) {
        onItemClickListener = listener;
    }

    public MapSelectPopupWindow(Context context, List<DataListTree<Site, MapInfo>> data) {
        mDataListTrees.clear();
        mDataListTrees.addAll(data);
        mContext = context;
        mSelectPopupWindow = new SuperPopupWindow(context, R.layout.pop_map_list);
        mSelectPopupWindow.setWarpContent();
        mSelectPopupWindow.setChangFocusable(true);
        View popupView = mSelectPopupWindow.getPopupView();
        initPopupWindow(popupView);
    }

    public void setData(List<DataListTree<Site, MapInfo>> data) {
        if (data == null) {
            return;
        }
        mExpandableRecyclerViewAdapter.updateData(data);
        mIvNoData.setVisibility(View.GONE);
        mapRyl.setVisibility(View.VISIBLE);
    }

    public boolean judgeData() {
        if (mDataListTrees != null && mDataListTrees.size() > 0) {
            return true;
        }
        return false;
    }

    private void initPopupWindow(View popupView) {
        RelativeLayout relativeLayout = popupView.findViewById(R.id.rl);
        mapRyl = popupView.findViewById(R.id.pop_map_ryl);
        mIvNoData = popupView.findViewById(R.id.iv_no_data);
        mapRyl.setLayoutManager(new LinearLayoutManager(mContext));
        mExpandableRecyclerViewAdapter = new ExpandableRecyclerViewAdapter(mContext);
        mExpandableRecyclerViewAdapter.setData(mDataListTrees);
        mapRyl.setLayoutManager(new LinearLayoutManager(mContext));
        mapRyl.setAdapter(mExpandableRecyclerViewAdapter);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
            }
        });
        mExpandableRecyclerViewAdapter.setExpandItemClick(new ExpandableRecyclerViewAdapter.onExpandItemClick() {
            @Override
            public void onItemClick(MapInfo mapInfo) {
                SharePref.put(mContext, "mapId", mapInfo.getMapId());//保存mapId
                SharePref.put(mContext, "mapName", mapInfo.getMapName());//保存mapId
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(mapInfo);
                }
                hide();
            }
        });
        if (mDataListTrees.size() == 0) {
            mIvNoData.setVisibility(View.VISIBLE);
            mapRyl.setVisibility(View.GONE);
        }
    }

    public void show() {
        mSelectPopupWindow.showPopupWindow();
    }

    public void hide() {
        if (mSelectPopupWindow.isShowing()) {
            mSelectPopupWindow.hidePopupWindow();
        }
    }


    public interface OnItemClick {
        void onItemClick(MapInfo mapInfo);
    }
}
