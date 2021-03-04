package com.zz.core.service;

import com.zz.core.auth.JwtToken;
import com.zz.core.vo.LoginUserTokenVo;

/**
 * 登陆信息Redis缓存操作服务
 *
 * @author geekidea
 * @date 2019-09-30
 * @since 1.3.0.RELEASE
 **/
public interface LoginRedisService {

    /**
     * 缓存登陆信息
     *
     * @param jwtToken
     * @param loginUserTokenVo
     */
    void cacheLoginInfo(JwtToken jwtToken, LoginUserTokenVo loginUserTokenVo);


    /**
     * 刷新登陆信息
     * @param oldToken
     * @param username
     * @param newJwtToken
     */
    void refreshLoginInfo(String oldToken, String username, JwtToken newJwtToken);

    /**
     * 通过用户名，从缓存中获取登陆用户LoginSysUserRedisVo
     *
     * @param username
     * @return
     */
    LoginUserTokenVo getLoginUserTokenVo(String username);

    /**
     * 通过用户名称获取盐值
     *
     * @param username
     * @return
     */
    String getSalt(String username);

    /**
     * 删除对应用户的Redis缓存
     *
     * @param token
     * @param username
     */
    void deleteLoginInfo(String token, String username);

    /**
     * 判断token在redis中是否存在
     *
     * @param token
     * @return
     */
    boolean exists(String token);

    /**
     * 删除用户所有登陆缓存
     * @param username
     */
    void deleteUserAllCache(String username);

}
