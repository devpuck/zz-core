package com.zz.core.enums;

/**
 * 数据权限维度枚举,code 值保持和资源表sub_type 保持一致
 */
public enum DataScopeType
{

    ITEM(1, "物料权限"),
    SHOP(2, "车间权限");

    private final int code;
    private final String msg;

    DataScopeType(final int code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
