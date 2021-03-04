package com.zz.core.auth;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.zz.core.vo.LoginUserTokenVo;
import lombok.experimental.UtilityClass;

/**
 * 用户上下文
 */
@UtilityClass
public class ZzUserContextHolder
{

    private final ThreadLocal<LoginUserTokenVo> THREAD_LOCAL_USER = new TransmittableThreadLocal<>();

    /**
     * 设置当前用户
     *
     * @param loginUserTokenVo
     */
    public void setUser(LoginUserTokenVo loginUserTokenVo)
    {
        THREAD_LOCAL_USER.set(loginUserTokenVo);
    }

    /**
     * 获取当前用户
     *
     * @return
     */
    public LoginUserTokenVo getUser()
    {
        return THREAD_LOCAL_USER.get();
    }

    public void clear()
    {
        THREAD_LOCAL_USER.remove();
    }
}
