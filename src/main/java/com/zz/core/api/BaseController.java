
package com.zz.core.api;

import com.zz.core.auth.ZzUserContextHolder;
import com.zz.core.bo.BaseBo;
import com.zz.core.vo.BaseVo;
import com.zz.core.vo.LoginUserTokenVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Slf4j
public abstract class BaseController extends com.zz.core.api.ApiController
{

    /**
     * 设置基础操作信息
     *
     * @param baseBo
     */
    public void setBaseField(BaseBo baseBo)
    {
        baseBo.setLastUpdatedDate(new Date());
        LoginUserTokenVo user = ZzUserContextHolder.getUser();
        if (user != null)
        {
            baseBo.setCreatedBy(user.getUserName());
            baseBo.setLastUpdatedBy(user.getUserName());
        }
    }

    /**
     * 设置基础操作信息
     *
     * @param baseVo
     */
    public void setBaseField(BaseVo baseVo)
    {
        baseVo.setLastUpdatedDate(new Date());
        LoginUserTokenVo user = ZzUserContextHolder.getUser();
        if (user != null)
        {
            baseVo.setCreatedBy(user.getUserName());
            baseVo.setLastUpdatedBy(user.getUserName());
        }
    }

    /**
     * 获取当前请求
     *
     * @return request
     */
    public HttpServletRequest getRequest()
    {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取当前请求
     *
     * @return response
     */
    public HttpServletResponse getResponse()
    {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }
}