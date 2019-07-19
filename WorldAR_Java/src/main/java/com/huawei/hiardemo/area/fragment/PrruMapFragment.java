package com.huawei.hiardemo.area.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.huawei.hiardemo.area.R;
import com.huawei.hiardemo.area.activity.FloorMapActivity;
import com.huawei.hiardemo.area.bean.DataBean;
import com.huawei.hiardemo.area.bean.NetWorkState;
import com.huawei.hiardemo.area.bean.Position;
import com.huawei.hiardemo.area.bean.PrruData;
import com.huawei.hiardemo.area.bean.PrruInfo;
import com.huawei.hiardemo.area.bean.UploadInfo;
import com.huawei.hiardemo.area.util.Constant;
import com.huawei.hiardemo.area.util.DistanceUtil;
import com.huawei.hiardemo.area.util.network.ApiManager;
import com.huawei.hiardemo.area.util.network.api.CallBack;
import com.huawei.hiardemo.area.view.floatview.MultiButtonView;
import com.huawei.hiardemo.area.view.popup.PhotoViewPopupWindow;

import net.yoojia.imagemap.HighlightImageView1;
import net.yoojia.imagemap.ImageMap1;
import net.yoojia.imagemap.core.Bubble;
import net.yoojia.imagemap.core.CircleRangeShape;
import net.yoojia.imagemap.core.CircleShape;
import net.yoojia.imagemap.core.CollectPointShape;
import net.yoojia.imagemap.core.MaChineShape;
import net.yoojia.imagemap.core.MoniPointShape;
import net.yoojia.imagemap.core.PrruInfoShape;
import net.yoojia.imagemap.core.PushMessageShape;
import net.yoojia.imagemap.core.Shape;
import net.yoojia.imagemap.core.ShapeExtension;
import net.yoojia.imagemap.core.SpecialShape;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.huawei.hiardemo.area.view.floatview.MultiButtonView.DRAW_ICON_ITEM;
import static com.huawei.hiardemo.area.view.floatview.MultiButtonView.RESET_ITEM;
import static com.huawei.hiardemo.area.view.floatview.MultiButtonView.SAVE_ITEM;
import static com.huawei.hiardemo.area.view.floatview.MultiButtonView.UPLOAD_ITEM;

public class PrruMapFragment extends Fragment {
    private Context mContext;
    private ImageMap1 mAMap;
    private int mMapHight;    //图片高度
    private View mMenuView;
    private View mMenuCamera;
    private PrruInfoShape mPrruInfoWithBubble;
    private PhotoViewPopupWindow mPopPhoto;
    private MultiButtonView mMultiBtn;
    private boolean drawHistoryFlag = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_prru_map_layout, container, false);
        initView(inflate);
        initData();
        return inflate;
    }

    private void initView(View view) {
        mAMap = view.findViewById(R.id.imagemap); //地图对象
        if (Constant.mSelectBitmap == null) {
            initOtherMap();
        }
        mAMap.setMapBitmap(Constant.mSelectBitmap);
        mAMap.setAllowRotate(false); //禁止旋转
        mMapHight = 100;
//        mMapHight = Constant.mSelectBitmap.getHeight();
    }

    private void initData() {
        mPopPhoto = new PhotoViewPopupWindow(mContext);
        initBubble();
        initMultiButton();
    }

    /**
     * 初始化多功能选择器
     */
    private void initMultiButton() {
        mMultiBtn = new MultiButtonView(mContext);
        mMultiBtn.setItemClick(new MultiButtonView.MultiItemClick() {
            @Override
            public void onItemClick(int num) {
                switch (num) {
                    case UPLOAD_ITEM:
                        ((FloorMapActivity) getActivity()).saveARPlane();
                        Toast.makeText(mContext, "保存AR平面成功", Toast.LENGTH_SHORT).show();
                        break;
                    case SAVE_ITEM:
                        if (getActivity() instanceof FloorMapActivity) {
                            ((FloorMapActivity) getActivity()).changeIgnore();
                        }
                        break;
                    case DRAW_ICON_ITEM:
                        if (drawHistoryFlag) {
                            drawHistoryFlag = false;
                        } else {
                            drawHistoryFlag = true;
                        }
                        break;
                    case RESET_ITEM:  //重置
//                        resetPointF();
                        break;
                    default:
                        break;
                }
            }
        });
    }


    /**
     * 定时更新新图
     */
    public void timerDraw(float x, float y) {
        List<PointF> mapLocation = ((FloorMapActivity) mContext).getMapLocation();
        List<NetWorkState> netWorkInfo = ((FloorMapActivity) mContext).getNetWorkInfo();
        drawCircle(mapLocation, netWorkInfo,x,y);//画定位点
    }

    /**
     * 初始化点击shape,出现bubble(shape右侧悬浮View)
     */
    private void initBubble() {
        mMenuView = View.inflate(mContext, R.layout.show_pic_layout, null);
        mMenuCamera = mMenuView.findViewById(R.id.menu_camera);
        View.OnClickListener onMenuClickListener = new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.menu_camera:
                        PointF center = mPrruInfoWithBubble.getCenter();
                        mPopPhoto.setPointF(center);
                        mPopPhoto.show();
                        break;
                    default:
                        break;
                }
            }
        };
        mMenuCamera.setOnClickListener(onMenuClickListener);

        mAMap.setBubbleView(mMenuView, new Bubble.RenderDelegate() {
            @Override
            public void onDisplay(Shape shape, View bubbleView) {

            }
        });
        mAMap.setOnShapeClickListener(new ShapeExtension.OnShapeActionListener() {


            @Override
            public void onCollectShapeClick(CollectPointShape collectPointShape, float f, float f2) {

            }

            @Override
            public void onMoniShapeClick(MoniPointShape moniPointShape, float f, float f2) {

            }

            @Override
            public void onPrruInfoShapeClick(PrruInfoShape prruinfoshape, float f, float f2) {
                mPrruInfoWithBubble = prruinfoshape;
            }

            @Override
            public void onPushMessageShapeClick(PushMessageShape pushMessageShape, float f, float f2) {

            }

            @Override
            public void onSpecialShapeClick(SpecialShape specialShape, float f, float f2) {

            }

            @Override
            public void outShapeClick(float f, float f2) {
            }
        });

        mAMap.setPrruListener(new HighlightImageView1.PrruModifyHListener() {
            @Override
            public void startTranslate(PrruInfoShape shape, float x, float y) {
                mAMap.setTranlateFlag(false);
                Log.e("startTranslate", "x" + x + " , y =" + y);
            }

            @Override
            public void moveTranslate(PrruInfoShape shape, float x, float y) {
                Log.e("moveTranslate", "x" + x + " , y =" + y);
                shape.setValues(x, y);
            }

            @Override
            public void endTranslate(PrruInfoShape shape, float x, float y) {
                mAMap.setTranlateFlag(true);
                Log.e("endTranslate", "x" + x + " , y =" + y);
                if (shape instanceof MaChineShape) {
                    ApiManager.updateMachineRoom(Constant.MAP_ID, x, y, new CallBack() {
                        @Override
                        public void onSucCallBack(Object data) {
                            String code = ((DataBean) data).getCode();
                            Toast.makeText(mContext, "code" + code, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailCallBack() {

                        }
                    });
                    Toast.makeText(mContext, "主机移动完成", Toast.LENGTH_SHORT).show();
                } else {  //prru移动
                    ApiManager.upLoadPrruInfo((String) shape.getTag(), x, y, new CallBack() {
                        @Override
                        public void onSucCallBack(Object data) {
                            String code = ((DataBean) data).getCode();
                            Toast.makeText(mContext, "code" + code, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailCallBack() {

                        }
                    });
                }
            }

            @Override
            public void clickBlank() {

            }

            @Override
            public void clickOutSide() {

            }
        });
    }


    /**
     * 在地图上画prru
     *
     * @param data prru数据
     */
    private void drawPrruInfoFormService(List<PrruData> data) {
        for (PrruData prruData : data) {
            String id = prruData.getId();
            PrruInfoShape prruInfoShape = new PrruInfoShape(id, Color.RED, mContext);
            prruInfoShape.setValues(prruData.getX(), prruData.getY());
            prruInfoShape.setPrruShowType(PrruInfoShape.pRRUType.outArea);
            mAMap.addShape(prruInfoShape, false);
        }
    }


//    /**
//     * 判断当前是否需要上传图片
//     *
//     * @return
//     */
//    private boolean judgeUpLoadImage() {
//        if (ivInit.getVisibility() == View.VISIBLE) {
//
//        } else { //初始化完成
//            String text = (String) tvTotal.getText();
//            String[] split = text.split("TOTAL: ");
//            Double distance = Double.valueOf(split[1]); //获取总距离
//            double num = distance / 5;
//            if (num > mTotalNum) { //保存并上传图片
//                mTotalNum++;
//                return true;
//            }
//        }
//        return false;
//    }

    /**
     * 上传rsrp和坐标信息
     */
    private void uploadInfo() {
        ((FloorMapActivity) mContext).setPackFlag(false); //关闭采集
        String mapId = Constant.MAP_ID;
        List<UploadInfo> mUploadInfos = new ArrayList<>();
        List<PointF> mapLocation = ((FloorMapActivity) mContext).getMapLocation();
        List<NetWorkState> netWorkInfo = ((FloorMapActivity) mContext).getNetWorkInfo();
        for (int i = 0; i < netWorkInfo.size(); i++) {
            PointF pointF = mapLocation.get(i);
            mUploadInfos.add(new UploadInfo(mapId, Integer.valueOf(netWorkInfo.get(i).getRsRp()), pointF.x, pointF.y));
        }
        ((FloorMapActivity) mContext).setPackFlag(true); //开启采集
        ApiManager.updateRsRpInfo(mUploadInfos, new CallBack() {
            @Override
            public void onSucCallBack(Object data) {
                if (data instanceof DataBean) {
                    String code = ((DataBean) data).getCode();
                    String message = ((DataBean) data).getMessage();
                    final List<PrruData> prruDataList = new ArrayList<>();
                    if (code.equals("0")) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        List<PrruData> prruData = (List<PrruData>) ((DataBean) data).getData();
                        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                        for (int i = 0; i < prruData.size(); i++) {
                            String jsonString = gson.toJson(prruData.get(i));
                            PrruData bean = gson.fromJson(jsonString, PrruData.class);
                            prruDataList.add(bean);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                drawPrruInfoFormService(prruDataList);
                            }
                        });

                    }

                }
            }

            @Override
            public void onFailCallBack() {
                Log.e("SVA", "onFailed");
            }
        });
    }

    /**
     * 画定位点
     *
     * @param pointFList   定位点
     * @param networkInfos 定位点信号值
     */
    private void drawCircle(List<PointF> pointFList, List<NetWorkState> networkInfos,float x,float y) {
        clearRsrpShapes(networkInfos); //清除所有已有shape
        int rsrpColor;
        if (drawHistoryFlag) {  //控制是否显示历史点
            for (int i = 0; i < networkInfos.size(); i++) {
                int rsrp = Integer.valueOf(networkInfos.get(i).getRsRp());
                if (-75 < rsrp && rsrp <= 0) {  //1e8449
                    rsrpColor = Color.parseColor("#1e8449");
                } else if (-95 < rsrp && rsrp <= -75) { //浅绿色
                    rsrpColor = Color.GREEN;
                } else if (-105 < rsrp && rsrp <= -95) {  //黄色
                    rsrpColor = Color.YELLOW;
                } else if (-120 < rsrp && rsrp <= -105) { //红色
                    rsrpColor = Color.RED;
                } else {
                    rsrpColor = Color.BLACK;
                }
                CircleShape circleShape = new CircleShape("tag" + i, rsrpColor, 6);
                circleShape.setValues(pointFList.get(i).x, pointFList.get(i).y);
                mAMap.addShape(circleShape, false);
            }
        }
        PointF nowStation = ((FloorMapActivity) getActivity()).getNowStation();
        CircleRangeShape circleRangeShape = new CircleRangeShape("loc", Color.RED);
        circleRangeShape.setValues(nowStation.x, nowStation.y);
        mAMap.addShape(circleRangeShape, false);
    }

    /**
     * 在地图上绘制计算出的prru位置
     *
     * @param list
     */
    private void drawPrruinfo(List<PrruInfo> list) {
        for (int i = 0; i < list.size(); i++) {
            PrruInfo prruInfo = list.get(i);
            float x = prruInfo.getPosition().getX();
            float y = prruInfo.getPosition().getY();
            float[] floats = DistanceUtil.realToMap(Constant.Scale, x, y, mMapHight);
            PrruInfoShape prruInfoShape = new PrruInfoShape("prru" + i, Color.RED, mContext);
            prruInfoShape.setValues(floats[0], floats[1]);
            mAMap.addShape(prruInfoShape, false);
        }
    }

    /**
     * 清除地图上的所有rsrp坐标点坐标
     */
    private void clearRsrpShapes(List<NetWorkState> networkInfos) {
        mAMap.removeShape("loc");  //移除红色点（当前定位点）
        for (int i = 0; i < networkInfos.size(); i++) {  //移除所有rsrp
            mAMap.removeShape("tag" + i);
        }
    }

    public double calculateDistance(Position p1, Position p2) {
        double a = p1.getX() - p2.getX();
        double b = p1.getY() - p2.getY();
        return Math.sqrt(a * a + b * b);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("垃圾");
        builder.setIcon(R.mipmap.ic_launcher_round);
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(true);
        //设置正面按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //设置反面按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

    }

    private void initOtherMap() {
        InputStream is = null;
        try {
            is = getActivity().getAssets().open("U5.png");
            BitmapDrawable bd = (BitmapDrawable) Drawable.createFromStream(is, null);
            Constant.mSelectBitmap = bd.getBitmap();
            is.close();
        } catch (IOException e) {
            Toast.makeText(getActivity(), "加载地图失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        Toast.makeText(getActivity(), "加载默认图片", Toast.LENGTH_SHORT).show();
    }
}
