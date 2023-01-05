package com.dwarfeng.familyhelper.plugin.notify.handler.pusher;

import com.dwarfeng.familyhelper.plugin.notify.handler.dispatcher.GeneralDispatcherRegistry;
import com.dwarfeng.familyhelper.plugin.notify.handler.router.PermissionRouterRegistry;
import com.dwarfeng.familyhelper.plugin.notify.handler.sender.BuiltinSenderRegistry;
import com.dwarfeng.familyhelper.plugin.notify.handler.sender.EmailSenderRegistry;
import com.dwarfeng.notify.impl.handler.dispatcher.EntireDispatcherRegistry;
import com.dwarfeng.notify.impl.handler.pusher.AbstractPusher;
import com.dwarfeng.notify.stack.bean.dto.NotifyInfo;
import com.dwarfeng.notify.stack.bean.entity.SendHistory;
import com.dwarfeng.notify.stack.service.NotifyService;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    public FamilyhelperPusher(
            @Qualifier("notifyService") NotifyService notifyService,
            ThreadPoolTaskExecutor executor
    ) {
        super(SUPPORT_TYPE);
        this.notifyService = notifyService;
        this.executor = executor;
    }

    @Override
    public void notifySent(SendHistory sendHistory) {
    }

    @Override
    public void notifySent(List<SendHistory> sendHistories) {
    }

    @Override
    public void routeReset() {
        executor.submit(this::internalRouteReset);
    }

    @SuppressWarnings("DuplicatedCode")
    private void internalRouteReset() {
        try {
            LongIdKey notifySettingKey = new LongIdKey(routeResetNotifySettingId);

            // 构造 routeInfoDetails。
            List<NotifyInfo.InfoDetail> routeInfoDetails = new ArrayList<>();
            routeInfoDetails.add(new NotifyInfo.InfoDetail(PermissionRouterRegistry.ROUTER_TYPE, StringUtils.EMPTY));

            // 构造 dispatchInfoDetails。
            List<NotifyInfo.InfoDetail> dispatchInfoDetails = new ArrayList<>();
            dispatchInfoDetails.add(new NotifyInfo.InfoDetail(
                    EntireDispatcherRegistry.DISPATCHER_TYPE, StringUtils.EMPTY
            ));
            dispatchInfoDetails.add(new NotifyInfo.InfoDetail(
                    GeneralDispatcherRegistry.DISPATCHER_TYPE, StringUtils.EMPTY
            ));

            // 构造 sendInfoDetails。
            List<NotifyInfo.InfoDetail> sendInfoDetails = new ArrayList<>();
            sendInfoDetails.add(new NotifyInfo.InfoDetail(BuiltinSenderRegistry.SENDER_TYPE, StringUtils.EMPTY));
            sendInfoDetails.add(new NotifyInfo.InfoDetail(EmailSenderRegistry.SENDER_TYPE, StringUtils.EMPTY));

            notifyService.notify(
                    new NotifyInfo(notifySettingKey, routeInfoDetails, dispatchInfoDetails, sendInfoDetails)
            );
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

            // 构造 routeInfoDetails。
            List<NotifyInfo.InfoDetail> routeInfoDetails = new ArrayList<>();
            routeInfoDetails.add(new NotifyInfo.InfoDetail(PermissionRouterRegistry.ROUTER_TYPE, StringUtils.EMPTY));

            // 构造 dispatchInfoDetails。
            List<NotifyInfo.InfoDetail> dispatchInfoDetails = new ArrayList<>();
            dispatchInfoDetails.add(new NotifyInfo.InfoDetail(
                    EntireDispatcherRegistry.DISPATCHER_TYPE, StringUtils.EMPTY
            ));
            dispatchInfoDetails.add(new NotifyInfo.InfoDetail(
                    GeneralDispatcherRegistry.DISPATCHER_TYPE, StringUtils.EMPTY
            ));

            // 构造 sendInfoDetails。
            List<NotifyInfo.InfoDetail> sendInfoDetails = new ArrayList<>();
            sendInfoDetails.add(new NotifyInfo.InfoDetail(BuiltinSenderRegistry.SENDER_TYPE, StringUtils.EMPTY));
            sendInfoDetails.add(new NotifyInfo.InfoDetail(EmailSenderRegistry.SENDER_TYPE, StringUtils.EMPTY));

            notifyService.notify(
                    new NotifyInfo(notifySettingKey, routeInfoDetails, dispatchInfoDetails, sendInfoDetails)
            );
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

            // 构造 routeInfoDetails。
            List<NotifyInfo.InfoDetail> routeInfoDetails = new ArrayList<>();
            routeInfoDetails.add(new NotifyInfo.InfoDetail(PermissionRouterRegistry.ROUTER_TYPE, StringUtils.EMPTY));

            // 构造 dispatchInfoDetails。
            List<NotifyInfo.InfoDetail> dispatchInfoDetails = new ArrayList<>();
            dispatchInfoDetails.add(new NotifyInfo.InfoDetail(
                    EntireDispatcherRegistry.DISPATCHER_TYPE, StringUtils.EMPTY
            ));
            dispatchInfoDetails.add(new NotifyInfo.InfoDetail(
                    GeneralDispatcherRegistry.DISPATCHER_TYPE, StringUtils.EMPTY
            ));

            // 构造 sendInfoDetails。
            List<NotifyInfo.InfoDetail> sendInfoDetails = new ArrayList<>();
            sendInfoDetails.add(new NotifyInfo.InfoDetail(BuiltinSenderRegistry.SENDER_TYPE, StringUtils.EMPTY));
            sendInfoDetails.add(new NotifyInfo.InfoDetail(EmailSenderRegistry.SENDER_TYPE, StringUtils.EMPTY));

            notifyService.notify(
                    new NotifyInfo(notifySettingKey, routeInfoDetails, dispatchInfoDetails, sendInfoDetails)
            );
        } catch (Exception e) {
            LOGGER.warn("发送发送重置消息时发送异常, 消息将不会被发送, 异常信息如下: ", e);
        }
    }
}
