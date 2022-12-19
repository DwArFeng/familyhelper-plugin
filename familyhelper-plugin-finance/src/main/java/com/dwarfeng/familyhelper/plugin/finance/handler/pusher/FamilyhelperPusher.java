package com.dwarfeng.familyhelper.plugin.finance.handler.pusher;

import com.dwarfeng.familyhelper.finance.impl.handler.pusher.AbstractPusher;
import com.dwarfeng.familyhelper.finance.sdk.bean.entity.FastJsonAccountBook;
import com.dwarfeng.familyhelper.finance.stack.bean.dto.RemindInfo;
import com.dwarfeng.familyhelper.finance.stack.bean.entity.User;
import com.dwarfeng.familyhelper.plugin.notify.handler.router.PermissionRouterRegistry;
import com.dwarfeng.familyhelper.plugin.notify.handler.sender.BuiltinSenderRegistry;
import com.dwarfeng.notify.impl.handler.dispatcher.EntireDispatcherRegistry;
import com.dwarfeng.notify.impl.handler.router.IdentityRouterRegistry;
import com.dwarfeng.notify.stack.bean.dto.NotifyInfo;
import com.dwarfeng.notify.stack.service.NotifyService;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Value("${pusher.familyhelper.notify_setting_id.remind_happened}")
    private long remindHappenedNotifySettingId;
    @Value("${pusher.familyhelper.notify_setting_id.remind_drive_reset}")
    private long remindDriveResetNotifySettingId;

    public FamilyhelperPusher(
            @Qualifier("notifyService") NotifyService notifyService
    ) {
        super(SUPPORT_TYPE);
        this.notifyService = notifyService;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void remindHappened(RemindInfo remindInfo) throws HandlerException {
        try {
            LongIdKey notifySettingKey = new LongIdKey(remindHappenedNotifySettingId);

            // 构造 routeInfoDetails。
            List<NotifyInfo.InfoDetail> routeInfoDetails = new ArrayList<>();
            List<StringIdKey> userKeys = remindInfo.getAimedUsers().stream().map(User::getKey)
                    .collect(Collectors.toList());
            routeInfoDetails.add(new NotifyInfo.InfoDetail(
                    IdentityRouterRegistry.ROUTER_TYPE, IdentityRouterRegistry.toRouteInfo(userKeys))
            );

            // 构造 dispatchInfoDetails。
            List<NotifyInfo.InfoDetail> dispatchInfoDetails = new ArrayList<>();
            dispatchInfoDetails.add(new NotifyInfo.InfoDetail(
                    EntireDispatcherRegistry.DISPATCHER_TYPE, StringUtils.EMPTY
            ));

            // 构造 sendInfoDetails。
            List<NotifyInfo.InfoDetail> sendInfoDetails = new ArrayList<>();
            Map<String, Object> placeholderMap = new HashMap<>();
            placeholderMap.put("account_book", FastJsonAccountBook.of(remindInfo.getAccountBook()));
            sendInfoDetails.add(new NotifyInfo.InfoDetail(
                    BuiltinSenderRegistry.SENDER_TYPE, BuiltinSenderRegistry.toSendInfo(placeholderMap)
            ));

            notifyService.notify(
                    new NotifyInfo(notifySettingKey, routeInfoDetails, dispatchInfoDetails, sendInfoDetails)
            );
        } catch (Exception e) {
            throw new HandlerException(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void remindDriveReset() throws HandlerException {
        try {
            LongIdKey notifySettingKey = new LongIdKey(remindDriveResetNotifySettingId);

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
