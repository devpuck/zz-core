package com.zz.core.exception;

/**
 * 系统登录异常
 */
public class SysLoginException extends com.zz.core.exception.ZzException
{

    public SysLoginException(String message) {
        super(message);
    }
    public SysLoginException(Integer errorCode, String message) {
        super(errorCode,message);
    }

}
