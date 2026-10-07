package com.dwarfeng.familyhelper.plugin.audit.handler.inspector.sensop;

import com.alibaba.fastjson.JSON;
import com.dwarfeng.audit.sdk.handler.inspector.AbstractInspectorRegistry;
import com.dwarfeng.audit.sdk.util.Constraints;
import com.dwarfeng.audit.stack.exception.InspectorException;
import com.dwarfeng.audit.stack.exception.InspectorMakeException;
import com.dwarfeng.audit.stack.handler.Inspector;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 敏感操作审计器注册。
 *
 * <p>
 * 该注册提供的审计器在滑动窗口内统计指定审计类别的全部审计条目，命中阈值恒为 1，
 * 只要窗口内出现任意命中就进入报警判定；判定时与审计器变量中保存的上一次命中窗口比较，
 * 在时间不重叠或极值创新高时创建自动审计报警。查询区间由上一次查询时间向前回退一个窗口长度后推进到当前时刻，
 * 因此相邻两次执行之间天然重叠一个窗口，跨执行边界的滑动窗口不需要额外维护历史数据。
 *
 * <p>
 * 该审计器不对命中条目做任何分组，因此查询只施加审计类别等值条件与创建时间区间条件。
 *
 * <p>
 * 该审计器只使用宿主提供的 {@link Inspector.Context} 能力：查询审计条目、读写审计器变量、创建自动审计报警。
 * 它不消费任何远程服务，也不访问被审计系统自身的数据库。
 *
 * <p>
 * <b>时间单位约定</b>：该审计器的全部时间参数以及审计器变量中存放的全部时间值，一律以毫秒为单位。
 *
 * <p>
 * 所有参数校验都在 {@link #makeInspector(String, String)} 中完成。该校验时机与宿主的调用时机一致：
 * 宿主在任务启动前调用该方法，因此配置错误会立即体现为自动审计任务失败，并携带参数原文，
 * 便于在自动审计任务事件中定位问题。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
@Component
public class SensitiveOperationInspectorRegistry extends AbstractInspectorRegistry {

    /**
     * 敏感操作审计器的类型标识。
     *
     * <p>
     * 该取值是宿主识别审计器实现的唯一依据，会被写入自动审计任务的类型字段，因此一经发布不得变更；
     * 变更该取值等同于丢弃历史上所有以该类型存储的审计器信息。
     */
    public static final String INSPECTOR_TYPE = "sensitive_operation_inspector";

    private final ApplicationContext ctx;

    public SensitiveOperationInspectorRegistry(ApplicationContext ctx) {
        super(INSPECTOR_TYPE);
        this.ctx = ctx;
    }

    @Override
    public String provideLabel() {
        return "敏感操作审计器";
    }

    @Override
    public String provideDescription() {
        return "在滑动窗口内统计指定审计类别的全部审计条目, 命中阈值恒为 1, " +
                "窗口极值与上一次命中窗口不重叠或创出新高时创建自动审计报警。时间参数以毫秒为单位。";
    }

    @Override
    public String provideExampleParam() {
        SensitiveOperationInspectorConfig config = new SensitiveOperationInspectorConfig(
                "sensitive_operation",
                300000L,
                "soi.last_query",
                "soi.last_window",
                86400000L,
                "webapi.system.account.reset_password"
        );
        return JSON.toJSONString(config, false);
    }

    @Override
    public Inspector makeInspector(String type, String param) throws InspectorException {
        try {
            // 通过 param 生成审计器的参数。
            SensitiveOperationInspectorConfig config = JSON.parseObject(param, SensitiveOperationInspectorConfig.class);
            // 校验参数。
            validateParam(config);
            // 通过 ctx 生成审计器。
            return ctx.getBean(SensitiveOperationInspector.class, ctx, config);
        } catch (Exception e) {
            throw new InspectorMakeException("构造敏感操作审计器失败, 类型: " + type + ", 参数: " + param, e);
        }
    }

    /**
     * 校验审计器参数。
     *
     * <p>
     * 校验内容包括各字段的非空、长度与大小关系约束。该审计器不做分组，命中阈值恒为 1，
     * 因此不涉及分组属性、阈值、失败码区间与分组脱敏开关的校验。
     *
     * @param config 审计器参数。
     * @throws IllegalArgumentException 参数不合法时抛出。
     */
    private void validateParam(SensitiveOperationInspectorConfig config) {
        if (Objects.isNull(config)) {
            throw new IllegalArgumentException("敏感操作审计器参数不能为空");
        }
        if (Objects.isNull(config.getAlarmType()) || config.getAlarmType().trim().isEmpty()) {
            throw new IllegalArgumentException("敏感操作审计器配置项 alarm_type 不能为空");
        }
        if (config.getAlarmType().length() > Constraints.LENGTH_TYPE) {
            throw new IllegalArgumentException(
                    "敏感操作审计器配置项 alarm_type 的长度不能超过 " + Constraints.LENGTH_TYPE
                            + ": " + config.getAlarmType()
            );
        }
        if (config.getWindow() < 1L) {
            throw new IllegalArgumentException("敏感操作审计器配置项 window 必须大于 0: " + config.getWindow());
        }
        if (Objects.isNull(config.getLastQueryVariableId()) || config.getLastQueryVariableId().trim().isEmpty()) {
            throw new IllegalArgumentException("敏感操作审计器配置项 last_query_variable_id 不能为空");
        }
        if (config.getLastQueryVariableId().length() > Constraints.LENGTH_STRING_ID) {
            throw new IllegalArgumentException(
                    "敏感操作审计器配置项 last_query_variable_id 的长度不能超过 "
                            + Constraints.LENGTH_STRING_ID + ": " + config.getLastQueryVariableId()
            );
        }
        if (Objects.isNull(config.getLastWindowVariableId()) || config.getLastWindowVariableId().trim().isEmpty()) {
            throw new IllegalArgumentException("敏感操作审计器配置项 last_window_variable_id 不能为空");
        }
        if (config.getLastWindowVariableId().length() > Constraints.LENGTH_STRING_ID) {
            throw new IllegalArgumentException(
                    "敏感操作审计器配置项 last_window_variable_id 的长度不能超过 "
                            + Constraints.LENGTH_STRING_ID + ": " + config.getLastWindowVariableId()
            );
        }
        if (config.getMaxInterval() < config.getWindow()) {
            throw new IllegalArgumentException(
                    "敏感操作审计器配置项 max_interval 不能小于 window: "
                            + config.getMaxInterval() + " < " + config.getWindow()
            );
        }
        if (Objects.isNull(config.getCategoryId()) || config.getCategoryId().trim().isEmpty()) {
            throw new IllegalArgumentException("敏感操作审计器配置项 category_id 不能为空");
        }
    }

    @Override
    public String toString() {
        return "SensitiveOperationInspectorRegistry{" +
                "ctx=" + ctx +
                ", inspectorType='" + inspectorType + '\'' +
                '}';
    }
}
