package com.huawei.hiardemo.area.view.popup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.huawei.hiardemo.area.R;
import com.huawei.hiardemo.area.bean.DataBean;
import com.huawei.hiardemo.area.framework.activity.BaseActivity;
import com.huawei.hiardemo.area.util.Constant;
import com.huawei.hiardemo.area.util.network.ApiManager;
import com.huawei.hiardemo.area.util.network.api.CallBack;
import com.huawei.hiardemo.area.view.photoview.PhotoView;


public class PhotoViewPopupWindow {
    private SuperPopupWindow mPop;
    private Context mContext;
    private PointF mPointF;
    private PhotoView photoView;
    private Bitmap bitmap;
    private ImageView ivGif;

    public PhotoViewPopupWindow(Context context) {
        mContext = context;
        mPop = new SuperPopupWindow(context, R.layout.photo_view_layout);
        View popupView = mPop.getPopupView();
        initView(popupView);
    }

    public void setPointF(PointF pointF) {
        mPointF = pointF;
    }

    private void initView(View view) {
        photoView = view.findViewById(R.id.ph);
        ImageView ivClose = view.findViewById(R.id.ph_close);
        ivGif = view.findViewById(R.id.gif);
        Glide.with(mContext).load(R.mipmap.loading).into(ivGif);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
            }
        });
    }

    public void show() {
        ivGif.setVisibility(View.VISIBLE);
        photoView.setImageResource(R.mipmap.bg);
        mPop.showPopupWindow();
        downloadPic();
    }

    public void hide() {
        mPop.hidePopupWindow();
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    private void downloadPic() {
        ApiManager.downloadPrruPic(Constant.MAP_ID, mPointF.x, mPointF.y, new CallBack() {
            @Override
            public void onSucCallBack(final Object data) {
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
                            byte[] decode = Base64.decode(temp, Base64.DEFAULT);
//                            bitmap = BitmapUtil.byteToBitmap(decode);
                            bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                            ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ivGif.setVisibility(View.GONE);
                                    photoView.setImageBitmap(bitmap);
                                }
                            });
                        }
                    }).start();

                }
            }

            @Override
            public void onFailCallBack() {

            }
        });
    }
}
