package com.dwarfeng.familyhelper.plugin.notify.handler.pusher;

import com.dwarfeng.familyhelper.plugin.notify.handler.router.PermissionRouterRegistry;
import com.dwarfeng.familyhelper.plugin.notify.handler.sender.BuiltinSenderRegistry;
import com.dwarfeng.notify.impl.handler.dispatcher.EntireDispatcherRegistry;
import com.dwarfeng.notify.impl.handler.pusher.AbstractPusher;
import com.dwarfeng.notify.stack.bean.dto.NotifyInfo;
import com.dwarfeng.notify.stack.bean.entity.SendHistory;
import com.dwarfeng.notify.stack.service.NotifyService;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

    private final NotifyService notifyService;

    @Value("${pusher.familyhelper.notify_setting_id.route_reset}")
    private long routeResetNotifySettingId;
    @Value("${pusher.familyhelper.notify_setting_id.dispatch_reset}")
    private long dispatchResetNotifySettingId;
    @Value("${pusher.familyhelper.notify_setting_id.send_reset}")
    private long sendResetNotifySettingId;

    public FamilyhelperPusher(
            @Qualifier("notifyService") NotifyService notifyService
    ) {
        super(SUPPORT_TYPE);
        this.notifyService = notifyService;
    }

    @Override
    public void notifySent(SendHistory sendHistory) {
    }

    @Override
    public void notifySent(List<SendHistory> sendHistories) {
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void routeReset() throws HandlerException {
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

            // 构造 sendInfoDetails。
            List<NotifyInfo.InfoDetail> sendInfoDetails = new ArrayList<>();
            sendInfoDetails.add(new NotifyInfo.InfoDetail(BuiltinSenderRegistry.SENDER_TYPE, StringUtils.EMPTY));

            notifyService.notify(
                    new NotifyInfo(notifySettingKey, routeInfoDetails, dispatchInfoDetails, sendInfoDetails)
            );
        } catch (Exception e) {
            throw new HandlerException(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void dispatchReset() throws HandlerException {
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

            // 构造 sendInfoDetails。
            List<NotifyInfo.InfoDetail> sendInfoDetails = new ArrayList<>();
            sendInfoDetails.add(new NotifyInfo.InfoDetail(BuiltinSenderRegistry.SENDER_TYPE, StringUtils.EMPTY));

            notifyService.notify(
                    new NotifyInfo(notifySettingKey, routeInfoDetails, dispatchInfoDetails, sendInfoDetails)
            );
        } catch (Exception e) {
            throw new HandlerException(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void sendReset() throws HandlerException {
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

            // 构造 sendInfoDetails。
            List<NotifyInfo.InfoDetail> sendInfoDetails = new ArrayList<>();
            sendInfoDetails.add(new NotifyInfo.InfoDetail(BuiltinSenderRegistry.SENDER_TYPE, StringUtils.EMPTY));

            notifyService.notify(
                    new NotifyInfo(notifySettingKey, routeInfoDetails, dispatchInfoDetails, sendInfoDetails)
            );
        } catch (Exception e) {
            throw new HandlerException(e);
        }
    }
}
