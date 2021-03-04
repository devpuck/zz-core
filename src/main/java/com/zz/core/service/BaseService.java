package com.zz.core.service;

import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public interface BaseService<T> extends IService<T> {

    /**
     * 删除实体，只需传入ID
     * @return
     */
    Boolean logicDeleteEntityById(Serializable id);

    /**
     * 批量删除实体，只需传入ID列表
     * @return
     */
    Boolean logicDeleteEntityByIds(List<Object> idList);
}
