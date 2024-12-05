package com.dwarfeng.familyhelper.plugin.clannad.handler.pusher;

import com.dwarfeng.acckeeper.stack.bean.entity.Account;
import com.dwarfeng.acckeeper.stack.service.AccountMaintainService;
import com.dwarfeng.familyhelper.clannad.impl.handler.pusher.PusherAdapter;
import com.dwarfeng.familyhelper.clannad.sdk.bean.dto.FastJsonBirthdayBlessInfo;
import com.dwarfeng.familyhelper.clannad.sdk.bean.entity.FastJsonMessage;
import com.dwarfeng.familyhelper.clannad.stack.bean.dto.BirthdayBlessInfo;
import com.dwarfeng.familyhelper.clannad.stack.bean.entity.Message;
import com.dwarfeng.familyhelper.clannad.stack.bean.entity.Profile;
import com.dwarfeng.familyhelper.clannad.stack.bean.key.NicknameKey;
import com.dwarfeng.familyhelper.clannad.stack.service.NicknameMaintainService;
import com.dwarfeng.familyhelper.clannad.stack.service.ProfileMaintainService;
import com.dwarfeng.familyhelper.plugin.commons.util.NotifyUtil;
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
public class FamilyhelperPusher extends PusherAdapter {

    public static final String SUPPORT_TYPE = "familyhelper";

    private static final Logger LOGGER = LoggerFactory.getLogger(FamilyhelperPusher.class);

    private final NotifyService notifyService;
    private final AccountMaintainService accountMaintainService;
    private final NicknameMaintainService nicknameMaintainService;
    private final ProfileMaintainService profileMaintainService;

    private final ThreadPoolTaskExecutor executor;

    @Value("${pusher.familyhelper.notify_setting_id.birthday_bless_happened}")
    private long birthdayBlessHappenedNotifySettingId;
    @Value("${pusher.familyhelper.notify_setting_id.message_sent}")
    private long messageSentNotifySettingId;

    @Value("${pusher.familyhelper.identity_router.identity_user_list_key}")
    private String identityRouterIdentityUserListKey;
    @Value("${pusher.familyhelper.builtin_sender.placeholder_map_key}")
    private String builtinSenderPlaceholderMapKey;
    @Value("${pusher.familyhelper.email_sender.placeholder_map_key}")
    private String emailSenderPlaceholderMapKey;

    @Value("${pusher.familyhelper.placeholder_map.master_entity_key}")
    private String placeholderMapMasterEntityKey;
    @Value("${pusher.familyhelper.placeholder_map.sent_user_display_name_key}")
    private String placeholderMapSentUserDisplayNameKey;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public FamilyhelperPusher(
            @Qualifier("notifyService") NotifyService notifyService,
            @Qualifier("accountMaintainService") AccountMaintainService accountMaintainService,
            NicknameMaintainService nicknameMaintainService,
            ProfileMaintainService profileMaintainService,
            ThreadPoolTaskExecutor executor
    ) {
        super(SUPPORT_TYPE);
        this.notifyService = notifyService;
        this.accountMaintainService = accountMaintainService;
        this.nicknameMaintainService = nicknameMaintainService;
        this.profileMaintainService = profileMaintainService;
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

    @Override
    public void messageSent(Message message) {
        executor.submit(() -> internalMessageSent(message));
    }

    private void internalMessageSent(Message message) {
        try {
            LongIdKey notifySettingKey = new LongIdKey(messageSentNotifySettingId);

            // 构造 routerInfoMap。
            Map<String, String> routeInfoMap = new HashMap<>();
            List<StringIdKey> identityUserList = Collections.singletonList(message.getReceiveUserKey());
            routeInfoMap.put(
                    identityRouterIdentityUserListKey,
                    IdentityRouterRegistry.stringifyIdentityUserList(identityUserList)
            );

            // 解析发送者的显示名称。
            String sentUserDisplayName = parseUserDisplayName(message.getReceiveUserKey(), message.getSendUserKey());

            // 构造 dispatchInfoMap。
            Map<String, String> dispatchInfoMap = Collections.emptyMap();

            // 构造 sendInfoDetails。
            Map<String, String> sendInfoMap = new HashMap<>();
            Map<String, Object> placeholderMap = new HashMap<>();
            placeholderMap.put(placeholderMapMasterEntityKey, FastJsonMessage.of(message));
            placeholderMap.put(placeholderMapSentUserDisplayNameKey, sentUserDisplayName);
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

    private String parseUserDisplayName(StringIdKey subjectUserKey, StringIdKey objectUserKey) throws Exception {
        Account account = accountMaintainService.get(objectUserKey);

        // 定义 displayName。
        String displayName;
        // 如果主语用户对宾语用户具有昵称，则 displayName 显示为昵称。
        NicknameKey nicknameKey = new NicknameKey(subjectUserKey.getStringId(), objectUserKey.getStringId());
        Profile profile = profileMaintainService.get(account.getKey());
        if (nicknameMaintainService.exists(nicknameKey)) {
            displayName = nicknameMaintainService.get(nicknameKey).getCall();
        }
        // 如果宾语用户的个人简介中名称不为空，则 displayName 显示为个人简介的名称。
        else if (!StringUtils.isEmpty(profile.getName())) {
            displayName = profile.getName();
        }
        // displayName 显示为 account 的 name。
        else {
            displayName = account.getDisplayName();
        }
        // 返回 displayName。
        return displayName;
    }
}
