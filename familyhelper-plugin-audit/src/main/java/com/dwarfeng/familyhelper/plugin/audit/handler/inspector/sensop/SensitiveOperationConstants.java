package com.dwarfeng.familyhelper.plugin.audit.handler.inspector.sensop;

/**
 * 敏感操作审计器常量。
 *
 * <p>
 * 该类集中定义敏感操作审计器在注册、参数校验与审计执行三个阶段共同使用的常量，
 * 包括结果集截断报警的类型后缀以及报警内容中的时间格式。
 *
 * <p>
 * 数据库字段长度之类的约束不在此定义，一律直接引用 audit SDK 的 {@link com.dwarfeng.audit.sdk.util.Constraints}。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public final class SensitiveOperationConstants {

    /**
     * 报警内容中时间文本的格式。
     *
     * <p>
     * 用于在报警内容中展示命中窗口的起止时刻，采用与区域设置无关的固定格式。
     */
    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    private SensitiveOperationConstants() {
        throw new IllegalStateException("禁止实例化");
    }
}
