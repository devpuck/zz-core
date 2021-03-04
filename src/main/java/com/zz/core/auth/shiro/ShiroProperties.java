
package com.zz.core.auth.shiro;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;

/**
 * Shiro配置映射类
 **/
@Data
@ConfigurationProperties(prefix = "zz.shiro")
public class ShiroProperties {

    /**
     * 是否启用
     */
    private boolean enable;

    /**
     * 路径权限配置
     */
    private String filterChainDefinitions;

    /**
     * 权限配置集合
     */
    @NestedConfigurationProperty
    private List<ShiroPermissionProperties> permission;

}
