package com.dwarfeng.familyhelper.plugin.notify.handler;

import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.subgrade.stack.handler.Handler;

/**
 * 同步处理器。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public interface SyncHandler extends Handler {

    /**
     * 为通知设置同步路由器。
     *
     * @param notifySettingKey 通知设置主键。
     * @throws HandlerException 处理器异常。
     */
    void syncRouterForNotifySetting(LongIdKey notifySettingKey) throws HandlerException;

    /**
     * 为通知设置同步发送器。
     *
     * @param notifySettingKey 通知设置主键。
     * @throws HandlerException 处理器异常。
     */
    void syncSenderForNotifySetting(LongIdKey notifySettingKey) throws HandlerException;

    /**
     * 为主题同步发送器。
     *
     * @param topicKey 主题主键。
     * @throws HandlerException 处理器异常。
     */
    void syncSenderForTopic(StringIdKey topicKey) throws HandlerException;
}
