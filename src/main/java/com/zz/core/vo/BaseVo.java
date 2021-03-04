package com.zz.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础vo 统一公用字段
 */
@ApiModel("基础vo 统一公用字段")
@Data
@Accessors(chain = true)
public abstract class BaseVo implements Serializable
{

    @ApiModelProperty(value = "创建人")
    private String createdBy;

    @ApiModelProperty(value = "创建时间")
    private Date createdDate;

    @ApiModelProperty(value = "最后修改人")
    private String lastUpdatedBy;

    @ApiModelProperty(value = "最后修改时间")
    private Date lastUpdatedDate;

    @ApiModelProperty(value = "删除人")
    private String deletedBy;

    @ApiModelProperty(value = "删除时间")
    private Date deletedDate;

    @ApiModelProperty(value = "是否删除：0-未删除，1-已删除")
    private Integer isDeleted;
}
