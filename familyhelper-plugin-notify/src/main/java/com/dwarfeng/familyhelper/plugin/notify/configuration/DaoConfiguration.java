package com.dwarfeng.familyhelper.plugin.notify.configuration;

import com.dwarfeng.familyhelper.plugin.notify.bean.entity.SendExecutorSupport;
import com.dwarfeng.subgrade.impl.dao.MemoryBatchBaseDao;
import com.dwarfeng.subgrade.impl.dao.MemoryEntireLookupDao;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration("familyhelperDaoConfiguration")
public class DaoConfiguration {

    @Bean("sendExecutorSupportMemory")
    public Map<StringIdKey, SendExecutorSupport> sendExecutorSupportMemory() {
        return new LinkedHashMap<>();
    }

    @Bean
    public MemoryBatchBaseDao<StringIdKey, SendExecutorSupport> sendExecutorSupportBatchBaseDao() {
        return new MemoryBatchBaseDao<>(sendExecutorSupportMemory());
    }

    @Bean
    public MemoryEntireLookupDao<StringIdKey, SendExecutorSupport> sendExecutorSupportEntireLookupDao() {
        return new MemoryEntireLookupDao<>(sendExecutorSupportMemory());
    }
}
