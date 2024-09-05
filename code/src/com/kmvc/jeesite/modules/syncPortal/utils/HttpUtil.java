package com.kmvc.jeesite.modules.syncPortal.utils;

import org.springframework.web.client.*;
import org.springframework.http.client.*;
import java.util.*;
import javax.servlet.http.*;

public class HttpUtil
{
    public static final int CONNECT_TIME_OUT = -1;
    public static final int READ_TIME_OUT = -1;
    public static final String CHART_SET = "utf-8";
    public static final String DEFAULT_CHARSET = "ISO-8859-1";
    private static RestTemplate restTemplate;
    private static final Object Lock;

    private static RestTemplate getSimpleRestTemplate() {
        if (HttpUtil.restTemplate == null) {
            synchronized (HttpUtil.Lock) {
                if (HttpUtil.restTemplate == null) {
                    final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
                    requestFactory.setConnectTimeout(-1);
                    requestFactory.setReadTimeout(-1);
                    HttpUtil.restTemplate = new RestTemplate((ClientHttpRequestFactory)requestFactory);
                }
            }
        }
        return HttpUtil.restTemplate;
    }

    public static String doPost(final String url, final Map<String, String> urlVariables) {
        final RestTemplate restTemplate1 = getSimpleRestTemplate();
        String result = (String)restTemplate1.postForObject(url, (Object)null, (Class)String.class, (Map)urlVariables);
        try {
            if (result != null) {
                result = filter(result);
                return result;
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static String doGet(final String url, final Map<String, String> urlVariables) {
        final RestTemplate restTemplate1 = getSimpleRestTemplate();
        String result;
        if (urlVariables == null) {
            result = (String)restTemplate1.getForObject(url, (Class)String.class, new Object[0]);
        }
        else {
            result = (String)restTemplate1.getForObject(url, (Class)String.class, (Map)urlVariables);
        }
        try {
            if (result != null) {
                result = filter(result);
                return result;
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static <T> T doGetAndToBean(final String url, final Map<String, String> urlVariables, final Class<T> clazz) {
        final String json = doGet(url, urlVariables);
        return JacksonUtil.toBean(json, clazz);
    }

    public static <T> List<T> doGetAndToListBean(final String url, final Map<String, String> urlVariables, final Class<T> clazz) {
        System.out.println("HttpUtil doGetAndToListBean:" + DateUtil.getFormatterValue(new Date(), "yyyy-MM-dd HH:mm:ss") + "URL:" + url);
        final String json = doGet(url, urlVariables);
        System.out.println("HttpUtil doGetAndToListBean:" + DateUtil.getFormatterValue(new Date(), "yyyy-MM-dd HH:mm:ss") + "json:" + json);
        return JacksonUtil.toListBean(json, clazz);
    }

    public static <T> List<T> doPostAndToListBean(final String url, final Map<String, String> urlVariables, final Class<T> clazz) {
        final String json = doPost(url, urlVariables);
        return JacksonUtil.toListBean(json, clazz);
    }

    public static <T> T doPostAndToBean(final String url, final Map<String, String> urlVariables, final Class<T> clazz) {
        final String json = doPost(url, urlVariables);
        return JacksonUtil.toBean(json, clazz);
    }

    private static String filter(String result) {
        if (result == null) {
            return result;
        }
        if (result.startsWith("\"")) {
            result = new StringBuffer(result).deleteCharAt(0).toString();
        }
        if (result.endsWith("\"")) {
            result = new StringBuffer(result).deleteCharAt(result.length() - 1).toString();
        }
        return result;
    }

    public static String allPathForWeb(final HttpServletRequest request) {
        final StringBuffer url = request.getRequestURL();
        final String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append(request.getContextPath()).toString();
        return tempContextUrl;
    }

    static {
        HttpUtil.restTemplate = null;
        Lock = new Object();
    }
}
