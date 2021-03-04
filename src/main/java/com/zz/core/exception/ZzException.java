package com.zz.core.exception;

public class ZzException extends Exception
{

    private Integer errorCode;
    private String message;

    public ZzException(String message)
    {
        super(message);
        this.message = message;
    }

    public ZzException(Integer errorCode, String message)
    {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }
}
