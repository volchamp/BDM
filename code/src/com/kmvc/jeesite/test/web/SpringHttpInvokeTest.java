package com.kmvc.jeesite.test.web;

import org.springframework.remoting.httpinvoker.*;
import com.thinkgem.jeesite.modules.sys.service.*;

public class SpringHttpInvokeTest
{
    public static void main(final String[] args) {
        final HttpInvokerProxyFactoryBean bean = new HttpInvokerProxyFactoryBean();
        bean.setServiceUrl("http://localhost:8080/dgbps/remoting/SyncService");
        bean.setServiceInterface((Class)ISynDataService.class);
        bean.afterPropertiesSet();
        System.out.println("\u8fdc\u7a0bBean\u8c03\u7528\u5b8c\u6210...");
    }
}
