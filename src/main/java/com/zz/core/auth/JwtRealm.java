
package com.zz.core.auth;

import com.zz.core.service.LoginRedisService;
import com.zz.core.vo.LoginUserTokenVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Shiro 授权认证
 *
 * @author geekidea
 * @date 2019-09-27
 * @since 1.3.0.RELEASE
 **/
@Slf4j
public class JwtRealm extends AuthorizingRealm {

    private LoginRedisService loginRedisService;

    public JwtRealm(LoginRedisService loginRedisService) {
        this.loginRedisService = loginRedisService;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && token instanceof JwtToken;
    }

    /**
     * 授权认证,设置角色/权限信息
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.debug("doGetAuthorizationInfo principalCollection...");
        // 设置角色/权限信息
        JwtToken jwtToken = (JwtToken) principalCollection.getPrimaryPrincipal();
        // 获取username
        String username = jwtToken.getUsername();
        // 获取登陆用户角色权限信息
        LoginUserTokenVo loginUserTokenVo = loginRedisService.getLoginUserTokenVo(username);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 设置角色
        authorizationInfo.setRoles(new HashSet<>(loginUserTokenVo.getRoleCodeList()));
        // 设置权限
        Set<String> stringPermissions  = loginUserTokenVo.getResourcesList().stream().filter(r->r.getType() == 1).map(r->r.getPerms()).collect(Collectors.toSet());
        authorizationInfo.setStringPermissions(stringPermissions);
        return authorizationInfo;
    }

    /**
     * 登陆认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.debug("doGetAuthenticationInfo authenticationToken...");
        // 校验token
        JwtToken jwtToken = (JwtToken) authenticationToken;
        if (jwtToken == null) {
            throw new AuthenticationException("jwtToken不能为空");
        }
        String salt = jwtToken.getSalt();
        if (StringUtils.isBlank(salt)) {
            throw new AuthenticationException("salt不能为空");
        }
        return new SimpleAuthenticationInfo(
                jwtToken,
                salt,
                getName()
        );

    }

}
