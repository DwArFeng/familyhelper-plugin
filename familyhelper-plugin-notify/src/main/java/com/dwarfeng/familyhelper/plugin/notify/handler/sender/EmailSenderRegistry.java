package com.dwarfeng.familyhelper.plugin.notify.handler.sender;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.familyhelper.clannad.stack.bean.entity.Profile;
import com.dwarfeng.familyhelper.clannad.stack.service.ProfileMaintainService;
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

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 电子邮件发送器注册。
 *
 * @author DwArFeng
 * @since 1.0.1
 */
@Component
public class EmailSenderRegistry extends AbstractSenderRegistry {

    public static final String SENDER_TYPE = "email_sender";

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
        return JSON.toJSONString(placeholderMap, false);
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

    private final ProfileMaintainService profileMaintainService;

    private final ExpressionParser expressionParser;
    private final ParserContext parserContext;

    public EmailSenderRegistry(
            ApplicationContext ctx,
            @Qualifier("profileMaintainService") ProfileMaintainService profileMaintainService,
            ExpressionParser expressionParser,
            ParserContext parserContext
    ) {
        super(SENDER_TYPE);
        this.ctx = ctx;
        this.profileMaintainService = profileMaintainService;
        this.expressionParser = expressionParser;
        this.parserContext = parserContext;
    }

    @Override
    public String provideLabel() {
        return "电子邮件发送器";
    }

    @Override
    public String provideDescription() {
        return "将消息发送到用户电子邮件的发送器，如果用户没设置电子邮件，则不发送";
    }

    @Override
    public String provideExampleParam() {
        Config config = new Config(
                "smtp-host-here", true, "username-here", "password-here", "mail-from-here",
                "请检查名称为 #{[NAME]} 的设备", "设备 \"#{[NAME]}\" 于 #{[DATE]} 发出报警信号，请检查！",
                Arrays.asList(
                        new Config.Attachment("报警报告-#{[DATE]}.pdf", "#{[ALARM_CONTENT]}"),
                        new Config.Attachment("解决方案参考.pdf", "#{[SOLUTION]}")
                ),
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
                    EmailSender.class, profileMaintainService, expressionParser, parserContext, config
            );
        } catch (Exception e) {
            throw new SenderMakeException(e, type, param);
        }
    }

    @Override
    public String toString() {
        return "EmailSenderRegistry{" +
                "senderType='" + senderType + '\'' +
                '}';
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class EmailSender implements Sender {

        private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);

        private static final String ATTACHMENT_CONTENT_TYPE = "application/octet-stream";

        private final ProfileMaintainService profileMaintainService;

        private final ExpressionParser expressionParser;
        private final ParserContext parserContext;

        private final Config config;

        public EmailSender(
                ProfileMaintainService profileMaintainService,
                ExpressionParser expressionParser,
                ParserContext parserContext,
                Config config
        ) {
            this.profileMaintainService = profileMaintainService;
            this.expressionParser = expressionParser;
            this.parserContext = parserContext;
            this.config = config;
        }

        @Override
        public List<Response> send(Map<String, String> sendInfoMap, List<StringIdKey> userKeys, Context context)
                throws SenderException {
            try {
                // 定义结果。
                List<Response> result = new ArrayList<>();

                // 建立中间变量（会话和信息模板）。
                Session session = buildSession();
                Message messageTemplate = buildMessageTemplate(sendInfoMap, session);

                // 遍历用户，调用发送动作。
                for (StringIdKey userKey : userKeys) {
                    Response response = sendMail(messageTemplate, userKey);
                    result.add(response);
                }

                // 返回结果。
                return result;
            } catch (Exception e) {
                throw new SenderExecutionException(e);
            }
        }

        private Session buildSession() {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", config.getSmtpHost());
            properties.put("mail.smtp.auth", config.isSmtpAuth());

            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(config.getUsername(), config.getPassword());
                }
            };

            return Session.getInstance(properties, authenticator);
        }

        private Message buildMessageTemplate(Map<String, String> sendInfoMap, Session session) throws Exception {
            String subject;
            String text;
            List<Attachment> attachments;

            // 获取占位符映射的字符串形式。
            String placeholderMapString = Optional.ofNullable(sendInfoMap)
                    .map(map -> map.get(config.getPlaceholderMapKey())).orElse(StringUtils.EMPTY);

            // 获取占位符映射字符串，并返回结果。
            if (StringUtils.isEmpty(placeholderMapString)) {
                subject = config.getSubjectTemplate();
                text = config.getTextTemplate();
                attachments = config.getAttachments().stream().map(
                        a -> new Attachment(a.getNameTemplate(), a.getContentTemplate())
                ).collect(Collectors.toList());
            } else {
                Map<String, Object> placeholderMap = parsePlaceholderMap(placeholderMapString);
                subject = expressionParser.parseExpression(config.getSubjectTemplate(), parserContext)
                        .getValue(placeholderMap, String.class);
                text = expressionParser.parseExpression(config.getTextTemplate(), parserContext)
                        .getValue(placeholderMap, String.class);
                attachments = new ArrayList<>();
                for (Config.Attachment configAttachment : config.getAttachments()) {
                    String name = expressionParser.parseExpression(
                            configAttachment.getNameTemplate(), parserContext
                    ).getValue(placeholderMap, String.class);
                    String content = expressionParser.parseExpression(
                            configAttachment.getContentTemplate(), parserContext
                    ).getValue(placeholderMap, String.class);
                    attachments.add(new Attachment(name, content));
                }
            }

            // 定义信息模板。
            Message messageTemplate = new MimeMessage(session);
            messageTemplate.setFrom(new InternetAddress(config.getMailFrom()));
            messageTemplate.setSubject(subject);
            messageTemplate.setSentDate(new Date());

            // 定义部件结构体。
            Multipart multipart = new MimeMultipart();

            // 添加正文结构体。
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(text);
            multipart.addBodyPart(textBodyPart);

            // 添加附件结构体。
            for (Attachment attachment : attachments) {
                MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                attachmentBodyPart.setFileName(attachment.getName());
                byte[] content = Base64.getDecoder().decode(attachment.getContent());
                attachmentBodyPart.setDataHandler(new DataHandler(
                        new ByteArrayDataSource(content, ATTACHMENT_CONTENT_TYPE)
                ));
                multipart.addBodyPart(attachmentBodyPart);
            }

            // 模板设置部件结构体。
            messageTemplate.setContent(multipart);

            // 返回结果。
            return messageTemplate;
        }

        private Response sendMail(Message messageTemplate, StringIdKey userKey) {
            // 查询用户的邮箱。
            String emailAddressString;
            try {
                emailAddressString = Optional.ofNullable(profileMaintainService.getIfExists(userKey))
                        .map(Profile::getEmailAddress).orElse(null);
            } catch (Exception e) {
                LOGGER.warn("个人简介接口调用失败, 电子邮件将不会发送, 异常信息如下: ", e);
                return new Response(userKey, false, "个人简介接口调用失败");
            }

            // 如果 emailAddressString 没填写，则返回发送失败。
            if (Objects.isNull(emailAddressString)) {
                return new Response(userKey, false, "未维护邮箱地址");
            }

            // 尝试解析电子邮件地址。
            InternetAddress emailAddress;
            try {
                emailAddress = new InternetAddress(emailAddressString);
            } catch (Exception e) {
                LOGGER.warn("无法解析电子邮件地址, 电子邮件将不会发送, 异常信息如下: ", e);
                return new Response(userKey, false, "无法解析电子邮件地址");
            }

            // 设置收件人。
            try {
                messageTemplate.setRecipients(Message.RecipientType.TO, new Address[]{emailAddress});
            } catch (Exception e) {
                LOGGER.warn("无法设置消息头, 电子邮件将不会发送, 异常信息如下: ", e);
                return new Response(userKey, false, "无法设置消息头");
            }

            // 尝试发送。
            try {
                Transport.send(messageTemplate);
                return new Response(userKey, true, "发送成功");
            } catch (Exception e) {
                LOGGER.warn("邮件发送失败,  异常信息如下: ", e);
                return new Response(userKey, false, "邮件发送失败");
            }
        }

        private static final class Attachment {

            private final String name;
            private final String content;

            public Attachment(String name, String content) {
                this.name = name;
                this.content = content;
            }

            public String getName() {
                return name;
            }

            public String getContent() {
                return content;
            }

            @Override
            public String toString() {
                return "Attachment{" +
                        "name='" + name + '\'' +
                        ", content='" + content + '\'' +
                        '}';
            }
        }
    }

    public static class Config implements Bean {

        private static final long serialVersionUID = -8235662751642994281L;

        @JSONField(name = "smtp_host", ordinal = 1)
        private String smtpHost;

        @JSONField(name = "smtp_auth", ordinal = 2)
        private boolean smtpAuth;

        @JSONField(name = "username", ordinal = 3)
        private String username;

        @JSONField(name = "password", ordinal = 4)
        private String password;

        @JSONField(name = "mail_from", ordinal = 5)
        private String mailFrom;

        @JSONField(name = "subject_template", ordinal = 6)
        private String subjectTemplate;

        @JSONField(name = "text_template", ordinal = 7)
        private String textTemplate;

        @JSONField(name = "attachments", ordinal = 8)
        private List<Attachment> attachments;

        @JSONField(name = "placeholder_map_key", ordinal = 9)
        private String placeholderMapKey;

        public Config() {
        }

        public Config(
                String smtpHost, boolean smtpAuth, String username, String password, String mailFrom,
                String subjectTemplate, String textTemplate, List<Attachment> attachments, String placeholderMapKey
        ) {
            this.smtpHost = smtpHost;
            this.smtpAuth = smtpAuth;
            this.username = username;
            this.password = password;
            this.mailFrom = mailFrom;
            this.subjectTemplate = subjectTemplate;
            this.textTemplate = textTemplate;
            this.attachments = attachments;
            this.placeholderMapKey = placeholderMapKey;
        }

        public String getSmtpHost() {
            return smtpHost;
        }

        public void setSmtpHost(String smtpHost) {
            this.smtpHost = smtpHost;
        }

        public boolean isSmtpAuth() {
            return smtpAuth;
        }

        public void setSmtpAuth(boolean smtpAuth) {
            this.smtpAuth = smtpAuth;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getMailFrom() {
            return mailFrom;
        }

        public void setMailFrom(String mailFrom) {
            this.mailFrom = mailFrom;
        }

        public String getSubjectTemplate() {
            return subjectTemplate;
        }

        public void setSubjectTemplate(String subjectTemplate) {
            this.subjectTemplate = subjectTemplate;
        }

        public String getTextTemplate() {
            return textTemplate;
        }

        public void setTextTemplate(String textTemplate) {
            this.textTemplate = textTemplate;
        }

        public List<Attachment> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<Attachment> attachments) {
            this.attachments = attachments;
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
                    "smtpHost='" + smtpHost + '\'' +
                    ", smtpAuth=" + smtpAuth +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", mailFrom='" + mailFrom + '\'' +
                    ", subjectTemplate='" + subjectTemplate + '\'' +
                    ", textTemplate='" + textTemplate + '\'' +
                    ", attachments=" + attachments +
                    ", placeholderMapKey='" + placeholderMapKey + '\'' +
                    '}';
        }

        public static class Attachment implements Bean {

            private static final long serialVersionUID = 8737383128619932874L;

            @JSONField(name = "name_template", ordinal = 1)
            private String nameTemplate;

            @JSONField(name = "content_template", ordinal = 2)
            private String contentTemplate;

            public Attachment() {
            }

            public Attachment(String nameTemplate, String contentTemplate) {
                this.nameTemplate = nameTemplate;
                this.contentTemplate = contentTemplate;
            }

            public String getNameTemplate() {
                return nameTemplate;
            }

            public void setNameTemplate(String nameTemplate) {
                this.nameTemplate = nameTemplate;
            }

            public String getContentTemplate() {
                return contentTemplate;
            }

            public void setContentTemplate(String contentTemplate) {
                this.contentTemplate = contentTemplate;
            }

            @Override
            public String toString() {
                return "Attachment{" +
                        "nameTemplate='" + nameTemplate + '\'' +
                        ", contentTemplate='" + contentTemplate + '\'' +
                        '}';
            }
        }
    }
}
