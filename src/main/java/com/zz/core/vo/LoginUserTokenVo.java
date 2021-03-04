
package com.zz.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 登录成功对象
 **/
@Data
@Accessors(chain = true)
@ApiModel("登陆用户信息TokenVO")
public class LoginUserTokenVo implements Serializable
{
    private static final long serialVersionUID = -4650803752566647697L;

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty(value = "用户ID")
    private Long id;

    @ApiModelProperty(value = "KeyID")
    private String userKey;

    @ApiModelProperty(value = "用户登录名，只能是数字和字符")
    private String userName;

    @ApiModelProperty(value = "用户登录密码")
    private String userPassword;

    @ApiModelProperty(value = "员工号")
    private String employeeNo;

    @ApiModelProperty(value = "部门编号")
    private String deptCode;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "角色ID列表")
    private List<Long> roleIdList;

    @ApiModelProperty(value = "角色名称列表")
    private List<String> roleNameList;

    @ApiModelProperty(value = "角色名称列表")
    private List<String> roleCodeList;

    @ApiModelProperty(value = "资源列表")
    private List<AuthResourcesVo> resourcesList;
}
