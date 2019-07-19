package com.huawei.hiardemo.area.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huawei.hiardemo.area.R;
import com.huawei.hiardemo.area.bean.MapData;


import java.util.List;

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.ViewHolder> {
    private List<MapData> mMapData;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public MapAdapter(List<MapData> data, Context context) {
        mMapData = data;
        mContext = context;
    }

    public void setOnClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.pop_map_list_item, parent, false);
        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTvName.setText(mMapData.get(position).getName());
//        if (position % 2 == 0) {   //white
            holder.mTvName.setTextColor(mContext.getColor(R.color.line_gray2));
            holder.mLlp.setBackground(mContext.getDrawable(R.color.white));
//        } else {
//            holder.mTvName.setTextColor(mContext.getColor(R.color.white));
//            holder.mLlp.setBackground(mContext.getDrawable(R.color.line_gray2));
//        }
        if (mOnItemClickListener != null) {
            holder.mLlp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(mMapData.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mMapData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvName;
        private LinearLayout mLlp;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.map_item_name);
            mLlp = itemView.findViewById(R.id.map_item);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(MapData mapData);
    }
}
