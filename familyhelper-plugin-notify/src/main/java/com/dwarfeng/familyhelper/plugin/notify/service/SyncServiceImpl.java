package com.dwarfeng.familyhelper.plugin.notify.service;

import com.dwarfeng.familyhelper.plugin.notify.handler.SyncHandler;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import org.springframework.stereotype.Service;

@Service
public class SyncServiceImpl implements SyncService {

    private final SyncHandler syncHandler;

    private final ServiceExceptionMapper sem;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public SyncServiceImpl(SyncHandler syncHandler, ServiceExceptionMapper sem) {
        this.syncHandler = syncHandler;
        this.sem = sem;
    }

    @Override
    public void syncRouterForNotifySetting(LongIdKey notifySettingKey) throws ServiceException {
        try {
            syncHandler.syncRouterForNotifySetting(notifySettingKey);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("为通知设置同步路由器时发生异常", LogLevel.WARN, sem, e);
        }
    }

    @Override
    public void syncSenderForNotifySetting(LongIdKey notifySettingKey) throws ServiceException {
        try {
            syncHandler.syncSenderForNotifySetting(notifySettingKey);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("为通知设置同步发送器时发生异常", LogLevel.WARN, sem, e);
        }
    }

    @Override
    public void syncSenderForTopic(StringIdKey topicKey) throws ServiceException {
        try {
            syncHandler.syncSenderForTopic(topicKey);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("为主题同步发送器时发生异常", LogLevel.WARN, sem, e);
        }
    }
}
