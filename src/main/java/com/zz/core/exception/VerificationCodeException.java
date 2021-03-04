package com.zz.core.exception;

/**
 * 验证码校验异常
 *
 * @author geekidea
 * @date 2018-11-08
 */
public class VerificationCodeException extends com.zz.core.exception.ZzException
{

    public VerificationCodeException(String message)
    {
        super(message);
    }

    public VerificationCodeException(Integer errorCode, String message)
    {
        super(errorCode, message);
    }

}
