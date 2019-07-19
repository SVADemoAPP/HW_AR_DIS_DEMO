package com.huawei.hiardemo.area.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String getDate() {
        String time = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time = simpleDateFormat.format(new Date());
        return time;
    }
}
