package com.zz.core.util;

import com.zz.core.api.ApiResultCode;

/**
 * @author puck
 * @date 2020/12/23 3:56 下午
 */
public class CheckParameter
{
    public static int checkParameter(String parmeter)
    {
        if(null == parmeter|| "".equals(parmeter))
        {
            return ApiResultCode.PARAMETER_EXCEPTION.getCode();
        }
        return ApiResultCode.SUCCESS.getCode();
    }

    public static int checkParameter(String parmeter,String parmeter2)
    {
        if(ApiResultCode.PARAMETER_EXCEPTION.getCode() == checkParameter(parmeter) ||(ApiResultCode.PARAMETER_EXCEPTION.getCode() == checkParameter(parmeter2)))
        {
            return ApiResultCode.PARAMETER_EXCEPTION.getCode();
        }

        return ApiResultCode.SUCCESS.getCode();
    }

    public static int checkParameter(String parmeter,String parmeter2,String parmeter3)
    {
        if(ApiResultCode.PARAMETER_EXCEPTION.getCode() == checkParameter(parmeter) ||(ApiResultCode.PARAMETER_EXCEPTION.getCode() == checkParameter(parmeter2)) ||(ApiResultCode.PARAMETER_EXCEPTION.getCode() == checkParameter(parmeter3)))
        {
            return ApiResultCode.PARAMETER_EXCEPTION.getCode();
        }

        return ApiResultCode.SUCCESS.getCode();
    }

    public static int checkParameter(String parmeter,String parmeter2,String parmeter3,String parmeter4)
    {
        if(ApiResultCode.PARAMETER_EXCEPTION.getCode() == checkParameter(parmeter,parmeter2) ||(ApiResultCode.PARAMETER_EXCEPTION.getCode() == checkParameter(parmeter3,parmeter4)))
        {
            return ApiResultCode.PARAMETER_EXCEPTION.getCode();
        }

        return ApiResultCode.SUCCESS.getCode();
    }

    public static int checkParameter(String parmeter,String parmeter2,String parmeter3,String parmeter4,String parmeter5)
    {
        if(ApiResultCode.PARAMETER_EXCEPTION.getCode() == checkParameter(parmeter,parmeter2) ||(ApiResultCode.PARAMETER_EXCEPTION.getCode() == checkParameter(parmeter3,parmeter4,parmeter5)))
        {
            return ApiResultCode.PARAMETER_EXCEPTION.getCode();
        }

        return ApiResultCode.SUCCESS.getCode();
    }

    public static int checkParameter(String parmeter,String parmeter2,String parmeter3,String parmeter4,String parmeter5,String parmeter6)
    {
        if(ApiResultCode.PARAMETER_EXCEPTION.getCode() == checkParameter(parmeter,parmeter2,parmeter6) ||(ApiResultCode.PARAMETER_EXCEPTION.getCode() == checkParameter(parmeter3,parmeter4,parmeter5)))
        {
            return ApiResultCode.PARAMETER_EXCEPTION.getCode();
        }

        return ApiResultCode.SUCCESS.getCode();
    }

    public static int checkParameter(String parmeter,String parmeter2,String parmeter3,String parmeter4,String parmeter5,String parmeter6,String parmeter7)
    {
        if(ApiResultCode.PARAMETER_EXCEPTION.getCode() == checkParameter(parmeter,parmeter2,parmeter6) ||(ApiResultCode.PARAMETER_EXCEPTION.getCode() == checkParameter(parmeter3,parmeter4,parmeter5,parmeter7)))
        {
            return ApiResultCode.PARAMETER_EXCEPTION.getCode();
        }

        return ApiResultCode.SUCCESS.getCode();
    }
}

