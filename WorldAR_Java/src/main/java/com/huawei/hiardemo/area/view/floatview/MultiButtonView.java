package com.huawei.hiardemo.area.view.floatview;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.huawei.hiardemo.area.R;


public class MultiButtonView {
    private Context mContext;
    private ImageView mIvAdd;
    private ImageView uploadItem;
    private ImageView saveItem;
    private ImageView drawIconItem;
    private ImageView resetIcon;
    private SubActionButton uploadBtn;
    private SubActionButton saveBtn;
    private SubActionButton drawIconBtn;
    private SubActionButton resetBtn;
    private FloatingActionMenu actionMenu;
    public static final int UPLOAD_ITEM = 0;
    public static final int SAVE_ITEM = 1;
    public static final int DRAW_ICON_ITEM = 2;
    public static final int RESET_ITEM = 3;
    private MultiItemClick multiItemClick;

    public MultiButtonView(Context context) {
        mContext = context;
        initView();
        addListener();
    }

    public void setItemClick(MultiItemClick listener) {
        multiItemClick = listener;
    }

    public void initView() {
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(mContext);
        uploadItem = new ImageView(mContext);
        uploadItem.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ar_data_save));

        saveItem = new ImageView(mContext);
        saveItem.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.save));

        drawIconItem = new ImageView(mContext);
        drawIconItem.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon));

        resetIcon = new ImageView(mContext);
        resetIcon.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.reset));

        uploadBtn = itemBuilder.setContentView(uploadItem).build();
        saveBtn = itemBuilder.setContentView(saveItem).build();
        drawIconBtn = itemBuilder.setContentView(drawIconItem).build();
        resetBtn = itemBuilder.setContentView(resetIcon).build();

        mIvAdd = new ImageView(mContext);
        mIvAdd.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_action_new_light));
        FloatingActionButton actionButton = new FloatingActionButton.Builder(mContext)
                .setContentView(mIvAdd)
                .setPosition(4)
                .build();
        actionMenu = new FloatingActionMenu.Builder(mContext)
                .addSubActionView(uploadBtn)
                .addSubActionView(saveBtn)
                .addSubActionView(drawIconBtn)
                .addSubActionView(resetBtn)
                .attachTo(actionButton)
                .build();
    }

    /**
     * 添加监听器
     */
    public void addListener() {
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "保存AR地图成功", Toast.LENGTH_SHORT).show();
                actionMenu.toggle(true);
                if (multiItemClick != null) {
                    multiItemClick.onItemClick(UPLOAD_ITEM);
                }
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "点击关闭,saveBtn", Toast.LENGTH_SHORT).show();
                actionMenu.toggle(true);
                if (multiItemClick != null) {
                    multiItemClick.onItemClick(SAVE_ITEM);
                }
            }
        });
        drawIconBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "切换显示", Toast.LENGTH_SHORT).show();
                actionMenu.toggle(true);
                if (multiItemClick != null) {
                    multiItemClick.onItemClick(DRAW_ICON_ITEM);
                }
            }
        });
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "点击关闭，resetBtn", Toast.LENGTH_SHORT).show();
                actionMenu.toggle(true);
                if (multiItemClick != null) {
                    multiItemClick.onItemClick(RESET_ITEM);
                }
            }
        });
        actionMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees clockwise
                mIvAdd.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 270);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(mIvAdd, pvhR);
                animation.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees counter-clockwise
                mIvAdd.setRotation(270);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(mIvAdd, pvhR);
                animation.start();
            }
        });
    }

    public interface MultiItemClick {
        void onItemClick(int num);
    }


}
