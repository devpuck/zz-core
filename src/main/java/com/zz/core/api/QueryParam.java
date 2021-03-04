
package com.zz.core.api;

import com.zz.core.constant.CoreConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询参数
 */
@Data
@ApiModel("查询参数对象")
public abstract class QueryParam implements Serializable
{
    private static final long serialVersionUID = -3263921252635611410L;

    @ApiModelProperty(value = "页码,默认为1", example = "1")
    private Integer current = CoreConstant.DEFAULT_PAGE_INDEX;
    @ApiModelProperty(value = "页大小,默认为10", example = "10")
    private Integer pageSize = CoreConstant.DEFAULT_PAGE_SIZE;
    @ApiModelProperty(value = "搜索字符串", example = "")
    private String keyword;

    public void setCurrent(Integer current)
    {
        if (current == null || current <= 0)
        {
            this.current = CoreConstant.DEFAULT_PAGE_INDEX;
        }
        else
        {
            this.current = current;
        }
    }

    public void setSize(Integer size)
    {
        if (size == null || size <= 0)
        {
            this.pageSize = CoreConstant.DEFAULT_PAGE_SIZE;
        }
        else
        {
            this.pageSize = size;
        }
    }

}
