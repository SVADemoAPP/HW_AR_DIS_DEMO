package com.huawei.hiardemo.area.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hiardemo.area.R;
import com.huawei.hiardemo.area.framework.activity.BaseActivity;
import com.huawei.hiardemo.area.util.Constant;
import com.huawei.hiardemo.area.util.MatchUtil;
import com.huawei.hiardemo.area.util.SharePref;
import com.huawei.hiardemo.area.util.network.utils.RetrofitUtil;

import static com.huawei.hiardemo.area.util.Constant.ADRESS;
import static com.huawei.hiardemo.area.util.Constant.HTTP_TYPE;


public class SettingActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private Context mContext;
    private TextView mTvToolName;
    private TextView mTvSave;
    private ImageView mIvBack;
    private EditText mEdtAdress;
    private String mEdtData;
    private TextView mIsHttp;
    private static final String IS_HTTP = "http";
    private static final String IS_HTTPS = "https";
    private String tempAdress = "";
    private int mWitch = 0;
    private int mTempWitch = -1;

    @Override
    public void setLayout() {
        mContext = this;
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void initView() {
        mTvToolName = findViewById(R.id.tool_name);
        mTvSave = findViewById(R.id.tv_right);
        mIvBack = findViewById(R.id.tool_back);
        mEdtAdress = findViewById(R.id.edt_adress);
        mIsHttp = findViewById(R.id.tv_ishttp);
        mTvSave.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mIsHttp.setOnClickListener(this);
        mEdtAdress.addTextChangedListener(this);
        mTvSave.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        mTvToolName.setText("设置");
        mTvSave.setText("保存");
        String isHttp = (String) SharePref.get(mContext, HTTP_TYPE, "");
        if (!isHttp.equals("")) {
            if (isHttp.equals(Constant.HTTP)) {
                mIsHttp.setText(IS_HTTP);
                mWitch = 0;
            } else if (isHttp.equals(Constant.HTTPS)) {
                mIsHttp.setText(IS_HTTPS);
                mWitch = 1;
            }
        } else {
            mIsHttp.setText(IS_HTTP);
        }
        String adress = (String) SharePref.get(mContext, ADRESS, "");
        if (!adress.equals("")) {
            mEdtAdress.setText(adress);
        } else {
            mEdtAdress.setText(Constant.DEFAULT_PATH); //设置一个默认的环境
        }
        mEdtAdress.setSelection(mEdtAdress.getText().length());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ishttp:
                judgeHttp();
                break;
            case R.id.tv_right: //保存按钮
                saveData();
                break;

            case R.id.tool_back: //返回按钮
                finish();
                break;
        }
    }

    /**
     * 保存配置相关数据
     */
    private void saveData() {
        mEdtData = mEdtAdress.getText().toString();
        String[] split = mEdtData.split(":");
        if (split.length == 1) {
            Toast.makeText(mContext, "请输入端口号", Toast.LENGTH_SHORT).show();
            return;
        } else if (split.length > 2) {
            Toast.makeText(mContext, "请检查IP地址加端口的合法性", Toast.LENGTH_SHORT).show();
        }
        if (MatchUtil.checkAddress(split[0]) && MatchUtil.checkPort(split[1])) {

        } else {
            Toast.makeText(mContext, "请检查IP地址加端口的合法性", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (mIsHttp.getText().equals(IS_HTTP)) {
                tempAdress = Constant.HTTP;
            } else {
                tempAdress = Constant.HTTPS;
            }
            SharePref.put(mContext, HTTP_TYPE, tempAdress);  //保存http||https(请求方式)
            SharePref.put(mContext, ADRESS, mEdtData); //保存请求地址
        } catch (Exception e) {
            Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
        }
        String isHttp = (String) SharePref.get(mContext, HTTP_TYPE, "");
        String adress = (String) SharePref.get(mContext, ADRESS, "");
        if (!isHttp.equals("") && !adress.equals("")) {
            Constant.REQUEST_ADRESS = isHttp + adress;
        }
        Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
        RetrofitUtil.destory();
        finish();
    }

    /**
     * 判断选用的请求方式
     */
    private void judgeHttp() {
        new AlertDialog.Builder(mContext)
                .setTitle("请选择请求方式:")
                .setSingleChoiceItems(new String[]{IS_HTTP, IS_HTTPS}, mWitch, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mTempWitch = which;
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mTempWitch = -1;
                dialogInterface.dismiss();
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (mTempWitch) {
                    case 0:
                        mIsHttp.setText(IS_HTTP);
                        break;
                    case 1:
                        mIsHttp.setText(IS_HTTPS);
                        break;
                }
                mWitch = mTempWitch;
                dialogInterface.dismiss();
            }
        }).show();

    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
}
