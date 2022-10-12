package com.dwarfeng.familyhelper.plugin.notify.service;

import com.dwarfeng.familyhelper.plugin.notify.bean.entity.SendExecutorSupport;
import com.dwarfeng.subgrade.impl.service.DaoOnlyBatchCrudService;
import com.dwarfeng.subgrade.impl.service.DaoOnlyEntireLookupService;
import com.dwarfeng.subgrade.sdk.interceptor.analyse.BehaviorAnalyse;
import com.dwarfeng.subgrade.sdk.interceptor.analyse.SkipRecord;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SendExecutorSupportMaintainServiceImpl implements SendExecutorSupportMaintainService {

    private final DaoOnlyBatchCrudService<StringIdKey, SendExecutorSupport> crudService;
    private final DaoOnlyEntireLookupService<SendExecutorSupport> entireLookupService;

    public SendExecutorSupportMaintainServiceImpl(
            DaoOnlyBatchCrudService<StringIdKey, SendExecutorSupport> crudService,
            DaoOnlyEntireLookupService<SendExecutorSupport> entireLookupService
    ) {
        this.crudService = crudService;
        this.entireLookupService = entireLookupService;
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", readOnly = true, rollbackFor = Exception.class)
    public boolean exists(StringIdKey key) throws ServiceException {
        return crudService.exists(key);
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", readOnly = true, rollbackFor = Exception.class)
    public SendExecutorSupport get(StringIdKey key) throws ServiceException {
        return crudService.get(key);
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", rollbackFor = Exception.class)
    public StringIdKey insert(SendExecutorSupport element) throws ServiceException {
        return crudService.insert(element);
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", rollbackFor = Exception.class)
    public void update(SendExecutorSupport element) throws ServiceException {
        crudService.update(element);
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", rollbackFor = Exception.class)
    public void delete(StringIdKey key) throws ServiceException {
        crudService.delete(key);
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", readOnly = true, rollbackFor = Exception.class)
    public SendExecutorSupport getIfExists(StringIdKey key) throws ServiceException {
        return crudService.getIfExists(key);
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", rollbackFor = Exception.class)
    public StringIdKey insertIfNotExists(SendExecutorSupport element) throws ServiceException {
        return crudService.insertIfNotExists(element);
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", rollbackFor = Exception.class)
    public void updateIfExists(SendExecutorSupport element) throws ServiceException {
        crudService.updateIfExists(element);
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", rollbackFor = Exception.class)
    public void deleteIfExists(StringIdKey key) throws ServiceException {
        crudService.deleteIfExists(key);
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", rollbackFor = Exception.class)
    public StringIdKey insertOrUpdate(SendExecutorSupport element) throws ServiceException {
        return crudService.insertOrUpdate(element);
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", readOnly = true, rollbackFor = Exception.class)
    public boolean allExists(@SkipRecord List<StringIdKey> keys) throws ServiceException {
        return crudService.allExists(keys);
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", readOnly = true, rollbackFor = Exception.class)
    public boolean nonExists(@SkipRecord List<StringIdKey> keys) throws ServiceException {
        return crudService.nonExists(keys);
    }

    @Override
    @BehaviorAnalyse
    @SkipRecord
    @Transactional(transactionManager = "hibernateTransactionManager", readOnly = true, rollbackFor = Exception.class)
    public List<SendExecutorSupport> batchGet(@SkipRecord List<StringIdKey> keys) throws ServiceException {
        return crudService.batchGet(keys);
    }

    @Override
    @BehaviorAnalyse
    @SkipRecord
    @Transactional(transactionManager = "hibernateTransactionManager", rollbackFor = Exception.class)
    public List<StringIdKey> batchInsert(@SkipRecord List<SendExecutorSupport> elements) throws ServiceException {
        return crudService.batchInsert(elements);
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", rollbackFor = Exception.class)
    public void batchUpdate(@SkipRecord List<SendExecutorSupport> elements) throws ServiceException {
        crudService.batchUpdate(elements);
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", rollbackFor = Exception.class)
    public void batchDelete(@SkipRecord List<StringIdKey> keys) throws ServiceException {
        crudService.batchDelete(keys);
    }

    @Override
    @BehaviorAnalyse
    @SkipRecord
    @Transactional(transactionManager = "hibernateTransactionManager", readOnly = true, rollbackFor = Exception.class)
    public List<SendExecutorSupport> batchGetIfExists(@SkipRecord List<StringIdKey> keys) throws ServiceException {
        return crudService.batchGetIfExists(keys);
    }

    @Override
    @BehaviorAnalyse
    @SkipRecord
    @Transactional(transactionManager = "hibernateTransactionManager", rollbackFor = Exception.class)
    public List<StringIdKey> batchInsertIfExists(@SkipRecord List<SendExecutorSupport> elements) throws ServiceException {
        return crudService.batchInsertIfExists(elements);
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", rollbackFor = Exception.class)
    public void batchUpdateIfExists(@SkipRecord List<SendExecutorSupport> elements) throws ServiceException {
        crudService.batchUpdateIfExists(elements);
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", rollbackFor = Exception.class)
    public void batchDeleteIfExists(@SkipRecord List<StringIdKey> keys) throws ServiceException {
        crudService.batchDeleteIfExists(keys);
    }

    @Override
    @BehaviorAnalyse
    @SkipRecord
    @Transactional(transactionManager = "hibernateTransactionManager", rollbackFor = Exception.class)
    public List<StringIdKey> batchInsertOrUpdate(@SkipRecord List<SendExecutorSupport> elements) throws ServiceException {
        return crudService.batchInsertOrUpdate(elements);
    }

    @Override
    @BehaviorAnalyse
    @SkipRecord
    @Transactional(transactionManager = "hibernateTransactionManager", readOnly = true, rollbackFor = Exception.class)
    public PagedData<SendExecutorSupport> lookup() throws ServiceException {
        return entireLookupService.lookup();
    }

    @Override
    @BehaviorAnalyse
    @SkipRecord
    @Transactional(transactionManager = "hibernateTransactionManager", readOnly = true, rollbackFor = Exception.class)
    public PagedData<SendExecutorSupport> lookup(PagingInfo pagingInfo) throws ServiceException {
        return entireLookupService.lookup(pagingInfo);
    }
}
