
package com.zz.core.api;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("数组参数")
public class ArrayParam<T> implements Serializable
{
    private static final long serialVersionUID = -5353973980674510450L;

    private List<T> dataList;
}
