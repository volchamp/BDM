package com.kmvc.jeesite.modules.syncPortal.interceptor;

import javax.servlet.http.*;
import org.springframework.web.servlet.*;

public class ServiceInterceptor implements HandlerInterceptor
{
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        return true;
    }

    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) throws Exception {
    }
}
