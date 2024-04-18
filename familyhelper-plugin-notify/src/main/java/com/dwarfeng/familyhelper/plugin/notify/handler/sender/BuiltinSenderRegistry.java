package com.dwarfeng.familyhelper.plugin.notify.handler.sender;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.familyhelper.clannad.stack.bean.dto.NotificationCreateInfo;
import com.dwarfeng.familyhelper.clannad.stack.service.NotificationOperateService;
import com.dwarfeng.familyhelper.plugin.commons.util.NotifyUtil;
import com.dwarfeng.notify.impl.handler.sender.AbstractSender;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
     * 将指定的发送器参数转换为字符串。
     *
     * @param config 指定的发送器参数。
     * @return 指定的参数转换成的字符串。
     */
    public static String stringifyParam(Config config) {
        return JSON.toJSONString(config, false);
    }

    /**
     * 从字符串中解析发送器参数。
     *
     * @param string 指定的字符串。
     * @return 解析指定的字符串获取到的发送器参数。
     */
    public static Config parseParam(String string) {
        return JSON.parseObject(string, Config.class);
    }

    /**
     * 将指定的占位符映射转换为字符串。
     *
     * @param placeholderMap 指定的占位符映射。
     * @return 指定的占位符映射转换成的字符串。
     */
    public static String stringifyPlaceholderMap(Map<String, Object> placeholderMap) {
        return NotifyUtil.stringifyBuiltinSenderPlaceholderMap(placeholderMap);
    }

    /**
     * 从指定的字符串中解析占位符映射。
     *
     * @param string 指定的字符串。
     * @return 解析指定的字符串获取到的占位符映射。
     */
    public static Map<String, Object> parsePlaceholderMap(String string) {
        return JSON.parseObject(string);
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
        Config config = new Config(
                "主题: 请检查名称为 #{[NAME]} 的设备",
                "正文: 设备 #{[NAME]} 出现了 #{[NUM]} 个报警，请及时处理",
                "your-placeholder-map-key-here"
        );
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
    public static class BuiltinSender extends AbstractSender {

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
        public List<Response> send(
                ContextInfo contextInfo, Map<String, String> sendInfoMap, List<StringIdKey> userKeys
        ) throws SenderException {
            try {
                // 定义结果列表。
                List<Response> result = new ArrayList<>();

                // 获取占位符映射的字符串形式。
                String placeholderMapString = Optional.ofNullable(sendInfoMap)
                        .map(map -> map.get(config.getPlaceholderMapKey())).orElse(StringUtils.EMPTY);

                // 解析发送文本。
                String subject;
                String body;
                if (StringUtils.isEmpty(placeholderMapString)) {
                    subject = config.getSubjectTemplate();
                    body = config.getBodyTemplate();
                } else {
                    Map<String, Object> placeholderMap = parsePlaceholderMap(placeholderMapString);
                    subject = expressionParser.parseExpression(config.getSubjectTemplate(), parserContext)
                            .getValue(placeholderMap, String.class);
                    body = expressionParser.parseExpression(config.getBodyTemplate(), parserContext)
                            .getValue(placeholderMap, String.class);
                }

                // 为每个用户发送消息。
                for (StringIdKey userKey : userKeys) {
                    result.add(sendSingleUser(subject, body, userKey));
                }

                // 返回结果列表。
                return result;
            } catch (Exception e) {
                throw new SenderExecutionException(e);
            }
        }

        private Response sendSingleUser(String subject, String body, StringIdKey userKey) {
            try {
                notificationOperateService.createNotification(new NotificationCreateInfo(
                        userKey, subject, body, "通过通知服务发送的消息文本"
                ));
                return new Response(userKey, true, "发送成功");
            } catch (Exception e) {
                LOGGER.warn("向用户 " + userKey + " 发送通知失败, 异常信息如下: ", e);
                return new Response(userKey, false, "发送失败");
            }
        }
    }

    public static class Config implements Bean {

        private static final long serialVersionUID = -7800582757320904399L;

        @JSONField(name = "subject_template", ordinal = 1)
        private String subjectTemplate;

        @JSONField(name = "body_template", ordinal = 2)
        private String bodyTemplate;

        @JSONField(name = "placeholder_map_key", ordinal = 3)
        private String placeholderMapKey;

        public Config() {
        }

        public Config(String subjectTemplate, String bodyTemplate, String placeholderMapKey) {
            this.subjectTemplate = subjectTemplate;
            this.bodyTemplate = bodyTemplate;
            this.placeholderMapKey = placeholderMapKey;
        }

        public String getSubjectTemplate() {
            return subjectTemplate;
        }

        public void setSubjectTemplate(String subjectTemplate) {
            this.subjectTemplate = subjectTemplate;
        }

        public String getBodyTemplate() {
            return bodyTemplate;
        }

        public void setBodyTemplate(String bodyTemplate) {
            this.bodyTemplate = bodyTemplate;
        }

        public String getPlaceholderMapKey() {
            return placeholderMapKey;
        }

        public void setPlaceholderMapKey(String placeholderMapKey) {
            this.placeholderMapKey = placeholderMapKey;
        }

        @Override
        public String toString() {
            return "Config{" +
                    "subjectTemplate='" + subjectTemplate + '\'' +
                    ", bodyTemplate='" + bodyTemplate + '\'' +
                    ", placeholderMapKey='" + placeholderMapKey + '\'' +
                    '}';
        }
    }
}
