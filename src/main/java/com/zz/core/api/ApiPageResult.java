package com.zz.core.api;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * REST API 返回分页结果
 * </p>
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class ApiPageResult<T> extends ApiResult<T>
{

    @ApiModelProperty("总行数")
    @JSONField(name = "total")
    @JsonProperty("total")
    private long total = 0;

    public ApiPageResult() { }

    public static ApiPageResult ok()
    {
        return ok(null);
    }

    public static<T> ApiPageResult ok(com.zz.core.api.Paging<T> paging)
    {
        ApiResult apiResult = result(ApiResultCode.SUCCESS, paging.getRecords());
        ApiPageResult pageResult =new  ApiPageResult();
        pageResult.setTotal(paging.getTotal());
        pageResult.setData(apiResult.getData())
                .setCode(apiResult.getCode())
                .setSuccess(apiResult.isSuccess())
                .setMsg(apiResult.getMsg())
                .setTime(apiResult.getTime());
        return pageResult;
    }
}
