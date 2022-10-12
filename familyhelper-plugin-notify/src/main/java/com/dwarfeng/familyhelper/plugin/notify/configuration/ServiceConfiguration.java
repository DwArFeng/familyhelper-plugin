package com.dwarfeng.familyhelper.plugin.notify.configuration;

import com.dwarfeng.familyhelper.plugin.notify.bean.entity.SendExecutorSupport;
import com.dwarfeng.familyhelper.plugin.notify.dao.SendExecutorSupportDao;
import com.dwarfeng.subgrade.impl.bean.key.ExceptionKeyFetcher;
import com.dwarfeng.subgrade.impl.service.DaoOnlyBatchCrudService;
import com.dwarfeng.subgrade.impl.service.DaoOnlyEntireLookupService;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("FamilyhelperServiceConfiguration")
public class ServiceConfiguration {

    private final SendExecutorSupportDao sendExecutorSupportDao;

    private final ServiceExceptionMapper sem;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ServiceConfiguration(
            SendExecutorSupportDao sendExecutorSupportDao,
            ServiceExceptionMapper sem
    ) {
        this.sendExecutorSupportDao = sendExecutorSupportDao;
        this.sem = sem;
    }

    @Bean
    public DaoOnlyBatchCrudService<StringIdKey, SendExecutorSupport> sendExecutorSupportBatchCrudService() {
        return new DaoOnlyBatchCrudService<>(
                sendExecutorSupportDao, new ExceptionKeyFetcher<>(), sem, LogLevel.WARN
        );
    }

    @Bean
    public DaoOnlyEntireLookupService<SendExecutorSupport> sendExecutorSupportEntireLookupService() {
        return new DaoOnlyEntireLookupService<>(
                sendExecutorSupportDao, sem, LogLevel.WARN
        );
    }
}
