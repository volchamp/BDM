package com.kmvc.jeesite.modules.common;

import com.kmvc.jeesite.modules.sys.entity.TSystem;
import com.thinkgem.jeesite.common.utils.*;

import java.util.*;
import java.util.regex.*;

public class Util
{
    private static PropertiesLoader staticPropLoader;

    public static boolean isMdmModeComplex(final boolean isUseStatic) {
        if (isUseStatic) {
            return Boolean.valueOf(Util.staticPropLoader.getProperty("mdm_mode"));
        }
        final PropertiesLoader propLoader = new PropertiesLoader(new String[] { "jeesite.properties" });
        return Boolean.valueOf(propLoader.getProperty("mdm_mode"));
    }

    public static List<TSystem> getIntersection(final List<String> systemIdList, final List<TSystem> list2) {
        final List<TSystem> result = new ArrayList<TSystem>();
        for (final TSystem s : list2) {
            if (systemIdList.contains(s.getId())) {
                result.add(s);
            }
        }
        return result;
    }

    public static boolean isContainChinese(final String str) {
        final Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        final Matcher m = p.matcher(str);
        return m.find();
    }

    public static boolean isScientific(final String input) {
        final String regx = "^((-?\\d+.?\\d*)[Ee]{1}(-?\\d+))$";
        final Pattern pattern = Pattern.compile(regx);
        return pattern.matcher(input).matches();
    }

    static {
        Util.staticPropLoader = new PropertiesLoader(new String[] { "jeesite.properties" });
    }
}
