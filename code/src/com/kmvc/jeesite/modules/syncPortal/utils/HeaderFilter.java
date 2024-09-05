package com.kmvc.jeesite.modules.syncPortal.utils;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class HeaderFilter implements Filter
{
    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse resp = (HttpServletResponse)response;
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        resp.setHeader("Access-Control-Max-Age", "3600");
        resp.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, client_id, uuid, Authorization");
        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        chain.doFilter(request, (ServletResponse)resp);
    }

    public void destroy() {
    }
}
