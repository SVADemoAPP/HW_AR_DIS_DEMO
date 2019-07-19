package com.huawei.hiardemo.area.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huawei.hiardemo.area.R;
import com.huawei.hiardemo.area.bean.MapFileBean;
import com.huawei.hiardemo.area.framework.activity.BaseActivity;

import java.util.List;

/**
 * Create by 91569
 * Time 2019/7/14
 * Describe :
 */
public class AreaMapAdapter extends RecyclerView.Adapter<AreaMapAdapter.ViewHolder> {
    private Context mContext;
    private List<MapFileBean> mData;
    private OnItemClick mListener;

    public AreaMapAdapter(Context context, List<MapFileBean> mapFileList,OnItemClick listener) {
        mContext = context;
        mData = mapFileList;
        mListener=listener;
    }
    public void setData(List<MapFileBean> data){
        mData.clear();
        mData.addAll(data);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.layout_area_map_item, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mMapName.setText(mData.get(position).getFileName());
        holder.mLlMapItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ((BaseActivity)mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mListener.onItemClick(mData.get(position).getFilePath(),mData.get(position).getFileName());
                            }
                        });
                    }
                }).start();
            }
        });
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mMapName;
        LinearLayout mLlMapItem;
        public ViewHolder(View itemView) {
            super(itemView);
            mMapName = itemView.findViewById(R.id.map_item_name);
            mLlMapItem=itemView.findViewById(R.id.map_item);
        }
    }

    public interface OnItemClick{
        void onItemClick(String path,String fileName);
    }
}
