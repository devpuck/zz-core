
package com.zz.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zz.core.api.QueryParam;
import com.zz.core.api.SortQueryParam;
import com.zz.core.auth.ZzUserContextHolder;
import com.zz.core.service.BaseService;
import com.zz.core.vo.LoginUserTokenVo;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author
 * @date
 */
public abstract class  BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

    protected Page setPageParam(QueryParam queryParam) {
        return setPageParam(queryParam,null);
    }

    protected Page setPageParam(QueryParam queryParam, OrderItem defaultOrder) {
        Page page = new Page();
        // 设置当前页码
        page.setCurrent(queryParam.getCurrent());
        // 设置页大小
        page.setSize(queryParam.getPageSize());
        /**
         * 如果是queryParam是OrderQueryParam，并且不为空，则使用前端排序
         * 否则使用默认排序
         */
        if (queryParam instanceof SortQueryParam){
            SortQueryParam orderQueryParam = (SortQueryParam) queryParam;
            List<OrderItem> orderItems = orderQueryParam.getOrders();
            if (CollectionUtils.isEmpty(orderItems)){
                page.setOrders(Arrays.asList(defaultOrder));
            }else{
                page.setOrders(orderItems);
            }
        }else{
            page.setOrders(Arrays.asList(defaultOrder));
        }

        return page;
    }

    /**
     * 逻辑删除实体
     * @param id
     * @return
     */
    public Boolean logicDeleteEntityById(Serializable id){
        LoginUserTokenVo userTokenVo = ZzUserContextHolder.getUser();
        UpdateWrapper<T> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("IS_DELETED", 1);
        updateWrapper.set("DELETED_BY", userTokenVo.getUserName());
        updateWrapper.set("DELETED_DATE", new Date());
        updateWrapper.eq("id", id);
        return this.update(updateWrapper);
    }

    /**
     * 逻辑删除实体
     * @param idList
     * @return
     */
    public Boolean logicDeleteEntityByIds(List<Object> idList){
        LoginUserTokenVo userTokenVo = ZzUserContextHolder.getUser();
        UpdateWrapper<T> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("IS_DELETED", 1);
        updateWrapper.set("DELETED_BY", userTokenVo.getUserName());
        updateWrapper.set("DELETED_DATE", new Date());
        updateWrapper.in("id", idList);
        return this.update(updateWrapper);
    }
}
