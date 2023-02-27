package com.dwarfeng.familyhelper.plugin.project.handler.pusher;

import com.dwarfeng.familyhelper.plugin.notify.handler.sender.BuiltinSenderRegistry;
import com.dwarfeng.familyhelper.plugin.notify.handler.sender.EmailSenderRegistry;
import com.dwarfeng.familyhelper.project.impl.handler.pusher.AbstractPusher;
import com.dwarfeng.familyhelper.project.sdk.bean.entity.FastJsonMemo;
import com.dwarfeng.familyhelper.project.sdk.bean.entity.FastJsonMemoRemindDriverInfo;
import com.dwarfeng.familyhelper.project.stack.bean.dto.MemoRemindInfo;
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

    @Value("${pusher.familyhelper.notify_setting_id.memo_remind_happened}")
    private long memoRemindHappenedNotifySettingId;
    @Value("${pusher.familyhelper.notify_setting_id.memo_remind_drive_reset}")
    private long memoRemindDriveResetNotifySettingId;

    @Value("${pusher.familyhelper.identity_router.identity_user_list_key}")
    private String identityRouterIdentityUserListKey;
    @Value("${pusher.familyhelper.builtin_sender.placeholder_map_key}")
    private String builtinSenderPlaceholderMapKey;
    @Value("${pusher.familyhelper.email_sender.placeholder_map_key}")
    private String emailSenderPlaceholderMapKey;

    @Value("${pusher.familyhelper.placeholder_map.entity_key.memo}")
    private String placeholderMapMemoEntityKey;
    @Value("${pusher.familyhelper.placeholder_map.entity_key.memo_remind_driver_info}")
    private String placeholderMapMemoRemindDriverInfoEntityKey;

    public FamilyhelperPusher(
            @Qualifier("notifyService") NotifyService notifyService,
            ThreadPoolTaskExecutor executor
    ) {
        super(SUPPORT_TYPE);
        this.notifyService = notifyService;
        this.executor = executor;
    }

    @Override
    public void memoRemindHappened(MemoRemindInfo memoRemindInfo) {
        executor.submit(() -> internalMemoRemindHappened(memoRemindInfo));
    }

    @SuppressWarnings("DuplicatedCode")
    private void internalMemoRemindHappened(MemoRemindInfo memoRemindInfo) {
        try {
            LongIdKey notifySettingKey = new LongIdKey(memoRemindHappenedNotifySettingId);

            // 构造 routerInfoMap。
            Map<String, String> routeInfoMap = new HashMap<>();
            List<StringIdKey> identityUserList = Collections.singletonList(memoRemindInfo.getUser().getKey());
            routeInfoMap.put(
                    identityRouterIdentityUserListKey,
                    IdentityRouterRegistry.stringifyIdentityUserList(identityUserList)
            );

            // 构造 dispatchInfoMap。
            Map<String, String> dispatchInfoMap = Collections.emptyMap();

            // 构造 sendInfoMap。
            Map<String, String> sendInfoMap = new HashMap<>();
            Map<String, Object> placeholderMap = new HashMap<>();
            placeholderMap.put(placeholderMapMemoEntityKey, FastJsonMemo.of(memoRemindInfo.getMemo()));
            placeholderMap.put(
                    placeholderMapMemoRemindDriverInfoEntityKey,
                    FastJsonMemoRemindDriverInfo.of(memoRemindInfo.getMemoRemindDriverInfo())
            );
            sendInfoMap.put(
                    builtinSenderPlaceholderMapKey, BuiltinSenderRegistry.stringifyPlaceholderMap(placeholderMap)
            );
            sendInfoMap.put(emailSenderPlaceholderMapKey, EmailSenderRegistry.stringifyPlaceholderMap(placeholderMap));

            // 调用通知方法。
            notifyService.notify(new NotifyInfo(notifySettingKey, routeInfoMap, dispatchInfoMap, sendInfoMap));
        } catch (Exception e) {
            LOGGER.warn("发送余额记录备忘录提醒消息时发送异常, 消息将不会被发送, 异常信息如下: ", e);
        }
    }

    @Override
    public void memoRemindDriveReset() {
        executor.submit(this::internalMemoRemindDriveReset);
    }

    @SuppressWarnings("DuplicatedCode")
    private void internalMemoRemindDriveReset() {
        try {
            LongIdKey notifySettingKey = new LongIdKey(memoRemindDriveResetNotifySettingId);

            // 构造 routeInfoMap。
            Map<String, String> routeInfoMap = Collections.emptyMap();

            // 构造 dispatchInfoMap。
            Map<String, String> dispatchInfoMap = Collections.emptyMap();

            // 构造 sendInfoMap。
            Map<String, String> sendInfoMap = Collections.emptyMap();

            // 调用通知方法。
            notifyService.notify(new NotifyInfo(notifySettingKey, routeInfoMap, dispatchInfoMap, sendInfoMap));
        } catch (Exception e) {
            LOGGER.warn("发送备忘录提醒驱动重置消息时发送异常, 消息将不会被发送, 异常信息如下: ", e);
        }
    }
}
