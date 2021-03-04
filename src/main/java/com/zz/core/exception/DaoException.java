package com.zz.core.exception;

/**
 * DAO异常
 */
public class DaoException extends com.zz.core.exception.ZzException
{

    public DaoException(String message) {
        super(message);
    }
    public DaoException(Integer errorCode, String message) {
        super(errorCode,message);
    }

}
