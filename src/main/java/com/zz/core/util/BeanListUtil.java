package com.zz.core.util;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 对象列表工具
 */
public class BeanListUtil<T, K> {


    /**
     * 对象列表复制
     * @param list
     * @param targetClass 目标类型
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> copyListProperties(List<?> list, Class<T> targetClass) throws Exception {
        List<T> targetList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (Object item : list) {
                T target = targetClass.newInstance();
                BeanUtils.copyProperties(item, target);
                targetList.add(target);
            }
        }
        return targetList;
    }

}
