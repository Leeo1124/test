//package com.leeo.web.filter;
//
//import java.io.IOException;
//
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.shiro.web.filter.AccessControlFilter;
//import org.apache.shiro.web.util.WebUtils;
//
//import com.leeo.jcaptcha.JCaptcha;
//
///**
// * 验证码过滤器
// */
//public class JCaptchaValidateFilter extends AccessControlFilter {
//
//    private boolean jcaptchaEbabled = true;
//
//    private String jcaptchaParam = "jcaptchaCode";
//
//    private String jcapatchaErrorUrl;
//
//    /**
//     * 是否开启jcaptcha
//     *
//     * @param jcaptchaEbabled
//     */
//    public void setJcaptchaEbabled(boolean jcaptchaEbabled) {
//        this.jcaptchaEbabled = jcaptchaEbabled;
//    }
//
//    /**
//     * 前台传入的验证码
//     *
//     * @param jcaptchaParam
//     */
//    public void setJcaptchaParam(String jcaptchaParam) {
//        this.jcaptchaParam = jcaptchaParam;
//    }
//
//    public void setJcapatchaErrorUrl(String jcapatchaErrorUrl) {
//        this.jcapatchaErrorUrl = jcapatchaErrorUrl;
//    }
//
//    public String getJcapatchaErrorUrl() {
//        return this.jcapatchaErrorUrl;
//    }
//
//    @Override
//    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
//        request.setAttribute("jcaptchaEbabled", this.jcaptchaEbabled);
//        return super.onPreHandle(request, response, mappedValue);
//    }
//
//    @Override
//    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        //验证码禁用 或不是表单提交 允许访问
//        if (this.jcaptchaEbabled == false || !"post".equals(httpServletRequest.getMethod().toLowerCase())) {
//            return true;
//        }
//        return JCaptcha.validateResponse(httpServletRequest, httpServletRequest.getParameter(this.jcaptchaParam));
//    }
//
//
//    @Override
//    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
//        redirectToLogin(request, response);
//        return true;
//    }
//
//
//    @Override
//    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
//        WebUtils.issueRedirect(request, response, getJcapatchaErrorUrl());
//    }
//
//}
