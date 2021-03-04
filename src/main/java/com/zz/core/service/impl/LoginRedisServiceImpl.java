package com.zz.core.service.impl;

import com.zz.core.auth.JwtProperties;
import com.zz.core.auth.JwtToken;
import com.zz.core.constant.CommonRedisKey;
import com.zz.core.service.LoginRedisService;
import com.zz.core.util.RedisUtil;
import com.zz.core.vo.LoginUserTokenVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.Duration;

/**
 * 登陆信息Redis缓存服务类
 *
 * @author geekidea
 * @date 2019-09-30
 * @since 1.3.0.RELEASE
 **/
@Service
public class LoginRedisServiceImpl implements LoginRedisService {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * key-value: 有过期时间-->token过期时间
     * 1. tokenMd5:jwtTokenRedisVo
     * 2. username:loginSysUserRedisVo
     * 3. username:salt
     * hash: 没有过期时间，统计在线的用户信息
     * username:num
     */
    @Override
    public void cacheLoginInfo(JwtToken jwtToken, LoginUserTokenVo loginUserTokenVo) {
        if (jwtToken == null) {
            throw new IllegalArgumentException("jwtToken不能为空");
        }
        if (loginUserTokenVo == null) {
            throw new IllegalArgumentException("loginSysUserVo不能为空");
        }
        // token
        String token = jwtToken.getToken();
        // 盐值
        String salt = jwtToken.getSalt();
        // 登陆用户名称
        String username = loginUserTokenVo.getUserName();
        // token md5值
        String tokenMd5 = DigestUtils.md5DigestAsHex(token.getBytes());

        // Redis过期时间与JwtToken过期时间一致
        Duration expireDuration = Duration.ofSeconds(jwtToken.getExpireSecond());

        // 判断是否启用单个用户登陆，如果是，这每个用户只有一个有效token
        boolean singleLogin = jwtProperties.isSingleLogin();
        if (singleLogin) {
            deleteUserAllCache(username);
        }
        loginUserTokenVo.setToken(token);
        // 1. tokenMd5:jwtTokenRedisVo
        String loginTokenRedisKey = String.format(CommonRedisKey.LOGIN_TOKEN, tokenMd5);
        RedisUtil.set(loginTokenRedisKey, jwtToken, expireDuration);
        // 2. username:loginSysUserRedisVo
        RedisUtil.set(String.format(CommonRedisKey.LOGIN_USER, username), loginUserTokenVo, expireDuration);
        // 3. salt hash,方便获取盐值鉴权
        RedisUtil.set(String.format(CommonRedisKey.LOGIN_SALT, username), salt, expireDuration);
    }

    @Override
    public void refreshLoginInfo(String oldToken, String username, JwtToken newJwtToken) {
        // 获取缓存的登陆用户信息
        LoginUserTokenVo loginSysUserRedisVo = getLoginSysUserRedisVo(username);
        // 删除之前的token信息
        deleteLoginInfo(oldToken, username);
        // 缓存登陆信息
        cacheLoginInfo(newJwtToken, loginSysUserRedisVo);
    }

    @Override
    public LoginUserTokenVo getLoginUserTokenVo(String username) {
        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("username不能为空");
        }
        LoginUserTokenVo userRedisVo = getLoginSysUserRedisVo(username);
        return userRedisVo;
    }

    @Override
    public String getSalt(String username) {
        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("username不能为空");
        }
        String salt = (String) RedisUtil.get(String.format(CommonRedisKey.LOGIN_SALT, username));
        return salt;
    }

    @Override
    public void deleteLoginInfo(String token, String username) {
        if (token == null) {
            throw new IllegalArgumentException("token不能为空");
        }
        if (username == null) {
            throw new IllegalArgumentException("username不能为空");
        }
        String tokenMd5 = DigestUtils.md5DigestAsHex(token.getBytes());
        // 1. delete tokenMd5
        RedisUtil.del(String.format(CommonRedisKey.LOGIN_TOKEN, tokenMd5));
        // 2. delete username
        RedisUtil.del(String.format(CommonRedisKey.LOGIN_USER, username));
        // 3. delete salt
        RedisUtil.del(String.format(CommonRedisKey.LOGIN_SALT, username));
    }

    @Override
    public boolean exists(String token) {
        if (token == null) {
            throw new IllegalArgumentException("token不能为空");
        }
        String tokenMd5 = DigestUtils.md5DigestAsHex(token.getBytes());
        Object object = RedisUtil.get(String.format(CommonRedisKey.LOGIN_TOKEN, tokenMd5));
        return object != null;
    }

    @Override
    public void deleteUserAllCache(String username) {
        // 检测是否已经有登录信息
        LoginUserTokenVo exitUser = this.getLoginSysUserRedisVo(username);
        if (exitUser != null) {
            // token md5值
            String tokenMd5 = DigestUtils.md5DigestAsHex(exitUser.getToken().getBytes());
            RedisUtil.del(String.format(CommonRedisKey.LOGIN_TOKEN, tokenMd5));
        }

        // 1. 删除登陆用户信息
        RedisUtil.del(String.format(CommonRedisKey.LOGIN_USER, username));
        // 2. 删除登陆用户盐值信息
        RedisUtil.del(String.format(CommonRedisKey.LOGIN_SALT, username));
    }

    /**
     * 根据UserName
     * @param username
     * @return
     */
    private LoginUserTokenVo getLoginSysUserRedisVo(String username) {
        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("username不能为空");
        }
        return RedisUtil.get(String.format(CommonRedisKey.LOGIN_USER, username), LoginUserTokenVo.class);
    }

}
