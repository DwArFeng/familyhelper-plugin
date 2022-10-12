package com.dwarfeng.familyhelper.plugin.notify.handler.sender;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.familyhelper.clannad.stack.bean.entity.NotifyMeta;
import com.dwarfeng.familyhelper.clannad.stack.bean.entity.NotifyTopic;
import com.dwarfeng.familyhelper.clannad.stack.bean.key.NotifyNodeKey;
import com.dwarfeng.familyhelper.clannad.stack.service.NotifyMetaMaintainService;
import com.dwarfeng.familyhelper.clannad.stack.service.NotifyTopicMaintainService;
import com.dwarfeng.notify.impl.handler.sender.AbstractSenderRegistry;
import com.dwarfeng.notify.stack.exception.SenderException;
import com.dwarfeng.notify.stack.exception.SenderExecutionException;
import com.dwarfeng.notify.stack.exception.SenderMakeException;
import com.dwarfeng.notify.stack.handler.Sender;
import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 家庭助手发送器注册。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class FamilyhelperSenderRegistry extends AbstractSenderRegistry {

    public static final String SENDER_TYPE = "familyhelper_sender";

    private final ApplicationContext ctx;

    private final NotifyMetaMaintainService notifyMetaMaintainService;
    private final NotifyTopicMaintainService notifyTopicMaintainService;

    private final List<SendExecutorFactory> sendExecutorFactories;

    public FamilyhelperSenderRegistry(
            ApplicationContext ctx,
            @Qualifier("notifyMetaMaintainService") NotifyMetaMaintainService notifyMetaMaintainService,
            @Qualifier("notifyTopicMaintainService") NotifyTopicMaintainService notifyTopicMaintainService,
            List<SendExecutorFactory> sendExecutorFactories
    ) {
        super(SENDER_TYPE);
        this.ctx = ctx;
        this.notifyMetaMaintainService = notifyMetaMaintainService;
        this.notifyTopicMaintainService = notifyTopicMaintainService;
        if (Objects.isNull(sendExecutorFactories)) {
            this.sendExecutorFactories = Collections.emptyList();
        } else {
            this.sendExecutorFactories = sendExecutorFactories;
        }
    }

    @Override
    public String provideLabel() {
        return "家庭助手发送器";
    }

    @Override
    public String provideDescription() {
        return "家庭助手工程使用的唯一发送器。";
    }

    @Override
    public String provideExampleParam() {
        Config config = new Config(1027169665828253696L, "010.clannad");
        return JSON.toJSONString(config, true);
    }

    @Override
    public Sender makeSender(String type, String param) throws SenderException {
        try {
            Config config = JSON.parseObject(param, Config.class);
            return ctx.getBean(
                    FamilyhelperSender.class,
                    config, notifyMetaMaintainService, notifyTopicMaintainService, sendExecutorFactories
            );
        } catch (Exception e) {
            throw new SenderMakeException(e, type, param);
        }
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class FamilyhelperSender implements Sender {

        private final Config config;

        private final NotifyMetaMaintainService notifyMetaMaintainService;
        private final NotifyTopicMaintainService notifyTopicMaintainService;

        private final List<SendExecutorFactory> sendExecutorFactories;

        public FamilyhelperSender(
                Config config,
                NotifyMetaMaintainService notifyMetaMaintainService,
                NotifyTopicMaintainService notifyTopicMaintainService,
                List<SendExecutorFactory> sendExecutorFactories
        ) {
            this.config = config;
            this.notifyMetaMaintainService = notifyMetaMaintainService;
            this.notifyTopicMaintainService = notifyTopicMaintainService;
            this.sendExecutorFactories = sendExecutorFactories;
        }

        @Override
        public void send(StringIdKey userKey, Object context) throws SenderException {
            try {
                // 更新通知元数据。
                NotifyMeta notifyMeta = new NotifyMeta(
                        new NotifyNodeKey(config.getNotifySettingId(), config.topicId, userKey.getStringId()),
                        new Date(),
                        "通过 Familyhelper Sender 更新"
                );
                notifyMetaMaintainService.insertOrUpdate(notifyMeta);

                // 获取通知的详细信息，根据详细信息构造发送执行器。
                SendExecutorFactory.Executor executor = createExecutor();

                // 调用执行器的代理方法。
                executor.send(userKey, context);
            } catch (Exception e) {
                throw new SenderExecutionException(e);
            }
        }

        @Override
        public void batchSend(List<StringIdKey> userKeys, Object context) throws SenderException {
            try {
                // 批量更新通知元数据。
                List<NotifyMeta> notifyMetas = new ArrayList<>();
                for (StringIdKey userKey : userKeys) {
                    notifyMetas.add(new NotifyMeta(
                            new NotifyNodeKey(config.getNotifySettingId(), config.topicId, userKey.getStringId()),
                            new Date(),
                            "通过 Familyhelper Sender 更新"
                    ));
                }
                notifyMetaMaintainService.batchInsertOrUpdate(notifyMetas);

                // 获取通知的详细信息，根据详细信息构造发送执行器。
                SendExecutorFactory.Executor executor = createExecutor();

                // 调用执行器的代理方法。
                executor.batchSend(userKeys, context);
            } catch (Exception e) {
                throw new SenderExecutionException(e);
            }
        }

        private SendExecutorFactory.Executor createExecutor() throws Exception {
            NotifyTopic notifyTopic = notifyTopicMaintainService.get(new StringIdKey(config.getTopicId()));
            String executorType = notifyTopic.getExecutorType();
            String executorParam = notifyTopic.getExecutorParam();
            SendExecutorFactory executorFactory = sendExecutorFactories.stream()
                    .filter(factory -> Objects.equals(executorType, factory.provideType())).findAny().orElse(null);
            if (Objects.isNull(executorFactory)) {
                throw new IllegalArgumentException("未知的执行器类型: " + executorType);
            }
            return executorFactory.createExecutor(executorParam);
        }
    }

    public static class Config implements Dto {

        private static final long serialVersionUID = -4993350823144059911L;

        @JSONField(name = "notify_setting_id", ordinal = 1)
        private long notifySettingId;

        @JSONField(name = "topic_id", ordinal = 2)
        private String topicId;

        public Config() {
        }

        public Config(long notifySettingId, String topicId) {
            this.notifySettingId = notifySettingId;
            this.topicId = topicId;
        }

        public long getNotifySettingId() {
            return notifySettingId;
        }

        public void setNotifySettingId(long notifySettingId) {
            this.notifySettingId = notifySettingId;
        }

        public String getTopicId() {
            return topicId;
        }

        public void setTopicId(String topicId) {
            this.topicId = topicId;
        }

        @Override
        public String toString() {
            return "Config{" +
                    "notifySettingId=" + notifySettingId +
                    ", topicId='" + topicId + '\'' +
                    '}';
        }
    }
}
