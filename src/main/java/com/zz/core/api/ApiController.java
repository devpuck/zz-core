
package com.zz.core.api;

/**
 * <p>
 * ApiResultEST API 公共控制器
 * </p>
 */
public class ApiController
{

    /**
     * <p>
     * 请求成功
     * </p>
     *
     * @param data 数据内容
     * @param <T>  对象泛型
     * @return
     */
    protected <T> ApiResult<T> ok(T data)
    {
        return ApiResult.ok(data);
    }

    /**
     * <p>
     * 请求失败
     * </p>
     *
     * @param msg 提示内容
     * @return
     */
    protected ApiResult<Object> fail(String msg)
    {
        return ApiResult.fail(msg);
    }

    /**
     * <p>
     * 请求失败
     * </p>
     *
     * @param apiResultCode 请求错误码
     * @return
     */
    protected ApiResult<Object> fail(ApiResultCode apiResultCode)
    {
        return ApiResult.fail(apiResultCode);
    }

}
