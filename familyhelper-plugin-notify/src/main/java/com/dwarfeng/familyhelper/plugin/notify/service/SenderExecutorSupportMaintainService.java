package com.dwarfeng.familyhelper.plugin.notify.service;

import com.dwarfeng.familyhelper.plugin.notify.bean.entity.SendExecutorSupport;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.service.BatchCrudService;
import com.dwarfeng.subgrade.stack.service.EntireLookupService;

/**
 * 发送执行器支持维护服务。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public interface SenderExecutorSupportMaintainService extends BatchCrudService<StringIdKey, SendExecutorSupport>,
        EntireLookupService<SendExecutorSupport> {
}
