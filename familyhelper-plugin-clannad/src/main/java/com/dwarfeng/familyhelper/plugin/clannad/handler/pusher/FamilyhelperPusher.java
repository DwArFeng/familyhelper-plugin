package com.dwarfeng.familyhelper.plugin.clannad.handler.pusher;

import com.dwarfeng.familyhelper.clannad.impl.handler.pusher.AbstractPusher;
import com.dwarfeng.familyhelper.clannad.sdk.bean.dto.FastJsonBirthdayBlessInfo;
import com.dwarfeng.familyhelper.clannad.stack.bean.dto.BirthdayBlessInfo;
import com.dwarfeng.familyhelper.plugin.notify.handler.dispatcher.GeneralDispatcherRegistry;
import com.dwarfeng.familyhelper.plugin.notify.handler.sender.BuiltinSenderRegistry;
import com.dwarfeng.familyhelper.plugin.notify.handler.sender.EmailSenderRegistry;
import com.dwarfeng.notify.impl.handler.dispatcher.EntireDispatcherRegistry;
import com.dwarfeng.notify.impl.handler.router.IdentityRouterRegistry;
import com.dwarfeng.notify.stack.bean.dto.NotifyInfo;
import com.dwarfeng.notify.stack.service.NotifyService;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.*;

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

    @Value("${pusher.familyhelper.notify_setting_id.birthday_bless_happened}")
    private long birthdayBlessHappenedNotifySettingId;

    public FamilyhelperPusher(
            @Qualifier("notifyService") NotifyService notifyService,
            ThreadPoolTaskExecutor executor
    ) {
        super(SUPPORT_TYPE);
        this.notifyService = notifyService;
        this.executor = executor;
    }

    @Override
    public void birthdayBlessHappened(BirthdayBlessInfo birthdayBlessInfo) {
        executor.submit(() -> internalRemindHappened(birthdayBlessInfo));
    }

    @Override
    public void birthdayBlessHappened(List<BirthdayBlessInfo> birthdayBlessInfos) {
        for (BirthdayBlessInfo birthdayBlessInfo : birthdayBlessInfos) {
            executor.submit(() -> internalRemindHappened(birthdayBlessInfo));
        }
    }

    @SuppressWarnings("DuplicatedCode")
    private void internalRemindHappened(BirthdayBlessInfo birthdayBlessInfo) {
        try {
            LongIdKey notifySettingKey = new LongIdKey(birthdayBlessHappenedNotifySettingId);

            // 构造 routeInfoDetails。
            List<NotifyInfo.InfoDetail> routeInfoDetails = new ArrayList<>();
            List<StringIdKey> userKeys = Collections.singletonList(birthdayBlessInfo.getKey());
            routeInfoDetails.add(new NotifyInfo.InfoDetail(
                    IdentityRouterRegistry.ROUTER_TYPE, IdentityRouterRegistry.toRouteInfo(userKeys))
            );

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
            Map<String, Object> placeholderMap = new HashMap<>();
            placeholderMap.put("birthday_bless_info", FastJsonBirthdayBlessInfo.of(birthdayBlessInfo));
            sendInfoDetails.add(new NotifyInfo.InfoDetail(
                    BuiltinSenderRegistry.SENDER_TYPE, BuiltinSenderRegistry.toSendInfo(placeholderMap)
            ));
            sendInfoDetails.add(new NotifyInfo.InfoDetail(
                    EmailSenderRegistry.SENDER_TYPE, EmailSenderRegistry.toSendInfo(placeholderMap)
            ));

            notifyService.notify(
                    new NotifyInfo(notifySettingKey, routeInfoDetails, dispatchInfoDetails, sendInfoDetails)
            );
        } catch (Exception e) {
            LOGGER.warn("发送提醒驱动重置消息时发送异常, 消息将不会被发送, 异常信息如下: ", e);
        }
    }
}
