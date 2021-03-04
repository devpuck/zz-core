package com.zz.core.auth.shiro;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Filter配置属性
 *
 **/
@Data
@ConfigurationProperties(prefix = "zz.filter")
public class ShiroPathFilterProperties {

    /**
     * 请求路径Filter配置
     */
    @NestedConfigurationProperty
    private FilterConfig requestPath = new FilterConfig();

    @Data
    public static class FilterConfig {

        /**
         * 是否启用
         */
        private boolean enabled;

        /**
         * 包含的路径
         */
        private String[] includePaths;

        /**
         * 排除路径
         */
        private String[] excludePaths;

    }
}
