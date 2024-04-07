package com.dwarfeng.familyhelper.plugin.notify.handler.pusher;

import com.dwarfeng.notify.impl.handler.pusher.AbstractPusher;
import com.dwarfeng.notify.stack.bean.dto.NotifyHistoryRecordInfo;
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
import java.util.Map;

/**
 * 家庭助手推送器。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
@Component
public class FamilyhelperPusher extends AbstractPusher {

    public static final String SUPPORT_TYPE = "familyhelper";

    private static final Logger LOGGER = LoggerFactory.getLogger(FamilyhelperPusher.class);

    private final NotifyService notifyService;

    private final ThreadPoolTaskExecutor executor;

    @Value("${pusher.familyhelper.notify_setting_id.route_reset}")
    private long routeResetNotifySettingId;
    @Value("${pusher.familyhelper.notify_setting_id.dispatch_reset}")
    private long dispatchResetNotifySettingId;
    @Value("${pusher.familyhelper.notify_setting_id.send_reset}")
    private long sendResetNotifySettingId;

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
    public void notifyHistoryRecorded(NotifyHistoryRecordInfo info) {
    }

    @Override
    public void routeReset() {
        executor.submit(this::internalRouteReset);
    }

    @SuppressWarnings("DuplicatedCode")
    private void internalRouteReset() {
        try {
            LongIdKey notifySettingKey = new LongIdKey(routeResetNotifySettingId);

            // 构造 routeInfoMap。
            Map<String, String> routeInfoMap = Collections.emptyMap();

            // 构造 dispatchInfoMap。
            Map<String, String> dispatchInfoMap = Collections.emptyMap();

            // 构造 sendInfoMap。
            Map<String, String> sendInfoMap = Collections.emptyMap();

            // 调用通知方法。
            notifyService.notify(new NotifyInfo(notifySettingKey, routeInfoMap, dispatchInfoMap, sendInfoMap));
        } catch (Exception e) {
            LOGGER.warn("发送路由重置消息时发送异常, 消息将不会被发送, 异常信息如下: ", e);
        }
    }

    @Override
    public void dispatchReset() {
        executor.submit(this::internalDispatchReset);
    }

    @SuppressWarnings("DuplicatedCode")
    private void internalDispatchReset() {
        try {
            LongIdKey notifySettingKey = new LongIdKey(dispatchResetNotifySettingId);

            // 构造 routeInfoMap。
            Map<String, String> routeInfoMap = Collections.emptyMap();

            // 构造 dispatchInfoMap。
            Map<String, String> dispatchInfoMap = Collections.emptyMap();

            // 构造 sendInfoMap。
            Map<String, String> sendInfoMap = Collections.emptyMap();

            // 调用通知方法。
            notifyService.notify(new NotifyInfo(notifySettingKey, routeInfoMap, dispatchInfoMap, sendInfoMap));
        } catch (Exception e) {
            LOGGER.warn("发送调度重置消息时发送异常, 消息将不会被发送, 异常信息如下: ", e);
        }
    }

    @Override
    public void sendReset() {
        executor.submit(this::internalSendReset);
    }

    @SuppressWarnings("DuplicatedCode")
    private void internalSendReset() {
        try {
            LongIdKey notifySettingKey = new LongIdKey(sendResetNotifySettingId);

            // 构造 routeInfoMap。
            Map<String, String> routeInfoMap = Collections.emptyMap();

            // 构造 dispatchInfoMap。
            Map<String, String> dispatchInfoMap = Collections.emptyMap();

            // 构造 sendInfoMap。
            Map<String, String> sendInfoMap = Collections.emptyMap();

            // 调用通知方法。
            notifyService.notify(new NotifyInfo(notifySettingKey, routeInfoMap, dispatchInfoMap, sendInfoMap));
        } catch (Exception e) {
            LOGGER.warn("发送发送重置消息时发送异常, 消息将不会被发送, 异常信息如下: ", e);
        }
    }
}
