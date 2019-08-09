package com.huawei.hiardemo.area.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hiardemo.area.R;
import com.huawei.hiardemo.area.framework.activity.BaseActivity;
import com.huawei.hiardemo.area.util.Constant;
import com.huawei.hiardemo.area.util.FileUtil;
import com.huawei.hiardemo.area.util.LogUtils;
import com.huawei.hiardemo.area.util.SharePref;
import com.huawei.hiardemo.area.view.PieView;
import com.huawei.hiardemo.area.view.hightlight.HighLight;
import com.huawei.hiardemo.area.view.hightlight.position.OnBaseCallback;
import com.huawei.hiardemo.area.view.hightlight.position.OnRightPosCallback;
import com.huawei.hiardemo.area.view.hightlight.shape.RectLightShape;

import net.yoojia.imagemap.ImageMap1;
import net.yoojia.imagemap.TouchImageView1;
import net.yoojia.imagemap.core.CircleShape;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SelectActivity extends BaseActivity implements View.OnClickListener {
    private static final String SELECT_ADDRESS = "select";
    private PointF mSelectPointF;
    private Context mContext;
    private TextView mTvCancel;
    private TextView mTvConfirm;
    private ImageMap1 mSelectMap;
    private PieView mSelectPieView;

    private float mAngle = -1;
    private boolean firstSelect = true;
    private int mCircleRadius = 10;
    private CircleShape mSelectShape;
    private HighLight mHightLight;

    @Override
    public void setLayout() {
        mContext = this;
        setContentView(R.layout.activity_select);
    }

    @Override
    public void initView() {
        mTvCancel = findViewById(R.id.select_cancel);
        mTvConfirm = findViewById(R.id.select_confirm);
        mSelectMap = findViewById(R.id.select_map);
        mSelectPieView = findViewById(R.id.select_pieView);

        mTvCancel.setOnClickListener(this);
        mTvConfirm.setOnClickListener(this);
        mSelectPieView.setOnPieViewTouchListener(new PieView.OnPieViewTouchListener() {
            @Override
            public void onTouch(View v, MotionEvent e, PieView.ClickedDirection d) {
                mAngle = -1f;
                switch (d) {
                    case UP:
                        mAngle = 0;
                        break;
                    case DOWN:
                        mAngle = 180;
                        break;
                    case LEFT:
                        mAngle = 270;
                        break;
                    case RIGHT:
                        mAngle = 90;
                        break;
                    case CENTER:
                        mAngle = -1;
                        break;
                    case UP_LEFT:
                        mAngle = 315;
                        break;
                    case UP_RIGHT:
                        mAngle = 45;
                        break;
                    case DOWN_LEFT:
                        mAngle = 225;
                        break;
                    case DOWN_RIGHT:
                        mAngle = 135;
                        break;

                }

            }
        });

        mSelectMap.setOnSingleClickListener(new TouchImageView1.OnSingleClickListener() {


            @Override
            public void onSingle(PointF pointF) {
                if (mSelectShape == null) {
                    mSelectShape = new CircleShape(SELECT_ADDRESS, Color.RED, mCircleRadius);
                }
                mSelectShape.setValues(pointF.x, pointF.y);
                mSelectPointF = pointF;
                if (firstSelect) {
                    firstSelect = false;
                    mSelectMap.addShape(mSelectShape, false);
                }
            }
        });
    }

    @Override
    public void initData() {
        initMap();
        writeFile();
    }


    private void initMap() {
        String mapPath = Constant.MAP;
        File file = new File(mapPath);
        boolean download = getIntent().getBooleanExtra("download", false);
        if (!download) {
            file.mkdirs();
            Toast.makeText(mContext, "加载默认图片", Toast.LENGTH_SHORT).show();
            initOtherMap();
        } else {
            mapPath = mapPath + File.separator + Constant.MAP_NAME;
            File arMap = new File(mapPath);
            if (arMap.exists()) {
                if (Constant.mSelectBitmap != null) {  //释放资源
                    Constant.mSelectBitmap.recycle();
                    Constant.mSelectBitmap = null;
                }
                Constant.mSelectBitmap = BitmapFactory.decodeFile(mapPath);
            } else {
                Toast.makeText(mContext, "加载图片不存在，请手动在/sdcard/VINS_AR/map/目录下加入ar.png", Toast.LENGTH_SHORT).show();
                initOtherMap();
            }

        }
        mSelectMap.setMapBitmap(Constant.mSelectBitmap); //加载地图
        mSelectMap.setAllowRotate(false);
    }

    private void initOtherMap() {
        InputStream is = null;
        try {
            is = getAssets().open("U1.png");
            BitmapDrawable bd = (BitmapDrawable) Drawable.createFromStream(is, null);
            Constant.mSelectBitmap = bd.getBitmap();
            is.close();
        } catch (IOException e) {
            Toast.makeText(this, "加载地图失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        Toast.makeText(this, "加载默认图片", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            showKnownTipView();
            deleteFile();
        }
    }

    private void goToFloorMapActivity() {
        SharePref.put(mContext, "select_pointF_x", mSelectPointF.x);   //获取之前选中的pointF
        SharePref.put(mContext, "select_pointF_y", mSelectPointF.y);
        SharePref.put(mContext, "select_angleF", mAngle);
        Intent intent = new Intent();
        intent.setClass(mContext, FloorMapActivity.class);
        intent.putExtra(Constant.SELECT_POINT, mSelectPointF);
        intent.putExtra(Constant.SELECT_ANGLE, mAngle);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_cancel:
                finish();
                break;
            case R.id.select_confirm:
                if (mAngle == -1) {
                    Toast.makeText(mContext, "请在左上角选择行走方向", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mSelectPointF == null) {
                    Toast.makeText(mContext, "请在地图上选择当前位置", Toast.LENGTH_SHORT).show();
                    return;
                }
                goToFloorMapActivity();
                break;
        }
    }

    /**
     * 傻瓜化提示信息
     */
    public void showKnownTipView() {
        mHightLight = new HighLight(mContext)//
                .autoRemove(false)//设置背景点击高亮布局自动移除为false 默认为true
                .enableNext()
                .intercept(true)//设置拦截属性为false 高亮布局不影响后面布局的滑动效果 而且使下方点击回调失效
                .setClickCallback(new HighLight.OnClickCallback() {
                    @Override
                    public void onClick() {

                    }
                })
                .anchor(findViewById(R.id.select_main))
                .addHighLight(R.id.select_map, R.layout.tips_select_map_layout, new OnRightPosCallback(0), new RectLightShape())
                .addHighLight(R.id.select_pieView, R.layout.tips_select_pie_layout, new OnRightPosCallback(0), new RectLightShape())
                .addHighLight(R.id.select_confirm, R.layout.tips_select_confirm, new OnBaseCallback() {
                    @Override
                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
                        marginInfo.leftMargin = rectF.right - rectF.width() / 2 - 300;
                        marginInfo.bottomMargin = bottomMargin + rectF.height() + offset;
                    }
                }, new RectLightShape());

        mHightLight.show();
    }

    public void clickKnown(View view) {
        if (mHightLight.isShowing() && mHightLight.isNext())//如果开启next模式
        {
            mHightLight.next();
        } else {
            remove(null);
        }
    }

    public void remove(View view) {
        mHightLight.remove();
    }

    private void deleteFile() {
        String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "VINS";
        FileUtil.deleteDirs(path);
    }

    /***
     * 判断是否是第一次进入应用
     * true:写入一个默认文件
     * false:不做任何操作
     */
    private boolean judgeFirst() {

        boolean flag = false;
        SharedPreferences sharedPreferences = getSharedPreferences("vins", MODE_PRIVATE);
        flag = sharedPreferences.getBoolean("first", false);
        if (!flag) {
            LogUtils.e("XHF", "第一次安装");
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putBoolean("first", true);
            edit.commit();
        }
        return flag;
    }

    private void writeFile() {
        if (!judgeFirst()) { //第一次进入程序
            String content = "{\n" +
                    "\t\"pRRUNumber\": 5,\n" +
                    "\t\"radius\": 20\n" +
                    "}";
            String path = Constant.FilePath + File.separator + "file";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(path + File.separator + "settings.txt");
                fileOutputStream.write(content.getBytes());
                fileOutputStream.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
