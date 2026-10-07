package com.dwarfeng.familyhelper.plugin.audit.handler.inspector.opburst;

import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.subgrade.stack.bean.Bean;

/**
 * 操作频次突发审计器的参数。
 *
 * <p>
 * 该参数是操作频次突发审计器的唯一配置面：报警类型、审计类别、滑动窗口的全部参数，
 * 以及分组属性与阈值，全部由该类的字段承载，插件不提供外置的默认配置文件。
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
public class OperationBurstInspectorConfig implements Bean {

    private static final long serialVersionUID = 983251613685641111L;

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
    private String lastWindowVariableIdRem =
            "存放各分组命中窗口的审计器变量 ID 基名，长度不能超过 99 个字符，实际变量 ID 由基名与分组值拼成。";

    @JSONField(name = "last_window_variable_id", ordinal = 8)
    private String lastWindowVariableId;

    @JSONField(name = "#max_interval", ordinal = 9)
    private String maxIntervalRem = "单次查询区间长度的上限，单位为毫秒，不能小于滑动窗口长度。";

    @JSONField(name = "max_interval", ordinal = 10)
    private long maxInterval;

    @JSONField(name = "#category_id", ordinal = 11)
    private String categoryIdRem = "被统计的审计类别 ID，例如 webapi.rbac.user.insert。";

    @JSONField(name = "category_id", ordinal = 12)
    private String categoryId;

    @JSONField(name = "#group_by_property_id", ordinal = 13)
    private String groupByPropertyIdRem = "用于分桶的审计条目属性 ID，该属性不存在的条目会被排除。";

    @JSONField(name = "group_by_property_id", ordinal = 14)
    private String groupByPropertyId;

    @JSONField(name = "#threshold", ordinal = 15)
    private String thresholdRem = "触发判定的极值阈值，窗口内的最大命中数达到该值时才会进入报警判定。";

    @JSONField(name = "threshold", ordinal = 16)
    private long threshold;

    @JSONField(name = "#show_group_in_message", ordinal = 17)
    private String showGroupInMessageRem = "是否在报警内容中显示分组值，为 false 时以 *** 代替，用于隐去账号等敏感信息。";

    @JSONField(name = "show_group_in_message", ordinal = 18)
    private boolean showGroupInMessage;

    public OperationBurstInspectorConfig() {
    }

    public OperationBurstInspectorConfig(
            String alarmType, long window, String lastQueryVariableId, String lastWindowVariableId, long maxInterval,
            String categoryId, String groupByPropertyId, long threshold, boolean showGroupInMessage
    ) {
        this.alarmType = alarmType;
        this.window = window;
        this.lastQueryVariableId = lastQueryVariableId;
        this.lastWindowVariableId = lastWindowVariableId;
        this.maxInterval = maxInterval;
        this.categoryId = categoryId;
        this.groupByPropertyId = groupByPropertyId;
        this.threshold = threshold;
        this.showGroupInMessage = showGroupInMessage;
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

    public String getGroupByPropertyIdRem() {
        return groupByPropertyIdRem;
    }

    public void setGroupByPropertyIdRem(String groupByPropertyIdRem) {
        this.groupByPropertyIdRem = groupByPropertyIdRem;
    }

    public String getGroupByPropertyId() {
        return groupByPropertyId;
    }

    public void setGroupByPropertyId(String groupByPropertyId) {
        this.groupByPropertyId = groupByPropertyId;
    }

    public String getThresholdRem() {
        return thresholdRem;
    }

    public void setThresholdRem(String thresholdRem) {
        this.thresholdRem = thresholdRem;
    }

    public long getThreshold() {
        return threshold;
    }

    public void setThreshold(long threshold) {
        this.threshold = threshold;
    }

    public String getShowGroupInMessageRem() {
        return showGroupInMessageRem;
    }

    public void setShowGroupInMessageRem(String showGroupInMessageRem) {
        this.showGroupInMessageRem = showGroupInMessageRem;
    }

    public boolean isShowGroupInMessage() {
        return showGroupInMessage;
    }

    public void setShowGroupInMessage(boolean showGroupInMessage) {
        this.showGroupInMessage = showGroupInMessage;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public String toString() {
        return "OperationBurstInspectorConfig{" +
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
                ", groupByPropertyIdRem='" + groupByPropertyIdRem + '\'' +
                ", groupByPropertyId='" + groupByPropertyId + '\'' +
                ", thresholdRem='" + thresholdRem + '\'' +
                ", threshold=" + threshold +
                ", showGroupInMessageRem='" + showGroupInMessageRem + '\'' +
                ", showGroupInMessage=" + showGroupInMessage +
                '}';
    }
}
