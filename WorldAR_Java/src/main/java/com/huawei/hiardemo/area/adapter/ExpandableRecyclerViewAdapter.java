package com.huawei.hiardemo.area.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hiardemo.area.R;
import com.huawei.hiardemo.area.bean.DataListTree;
import com.huawei.hiardemo.area.bean.ItemStatus;
import com.huawei.hiardemo.area.bean.MapInfo;
import com.huawei.hiardemo.area.bean.Site;

import java.util.ArrayList;
import java.util.List;

public class ExpandableRecyclerViewAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<DataListTree<Site, MapInfo>> mDataListTrees;
    private List<Boolean> mGroupItemStatus; // 保存一级标题的开关状态
    private int mGroupOpen = -1;
    private onExpandItemClick mListener;

    public ExpandableRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    public void setExpandItemClick(onExpandItemClick listener) {
        mListener = listener;
    }

    /**
     * 设置显示的数据
     *
     * @param dataListTrees
     */
    public void setData(List<DataListTree<Site, MapInfo>> dataListTrees) {
        this.mDataListTrees = dataListTrees;
        initGroupItemStatus();
        notifyDataSetChanged();
    }

    public void updateData(List<DataListTree<Site, MapInfo>> dataListTrees) {
        this.mDataListTrees.clear();
        this.mDataListTrees.addAll(dataListTrees);
        initGroupItemStatus();
        notifyDataSetChanged();
    }

    /**
     * 初始化一级列表开关状态
     */
    private void initGroupItemStatus() {
        mGroupItemStatus = new ArrayList<>();
        for (int i = 0; i < mDataListTrees.size(); i++) {
            mGroupItemStatus.add(false);
        }
    }

    /**
     * 根据item的位置，获取当前Item的状态
     *
     * @param position 当前item的位置（此position的计数包含groupItem和subItem合计）
     * @return 当前Item的状态（此Item可能是groupItem，也可能是SubItem）
     */
    private ItemStatus getItemStatusByPosition(int position) {
        ItemStatus itemStatus = new ItemStatus();
        int itemCount = 0;
        int i;
        //轮询 groupItem 的开关状态
        for (i = 0; i < mGroupItemStatus.size(); i++) {
            if (itemCount == position) { //position刚好等于计数时，item为groupItem
                itemStatus.setViewType(ItemStatus.VIEW_TYPE_GROUP_ITEM);
                itemStatus.setGroupItemIndex(i);
                break;
            } else if (itemCount > position) { //position大于计数时，item为groupItem(i - 1)中的某个subItem
                itemStatus.setViewType(ItemStatus.VIEW_TYPE_SUB_ITEM);
                itemStatus.setGroupItemIndex(i - 1); // 指定的position组索引
                // 计算指定的position前，统计的列表项和
                int temp = (itemCount - mDataListTrees.get(i - 1).getSubItem().size());
                // 指定的position的子项索引：即为position-之前统计的列表项和
                itemStatus.setSubItemIndex(position - temp);
                break;
            }

            itemCount++;
            if (mGroupItemStatus.get(i)) {
                itemCount += mDataListTrees.get(i).getSubItem().size();
            }
        }
        // 轮询到最后一组时，未找到对应位置
        if (i >= mGroupItemStatus.size()) {
            itemStatus.setViewType(ItemStatus.VIEW_TYPE_SUB_ITEM); // 设置为二级标签类型
            itemStatus.setGroupItemIndex(i - 1); // 设置一级标签为最后一组
            itemStatus.setSubItemIndex(position - (itemCount - mDataListTrees.get(i - 1).getSubItem().size()));
        }
        return itemStatus;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == ItemStatus.VIEW_TYPE_GROUP_ITEM) {
            // parent需要传入对应的位置，否则列表项触发不了点击事件
            view = LayoutInflater.from(mContext).inflate(R.layout.expandalbe_group_item, parent, false);
            viewHolder = new GroupItemViewHolder(view);
        } else if (viewType == ItemStatus.VIEW_TYPE_SUB_ITEM) {
            view = LayoutInflater.from(mContext).inflate(R.layout.expandalbe_sub_item, parent, false);
            viewHolder = new SubItemViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ItemStatus itemStatus = getItemStatusByPosition(position); // 获取列表项状态
        final DataListTree data = mDataListTrees.get(itemStatus.getGroupItemIndex());

        if (itemStatus.getViewType() == ItemStatus.VIEW_TYPE_GROUP_ITEM) { // 组类型
            final GroupItemViewHolder groupItemViewHolder = (GroupItemViewHolder) holder;
            groupItemViewHolder.mGroupItemTitle.setText(((Site) data.getGroupItem()).getSiteName());

            final int groupIndex = itemStatus.getGroupItemIndex(); // 组索引
            if (mGroupOpen != -1) {
                if (groupIndex == mGroupOpen) {
                    groupItemViewHolder.mIvIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.arrow_drop_up));
                } else {
                    groupItemViewHolder.mIvIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.arrow_drop_down));
                }
            }
            groupItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mGroupOpen = groupIndex;
                    if (mGroupItemStatus.get(groupIndex)) { // 一级标题关闭
                        groupItemViewHolder.mIvIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.arrow_drop_down));
                        mGroupItemStatus.set(groupIndex, false);
                        notifyItemRangeRemoved(groupItemViewHolder.getAdapterPosition() + 1, data.getSubItem().size());
                    } else { // 一级标题打开
                        groupItemViewHolder.mIvIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.arrow_drop_up));
                        initGroupItemStatus(); // 1. 实现只展开一个组的功能，缺点是没有动画效果
                        mGroupItemStatus.set(groupIndex, true);
                        notifyDataSetChanged(); // 1. 实现只展开一个组的功能，缺点是没有动画效果
//                    notifyItemRangeInserted(groupItemViewHolder.getAdapterPosition() + 1, data.getSubItem().size()); // 2. 实现展开可多个组的功能，带动画效果
                    }
                }
            });
        } else if (itemStatus.getViewType() == ItemStatus.VIEW_TYPE_SUB_ITEM) { // 子项类型
            SubItemViewHolder subItemViewHolder = (SubItemViewHolder) holder;
            MapInfo temp = (MapInfo) data.getSubItem().get(itemStatus.getSubItemIndex());
            subItemViewHolder.mSubItemTitle.setText(temp.getMapName());
//            if () {
//                subItemViewHolder.mLine.setVisibility(View.GONE);
//            }
            subItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("XHF", String.valueOf(position));
                    MapInfo mapInfo = mDataListTrees.get(itemStatus.getGroupItemIndex()).getSubItem().get(itemStatus.getSubItemIndex());
                    mListener.onItemClick(mapInfo);
                    Toast.makeText(mContext, mapInfo.getMapName(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;

        if (0 == mGroupItemStatus.size()) {
            return itemCount;
        }

        for (int i = 0; i < mDataListTrees.size(); i++) {
            itemCount++; // 每个一级标题项+1
            if (mGroupItemStatus.get(i)) { // 二级标题展开时，再加上二级标题的数量
                itemCount += mDataListTrees.get(i).getSubItem().size();
            }
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        return getItemStatusByPosition(position).getViewType();
    }

    /**
     * 组项ViewHolder
     */
    static class GroupItemViewHolder extends RecyclerView.ViewHolder {
        TextView mGroupItemTitle;
        ImageView mIvIcon;

        GroupItemViewHolder(View itemView) {
            super(itemView);
            mGroupItemTitle = (TextView) itemView.findViewById(R.id.group_item_title);
            mIvIcon = itemView.findViewById(R.id.group_item_icon);
        }
    }

    /**
     * 子项ViewHolder
     */
    static class SubItemViewHolder extends RecyclerView.ViewHolder {
        TextView mSubItemTitle;
        View mLine;

        SubItemViewHolder(View itemView) {
            super(itemView);
            mSubItemTitle = (TextView) itemView.findViewById(R.id.sub_item_title);
            mLine = itemView.findViewById(R.id.line);
        }
    }

    public interface onExpandItemClick {
        void onItemClick(MapInfo mapInfo);
    }


}