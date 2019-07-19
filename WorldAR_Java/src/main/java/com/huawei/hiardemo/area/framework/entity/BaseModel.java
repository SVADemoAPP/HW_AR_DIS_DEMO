package com.huawei.hiardemo.area.framework.entity;

import java.io.Serializable;

/**
 * 解析对象的基类
 */
public class BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    public int resultStatus;
    public String resultMsg = "";

}
