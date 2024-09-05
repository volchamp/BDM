package com.kmvc.jeesite.modules.sys.security;

import com.thinkgem.jeesite.common.utils.StringUtils;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.thinkgem.jeesite.modules.sys.security.UsernamePasswordToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Service;

@Service
public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {
    public static final String DEFAULT_CAPTCHA_PARAM = "validateCode";
    public static final String DEFAULT_MOBILE_PARAM = "mobileLogin";
    public static final String DEFAULT_MESSAGE_PARAM = "message";
    private String captchaParam = "validateCode";
    private String mobileLoginParam = "mobileLogin";
    private String messageParam = "message";

    public FormAuthenticationFilter() {
    }

    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username = this.getUsername(request);
        String password = this.getPassword(request);
        if (password == null) {
            password = "";
        }

        boolean rememberMe = this.isRememberMe(request);
        String host = StringUtils.getRemoteAddr((HttpServletRequest)request);
        String captcha = this.getCaptcha(request);
        boolean mobile = this.isMobileLogin(request);
        return new UsernamePasswordToken(username, password.toCharArray(), rememberMe, host, captcha, mobile);
    }

    public String getCaptchaParam() {
        return this.captchaParam;
    }

    protected String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, this.getCaptchaParam());
    }

    public String getMobileLoginParam() {
        return this.mobileLoginParam;
    }

    protected boolean isMobileLogin(ServletRequest request) {
        return WebUtils.isTrue(request, this.getMobileLoginParam());
    }

    public String getMessageParam() {
        return this.messageParam;
    }

    public String getSuccessUrl() {
        return super.getSuccessUrl();
    }

    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
        WebUtils.issueRedirect(request, response, this.getSuccessUrl(), (Map)null, true);
    }

    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        String className = e.getClass().getName();
        String message = "";
        if (!IncorrectCredentialsException.class.getName().equals(className) && !UnknownAccountException.class.getName().equals(className)) {
            if (e.getMessage() != null && StringUtils.startsWith(e.getMessage(), "msg:")) {
                message = StringUtils.replace(e.getMessage(), "msg:", "");
            } else {
                message = "系统出现点问题，请稍后再试！";
                e.printStackTrace();
            }
        } else {
            message = "用户或密码错误, 请重试.";
        }

        request.setAttribute(this.getFailureKeyAttribute(), className);
        request.setAttribute(this.getMessageParam(), message);
        return true;
    }
}
