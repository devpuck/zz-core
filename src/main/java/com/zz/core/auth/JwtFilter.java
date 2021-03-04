package com.zz.core.auth;

import com.zz.core.api.ApiResult;
import com.zz.core.api.ApiResultCode;
import com.zz.core.constant.CoreConstant;
import com.zz.core.service.LoginRedisService;
import com.zz.core.util.HttpServletResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Shiro JWT授权过滤器
 **/
@Slf4j
public class JwtFilter extends AuthenticatingFilter
{

    private LoginRedisService loginRedisService;

    private JwtProperties jwtProperties;

    public JwtFilter(LoginRedisService loginRedisService, JwtProperties jwtProperties)
    {
        this.loginRedisService = loginRedisService;
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception
    {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (httpRequest.getMethod().equals(RequestMethod.OPTIONS.name()))
        {
            //跨域的header设置
            setHeader(httpRequest, httpResponse);
            httpResponse.setStatus(HttpStatus.OK.value());
            return true;
        }
        return super.preHandle(request, response);
    }

    /**
     * 将JWT Token包装成AuthenticationToken
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception
    {
        String token = com.zz.core.auth.JwtTokenUtil.getToken();
        if (StringUtils.isBlank(token))
        {
            throw new AuthenticationException("token不能为空");
        }
        try
        {
            // 尝试解析JWT
            JwtUtil.getJwtInfo(token);
        } catch (Exception e)
        {
            throw new AuthenticationException("非法的token:" + token);
        }
        if (JwtUtil.isExpired(token))
        {
            throw new AuthenticationException("JWT Token已过期,token:" + token);
        }

//        // 如果开启redis二次校验，或者设置为单个用户token登陆，则先在redis中判断token是否存在
//        if (jwtProperties.isRedisCheck() || jwtProperties.isSingleLogin())
//        {
//            boolean redisExpired = loginRedisService.exists(token);
//            if (!redisExpired)
//            {
//                throw new AuthenticationException("Redis Token不存在,token:" + token);
//            }
//        }

        String username = JwtUtil.getUsername(token);
        log.info("BASE TOKE :"+token);
        log.info("BASE USERNAME:"+username);
        String salt;
        if (jwtProperties.isSaltCheck())
        {
            salt = loginRedisService.getSalt(username);
        }
        else
        {
            salt = jwtProperties.getSecret();
        }
        return JwtToken.build(token, username, salt, jwtProperties.getExpireSecond());
    }

    /**
     * 访问失败处理
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception
    {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        // 返回401
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // 设置响应码为401或者直接输出消息
        String url = httpServletRequest.getRequestURI();
        log.error("onAccessDenied url：{}", url);
        ApiResult apiResult = ApiResult.fail(ApiResultCode.UNAUTHORIZED);
        //跨域的header设置
        setHeader(httpServletRequest, httpServletResponse);
        HttpServletResponseUtil.printJSON(httpServletResponse, apiResult);
        return false;
    }

    /**
     * 判断是否允许访问
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
    {
        String url = WebUtils.toHttp(request).getRequestURI();
//        log.debug("isAccessAllowed url:{}", url);
        log.info("isAccessAllowed url:{}", url);
        if (this.isLoginRequest(request, response))
        {
            return true;
        }
        boolean allowed = false;
        try
        {
            allowed = executeLogin(request, response);
        } catch (IllegalStateException e)
        { //not found any token
            log.error("Token不能为空", e);
        } catch (Exception e)
        {
            log.error("访问错误", e);
        }
        return allowed || super.isPermissive(mappedValue);
    }

    /**
     * 登陆成功处理
     *
     * @param token
     * @param subject
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception
    {
        String url = WebUtils.toHttp(request).getRequestURI();
        JwtToken jwtToken = (JwtToken) token;
        log.debug("鉴权成功,userName:{},salt:{},url:{}", jwtToken.getUsername(), jwtToken.getSalt(), url);
        // 刷新token,改动到org 统一刷新
        // 设置上下文
        com.zz.core.auth.ZzUserContextHolder.setUser(loginRedisService.getLoginUserTokenVo(jwtToken.getUsername()));
        return true;
    }

    /**
     * 登陆失败处理
     *
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response)
    {
        log.error("登陆失败，token:" + token + ",error:" + e.getMessage(), e);
        return false;
    }

    @Override
    public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception) throws Exception
    {
        super.afterCompletion(request, response, exception);
        // 清理上下文，这里会引起一个问题，就是如果请求处理逻辑中发起了异步任务，而任务中使用了回话信息
        com.zz.core.auth.ZzUserContextHolder.clear();
        log.debug("会话清理end");
    }

    /**
     * 为response设置header，实现跨域
     */
    private void setHeader(HttpServletRequest request, HttpServletResponse response)
    {
        //跨域的header设置
        response.setHeader("Access-control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", request.getMethod());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
        //防止乱码，适用于传输JSON数据
        response.setHeader(CoreConstant.CONTENT_TYPE, CoreConstant.CONTENT_JSON);
        // this.requestEntity.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }
}
