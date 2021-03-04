package com.zz.core.db;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.Objects;

/**
 * 当查询条件为空时，比较空值，返回isNull
 * @author puck
 * @date 2020/12/28 6:32 下午
 */
public class ExcludeNullQueryWrapper<T> extends QueryWrapper<T>
{
    @Override
    public QueryWrapper<T> eq(boolean condition, String column, Object val)
    {
//        System.out.println("*************************************************");
        boolean flag = Objects.isNull(val);
        if(flag)
        {
//            System.out.println("111111111111111111111111111111111111111111111");
            return isNull(column);
        }
//        System.out.println("2222222222222222222222222222222222");
        return super.eq(condition,column, val);
    }
}
