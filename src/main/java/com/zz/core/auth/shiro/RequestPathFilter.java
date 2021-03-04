package com.zz.core.auth.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

/**
 * 请求路径过滤器
 *
 */
@Slf4j
public class RequestPathFilter implements Filter {

    private static String[] excludePaths;

    private boolean isEnabled;

    public RequestPathFilter(ShiroPathFilterProperties.FilterConfig filterConfig) {
        isEnabled = filterConfig.isEnabled();
        excludePaths = filterConfig.getExcludePaths();
        log.debug("isEnabled:" + isEnabled);
        log.debug("excludePaths:" + Arrays.toString(excludePaths));
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!isEnabled) {
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getServletPath();
        String url = req.getRequestURL().toString();
        PathMatcher pathMatcher = new AntPathMatcher();
        boolean isOut = true;
        if (ArrayUtils.isNotEmpty(excludePaths)) {
            for (String pattern : excludePaths) {
                if (pathMatcher.match(pattern, path)) {
                    isOut = false;
                    break;
                }
            }
        }
        if (isOut) {
            log.debug(url);
        }
        chain.doFilter(req, response);
    }

    @Override
    public void destroy() {

    }
}
