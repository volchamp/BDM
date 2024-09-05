package com.kmvc.jeesite.modules.syncPortal.utils;

import org.apache.commons.codec.binary.*;

public class Base64Util
{
    public static String decode(final String key) {
        return new String(Base64.decodeBase64(key));
    }

    public static String encode(final String key) throws Exception {
        return new String(Base64.encodeBase64(key.getBytes()));
    }

    public static void main(final String[] args) throws Exception {
    }
}
