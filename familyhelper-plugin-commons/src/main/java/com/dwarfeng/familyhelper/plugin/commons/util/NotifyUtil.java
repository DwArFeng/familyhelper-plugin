package com.dwarfeng.familyhelper.plugin.commons.util;

import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * 通知工具类。
 *
 * @author DwArFeng
 * @since 1.5.0
 */
public final class NotifyUtil {

    /**
     * 将指定的内置发送器的占位符映射转换为字符串。
     *
     * @param placeholderMap 指定的占位符映射。
     * @return 指定的内置发送器的占位符映射转换成的字符串。
     */
    public static String stringifyBuiltinSenderPlaceholderMap(Map<String, Object> placeholderMap) {
        return JSON.toJSONString(placeholderMap, false);
    }

    /**
     * 将指定的电子邮件发送器的占位符映射转换为字符串。
     *
     * @param placeholderMap 指定的占位符映射。
     * @return 指定的电子邮件发送器的占位符映射转换成的字符串。
     */
    public static String stringifyEmailSenderPlaceholderMap(Map<String, Object> placeholderMap) {
        return JSON.toJSONString(placeholderMap, false);
    }

    private NotifyUtil() {
        throw new IllegalStateException("禁止实例化");
    }
}
