
package com.zz.core.api;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 可排序查询参数对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("可排序查询参数对象")
public abstract class SortQueryParam extends QueryParam
{

    private static final long serialVersionUID = 4575871177830275759L;

    @ApiModelProperty(value = "排序")
    private List<OrderItem> orders;

    public void defaultOrder(OrderItem orderItem)
    {
        this.defaultOrders(Arrays.asList(orderItem));
    }

    public void defaultOrders(List<OrderItem> orderItems)
    {
        if (CollectionUtils.isEmpty(orderItems))
        {
            return;
        }
        this.orders = orderItems;
    }

}
