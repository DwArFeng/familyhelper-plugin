package com.dwarfeng.familyhelper.plugin.notify.dao;

import com.dwarfeng.dutil.basic.str.StringComparator;
import com.dwarfeng.familyhelper.plugin.notify.bean.entity.SendExecutorSupport;
import com.dwarfeng.familyhelper.plugin.notify.handler.sender.SendExecutorFactory;
import com.dwarfeng.subgrade.impl.dao.MemoryBatchBaseDao;
import com.dwarfeng.subgrade.impl.dao.MemoryEntireLookupDao;
import com.dwarfeng.subgrade.sdk.interceptor.analyse.BehaviorAnalyse;
import com.dwarfeng.subgrade.sdk.interceptor.analyse.SkipRecord;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class SendExecutorSupportDaoImpl implements SendExecutorSupportDao {

    private final Map<StringIdKey, SendExecutorSupport> memory;
    private final MemoryBatchBaseDao<StringIdKey, SendExecutorSupport> batchBaseDao;
    private final MemoryEntireLookupDao<StringIdKey, SendExecutorSupport> entireLookupDao;

    private final List<SendExecutorFactory> sendExecutorFactories;

    public SendExecutorSupportDaoImpl(
            @Qualifier("sendExecutorSupportMemory") Map<StringIdKey, SendExecutorSupport> memory,
            MemoryBatchBaseDao<StringIdKey, SendExecutorSupport> batchBaseDao,
            MemoryEntireLookupDao<StringIdKey, SendExecutorSupport> entireLookupDao,
            List<SendExecutorFactory> sendExecutorFactories
    ) {
        this.memory = memory;
        this.batchBaseDao = batchBaseDao;
        this.entireLookupDao = entireLookupDao;
        if (Objects.isNull(sendExecutorFactories)) {
            this.sendExecutorFactories = Collections.emptyList();
        } else {
            this.sendExecutorFactories = sendExecutorFactories;
        }
    }

    @PostConstruct
    public void init() {
        List<SendExecutorSupport> sendExecutorSupports = sendExecutorFactories.stream().map(
                factory -> new SendExecutorSupport(
                        new StringIdKey(factory.provideType()), factory.provideLabel(), factory.provideDescription(),
                        factory.provideExampleParam())).sorted(SendExecutorSupportComparator.INSTANCE
        ).collect(Collectors.toList());
        for (SendExecutorSupport sendExecutorSupport : sendExecutorSupports) {
            memory.put(sendExecutorSupport.getKey(), sendExecutorSupport);
        }
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", rollbackFor = Exception.class)
    public StringIdKey insert(SendExecutorSupport element) throws DaoException {
        return batchBaseDao.insert(element);
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", rollbackFor = Exception.class)
    public void update(SendExecutorSupport element) throws DaoException {
        batchBaseDao.update(element);
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", rollbackFor = Exception.class)
    public void delete(StringIdKey key) throws DaoException {
        batchBaseDao.delete(key);
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", readOnly = true, rollbackFor = Exception.class)
    public boolean exists(StringIdKey key) {
        return batchBaseDao.exists(key);
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", readOnly = true, rollbackFor = Exception.class)
    public SendExecutorSupport get(StringIdKey key) throws DaoException {
        return batchBaseDao.get(key);
    }

    @Override
    @BehaviorAnalyse
    @SkipRecord
    @Transactional(transactionManager = "hibernateTransactionManager", rollbackFor = Exception.class)
    public List<StringIdKey> batchInsert(@SkipRecord List<SendExecutorSupport> elements) throws DaoException {
        return batchBaseDao.batchInsert(elements);
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", rollbackFor = Exception.class)
    public void batchUpdate(@SkipRecord List<SendExecutorSupport> elements) throws DaoException {
        batchBaseDao.batchUpdate(elements);
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", rollbackFor = Exception.class)
    public void batchDelete(@SkipRecord List<StringIdKey> keys) {
        batchBaseDao.batchDelete(keys);
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", readOnly = true, rollbackFor = Exception.class)
    public boolean allExists(@SkipRecord List<StringIdKey> keys) {
        return batchBaseDao.allExists(keys);
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", readOnly = true, rollbackFor = Exception.class)
    public boolean nonExists(@SkipRecord List<StringIdKey> keys) {
        return batchBaseDao.nonExists(keys);
    }

    @Override
    @BehaviorAnalyse
    @SkipRecord
    @Transactional(transactionManager = "hibernateTransactionManager", readOnly = true, rollbackFor = Exception.class)
    public List<SendExecutorSupport> batchGet(@SkipRecord List<StringIdKey> keys) {
        return batchBaseDao.batchGet(keys);
    }

    @Override
    @BehaviorAnalyse
    @SkipRecord
    @Transactional(transactionManager = "hibernateTransactionManager", readOnly = true, rollbackFor = Exception.class)
    public List<SendExecutorSupport> lookup() {
        return entireLookupDao.lookup();
    }

    @Override
    @BehaviorAnalyse
    @SkipRecord
    @Transactional(transactionManager = "hibernateTransactionManager", readOnly = true, rollbackFor = Exception.class)
    public List<SendExecutorSupport> lookup(PagingInfo pagingInfo) {
        return entireLookupDao.lookup(pagingInfo);
    }

    @Override
    @BehaviorAnalyse
    @Transactional(transactionManager = "hibernateTransactionManager", readOnly = true, rollbackFor = Exception.class)
    public int lookupCount() {
        return entireLookupDao.lookupCount();
    }

    private static class SendExecutorSupportComparator implements Comparator<SendExecutorSupport> {

        public static final SendExecutorSupportComparator INSTANCE = new SendExecutorSupportComparator();

        private static final StringComparator STRING_COMPARATOR = new StringComparator();

        private SendExecutorSupportComparator() {
        }

        @Override
        public int compare(SendExecutorSupport o1, SendExecutorSupport o2) {
            String id1 = o1.getKey().getStringId();
            String id2 = o2.getKey().getStringId();
            return STRING_COMPARATOR.compare(id1, id2);
        }
    }
}
