package com.zz.core.api;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * REST API 返回结果
 * </p>
 */
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
public class ApiResult<T> implements Serializable
{
    /**
     * 响应码
     */
    private int code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time = new Date();

    public ApiResult()
    {

    }

    public static ApiResult result(boolean flag)
    {
        if (flag)
        {
            return ok();
        }
        return fail();
    }

    /**
     * 新增，用于无数据时返回，根据错误码直接返回
     * @param result
     * @return
     */
    public static ApiResult result(int result)
    {
        boolean success = false;
        if (result == ApiResultCode.SUCCESS.getCode())
        {
            success = true;
        }
        String message = ApiResultCode.getApiCode(result).getMsg();

        return ApiResult.builder()
                .code(result)
                .msg(message)
                .message(message)
                .data(null)
                .success(success)
                .time(new Date())
                .build();
    }

    public static ApiResult result(ApiResultCode apiResultCode)
    {
        return result(apiResultCode, null);
    }

    public static ApiResult result(ApiResultCode apiResultCode, Object data)
    {
        return result(apiResultCode, null, data);
    }

    public static ApiResult result(ApiResultCode apiResultCode, String msg, Object data)
    {
        boolean success = false;
        if (apiResultCode.getCode() == ApiResultCode.SUCCESS.getCode())
        {
            success = true;
        }
        String message = apiResultCode.getMsg();
        if (StringUtils.isNotBlank(msg))
        {
            message = msg;
        }
        return ApiResult.builder()
                .code(apiResultCode.getCode())
                .msg(message)
                .message(message)
                .data(data)
                .success(success)
                .time(new Date())
                .build();
    }

    public static ApiResult ok()
    {
        return ok(null);
    }

    public static <T> ApiResult ok(T data)
    {
        return result(ApiResultCode.SUCCESS, data);
    }

    public static ApiResult ok(Object data, String msg)
    {
        return result(ApiResultCode.SUCCESS, msg, data);
    }

    public static ApiResult okMap(String key, Object value)
    {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return ok(map);
    }

    public static ApiResult fail(ApiResultCode apiResultCode)
    {
        return result(apiResultCode, null);
    }

    public static ApiResult fail(String msg)
    {
        return result(ApiResultCode.FAIL, msg, null);

    }

    public static ApiResult fail(ApiResultCode apiResultCode, Object data)
    {
        if (ApiResultCode.SUCCESS == apiResultCode)
        {
            throw new RuntimeException("失败结果状态码不能为" + ApiResultCode.SUCCESS.getCode());
        }
        return result(apiResultCode, data);

    }

    public static ApiResult fail(String key, Object value)
    {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return result(ApiResultCode.FAIL, map);
    }

    public static ApiResult fail()
    {
        return fail(ApiResultCode.FAIL);
    }
}
