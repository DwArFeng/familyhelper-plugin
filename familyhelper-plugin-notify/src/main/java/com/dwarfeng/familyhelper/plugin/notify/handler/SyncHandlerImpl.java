package com.dwarfeng.familyhelper.plugin.notify.handler;

import com.alibaba.fastjson.JSON;
import com.dwarfeng.familyhelper.plugin.notify.handler.router.FamilyhelperRouterRegistry;
import com.dwarfeng.familyhelper.plugin.notify.handler.sender.FamilyhelperSenderRegistry;
import com.dwarfeng.notify.impl.handler.HandlerValidator;
import com.dwarfeng.notify.stack.bean.entity.*;
import com.dwarfeng.notify.stack.bean.entity.key.SenderRelationKey;
import com.dwarfeng.notify.stack.service.*;
import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SyncHandlerImpl implements SyncHandler {

    private static final String DEFAULT_LABEL = "synced";
    private static final String DEFAULT_REMARK = "通过 familyhelper-plugin 进行同步";

    private final RouterInfoMaintainService routerInfoMaintainService;
    private final SenderRelationMaintainService senderRelationMaintainService;
    private final SenderInfoMaintainService senderInfoMaintainService;
    private final TopicMaintainService topicMaintainService;
    private final NotifySettingMaintainService notifySettingMaintainService;

    private final KeyFetcher<LongIdKey> keyFetcher;

    private final HandlerValidator handlerValidator;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public SyncHandlerImpl(
            RouterInfoMaintainService routerInfoMaintainService,
            SenderRelationMaintainService senderRelationMaintainService,
            SenderInfoMaintainService senderInfoMaintainService,
            TopicMaintainService topicMaintainService,
            NotifySettingMaintainService notifySettingMaintainService,
            KeyFetcher<LongIdKey> keyFetcher,
            HandlerValidator handlerValidator
    ) {
        this.routerInfoMaintainService = routerInfoMaintainService;
        this.senderRelationMaintainService = senderRelationMaintainService;
        this.senderInfoMaintainService = senderInfoMaintainService;
        this.topicMaintainService = topicMaintainService;
        this.notifySettingMaintainService = notifySettingMaintainService;
        this.keyFetcher = keyFetcher;
        this.handlerValidator = handlerValidator;
    }

    @Override
    public void syncRouterForNotifySetting(LongIdKey notifySettingKey) throws HandlerException {
        try {
            // 确认通知设置存在。
            handlerValidator.makeSureNotifySettingExists(notifySettingKey);

            // 删除通知设置原有的所有路由器。
            List<LongIdKey> routerInfoKeysToDelete = routerInfoMaintainService.lookupAsList(
                    RouterInfoMaintainService.CHILD_FOR_NOTIFY_SETTING, new Object[]{notifySettingKey}
            ).stream().map(RouterInfo::getKey).collect(Collectors.toList());
            routerInfoMaintainService.batchDelete(routerInfoKeysToDelete);

            // 生成家庭助手使用的路由器信息，并插入。
            RouterInfo routerInfo = new RouterInfo(
                    null, notifySettingKey, DEFAULT_LABEL, FamilyhelperRouterRegistry.ROUTER_TYPE,
                    Long.toString(notifySettingKey.getLongId()), DEFAULT_REMARK, true
            );
            routerInfoMaintainService.insert(routerInfo);
        } catch (HandlerException e) {
            throw e;
        } catch (Exception e) {
            throw new HandlerException(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void syncSenderForNotifySetting(LongIdKey notifySettingKey) throws HandlerException {
        try {
            // 确认通知设置存在。
            handlerValidator.makeSureNotifySettingExists(notifySettingKey);

            // 查询与通知设置有关的所有关联信息。
            List<SenderRelation> senderRelations = senderRelationMaintainService.lookupAsList(
                    SenderRelationMaintainService.CHILD_FOR_NOTIFY_SETTING, new Object[]{notifySettingKey}
            );
            // 删除所有相关的关联信息。
            List<SenderRelationKey> senderRelationKeysToDelete = senderRelations.stream().map(SenderRelation::getKey)
                    .collect(Collectors.toList());
            senderRelationMaintainService.batchDelete(senderRelationKeysToDelete);
            // 删除所有相关的发送器。
            List<LongIdKey> senderInfoKeysToDelete = senderRelations.stream().map(SenderRelation::getSenderInfoKey)
                    .collect(Collectors.toList());
            senderInfoMaintainService.batchDelete(senderInfoKeysToDelete);

            // 查询所有的主题，对每个主题生成发送器和关联信息。
            List<Topic> topics = topicMaintainService.lookupAsList();
            List<SenderInfo> senderInfosToInsert = new ArrayList<>();
            List<SenderRelation> senderRelationToInsert = new ArrayList<>();
            for (Topic topic : topics) {
                LongIdKey senderInfoKey = keyFetcher.fetchKey();
                FamilyhelperSenderRegistry.Config senderConfig = new FamilyhelperSenderRegistry.Config(
                        notifySettingKey.getLongId(), topic.getKey().getStringId()
                );
                senderInfosToInsert.add(new SenderInfo(
                        senderInfoKey, DEFAULT_LABEL, FamilyhelperSenderRegistry.SENDER_TYPE,
                        JSON.toJSONString(senderConfig, false), DEFAULT_REMARK
                ));
                senderRelationToInsert.add(new SenderRelation(
                        new SenderRelationKey(notifySettingKey.getLongId(), topic.getKey().getStringId()),
                        senderInfoKey, DEFAULT_REMARK
                ));
            }
            // 调用维护服务批量插入。
            senderInfoMaintainService.batchInsert(senderInfosToInsert);
            senderRelationMaintainService.batchInsert(senderRelationToInsert);
        } catch (HandlerException e) {
            throw e;
        } catch (Exception e) {
            throw new HandlerException(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void syncSenderForTopic(StringIdKey topicKey) throws HandlerException {
        try {
            // 确认主题存在。
            handlerValidator.makeSureTopicExists(topicKey);

            // 查询与通知设置有关的所有关联信息。
            List<SenderRelation> senderRelations = senderRelationMaintainService.lookupAsList(
                    SenderRelationMaintainService.CHILD_FOR_TOPIC, new Object[]{topicKey}
            );
            // 删除所有相关的关联信息。
            List<SenderRelationKey> senderRelationKeysToDelete = senderRelations.stream().map(SenderRelation::getKey)
                    .collect(Collectors.toList());
            senderRelationMaintainService.batchDelete(senderRelationKeysToDelete);
            // 删除所有相关的发送器。
            List<LongIdKey> senderInfoKeysToDelete = senderRelations.stream().map(SenderRelation::getSenderInfoKey)
                    .collect(Collectors.toList());
            senderInfoMaintainService.batchDelete(senderInfoKeysToDelete);

            // 查询所有的通知设置，对每个通知设置生成发送器和关联信息。
            List<NotifySetting> notifySettings = notifySettingMaintainService.lookupAsList();
            List<SenderInfo> senderInfosToInsert = new ArrayList<>();
            List<SenderRelation> senderRelationToInsert = new ArrayList<>();
            for (NotifySetting notifySetting : notifySettings) {
                LongIdKey senderInfoKey = keyFetcher.fetchKey();
                FamilyhelperSenderRegistry.Config senderConfig = new FamilyhelperSenderRegistry.Config(
                        notifySetting.getKey().getLongId(), topicKey.getStringId()
                );
                senderInfosToInsert.add(new SenderInfo(
                        senderInfoKey, DEFAULT_LABEL, FamilyhelperSenderRegistry.SENDER_TYPE,
                        JSON.toJSONString(senderConfig, false), DEFAULT_REMARK
                ));
                senderRelationToInsert.add(new SenderRelation(
                        new SenderRelationKey(notifySetting.getKey().getLongId(), topicKey.getStringId()),
                        senderInfoKey, DEFAULT_REMARK
                ));
            }
            // 调用维护服务批量插入。
            senderInfoMaintainService.batchInsert(senderInfosToInsert);
            senderRelationMaintainService.batchInsert(senderRelationToInsert);
        } catch (HandlerException e) {
            throw e;
        } catch (Exception e) {
            throw new HandlerException(e);
        }
    }
}
