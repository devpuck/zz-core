package com.zz.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <pre>
 * 权限资源（菜单）信息表 查询结果对象
 * </pre>
 *
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ResourcesVo对象", description = "权限资源（菜单）信息表查询参数")
public class AuthResourcesVo implements Serializable
{

    private static final long serialVersionUID = 9164239257379136186L;

    @ApiModelProperty(value = "权限标识")
    private String perms;

    @ApiModelProperty(value = "类型:0-菜单 1-操作 2-数据权限")
    private Integer type;

    @ApiModelProperty(value = "Type=2 数据权限才有值：1-物料权限")
    private Integer subType;

    @ApiModelProperty(value = "数据权限关联的资源ID")
    private String bizId;
}