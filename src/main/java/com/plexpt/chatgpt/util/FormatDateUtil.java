package com.plexpt.chatgpt.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatDateUtil {
    /**
     * 使用SimpleDateFormat格式化日期为yyyy-MM-dd，每次新建对象避免线程安全问题
     * @param date 日期
     * @return yyyy-MM-dd格式的日期字符串对象
     */
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(date);
    }
}
