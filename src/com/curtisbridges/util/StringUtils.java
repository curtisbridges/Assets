package com.curtisbridges.util;

public class StringUtils {
    public static boolean stringContainsSubstring(String string, String substring) {
        return string.indexOf(substring) >= 0;
    }
}
