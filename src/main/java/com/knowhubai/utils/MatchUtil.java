package com.knowhubai.utils;

/**
 * @author ：何汉叁
 * @date ：2024/4/13 19:34
 * @description：截取url的文件名
 */
public class MatchUtil {
    public static String getFileName(String url) {
        int endIndex = url.contains("?") ? url.indexOf("?") : url.length();
        return url.substring(url.lastIndexOf("/") + 1,endIndex);
    }
}
