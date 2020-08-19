package com.info.baymax.common.queryapi.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExp {

    public static boolean match(String reg, String str) {
        return Pattern.matches(reg, str);
    }

    public static List<String> find(String reg, String str) {
        Matcher matcher = Pattern.compile(reg).matcher(str);
        List<String> list = new ArrayList<String>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    public static List<String> find(String reg, String str, int index) {
        Matcher matcher = Pattern.compile(reg).matcher(str);
        List<String> list = new ArrayList<String>();
        while (matcher.find()) {
            list.add(matcher.group(index));
        }
        return list;
    }

    public static String findString(String reg, String str, int index) {
        String returnStr = null;
        List<String> list = find(reg, str, index);
        if (list.size() != 0)
            returnStr = list.get(0);
        return returnStr;
    }

    public static String findString(String reg, String str) {
        String returnStr = null;
        List<String> list = find(reg, str);
        if (list.size() != 0)
            returnStr = list.get(0);
        return returnStr;
    }

    public static void main(String[] args) {
        System.out.println(RegExp.find("(a)b", "ababab", 1));
    }
}
