package com.dwarfeng.familyhelper.plugin.notify.dao;

import com.dwarfeng.familyhelper.plugin.notify.bean.entity.SendExecutorSupport;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.dao.BatchBaseDao;
import com.dwarfeng.subgrade.stack.dao.EntireLookupDao;

/**
 * 发送执行器支持数据访问层。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public interface SendExecutorSupportDao extends BatchBaseDao<StringIdKey, SendExecutorSupport>,
        EntireLookupDao<SendExecutorSupport> {
}
