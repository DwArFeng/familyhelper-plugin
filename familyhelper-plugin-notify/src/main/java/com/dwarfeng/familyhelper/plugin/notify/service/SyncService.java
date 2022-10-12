package com.dwarfeng.familyhelper.plugin.notify.service;

import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.exception.ServiceException;

/**
 * @author DwArFeng
 * @since 1.0.0
 */
public interface SyncService {

    /**
     * 为通知设置同步路由器。
     *
     * @param notifySettingKey 通知设置主键。
     * @throws ServiceException 服务异常。
     */
    void syncRouterForNotifySetting(LongIdKey notifySettingKey) throws ServiceException;

    /**
     * 为通知设置同步发送器。
     *
     * @param notifySettingKey 通知设置主键。
     * @throws ServiceException 服务异常。
     */
    void syncSenderForNotifySetting(LongIdKey notifySettingKey) throws ServiceException;

    /**
     * 为主题同步发送器。
     *
     * @param topicKey 主题主键。
     * @throws ServiceException 服务异常。
     */
    void syncSenderForTopic(StringIdKey topicKey) throws ServiceException;
}
