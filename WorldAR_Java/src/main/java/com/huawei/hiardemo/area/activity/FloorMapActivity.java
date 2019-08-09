package com.huawei.hiardemo.area.activity;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hiar.ARPose;
import com.huawei.hiardemo.area.R;
import com.huawei.hiardemo.area.bean.NetWorkState;
import com.huawei.hiardemo.area.bean.Position;
import com.huawei.hiardemo.area.fragment.ARFragment;
import com.huawei.hiardemo.area.fragment.PrruMapFragment;
import com.huawei.hiardemo.area.framework.activity.BaseActivity;
import com.huawei.hiardemo.area.util.Constant;
import com.huawei.hiardemo.area.util.DistanceUtil;
import com.huawei.hiardemo.area.util.UpdateCommunityInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class FloorMapActivity extends BaseActivity implements View.OnClickListener {
    private static final Long TIMER_TIME = 500L;
    private Context mContext;
    private boolean mFirst = false;
    private TextView mToolName;
    private LinearLayout mBack;
    private LinearLayout mAdd;
    private PrruMapFragment mPrruMapFragment;
    private ARFragment mArFragment;
    private PointF mSelectPointF;
    private boolean mPackFlag = true;
    private UpdateCommunityInfo mUpdateCommunityInfo;
    private float mAngle;
    private float mCoordinateX; //全局实时X
    private float mCoordinateY; //全局实时Y
    private float mMapX;
    private float mMapY;
    private List<NetWorkState> mNetWorkInfo = new ArrayList<>();
    private List<PointF> mMapLocation = new ArrayList<>(); //采集的地图定位点定位坐标
    private Handler mTimerHandler = new Handler();
    private Runnable mTimerRunnable = new Runnable() {
        @Override
        public void run() {
            if (mPackFlag) {
                if (mIgnoreFlag) {
                    packData(); //采集数据
                }
                if (mPrruMapFragment != null) {
                    mPrruMapFragment.timerDraw(mMapX, mMapY);
                }
                /*if(mArFragment != null){
                    mArFragment.judgeARAnchor();
                }*/
            }
            mTimerHandler.postDelayed(this, TIMER_TIME);
        }
    };
    //    private SelectPopupWindow mSelectPopupWindow;
    private Position currentPosition;
    private Position initPosition;
    private boolean mIgnoreFlag = true;

    public void changeIgnore() {
        if (mIgnoreFlag) {
            mIgnoreFlag = false;
        } else {
            mIgnoreFlag = true;
        }
    }

    public void setPackFlag(boolean flag) {
        mPackFlag = flag;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (!mFirst) {
                mFirst = true;
//                mSelectPopupWindow.showPopupWindow();
            }
        }
    }

    public float getAngle() {
        return mAngle;
    }

    public PointF getSelectPoint() {
        return new PointF(mSelectPointF.x, mSelectPointF.y);
    }

    /**
     * 采集测试数据---数据采集
     */
    public void startCollectionData() {
        mTimerHandler.post(mTimerRunnable);
    }

    /**
     * 组装数据
     */
    private void packData() {
        mMapLocation.add(new PointF(mMapX, mMapY));
        mNetWorkInfo.add(new NetWorkState(mUpdateCommunityInfo.RSRP, mUpdateCommunityInfo.SINR));//获取网络状态信息
    }

    public PointF getNowStation() {
        return new PointF(mMapX, mMapY);
    }

    /**
     * @return 地图坐标定位点
     */
    public List<PointF> getMapLocation() {
        return mMapLocation;
    }

    /**
     * 返回网络信号信息
     *
     * @return
     */
    public List<NetWorkState> getNetWorkInfo() {
        return mNetWorkInfo;
    }

//    private void closeArFragment(float x, float y) {
//        if (mArFragment == null) {
//            return;
//        }
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.remove(mArFragment);
//        fragmentTransaction.commit();
//        mArFragment = null;
//        try {
//            Thread.sleep(400);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
////        mSelectPopupWindow.setmSelectShape(x, y);
////        mSelectPopupWindow.showPopupWindow();
//    }

    @Override
    public void setLayout() {
        mContext = this;
        setContentView(R.layout.activity_floor_map);
    }

    @Override
    public void initView() {
        mSelectPointF = (PointF) getIntent().getParcelableExtra(Constant.SELECT_POINT);
        mAngle = getIntent().getFloatExtra(Constant.SELECT_ANGLE, 0f);
        mPrruMapFragment = new PrruMapFragment();
        mArFragment = new ARFragment();
        mUpdateCommunityInfo = new UpdateCommunityInfo(this, (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE), new Handler());
        mUpdateCommunityInfo.startUpdateData();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.prru_replace, mPrruMapFragment);
        fragmentTransaction.add(R.id.ar_replace, mArFragment);
        fragmentTransaction.commit();

        mBack = findViewById(R.id.back);
        mToolName = findViewById(R.id.tool_top_name);
        mAdd = findViewById(R.id.tool_right_add);
        mAdd.setVisibility(View.GONE);
        mBack.setVisibility(View.VISIBLE);
        mBack.setOnClickListener(this);
        mToolName.setText("区域采样");
        String arpath = getIntent().getStringExtra("arpath");
        if (arpath != null && !arpath.equals("")) {
            mToolName.setText("区域定位");
        }
        currentPosition = new Position();
        currentPosition.setX(0f);
        currentPosition.setY(0f);

        initPosition = new Position();
        initPosition.setX(0f);
        initPosition.setY(0f);
    }

    @Override
    public void initData() {
//        initSelectPopWindow();
        mArFragment.setArCameraListener(new ARFragment.ArCameraListener() {  //获取ar返回的实时坐标
            @Override
            public void getCameraPose(ARPose arPose) {   //获取到相机返回的实时坐标
                if (mSelectPointF != null) {
                    float[] point = DistanceUtil.getPoint(arPose.tz(), arPose.tx(), mAngle);
                    float[] real = DistanceUtil.mapToReal(Constant.Scale, mSelectPointF.x, mSelectPointF.y, Constant.MAP_HEIGHT);
                    float[] pix = DistanceUtil.realToMap(Constant.Scale, (real[0] + point[0]), (real[1] + point[1]), Constant.MAP_HEIGHT);
                    mCoordinateX = arPose.tz();
                    mCoordinateY = arPose.tx();
                    mMapX = pix[0];
                    mMapY = pix[1];
                    currentPosition.setX(point[0]);
                    currentPosition.setY(point[1]);
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        mPackFlag = false;
        mUpdateCommunityInfo.endUpdateData();
        mArFragment.destroy();
        mTimerHandler.removeCallbacks(mTimerRunnable);
        super.onDestroy();
    }

    private void initOtherMap() {
        InputStream is = null;
        try {
            is = getAssets().open("U5.png");
            BitmapDrawable bd = (BitmapDrawable) Drawable.createFromStream(is, null);
            Constant.mSelectBitmap = bd.getBitmap();
            is.close();
        } catch (IOException e) {
            Toast.makeText(this, "加载地图失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        Toast.makeText(this, "加载默认图片", Toast.LENGTH_SHORT).show();
    }

//    private void initSelectPopWindow() {
//        if (Constant.mSelectBitmap == null) {
//            initOtherMap();
//        }
//        mSelectPopupWindow = new SelectPopupWindow(mContext, Constant.mSelectBitmap);
//        mSelectPopupWindow.setSelectListener(new SelectPopupWindow.SelectPointListener() {
//            @Override
//            public void getPoint(PointF pointF) { //有选择点返回
//                mSelectPointF = pointF;
//                try {
////                    DBUtil.addARLocation("U5-2F", "2F", pointF);//存储
//                    mArFragment = new ARFragment();
//                    FragmentManager fragmentManager = getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.add(R.id.ar_replace, mArFragment);
//                    fragmentTransaction.commit();
//                    mArFragment.setArCameraListener(new ARFragment.ArCameraListener() {  //获取ar返回的实时坐标
//                        @Override
//                        public void getCameraPose(ARPose arPose) {   //获取到相机返回的实时坐标
//                            //getAngle(mArFragment.getAzimuthAngle());
//                            if (mSelectPointF != null) {
//                                float[] point = DistanceUtil.getPoint(arPose.tz(), arPose.tx(), mAngle);
//                                float[] real = DistanceUtil.mapToReal(Constant.Scale, mSelectPointF.x, mSelectPointF.y, Constant.MAP_HEIGHT);
//                                float[] pix = DistanceUtil.realToMap(Constant.Scale, (real[0] + point[0]), (real[1] + point[1]), Constant.MAP_HEIGHT);
//                                mCoordinateX = arPose.tz();
//                                mCoordinateY = arPose.tx();
//                                LogUtils.e("AR", "mCoordinateX = " + mCoordinateX + " , mCoordinateY = " + mCoordinateY);
////                                //转换坐标为地图坐标
////                                float[] point = DistanceUtil.getPoint(mCoordinateX, mCoordinateY, mAngle);
////                                float[] real = DistanceUtil.mapToReal(Constant.Scale, mSelectPointF.x, mSelectPointF.y, Constant.MAP_HEIGHT);
////                                float[] pix = DistanceUtil.realToMap(Constant.Scale, (real[0] + point[0]), (real[1] + point[1]), Constant.MAP_HEIGHT);
//                                mMapX = pix[0];
//                                mMapY = pix[1];
//                                LogUtils.e("AR", "mMapX = " + mMapX + " , mMapY = " + mMapY);
//                                currentPosition.setX(point[0]);
//                                currentPosition.setY(point[1]);
////                                if (mPrruMapFragment.calculateDistance(currentPosition, initPosition) > 25) {
////                                    mArFragment.destroy();
////                                    mSelectPointF = null;
////                                    closeArFragment(pix[0], pix[1]);
////                                }
//                            }
//                        }
//                    });
//                } catch (Exception e) {
//                    Log.e("XHF", "存储失败");
//                }
//            }
//
//            @Override
//            public void cancel() {  //没有选择
//                mSelectPopupWindow.hidePopupWindow();
//                finish();
//            }
//        });
//
//        mSelectPopupWindow.setSelectAngleListener(new SelectPopupWindow.AngeleListener() {
//            @Override
//            public void getAngle(float angle) {
//                mAngle = angle;
//            }
//        });
//    }


    public void saveARPlane() {
        mArFragment.saveARPlanes();
    }

    public void addARPose() {
        mArFragment.setAnchor();
    }
}
