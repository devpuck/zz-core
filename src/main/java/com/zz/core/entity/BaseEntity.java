package com.zz.core.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体父类
 */
@Data
@Accessors(chain = true)
public abstract class BaseEntity implements Serializable
{

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date createdDate;

    /**
     * 最后修改人
     */
    private String lastUpdatedBy;

    /**
     * 最后修改时间
     */
    private Date lastUpdatedDate;

    /**
     * 删除人
     */
    private String deletedBy;

    /**
     * 删除时间
     */
    private Date deletedDate;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableLogic
    private Integer isDeleted;

}
