package com.dwarfeng.familyhelper.plugin.notify.handler.sender;

import com.dwarfeng.familyhelper.clannad.stack.bean.dto.NotificationCreateInfo;
import com.dwarfeng.familyhelper.clannad.stack.service.NotificationOperateService;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Clannad 发送执行器工厂。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
@Component
public class ClannadSendExecutorFactory implements SendExecutorFactory {

    private final ApplicationContext ctx;

    private final NotificationOperateService notificationOperateService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ClannadSendExecutorFactory(
            ApplicationContext ctx,
            @Qualifier("notificationOperateService") NotificationOperateService notificationOperateService
    ) {
        this.ctx = ctx;
        this.notificationOperateService = notificationOperateService;
    }

    @Override
    public String provideType() {
        return "clannad_send_executor";
    }

    @Override
    public String provideLabel() {
        return "Clannad 发送执行器";
    }

    @Override
    public String provideDescription() {
        return "将消息发送到位于家庭助手 Clannad 模块的内置通知系统中";
    }

    @Override
    public String provideExampleParam() {
        return "";
    }

    @Override
    public Executor createExecutor(String param) {
        return ctx.getBean(ClannadSendExecutor.class, notificationOperateService);
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class ClannadSendExecutor implements Executor {

        private final NotificationOperateService notificationOperateService;

        public ClannadSendExecutor(NotificationOperateService notificationOperateService) {
            this.notificationOperateService = notificationOperateService;
        }

        @Override
        public void send(StringIdKey userKey, Object context) throws Exception {
            // 根据 context 解析发送文本。
            String message;
            if (context instanceof String) {
                message = (String) context;
            } else {
                message = "非文本消息，请使用其它途径查看！";
            }

            // 发送通知。
            notificationOperateService.createNotification(new NotificationCreateInfo(
                    userKey, message, "通过通知服务发送的消息文本"
            ));
        }
    }
}
