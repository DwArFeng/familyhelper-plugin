package com.dwarfeng.familyhelper.plugin.notify.handler.sender;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.familyhelper.clannad.stack.bean.dto.NotificationCreateInfo;
import com.dwarfeng.familyhelper.clannad.stack.service.NotificationOperateService;
import com.dwarfeng.notify.impl.handler.sender.AbstractSenderRegistry;
import com.dwarfeng.notify.stack.exception.SenderException;
import com.dwarfeng.notify.stack.exception.SenderExecutionException;
import com.dwarfeng.notify.stack.exception.SenderMakeException;
import com.dwarfeng.notify.stack.handler.Sender;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 内置发送器注册。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
@Component
public class BuiltinSenderRegistry extends AbstractSenderRegistry {

    public static final String SENDER_TYPE = "builtin_sender";

    /**
     * 将指定的发送参数转换为参数。
     *
     * @param config 指定的发送参数。
     * @return 指定的参数转换成的参数。
     */
    public static String toParam(Config config) {
        return JSON.toJSONString(config, false);
    }

    /**
     * 解析参数并获取发送参数。
     *
     * @param param 指定的参数。
     * @return 解析参数获取到的发送参数。
     */
    public static Config parseParam(String param) {
        return JSON.parseObject(param, Config.class);
    }

    /**
     * 将指定的发送参数转换为发送信息文本。
     *
     * @param placeholderMap 指定的发送参数。
     * @return 指定的参数转换成的发送信息文本。
     */
    public static String toSendInfo(Map<String, Object> placeholderMap) {
        return JSON.toJSONString(placeholderMap, false);
    }

    /**
     * 解析发送信息文本并获取发送参数。
     *
     * @param sendInfo 指定的发送信息文本。
     * @return 解析发送信息文本获取到的发送参数。
     */
    public static Map<String, Object> parseSendInfo(String sendInfo) {
        return JSON.parseObject(sendInfo);
    }

    private final ApplicationContext ctx;

    private final NotificationOperateService notificationOperateService;

    private final ExpressionParser expressionParser;
    private final ParserContext parserContext;

    public BuiltinSenderRegistry(
            ApplicationContext ctx,
            @Qualifier("notificationOperateService") NotificationOperateService notificationOperateService,
            ExpressionParser expressionParser,
            ParserContext parserContext
    ) {
        super(SENDER_TYPE);
        this.ctx = ctx;
        this.notificationOperateService = notificationOperateService;
        this.expressionParser = expressionParser;
        this.parserContext = parserContext;
    }

    @Override
    public String provideLabel() {
        return "内置发送器";
    }

    @Override
    public String provideDescription() {
        return "将消息发送到内置的通知系统的发送器";
    }

    @Override
    public String provideExampleParam() {
        Config config = new Config("请检查名称为 #{[NAME]} 的设备");
        return JSON.toJSONString(config, false);
    }

    @Override
    public Sender makeSender(String type, String param) throws SenderException {
        try {
            // 通过 param 生成发送器的参数。
            Config config = parseParam(param);

            // 通过 ctx 生成发送器。
            return ctx.getBean(
                    BuiltinSender.class, notificationOperateService, expressionParser, parserContext, config
            );
        } catch (Exception e) {
            throw new SenderMakeException(e, type, param);
        }
    }

    @Override
    public String toString() {
        return "BuiltinSenderRegistry{" +
                "senderType='" + senderType + '\'' +
                '}';
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class BuiltinSender implements Sender {

        private static final Logger LOGGER = LoggerFactory.getLogger(BuiltinSender.class);

        private final NotificationOperateService notificationOperateService;

        private final ExpressionParser expressionParser;
        private final ParserContext parserContext;

        private final Config config;

        public BuiltinSender(
                NotificationOperateService notificationOperateService,
                ExpressionParser expressionParser,
                ParserContext parserContext,
                Config config
        ) {
            this.notificationOperateService = notificationOperateService;
            this.expressionParser = expressionParser;
            this.parserContext = parserContext;
            this.config = config;
        }

        @Override
        public List<Response> send(String sendInfo, List<StringIdKey> userKeys, Context context)
                throws SenderException {
            try {
                // 定义结果列表。
                List<Response> result = new ArrayList<>();

                // 解析发送文本。
                String message;
                if (StringUtils.isEmpty(sendInfo)) {
                    message = config.getTemplate();
                } else {
                    Map<String, Object> placeholderMap = parseSendInfo(sendInfo);
                    message = expressionParser.parseExpression(config.getTemplate(), parserContext)
                            .getValue(placeholderMap, String.class);
                }

                // 为每个用户发送消息。
                for (StringIdKey userKey : userKeys) {
                    result.add(sendSingleUser(message, userKey));
                }

                // 返回结果列表。
                return result;
            } catch (Exception e) {
                throw new SenderExecutionException(e);
            }
        }

        private Response sendSingleUser(String message, StringIdKey userKey) {
            try {
                notificationOperateService.createNotification(new NotificationCreateInfo(
                        userKey, message, "通过通知服务发送的消息文本"
                ));
                return new Response(userKey, new Date(), true, "发送成功");
            } catch (Exception e) {
                LOGGER.warn("向用户 " + userKey + " 发送通知失败, 异常信息如下: ", e);
                return new Response(userKey, new Date(), false, "发送失败");
            }
        }
    }

    public static class Config implements Bean {

        private static final long serialVersionUID = -8939272845398435273L;

        @JSONField(name = "template", ordinal = 1)
        private String template;

        public Config() {
        }

        public Config(String template) {
            this.template = template;
        }

        public String getTemplate() {
            return template;
        }

        public void setTemplate(String template) {
            this.template = template;
        }

        @Override
        public String toString() {
            return "Config{" +
                    "template='" + template + '\'' +
                    '}';
        }
    }
}
