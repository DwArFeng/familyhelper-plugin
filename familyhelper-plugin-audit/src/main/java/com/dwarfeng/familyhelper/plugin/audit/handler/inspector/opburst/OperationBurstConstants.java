package com.dwarfeng.familyhelper.plugin.audit.handler.inspector.opburst;

/**
 * 操作频次突发审计器常量。
 *
 * <p>
 * 该类集中定义操作频次突发审计器在注册、参数校验与审计执行三个阶段共同使用的常量，
 * 包括变量 ID 的分隔符、结果集截断报警的类型后缀以及报警内容中的时间格式。
 *
 * <p>
 * 数据库字段长度之类的约束不在此定义，一律直接引用 audit SDK 的 {@link com.dwarfeng.audit.sdk.util.Constraints}。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public final class OperationBurstConstants {

    /**
     * 分组变量的变量 ID 中，配置基名与分组值之间的分隔符。
     *
     * <p>
     * 校验阶段按 <code>字段长度上限 - 分隔符长度</code> 计算基名允许的最大长度，
     * 执行阶段按 <code>基名 + 分隔符 + 分组值</code> 拼接变量 ID，两处必须取到同一个值。
     */
    static final String VARIABLE_ID_SEPARATOR = ".";

    /**
     * 报警内容中时间文本的格式。
     *
     * <p>
     * 用于在报警内容中展示命中窗口的起止时刻，采用与区域设置无关的固定格式。
     */
    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    private OperationBurstConstants() {
        throw new IllegalStateException("禁止实例化");
    }
}
