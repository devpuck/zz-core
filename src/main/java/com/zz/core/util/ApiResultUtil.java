package com.zz.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.zz.core.api.ApiResult;

import java.lang.reflect.Type;
import java.util.List;

public class ApiResultUtil
{

    /**
     * 解析ApiResult
     *
     * @param jsonStr
     * @param <T>
     * @return
     */
    public static <T> T jsonToBo(String jsonStr, Class<T> targetClass)
    {
        return JSON.parseObject(jsonStr, new TypeReference<ApiResult<T>>(targetClass)
        {
        }).getData();
    }


    /**
     * 解析列表
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> ApiResult<List<T>> parseListResult(String json, Class<T> clazz)
    {
        ParameterizedTypeImpl inner = new ParameterizedTypeImpl(new Type[]{clazz}, null, List.class);
        ParameterizedTypeImpl outer = new ParameterizedTypeImpl(new Type[]{inner}, null, ApiResult.class);
        return JSONObject.parseObject(json, outer);
    }

}
