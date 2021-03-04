package com.zz.core.exception;

/**
 * 业务异常
 */
public class BusinessException extends com.zz.core.exception.ZzException
{

    public BusinessException(String message) {
        super(message);
    }
    public BusinessException(Integer errorCode, String message) {
        super(errorCode,message);
    }

}
