package com.dwarfeng.familyhelper.plugin.clannad.handler.pusher;

import com.dwarfeng.familyhelper.clannad.impl.handler.pusher.AbstractPusher;
import com.dwarfeng.familyhelper.clannad.sdk.bean.dto.FastJsonBirthdayBlessInfo;
import com.dwarfeng.familyhelper.clannad.stack.bean.dto.BirthdayBlessInfo;
import com.dwarfeng.familyhelper.plugin.commons.util.NotifyUtil;
import com.dwarfeng.notify.impl.handler.router.IdentityRouterRegistry;
import com.dwarfeng.notify.stack.bean.dto.NotifyInfo;
import com.dwarfeng.notify.stack.service.NotifyService;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

    @Value("${pusher.familyhelper.notify_setting_id.birthday_bless_happened}")
    private long birthdayBlessHappenedNotifySettingId;

    @Value("${pusher.familyhelper.identity_router.identity_user_list_key}")
    private String identityRouterIdentityUserListKey;
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
    public void birthdayBlessHappened(BirthdayBlessInfo birthdayBlessInfo) {
        executor.submit(() -> internalRemindHappened(birthdayBlessInfo));
    }

    @Override
    public void birthdayBlessHappened(List<BirthdayBlessInfo> birthdayBlessInfos) {
        for (BirthdayBlessInfo birthdayBlessInfo : birthdayBlessInfos) {
            executor.submit(() -> internalRemindHappened(birthdayBlessInfo));
        }
    }

    private void internalRemindHappened(BirthdayBlessInfo birthdayBlessInfo) {
        try {
            LongIdKey notifySettingKey = new LongIdKey(birthdayBlessHappenedNotifySettingId);

            // 构造 routerInfoMap。
            Map<String, String> routeInfoMap = new HashMap<>();
            List<StringIdKey> identityUserList = Collections.singletonList(birthdayBlessInfo.getKey());
            routeInfoMap.put(
                    identityRouterIdentityUserListKey,
                    IdentityRouterRegistry.stringifyIdentityUserList(identityUserList)
            );

            // 构造 dispatchInfoMap。
            Map<String, String> dispatchInfoMap = Collections.emptyMap();

            // 构造 sendInfoDetails。
            Map<String, String> sendInfoMap = new HashMap<>();
            Map<String, Object> placeholderMap = new HashMap<>();
            placeholderMap.put(placeholderMapMasterEntityKey, FastJsonBirthdayBlessInfo.of(birthdayBlessInfo));
            sendInfoMap.put(
                    builtinSenderPlaceholderMapKey, NotifyUtil.stringifyBuiltinSenderPlaceholderMap(placeholderMap)
            );
            sendInfoMap.put(
                    emailSenderPlaceholderMapKey, NotifyUtil.stringifyEmailSenderPlaceholderMap(placeholderMap)
            );

            // 调用通知方法。
            notifyService.notify(new NotifyInfo(notifySettingKey, routeInfoMap, dispatchInfoMap, sendInfoMap));
        } catch (Exception e) {
            LOGGER.warn("发送提醒驱动重置消息时发送异常, 消息将不会被发送, 异常信息如下: ", e);
        }
    }
}
