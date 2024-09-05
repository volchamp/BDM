package com.kmvc.jeesite.modules.sys.utils;

import net.sourceforge.pinyin4j.*;
import java.util.regex.*;

public class PinyinUtils
{
    public static String getPinYinHeadChar(final String chinese) {
        final StringBuffer pinyin = new StringBuffer();
        if (chinese != null && !chinese.trim().equalsIgnoreCase("")) {
            for (int j = 0; j < chinese.length(); ++j) {
                final char word = chinese.charAt(j);
                final String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
                if (pinyinArray != null) {
                    pinyin.append(pinyinArray[0].charAt(0));
                }
                else {
                    pinyin.append(word);
                }
            }
        }
        return pinyin.toString();
    }

    public static String getPinYinHeadCharFilter(final String chinese) {
        return strFilter(getPinYinHeadChar(chinese));
    }

    public static String strFilter(final String str) throws PatternSyntaxException {
        final String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~\uff01@#\uffe5%\u2026\u2026&*\uff08\uff09\u2014\u2014+|{}\u3010\u3011\u2018\uff1b\uff1a\u201d\u201c\u2019\u3002\uff0c\u3001\uff1f\"]";
        final Pattern p = Pattern.compile(regEx);
        final Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}
