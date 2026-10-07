package com.dwarfeng.familyhelper.plugin.audit.handler.inspector.acctloginfail;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.audit.sdk.handler.inspector.AbstractExecutor;
import com.dwarfeng.audit.sdk.util.Constants;
import com.dwarfeng.audit.sdk.util.Constraints;
import com.dwarfeng.audit.stack.bean.dto.*;
import com.dwarfeng.audit.stack.bean.entity.AuditEntryProperty;
import com.dwarfeng.audit.stack.exception.InspectorExecutionException;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 账号登录失败审计器执行器。
 *
 * <p>
 * 该执行器在滑动窗口内统计指定审计类别中符合失败码区间的审计条目，按照配置的分组属性分桶，
 * 取每个桶在窗口内的最大命中数作为极值，并在极值达到阈值时创建自动审计报警。
 *
 * <p>
 * 执行流程分为五步：
 * <ol>
 *     <li>读取审计器变量中的上一次查询时间。变量不存在时按首次执行处理，写入当前时刻和首次执行简报后直接结束。</li>
 *     <li>把查询区间的起点取为"上一次查询时间向前回退一个窗口长度"与"当前时刻向前回退查询区间上限"的较大者，
 *     终点取为当前时刻。回退一个窗口长度使相邻两次执行之间重叠一个窗口，
 *     跨执行边界的滑动窗口因此不需要在变量中额外维护历史数据；查询区间上限则限制单次执行拉取的数据量。</li>
 *     <li>在查询区间内查询命中条目，按分组属性分桶，对每个桶的时间戳求长度为窗口的滑动窗口最大命中数，
 *     即该桶的极值，同时记录取到极值的那个窗口。</li>
 *     <li>极值低于阈值时不创建报警；达到阈值时，与变量中保存的上一次命中窗口按时间是否重叠来判断是否属于同一事件：
 *     不重叠说明是新事件，直接报警并覆盖历史；重叠说明是同一事件的延续，只有极值创出新高才报警并更新历史，
 *     否则完全静默。</li>
 *     <li>把审计器变量中的上一次查询时间推进到本次查询的终点，并把查询区间、命中数、有效分组数、
 *     达到阈值的分组数与报警次数写入任务锚点消息和任务事件。</li>
 * </ol>
 *
 * <p>
 * 上一次查询时间在查询成功之后、报警判定之前推进：查询抛出的异常向上抛出，该段数据在下一次执行时
 * 被完整重取；报警创建失败不影响该段数据已被消费的事实。任务简报在所有正常结束路径上写入，
 * 包括首次执行、区间内没有命中以及极值未达到阈值的情况。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
@Component("accountLoginFailureInspectorRegistry.accountLoginFailureExecutor")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AccountLoginFailureExecutor extends AbstractExecutor {

    private final AccountLoginFailureInspectorConfig config;

    public AccountLoginFailureExecutor(AccountLoginFailureInspectorConfig config) {
        this.config = config;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void inspect() throws Exception {
        try {
            // 读取上一次查询时间。变量不存在说明这是首次执行。
            Long lastQuery = readLongVariable(config.getLastQueryVariableId());
            if (Objects.isNull(lastQuery)) {
                long currentTimeMillis = System.currentTimeMillis();
                upsertLongVariable(config.getLastQueryVariableId(), currentTimeMillis);
                writeInspectionSummary("账号登录失败检查完成：首次执行，初始化查询时间，未执行审计。");
                return;
            }

            // 计算查询区间。起点同时受窗口回退量与查询区间上限约束。
            long currentTimeMillis = System.currentTimeMillis();
            long startTimeMillis = Math.max(
                    lastQuery - config.getWindow(), currentTimeMillis - config.getMaxInterval()
            );
            Date startCreatedDate = new Date(startTimeMillis);
            Date endCreatedDate = new Date(currentTimeMillis);

            // 执行组合查询。
            AuditEntryLookupResult lookupResult = context.lookupComposite(
                    buildLookupInfo(startCreatedDate, endCreatedDate)
            );
            List<AuditEntryLookupResult.Data> dataList =
                    Objects.isNull(lookupResult.getData()) ? Collections.emptyList() : lookupResult.getData();

            // 成功查询后推进上一次查询时间。该动作位于报警判定之前。
            upsertLongVariable(config.getLastQueryVariableId(), currentTimeMillis);

            // 按分组属性分桶，收集每个分组的命中时刻。
            Map<String, List<Long>> timestampMap = new LinkedHashMap<>();
            for (AuditEntryLookupResult.Data data : dataList) {
                Map<String, AuditEntryProperty> propertyMap = data.getAuditEntryPropertyMap();
                if (Objects.isNull(propertyMap) || !propertyMap.containsKey(config.getGroupByPropertyId())) {
                    continue;
                }
                String groupValue = toGroupValue(propertyMap.get(config.getGroupByPropertyId()));
                if (Objects.isNull(groupValue)) {
                    continue;
                }
                if (Objects.isNull(data.getAuditEntry()) || Objects.isNull(data.getAuditEntry().getCreatedDate())) {
                    continue;
                }
                timestampMap.computeIfAbsent(groupValue, key -> new ArrayList<>())
                        .add(data.getAuditEntry().getCreatedDate().getTime());
            }

            // 逐个分组计算滑动窗口极值，并按照判定规则决定是否报警。
            int thresholdGroupCount = 0;
            int alarmCount = 0;
            for (Map.Entry<String, List<Long>> entry : timestampMap.entrySet()) {
                String groupValue = entry.getKey();
                List<Long> timestamps = entry.getValue();
                Collections.sort(timestamps);
                LongWindow peakWindow = computePeakWindow(timestamps);
                if (Objects.isNull(peakWindow) || peakWindow.getHitCount() < config.getThreshold()) {
                    continue;
                }
                thresholdGroupCount++;
                if (processGroup(groupValue, peakWindow)) {
                    alarmCount++;
                }
            }

            writeInspectionSummary(buildSummary(
                    startCreatedDate, endCreatedDate, dataList.size(), timestampMap.size(),
                    thresholdGroupCount, alarmCount
            ));
        } catch (Exception e) {
            throw new InspectorExecutionException(e);
        }
    }

    /**
     * 构造组合查询信息。
     *
     * <p>
     * 查询项中的每个属性条件对应一个独立的 EXISTS 子查询，子查询之间为与关系，因此列表中的两项依次表达
     * "分组属性存在"与"失败码落在失败码区间"。创建时间区间不由属性条件表达，而是由查询信息的起止字段表达：
     * 宿主据此对审计条目实体自身的 <code>createdDate</code> 施加区间条件，而 <code>createdDate</code>
     * 并不是审计条目属性，写成属性条件会得到一个永远为空的查询。
     *
     * @param startCreatedDate 查询区间的起始时刻。
     * @param endCreatedDate   查询区间的结束时刻。
     * @return 组合查询信息。
     */
    private AuditEntryCompositeLookupInfo buildLookupInfo(Date startCreatedDate, Date endCreatedDate) {
        List<AuditEntryCompositeLookupInfo.CompositeItem> compositeItems = new ArrayList<>();
        // 失败码区间条件。该条件使用 BETWEEN 语义，区间为闭区间。
        compositeItems.add(new AuditEntryCompositeLookupInfo.CompositeItem(
                config.getCodePropertyId(), Constants.PROPERTY_TYPE_LONG,
                config.getCodeFirst(), config.getCodeSecond(), true
        ));
        return new AuditEntryCompositeLookupInfo(
                new PagingInfo(0, Integer.MAX_VALUE), new StringIdKey(config.getCategoryId()), null,
                startCreatedDate, endCreatedDate, compositeItems
        );
    }

    /**
     * 计算滑动窗口极值。
     *
     * <p>
     * 输入的时间戳必须已经升序排列。算法使用双指针：右指针逐个推进，左指针在窗口左边界越过当前右指针所指时刻
     * 减去窗口长度时推进。两个指针都只前进，因此算法的时间复杂度与命中数量成线性关系。
     *
     * @param timestamps 升序排列的命中时刻。
     * @return 命中数最大的窗口，命中数量为 0 时返回 <code>null</code>。
     */
    @SuppressWarnings("DuplicatedCode")
    private LongWindow computePeakWindow(List<Long> timestamps) {
        if (timestamps.isEmpty()) {
            return null;
        }
        LongWindow peak = null;
        int left = 0;
        for (int right = 0; right < timestamps.size(); right++) {
            long rightTime = timestamps.get(right);
            while (timestamps.get(left) < rightTime - config.getWindow()) {
                left++;
            }
            long hitCount = right - left + 1L;
            if (Objects.isNull(peak) || hitCount > peak.getHitCount()) {
                peak = new LongWindow(timestamps.get(left), rightTime, hitCount);
            }
        }
        return peak;
    }

    /**
     * 处理单个分组。
     *
     * <p>
     * 判定规则为：分组没有历史命中窗口时直接报警并覆盖历史；有历史命中窗口时，
     * 若本次命中窗口与历史窗口在时间上不重叠，说明是新事件，报警并覆盖历史；
     * 若两者重叠，说明是同一事件的延续，只有本次极值大于历史极值才报警并更新历史，否则完全静默。
     *
     * @param groupValue 分组值。
     * @param peakWindow 本次命中窗口。
     * @return 本次处理是否创建了报警。
     * @throws Exception 方法执行过程中发生的任何异常。
     */
    @SuppressWarnings("DuplicatedCode")
    private boolean processGroup(String groupValue, LongWindow peakWindow) throws Exception {
        String variableId = buildGroupVariableId(config.getLastWindowVariableId(), groupValue);
        LastWindowState state = readLastWindowState(variableId);
        if (Objects.isNull(state)) {
            createAlarm(config.getAlarmType(), buildMessage(groupValue, peakWindow));
            upsertLastWindowState(variableId, peakWindow, peakWindow.getHitCount());
            return true;
        }
        boolean overlapped = peakWindow.getStartPeriod() <= state.getEndPeriod()
                && state.getStartPeriod() <= peakWindow.getEndPeriod();
        if (!overlapped) {
            createAlarm(config.getAlarmType(), buildMessage(groupValue, peakWindow));
            upsertLastWindowState(variableId, peakWindow, peakWindow.getHitCount());
            return true;
        }
        if (peakWindow.getHitCount() > state.getExtreme()) {
            createAlarm(config.getAlarmType(), buildMessage(groupValue, peakWindow));
            upsertLastWindowState(variableId, peakWindow, peakWindow.getHitCount());
            return true;
        }
        return false;
    }

    /**
     * 读取指定分组的命中窗口状态。
     *
     * <p>
     * 变量不存在、内容不是合法的 JSON、或者解析结果缺少必要字段时，一律按"没有历史状态"处理。
     * 该容错保证一条脏变量不会让整条巡检任务持续失败。
     *
     * @param variableId 分组变量的变量 ID。
     * @return 命中窗口状态，不存在时返回 <code>null</code>。
     * @throws Exception 方法执行过程中发生的任何异常。
     */
    @SuppressWarnings("DuplicatedCode")
    private LastWindowState readLastWindowState(String variableId) throws Exception {
        String text = readStringVariable(variableId);
        if (Objects.isNull(text) || text.trim().isEmpty()) {
            return null;
        }
        try {
            LastWindowState state = JSON.parseObject(text, LastWindowState.class);
            if (Objects.isNull(state)) {
                return null;
            }
            if (state.getStartPeriod() <= 0L || state.getEndPeriod() < state.getStartPeriod()) {
                return null;
            }
            return state;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 写入指定分组的命中窗口状态。
     *
     * @param variableId 分组变量的变量 ID。
     * @param peakWindow 本次命中窗口。
     * @param extreme    本次写入的极值。
     * @throws Exception 方法执行过程中发生的任何异常。
     */
    private void upsertLastWindowState(String variableId, LongWindow peakWindow, long extreme) throws Exception {
        LastWindowState state = new LastWindowState(
                peakWindow.getStartPeriod(), peakWindow.getEndPeriod(), extreme, peakWindow.getHitCount()
        );
        upsertStringVariable(variableId, JSON.toJSONString(state, false));
    }

    /**
     * 构造报警内容。
     *
     * @param groupValue 分组值。
     * @param peakWindow 本次命中窗口。
     * @return 报警内容。
     */
    private String buildMessage(String groupValue, LongWindow peakWindow) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(AccountLoginFailureConstants.DATE_FORMAT);
        return String.format(
                "账号登录失败命中: %s在 %s 至 %s 内的失败次数为 %s, 超过阈值 %s。",
                buildGroupClause(groupValue),
                dateFormat.format(new Date(peakWindow.getStartPeriod())),
                dateFormat.format(new Date(peakWindow.getEndPeriod())),
                peakWindow.getHitCount(), config.getThreshold()
        );
    }

    /**
     * 构造报警内容中的分组子句。
     *
     * <p>
     * 分组值可能是账号等敏感信息，配置允许在报警内容中隐去该值。
     *
     * @param groupValue 分组值。
     * @return 分组子句。
     */
    private String buildGroupClause(String groupValue) {
        if (!config.isShowGroupInMessage()) {
            return "分组 *** ";
        }
        return "分组 " + groupValue + " ";
    }

    /**
     * 查看指定审计器变量的整数值。
     *
     * <p>
     * 变量不存在，或者变量存在但取值不是数字时，返回 <code>null</code>，由调用方按"无历史状态"处理。
     *
     * @param variableId 审计器变量 ID。
     * @return 审计器变量的整数值，不存在时返回 <code>null</code>。
     * @throws Exception 方法执行过程中发生的任何异常。
     */
    @SuppressWarnings("DuplicatedCode")
    private Long readLongVariable(String variableId) throws Exception {
        InspectorVariableInspectResult result = context.inspectInspectorVariable(
                new InspectorVariableInspectInfo(context.getInspectorInfo().getKey(), variableId)
        );
        if (Objects.isNull(result) || Objects.isNull(result.getValue())) {
            return null;
        }
        if (!(result.getValue() instanceof Number)) {
            return null;
        }
        return ((Number) result.getValue()).longValue();
    }

    /**
     * 插入或更新指定审计器变量的整数值。
     *
     * @param variableId 审计器变量 ID。
     * @param value      整数值。
     * @throws Exception 方法执行过程中发生的任何异常。
     */
    private void upsertLongVariable(String variableId, long value) throws Exception {
        context.upsertInspectorVariable(new InspectorVariableUpsertInfo(
                context.getInspectorInfo().getKey(), variableId,
                Constants.INSPECTOR_VARIABLE_VALUE_TYPE_LONG, value
        ));
    }

    /**
     * 查看指定审计器变量的字符串值。
     *
     * <p>
     * 变量不存在，或者变量存在但取值不是字符串时，返回 <code>null</code>。
     *
     * @param variableId 审计器变量 ID。
     * @return 审计器变量的字符串值，不存在时返回 <code>null</code>。
     * @throws Exception 方法执行过程中发生的任何异常。
     */
    @SuppressWarnings("DuplicatedCode")
    private String readStringVariable(String variableId) throws Exception {
        InspectorVariableInspectResult result = context.inspectInspectorVariable(
                new InspectorVariableInspectInfo(context.getInspectorInfo().getKey(), variableId)
        );
        if (Objects.isNull(result) || Objects.isNull(result.getValue())) {
            return null;
        }
        if (!(result.getValue() instanceof String)) {
            return null;
        }
        return (String) result.getValue();
    }

    /**
     * 插入或更新指定审计器变量的字符串值。
     *
     * @param variableId 审计器变量 ID。
     * @param value      字符串值。
     * @throws Exception 方法执行过程中发生的任何异常。
     */
    private void upsertStringVariable(String variableId, String value) throws Exception {
        context.upsertInspectorVariable(new InspectorVariableUpsertInfo(
                context.getInspectorInfo().getKey(), variableId,
                Constants.INSPECTOR_VARIABLE_VALUE_TYPE_STRING, value
        ));
    }

    /**
     * 构造分组变量的变量 ID。
     *
     * <p>
     * 变量 ID 由配置的基名、{@link AccountLoginFailureConstants#VARIABLE_ID_SEPARATOR} 与分组值依次拼接而成。
     * 拼接结果的长度超过 {@link Constraints#LENGTH_STRING_ID} 时抛出 {@link IllegalStateException}，
     * 不做截断，也不做替代性改写。
     *
     * @param baseVariableId 配置的分组变量基名。
     * @param groupValue     分组值。
     * @return 分组变量的变量 ID。
     */
    private String buildGroupVariableId(String baseVariableId, String groupValue) {
        String variableId =
                baseVariableId + AccountLoginFailureConstants.VARIABLE_ID_SEPARATOR + groupValue;
        if (variableId.length() > Constraints.LENGTH_STRING_ID) {
            throw new IllegalStateException(
                    "分组变量的 ID 长度为 " + variableId.length() + ", 超过上限 "
                            + Constraints.LENGTH_STRING_ID + ", 请缩短分组窗口变量的 ID 基名: " + variableId
            );
        }
        return variableId;
    }

    /**
     * 创建自动审计报警。
     *
     * @param type    报警类型。
     * @param message 报警内容。
     * @throws Exception 方法执行过程中发生的任何异常。
     */
    private void createAlarm(String type, String message) throws Exception {
        context.createInspectionAlarm(new InspectionAlarmCreateInfo(
                context.getInspection().getKey(), context.getInspectionTask().getKey(),
                context.getInspectorInfo().getKey(), truncateType(type), truncateMessage(message)
        ));
    }

    /**
     * 写入检查结果简报。
     *
     * <p>
     * 简报同时写入自动审计任务的锚点消息和任务事件，使任务界面可以直接展示最近一次检查的概要，
     * 事件列表则保留每次检查的完成记录。写入前使用与报警内容相同的长度约束进行裁剪。
     *
     * @param summary 检查结果简报。
     * @throws Exception 方法执行过程中发生的任何异常。
     */
    private void writeInspectionSummary(String summary) throws Exception {
        String message = truncateMessage(summary);
        context.updateInspectorTaskModal(new InspectionTaskUpdateModalInfo(
                context.getInspectionTask().getKey(), message
        ));
        context.createInspectorTaskEvent(new InspectionTaskEventCreateInfo(
                context.getInspectionTask().getKey(), new Date(), message
        ));
    }

    /**
     * 构造检查结果简报。
     *
     * @param startCreatedDate 查询区间的起始时刻。
     * @param endCreatedDate   查询区间的结束时刻。
     * @param hitCount         查询命中的条目数量。
     * @param groupCount       参与统计的有效分组数量。
     * @param thresholdCount   极值达到阈值并被判定处理的分组数量。
     * @param alarmCount       实际创建报警的次数。
     * @return 检查结果简报。
     */
    private String buildSummary(
            Date startCreatedDate, Date endCreatedDate, int hitCount, int groupCount,
            int thresholdCount, int alarmCount
    ) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(AccountLoginFailureConstants.DATE_FORMAT);
        return String.format(
                "账号登录失败检查完成：查询 %s 至 %s，命中 %s 条，有效分组 %s 个，达到阈值 %s 个，创建报警 %s 次。",
                dateFormat.format(startCreatedDate), dateFormat.format(endCreatedDate),
                hitCount, groupCount, thresholdCount, alarmCount
        );
    }

    /**
     * 裁剪报警类型，使其不超过数据库字段允许的最大长度。
     *
     * @param type 报警类型。
     * @return 裁剪后的报警类型。
     */
    private String truncateType(String type) {
        if (Objects.isNull(type) || type.length() <= Constraints.LENGTH_TYPE) {
            return type;
        }
        return type.substring(0, Constraints.LENGTH_TYPE);
    }

    /**
     * 裁剪报警内容，使其不超过数据库字段允许的最大长度。
     *
     * <p>
     * 超长时保留前缀并追加省略号。
     *
     * @param message 报警内容。
     * @return 裁剪后的报警内容。
     */
    private String truncateMessage(String message) {
        if (Objects.isNull(message) || message.length() <= Constraints.LENGTH_MESSAGE) {
            return message;
        }
        return message.substring(0, Constraints.LENGTH_MESSAGE - 3) + "...";
    }

    /**
     * 将审计条目属性转换为用于分组的值字符串。
     *
     * <p>
     * 只有字符串属性与整数属性参与分组：字符串属性直接使用其文本值，整数属性使用其十进制文本；
     * 其它类型的属性不参与分组，返回 <code>null</code>。
     *
     * @param property 审计条目属性。
     * @return 用于分组的值字符串，属性值不存在时返回 <code>null</code>。
     */
    @SuppressWarnings("DuplicatedCode")
    private String toGroupValue(AuditEntryProperty property) {
        if (Objects.isNull(property)) {
            return null;
        }
        switch (property.getPropertyType()) {
            case Constants.PROPERTY_TYPE_STRING:
                return property.getStringValue();
            case Constants.PROPERTY_TYPE_LONG:
                return Objects.isNull(property.getLongValue()) ? null : String.valueOf(property.getLongValue());
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return "AccountLoginFailureExecutor{" +
                "config=" + config +
                ", context=" + context +
                '}';
    }

    /**
     * 命中窗口。
     *
     * <p>
     * 该结构只用于在内存中传递滑动窗口的计算结果，不写入审计器变量。
     *
     * @author DwArFeng
     * @since 1.0.0
     */
    private static class LongWindow {

        /**
         * 窗口起始时刻。
         */
        private final long startPeriod;

        /**
         * 窗口结束时刻。
         */
        private final long endPeriod;

        /**
         * 窗口内的命中数量。
         */
        private final long hitCount;

        private LongWindow(long startPeriod, long endPeriod, long hitCount) {
            this.startPeriod = startPeriod;
            this.endPeriod = endPeriod;
            this.hitCount = hitCount;
        }

        public long getStartPeriod() {
            return startPeriod;
        }

        public long getEndPeriod() {
            return endPeriod;
        }

        public long getHitCount() {
            return hitCount;
        }

        @Override
        public String toString() {
            return "LongWindow{" +
                    "startPeriod=" + startPeriod +
                    ", endPeriod=" + endPeriod +
                    ", hitCount=" + hitCount +
                    '}';
        }
    }

    /**
     * 上一次命中窗口的状态。
     *
     * <p>
     * 该结构以 JSON 字符串的形式存放在审计器变量中，因此字段带有 {@link JSONField} 注解，
     * 便于在审计器变量界面上直接查看。四个字段的含义分别是：窗口起点、窗口终点、极值、窗口内命中数量。
     * 极值用于跨执行比较，命中数量用于人工核对与报警内容展示。
     *
     * @author DwArFeng
     * @since 1.0.0
     */
    public static class LastWindowState {

        @JSONField(name = "start_period", ordinal = 1)
        private long startPeriod;

        @JSONField(name = "end_period", ordinal = 2)
        private long endPeriod;

        @JSONField(name = "extreme", ordinal = 3)
        private long extreme;

        @JSONField(name = "hit_count", ordinal = 4)
        private long hitCount;

        public LastWindowState() {
        }

        public LastWindowState(long startPeriod, long endPeriod, long extreme, long hitCount) {
            this.startPeriod = startPeriod;
            this.endPeriod = endPeriod;
            this.extreme = extreme;
            this.hitCount = hitCount;
        }

        public long getStartPeriod() {
            return startPeriod;
        }

        public void setStartPeriod(long startPeriod) {
            this.startPeriod = startPeriod;
        }

        public long getEndPeriod() {
            return endPeriod;
        }

        public void setEndPeriod(long endPeriod) {
            this.endPeriod = endPeriod;
        }

        public long getExtreme() {
            return extreme;
        }

        public void setExtreme(long extreme) {
            this.extreme = extreme;
        }

        public long getHitCount() {
            return hitCount;
        }

        public void setHitCount(long hitCount) {
            this.hitCount = hitCount;
        }

        @Override
        public String toString() {
            return "LastWindowState{" +
                    "startPeriod=" + startPeriod +
                    ", endPeriod=" + endPeriod +
                    ", extreme=" + extreme +
                    ", hitCount=" + hitCount +
                    '}';
        }
    }
}
