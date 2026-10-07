package com.dwarfeng.familyhelper.plugin.audit.handler.pusher;

import com.dwarfeng.audit.sdk.bean.entity.FastJsonInspectionAlarm;
import com.dwarfeng.audit.sdk.handler.pusher.PusherAdapter;
import com.dwarfeng.audit.stack.bean.entity.InspectionAlarm;
import com.dwarfeng.familyhelper.plugin.commons.util.NotifyUtil;
import com.dwarfeng.notify.stack.bean.dto.NotifyInfo;
import com.dwarfeng.notify.stack.service.NotifyService;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 家庭助手推送器。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
@Component
public class FamilyhelperPusher extends PusherAdapter {

    public static final String SUPPORT_TYPE = "familyhelper";

    private static final Logger LOGGER = LoggerFactory.getLogger(FamilyhelperPusher.class);

    private final NotifyService notifyService;

    private final ThreadPoolTaskExecutor executor;

    @Value("${pusher.familyhelper.notify_setting_id.audit_record_reset}")
    private long auditRecordResetNotifySettingId;
    @Value("${pusher.familyhelper.notify_setting_id.inspection_supervise_reset}")
    private long inspectionSuperviseResetNotifySettingId;
    @Value("${pusher.familyhelper.notify_setting_id.inspection_job_reset}")
    private long inspectionJobResetNotifySettingId;
    @Value("${pusher.familyhelper.notify_setting_id.inspection_alarm_created}")
    private long inspectionAlarmCreatedNotifySettingId;

    @Value("${pusher.familyhelper.builtin_sender.placeholder_map_key}")
    private String builtinSenderPlaceholderMapKey;
    @Value("${pusher.familyhelper.email_sender.placeholder_map_key}")
    private String emailSenderPlaceholderMapKey;

    @Value("${pusher.familyhelper.placeholder_map.master_entity_key}")
    private String placeholderMapMasterEntityKey;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public FamilyhelperPusher(
            @Qualifier("notifyService") NotifyService notifyService,
            ThreadPoolTaskExecutor executor
    ) {
        super(SUPPORT_TYPE);
        this.notifyService = notifyService;
        this.executor = executor;
    }

    @Override
    public void auditRecordReset() {
        executor.submit(this::internalAuditRecordReset);
    }

    @SuppressWarnings("DuplicatedCode")
    private void internalAuditRecordReset() {
        try {
            LongIdKey notifySettingKey = new LongIdKey(auditRecordResetNotifySettingId);

            // 构造 routeInfoMap。
            Map<String, String> routeInfoMap = Collections.emptyMap();

            // 构造 dispatchInfoMap。
            Map<String, String> dispatchInfoMap = Collections.emptyMap();

            // 构造 sendInfoMap。
            Map<String, String> sendInfoMap = Collections.emptyMap();

            // 调用通知方法。
            notifyService.notify(new NotifyInfo(notifySettingKey, routeInfoMap, dispatchInfoMap, sendInfoMap));
        } catch (Exception e) {
            LOGGER.warn("发送审核记录重置消息时发送异常, 消息将不会被发送, 异常信息如下: ", e);
        }
    }

    @Override
    public void inspectionSuperviseReset() {
        executor.submit(this::internalInspectionSuperviseReset);
    }

    @SuppressWarnings("DuplicatedCode")
    private void internalInspectionSuperviseReset() {
        try {
            LongIdKey notifySettingKey = new LongIdKey(inspectionSuperviseResetNotifySettingId);

            // 构造 routeInfoMap。
            Map<String, String> routeInfoMap = Collections.emptyMap();

            // 构造 dispatchInfoMap。
            Map<String, String> dispatchInfoMap = Collections.emptyMap();

            // 构造 sendInfoMap。
            Map<String, String> sendInfoMap = Collections.emptyMap();

            // 调用通知方法。
            notifyService.notify(new NotifyInfo(notifySettingKey, routeInfoMap, dispatchInfoMap, sendInfoMap));
        } catch (Exception e) {
            LOGGER.warn("发送自动审计主管重置消息时发送异常, 消息将不会被发送, 异常信息如下: ", e);
        }
    }

    @Override
    public void inspectionJobReset() {
        executor.submit(this::internalInspectionJobReset);
    }

    @SuppressWarnings("DuplicatedCode")
    private void internalInspectionJobReset() {
        try {
            LongIdKey notifySettingKey = new LongIdKey(inspectionJobResetNotifySettingId);

            // 构造 routeInfoMap。
            Map<String, String> routeInfoMap = Collections.emptyMap();

            // 构造 dispatchInfoMap。
            Map<String, String> dispatchInfoMap = Collections.emptyMap();

            // 构造 sendInfoMap。
            Map<String, String> sendInfoMap = Collections.emptyMap();

            // 调用通知方法。
            notifyService.notify(new NotifyInfo(notifySettingKey, routeInfoMap, dispatchInfoMap, sendInfoMap));
        } catch (Exception e) {
            LOGGER.warn("发送自动审计作业重置消息时发送异常, 消息将不会被发送, 异常信息如下: ", e);
        }
    }

    @Override
    public void inspectionAlarmCreated(InspectionAlarm inspectionAlarm) {
        executor.submit(() -> internalInspectionAlarmCreated(inspectionAlarm));
    }

    private void internalInspectionAlarmCreated(InspectionAlarm inspectionAlarm) {
        try {
            LongIdKey notifySettingKey = new LongIdKey(inspectionAlarmCreatedNotifySettingId);

            // 构造 routeInfoMap。
            Map<String, String> routeInfoMap = Collections.emptyMap();

            // 构造 dispatchInfoMap。
            Map<String, String> dispatchInfoMap = Collections.emptyMap();

            // 构造 sendInfoMap。
            Map<String, String> sendInfoMap = new HashMap<>();
            Map<String, Object> placeholderMap = new HashMap<>();
            placeholderMap.put(placeholderMapMasterEntityKey, FastJsonInspectionAlarm.of(inspectionAlarm));
            sendInfoMap.put(
                    builtinSenderPlaceholderMapKey, NotifyUtil.stringifyBuiltinSenderPlaceholderMap(placeholderMap)
            );
            sendInfoMap.put(
                    emailSenderPlaceholderMapKey, NotifyUtil.stringifyEmailSenderPlaceholderMap(placeholderMap)
            );

            // 调用通知方法。
            notifyService.notify(new NotifyInfo(notifySettingKey, routeInfoMap, dispatchInfoMap, sendInfoMap));
        } catch (Exception e) {
            LOGGER.warn("发送自动审计报警创建消息时发送异常, 消息将不会被发送, 异常信息如下: ", e);
        }
    }
}
