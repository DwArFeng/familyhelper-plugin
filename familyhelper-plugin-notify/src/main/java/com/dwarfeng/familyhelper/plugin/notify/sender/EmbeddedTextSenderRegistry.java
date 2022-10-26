package com.dwarfeng.familyhelper.plugin.notify.sender;

import com.dwarfeng.familyhelper.clannad.stack.bean.dto.NotificationCreateInfo;
import com.dwarfeng.familyhelper.clannad.stack.service.NotificationOperateService;
import com.dwarfeng.notify.impl.handler.sender.AbstractSenderRegistry;
import com.dwarfeng.notify.stack.exception.SenderException;
import com.dwarfeng.notify.stack.exception.SenderMakeException;
import com.dwarfeng.notify.stack.handler.Sender;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 内置发送器注册。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
@Component
public class EmbeddedTextSenderRegistry extends AbstractSenderRegistry {

    public static final String SENDER_TYPE = "embedded_text_sender";

    private final ApplicationContext ctx;

    public EmbeddedTextSenderRegistry(ApplicationContext ctx) {
        super(SENDER_TYPE);
        this.ctx = ctx;
    }

    @Override
    public String provideLabel() {
        return "内置文本发送器";
    }

    @Override
    public String provideDescription() {
        return "将通知发送到内置的系统中。虽然登录系统后可方便地查看，但是不登录系统则无法查看。";
    }

    @Override
    public String provideExampleParam() {
        return "";
    }

    @Override
    public Sender makeSender(String type, String param) throws SenderException {
        try {
            return ctx.getBean(EmbeddedSender.class);
        } catch (Exception e) {
            throw new SenderMakeException(e, type, param);
        }
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class EmbeddedSender implements Sender {

        private final NotificationOperateService notificationOperateService;

        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
        public EmbeddedSender(
                @Qualifier("familyhelperClannadNotificationOperateService")
                NotificationOperateService notificationOperateService
        ) {
            this.notificationOperateService = notificationOperateService;
        }

        @Override
        public void send(StringIdKey userKey, Object context) throws SenderException {
            if (!(context instanceof String)) {
                throw new SenderException("context 对象必须属于 String 类型");
            }
            NotificationCreateInfo createInfo = new NotificationCreateInfo(
                    userKey, (String) context, "通过 familyhelper 通知插件创建"
            );
            try {
                notificationOperateService.createNotification(createInfo);
            } catch (ServiceException e) {
                throw new SenderException("调用通知服务请求时发生异常, 异常信息如下: ", e);
            }
        }
    }
}
