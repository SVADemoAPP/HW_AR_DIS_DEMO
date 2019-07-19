package com.huawei.hiardemo.area.view.popup;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hiardemo.area.R;

import java.util.Map;


/**
 * Create by 91569
 * Time 2019/7/18
 * Describe :
 */
public class RePortPopupWindow {
    private SuperPopupWindow mSelectPopupWindow;
    private Context mContext;
    private OnItemClick onItemClickListener;
    private RelativeLayout relativeLayout;
    private String selectFilePath;
    private TextView mTvTime;
    private TextView mTvIp;
    private TextView mTvArea;
    private TextView mTvData;
    private TextView mTvValue2;
    private TextView mTvValue1;
    private TextView mTvValue3;
    private TextView mTvValue4;
    private TextView mTvCancel;
    private TextView mTvSave;

    /**
     * 构造函数
     *
     * @param context 上下文对象
     */
    public RePortPopupWindow(Context context) {
        mContext = context;
        mSelectPopupWindow = new SuperPopupWindow(context, R.layout.layout_report_show);
        mSelectPopupWindow.setWarpContent();
        mSelectPopupWindow.setChangFocusable(true);
        View popupView = mSelectPopupWindow.getPopupView();
        initPopupWindow(popupView);
    }

    public void setDetails(Map<String, String> map) {
        String time = map.get("time");
        String ip = map.get("ip");
        String area = map.get("area");
        String data = map.get("data");
        String value1 = map.get("value1"); //-75 - 0
        String value2 = map.get("value2"); //-95 -  -75
        String value3 = map.get("value3"); //-105 - -95
        String value4 = map.get("value4"); //-120 - -105
        mTvTime.setText(time);
        mTvIp.setText(ip);
        mTvArea.setText(area);
        mTvData.setText(data);
        mTvValue1.setText(value1);
        mTvValue2.setText(value2);
        mTvValue3.setText(value3);
        mTvValue4.setText(value4);

    }


    public void setOnItemClick(OnItemClick listener) {
        onItemClickListener = listener;
    }


    private void initPopupWindow(View popupView) {
        relativeLayout = popupView.findViewById(R.id.rl);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide(); //点击空白处隐藏
            }
        });
        //测试时间
        mTvTime = popupView.findViewById(R.id.tv_test_time);
        //测完Ip
        mTvIp = popupView.findViewById(R.id.tv_test_ip);
        //测试场馆
        mTvArea = popupView.findViewById(R.id.tv_test_area);
        //测试详细数据
        mTvData = popupView.findViewById(R.id.tv_test_data);
        mTvValue1 = popupView.findViewById(R.id.tv_value1);
        mTvValue2 = popupView.findViewById(R.id.tv_value2);
        mTvValue3 = popupView.findViewById(R.id.tv_value3);
        mTvValue4 = popupView.findViewById(R.id.tv_value4);
        mTvCancel = popupView.findViewById(R.id.cancel);
        mTvSave = popupView.findViewById(R.id.save);
        mTvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "取消", Toast.LENGTH_SHORT).show();
                hide();
            }
        });
        mTvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "已保存", Toast.LENGTH_SHORT).show();
                hide();
            }
        });
    }

    public void show() {
        mSelectPopupWindow.showPopupWindow();
    }

    public void hide() {
        if (mSelectPopupWindow.isShowing()) {
            mSelectPopupWindow.hidePopupWindow();
        }
    }

    public boolean isShowing() {
        return mSelectPopupWindow.isShowing();
    }

    public interface OnItemClick {
        void onItemClick(String mapName, String path);
    }
}



