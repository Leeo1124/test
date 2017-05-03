package com.leeo.web.interceptors;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 权限拦截器
 * 
 * @author Leeo
 */
public class AuthInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory
        .getLogger(AuthInterceptor.class);

    private List<String> excludeUrls;

    private final PathMatcher pathMatcher = new AntPathMatcher();

    private String[] excludeUrlPatterns = null;

    public void setExcludeUrlPatterns(final String[] excludeUrlPatterns) {
        this.excludeUrlPatterns = excludeUrlPatterns;
    }

    public void setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }

    @Override
    public void afterCompletion(HttpServletRequest arg0,
            HttpServletResponse arg1, Object arg2, Exception arg3) {
        logger.info("----controller执行完成后拦截----");
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
            Object arg2, ModelAndView arg3) {
        logger.info("----controller请求后拦截----");
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object arg2) {
        logger.info("----controller请求前拦截----");
        logger.info("----excludeUrls: " + this.excludeUrls);
        logger.info("----excludeUrlPatterns: " + Arrays.toString(this.excludeUrlPatterns));
//        if(this.excludeUrls.contains(getRequestPath(request))){
//            return true;
//        }
        if (isExclude(request)) {
            return true;
        }
        HttpSession session = request.getSession();
        if(null != session && null != session.getAttribute("user")){
            return true;
        }

        return false;
    }

    private boolean isExclude(final HttpServletRequest request) {
        logger.info("---Request Path: "+request.getServletPath());
        if (this.excludeUrlPatterns == null) {
            return false;
        }
        for (String pattern : this.excludeUrlPatterns) {
            if (this.pathMatcher.match(pattern, request.getServletPath())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获得请求路径
     * 
     * @param request
     * @return
     */
    public static String getRequestPath(HttpServletRequest request) {
        String requestPath = request.getRequestURI() + "?"
            + request.getQueryString();
        if (requestPath.indexOf("&") > -1) {// 去掉其他参数
            requestPath = requestPath.substring(0, requestPath.indexOf("&"));
        }
        requestPath = requestPath
            .substring(request.getContextPath().length() + 1);// 去掉项目路径
        return requestPath;
    }

}
