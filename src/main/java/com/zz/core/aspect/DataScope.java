package com.zz.core.aspect;

import com.zz.core.enums.DataScopeType;

import java.lang.annotation.*;

/**
 * 数据权限过滤注解
 * 
 * @author ruoyi
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope
{

    /**
     * 设置数据维度，需要下面filterFiled一一对应
     * @return
     */
    DataScopeType[] dataType() default DataScopeType.ITEM;

    /**
     * 要适配的数据资源，比如，适配用户的物料权限
     * item.id
     * 注意只能写SQL 已经有的别名
     * @return
     */
    String[] filterFiled() default {};

    /**
     * 忽略过滤的角色
     * item.id
     * @return
     */
    String[] ignoreRole() default {};
}
