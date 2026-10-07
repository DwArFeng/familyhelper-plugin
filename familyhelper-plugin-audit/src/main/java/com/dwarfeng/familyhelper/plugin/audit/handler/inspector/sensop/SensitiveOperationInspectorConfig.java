package com.dwarfeng.familyhelper.plugin.audit.handler.inspector.sensop;

import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.subgrade.stack.bean.Bean;

/**
 * 敏感操作审计器的参数。
 *
 * <p>
 * 该参数是敏感操作审计器的唯一配置面：报警类型、审计类别、滑动窗口的全部参数，
 * 以及上一次查询时间与命中窗口状态的变量 ID，全部由该类的字段承载，插件不提供外置的默认配置文件。
 *
 * <p>
 * 该审计器不对命中条目做任何分组，命中阈值恒为 1，因此参数面不包含分组属性、阈值、
 * 失败码区间与分组脱敏开关。
 *
 * <p>
 * 字段的 {@link JSONField#ordinal()} 取值与字段的声明顺序一致。
 * 当前使用的 fastjson 在序列化时输出的是声明顺序而非序号顺序，因此调整字段顺序时必须同步调整序号，
 * 否则在升级 fastjson 之后序列化结果的键顺序会发生变化。
 *
 * <p>
 * 每个需要填写的字段都配有一个以 <code>#</code> 为键名前缀的说明字段。说明字段是序列化结果的组成部分，
 * 其取值即该字段的填写说明，因此审计器的参数界面上会直接显示每个参数的用途与约束。说明字段不参与参数校验，也不参与审计逻辑。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class SensitiveOperationInspectorConfig implements Bean {

    private static final long serialVersionUID = 9134371176791484006L;

    @JSONField(name = "#alarm_type", ordinal = 1)
    private String alarmTypeRem = "自动审计报警的类型，长度不能超过 50 个字符。";

    @JSONField(name = "alarm_type", ordinal = 2)
    private String alarmType;

    @JSONField(name = "#window", ordinal = 3)
    private String windowRem = "滑动窗口的长度，单位为毫秒，必须大于 0，同时也是相邻两次查询之间的回退量。";

    @JSONField(name = "window", ordinal = 4)
    private long window;

    @JSONField(name = "#last_query_variable_id", ordinal = 5)
    private String lastQueryVariableIdRem = "存放上一次查询时间的审计器变量 ID，长度不能超过 100 个字符。";

    @JSONField(name = "last_query_variable_id", ordinal = 6)
    private String lastQueryVariableId;

    @JSONField(name = "#last_window_variable_id", ordinal = 7)
    private String lastWindowVariableIdRem = "存放命中窗口状态的审计器变量 ID，长度不能超过 100 个字符。";

    @JSONField(name = "last_window_variable_id", ordinal = 8)
    private String lastWindowVariableId;

    @JSONField(name = "#max_interval", ordinal = 9)
    private String maxIntervalRem = "单次查询区间长度的上限，单位为毫秒，不能小于滑动窗口长度。";

    @JSONField(name = "max_interval", ordinal = 10)
    private long maxInterval;

    @JSONField(name = "#category_id", ordinal = 11)
    private String categoryIdRem = "被统计的审计类别 ID，例如 webapi.system.account.reset_password。";

    @JSONField(name = "category_id", ordinal = 12)
    private String categoryId;

    public SensitiveOperationInspectorConfig() {
    }

    public SensitiveOperationInspectorConfig(
            String alarmType, long window, String lastQueryVariableId, String lastWindowVariableId, long maxInterval,
            String categoryId
    ) {
        this.alarmType = alarmType;
        this.window = window;
        this.lastQueryVariableId = lastQueryVariableId;
        this.lastWindowVariableId = lastWindowVariableId;
        this.maxInterval = maxInterval;
        this.categoryId = categoryId;
    }

    public String getAlarmTypeRem() {
        return alarmTypeRem;
    }

    public void setAlarmTypeRem(String alarmTypeRem) {
        this.alarmTypeRem = alarmTypeRem;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getWindowRem() {
        return windowRem;
    }

    public void setWindowRem(String windowRem) {
        this.windowRem = windowRem;
    }

    public long getWindow() {
        return window;
    }

    public void setWindow(long window) {
        this.window = window;
    }

    public String getLastQueryVariableIdRem() {
        return lastQueryVariableIdRem;
    }

    public void setLastQueryVariableIdRem(String lastQueryVariableIdRem) {
        this.lastQueryVariableIdRem = lastQueryVariableIdRem;
    }

    public String getLastQueryVariableId() {
        return lastQueryVariableId;
    }

    public void setLastQueryVariableId(String lastQueryVariableId) {
        this.lastQueryVariableId = lastQueryVariableId;
    }

    public String getLastWindowVariableIdRem() {
        return lastWindowVariableIdRem;
    }

    public void setLastWindowVariableIdRem(String lastWindowVariableIdRem) {
        this.lastWindowVariableIdRem = lastWindowVariableIdRem;
    }

    public String getLastWindowVariableId() {
        return lastWindowVariableId;
    }

    public void setLastWindowVariableId(String lastWindowVariableId) {
        this.lastWindowVariableId = lastWindowVariableId;
    }

    public String getMaxIntervalRem() {
        return maxIntervalRem;
    }

    public void setMaxIntervalRem(String maxIntervalRem) {
        this.maxIntervalRem = maxIntervalRem;
    }

    public long getMaxInterval() {
        return maxInterval;
    }

    public void setMaxInterval(long maxInterval) {
        this.maxInterval = maxInterval;
    }

    public String getCategoryIdRem() {
        return categoryIdRem;
    }

    public void setCategoryIdRem(String categoryIdRem) {
        this.categoryIdRem = categoryIdRem;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "SensitiveOperationInspectorConfig{" +
                "alarmTypeRem='" + alarmTypeRem + '\'' +
                ", alarmType='" + alarmType + '\'' +
                ", windowRem='" + windowRem + '\'' +
                ", window=" + window +
                ", lastQueryVariableIdRem='" + lastQueryVariableIdRem + '\'' +
                ", lastQueryVariableId='" + lastQueryVariableId + '\'' +
                ", lastWindowVariableIdRem='" + lastWindowVariableIdRem + '\'' +
                ", lastWindowVariableId='" + lastWindowVariableId + '\'' +
                ", maxIntervalRem='" + maxIntervalRem + '\'' +
                ", maxInterval=" + maxInterval +
                ", categoryIdRem='" + categoryIdRem + '\'' +
                ", categoryId='" + categoryId + '\'' +
                '}';
    }
}
