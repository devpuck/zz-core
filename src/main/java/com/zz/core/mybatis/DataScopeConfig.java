package com.zz.core.mybatis;

import com.zz.core.enums.DataScopeType;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 数据权限过滤配置
 */
public class DataScopeConfig implements Serializable {

    /**
     * 数据维度
     */
    private DataScopeType[] dataScopeType;

    /**
     * 要过滤的字段，注意只能是SQL 语句里面存在的别名
     */
    private List<String> filterFiled;


    /**
     * 忽略过滤的角色
     */
    private List<String> ignoreRole;

    public List<String> getFilterFiled() {
        return filterFiled;
    }

    public void setFilterFiled(List<String> filterFiled) {
        this.filterFiled = filterFiled;
    }

    public List<String> getIgnoreRole() {
        return ignoreRole;
    }

    public void setIgnoreRole(List<String> ignoreRole) {
        this.ignoreRole = ignoreRole;
    }

    /**
     * 匹配跳过数据权限角色
     * @param roleList
     * @return
     */
    public boolean isIgnoreRole(List<String> roleList){
        if(!CollectionUtils.isEmpty(roleList)){
            for (String role:roleList){
                if(this.ignoreRole.stream().anyMatch(s->s.equals(role))){
                    return true;
                }
            }
        }
        return false;
    }

    public DataScopeType[] getDataScopeType() {
        return dataScopeType;
    }

    public void setDataScopeType(DataScopeType[] dataScopeType) {
        this.dataScopeType = dataScopeType;
    }
}
