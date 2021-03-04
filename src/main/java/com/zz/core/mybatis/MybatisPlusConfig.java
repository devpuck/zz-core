
package com.zz.core.mybatis;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.core.parser.SqlParserHelper;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.zz.core.aspect.DataScope;
import com.zz.core.auth.ZzUserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * <p>
 *     MybatisPlus配置
 * </p>
 */
@Configuration
@Slf4j
public class MybatisPlusConfig implements ApplicationListener<ContextRefreshedEvent> {

    private static final Map<String, DataScopeConfig>  mapperDataScopeConfig = new HashMap();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //获取注解的配置
        final Map<String, Object> beansWithAnnotation = contextRefreshedEvent.getApplicationContext()
                .getBeansWithAnnotation(Repository.class);

        if (beansWithAnnotation != null) {
            for (String key : beansWithAnnotation.keySet()) {
                Object clzObj = beansWithAnnotation.get(key);
                //Spring 代理类导致Method无法获取,这里使用AopUtils.getTargetClass()方法
                Method[] methods = ReflectionUtils.getAllDeclaredMethods(clzObj.getClass());
                for (Method method : methods) {
                    //获取指定方法上的注解的属性
                    final DataScope dataScope = AnnotationUtils.findAnnotation(method, DataScope.class);
                    if(dataScope != null){
                        String methodName = AopProxyUtils.proxiedUserInterfaces(clzObj)[0].getName() + "." + method.getName();
                        DataScopeConfig dataScopeConfig = new DataScopeConfig();
                        dataScopeConfig.setFilterFiled(Arrays.asList(dataScope.filterFiled()));
                        dataScopeConfig.setIgnoreRole(Arrays.asList(dataScope.ignoreRole()));
                        dataScopeConfig.setDataScopeType(dataScope.dataType());
                        mapperDataScopeConfig.put(methodName, dataScopeConfig);
                    }
                }
            }
        }
    }

    /**
     * mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();

        List<ISqlParser> sqlParserList = new ArrayList<>();
        sqlParserList.add(new com.zz.core.mybatis.DataScopeSqlParser());
        paginationInterceptor.setSqlParserList(sqlParserList);
        paginationInterceptor.setSqlParserFilter(new ISqlParserFilter() {
            @Override
            public boolean doFilter(MetaObject metaObject) {
                MappedStatement ms = SqlParserHelper.getMappedStatement(metaObject);
                // 判断是否有 DataScope  注解
                if (mapperDataScopeConfig.containsKey(ms.getId())) {
                    // 再判断当前用户角色是不是不过滤
                    if(!mapperDataScopeConfig.get(ms.getId()).isIgnoreRole(ZzUserContextHolder.getUser().getRoleCodeList())){
                        DataScopeContextHolder.setDataScopeConfig(mapperDataScopeConfig.get(ms.getId()));
                        return false;
                    }
                }
                return true;
            }
        });
        return paginationInterceptor;
    }
}
