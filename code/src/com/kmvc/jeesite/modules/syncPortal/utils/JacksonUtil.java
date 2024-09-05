package com.kmvc.jeesite.modules.syncPortal.utils;

import org.apache.log4j.*;
import org.codehaus.jackson.map.*;
import java.io.*;
import java.util.*;
import org.codehaus.jackson.type.*;
import org.codehaus.jackson.*;

public class JacksonUtil
{
    private static final Logger Log;
    private static final ObjectMapper Mapper;

    public static String fromObject(final Object object) {
        final StringWriter writer = new StringWriter();
        try {
            JacksonUtil.Mapper.writeValue((Writer)writer, object);
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e2) {
            throw new RuntimeException(e2);
        }
        final String json = writer.toString();
        JacksonUtil.Log.debug((Object)("\u8f6c\u540e\u7684json\u5b57\u7b26\u4e32\u4e3a\uff1a" + json));
        return json;
    }

    public static <T> T toBean(String json, final Class<T> clazz) {
        T object = null;
        try {
            if (json == null) {
                return null;
            }
            json = json.replace("'null'", "null");
            json = json.replace("\"null\"", "null");
            object = (T)JacksonUtil.Mapper.readValue(json, (Class)clazz);
            JacksonUtil.Log.info((Object)("\u5e73\u53f0\u4f20\u9012\u7684json\u4e3a\uff1a" + json));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return object;
    }

    public static <T> List<T> toListBean(String jsonStr, final Class<T> clazz) {
        List object = null;
        try {
            if (jsonStr == null) {
                return null;
            }
            jsonStr = jsonStr.replace("'null'", "null");
            jsonStr = jsonStr.replace("\"null\"", "null");
            final JavaType javaType = JacksonUtil.Mapper.getTypeFactory().constructParametricType((Class)ArrayList.class, new Class[] { clazz });
            object = (List)JacksonUtil.Mapper.readValue(jsonStr, javaType);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return (List<T>)object;
    }

    static {
        Log = Logger.getLogger((Class)JacksonUtil.class);
        (Mapper = new ObjectMapper()).configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        JacksonUtil.Mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    }
}
