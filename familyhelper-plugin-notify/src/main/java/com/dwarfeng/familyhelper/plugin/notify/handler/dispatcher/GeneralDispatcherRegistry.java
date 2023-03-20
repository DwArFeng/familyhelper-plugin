package com.dwarfeng.familyhelper.plugin.notify.handler.dispatcher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.notify.impl.handler.dispatcher.AbstractDispatcher;
import com.dwarfeng.notify.impl.handler.dispatcher.AbstractDispatcherRegistry;
import com.dwarfeng.notify.stack.exception.DispatcherException;
import com.dwarfeng.notify.stack.exception.DispatcherExecutionException;
import com.dwarfeng.notify.stack.exception.DispatcherMakeException;
import com.dwarfeng.notify.stack.handler.Dispatcher;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 通用调度器注册。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
@Component
public class GeneralDispatcherRegistry extends AbstractDispatcherRegistry {

    public static final String DISPATCHER_TYPE = "general_dispatcher";

    /**
     * 将指定的调度器参数转换为字符串。
     *
     * @param config 指定的调度器参数。
     * @return 指定的参数转换成的字符串。
     */
    public static String stringifyParam(Config config) {
        return JSON.toJSONString(config, false);
    }

    /**
     * 从指定的字符串中解析调度器参数。
     *
     * @param string 指定的字符串。
     * @return 解析指定的字符串获取到的调度器参数。
     */
    public static Config parseParam(String string) {
        return JSON.parseObject(string, Config.class);
    }

    private final ApplicationContext ctx;

    public GeneralDispatcherRegistry(ApplicationContext ctx) {
        super(DISPATCHER_TYPE);
        this.ctx = ctx;
    }

    @Override
    public String provideLabel() {
        return "通用调度器";
    }

    @Override
    public String provideDescription() {
        return "全面考虑用户偏好、元数据等信息的调度器";
    }

    @Override
    public String provideExampleParam() {
        Config config = new Config("0000.preferred");
        return stringifyParam(config);
    }

    @Override
    public Dispatcher makeDispatcher(String type, String param) throws DispatcherException {
        try {
            // 通过 param 生成调度器的参数。
            Config config = parseParam(param);

            // 通过 ctx 生成调度器。
            return ctx.getBean(GeneralDispatcher.class, config);
        } catch (Exception e) {
            throw new DispatcherMakeException(e, type, param);
        }
    }

    @Override
    public String toString() {
        return "GeneralDispatcherRegistry{" +
                "dispatcherType='" + dispatcherType + '\'' +
                '}';
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class GeneralDispatcher extends AbstractDispatcher {

        private final Config config;

        public GeneralDispatcher(Config config) {
            this.config = config;
        }

        @Override
        public List<StringIdKey> dispatch(
                ContextInfo contextInfo, Map<String, String> dispatchInfoMap, List<StringIdKey> userKeys
        ) throws DispatcherException {
            try {
                List<StringIdKey> result = new ArrayList<>();
                for (StringIdKey userKey : userKeys) {
                    if (acceptUser(contextInfo, userKey, dispatchInfoMap)) {
                        result.add(userKey);
                    }
                }
                return result;
            } catch (DispatcherException e) {
                throw e;
            } catch (Exception e) {
                throw new DispatcherExecutionException(e);
            }
        }

        // 随着判断逻辑的增加，下列警告都将消失。
        @SuppressWarnings({"RedundantIfStatement", "unused"})
        private boolean acceptUser(ContextInfo contextInfo, StringIdKey userKey, Map<String, String> dispatchInfoMap)
                throws Exception {
            // 取出 contextInfo 中的字段值。
            LongIdKey notifySettingKey = contextInfo.getNotifySettingKey();
            StringIdKey topicKey = contextInfo.getTopicKey();
            // 判断用户是否偏好此主题。
            String preferredString = context.getMetaOrDefault(
                    notifySettingKey, topicKey, userKey, config.getPreferredMetaId()
            );
            boolean preferred = Boolean.parseBoolean(preferredString);
            if (!preferred) {
                return false;
            }

            // 所有判断都通过后，接收该用户。
            return true;
        }
    }

    public static class Config implements Bean {

        private static final long serialVersionUID = -5275935822413755015L;

        @JSONField(name = "preferred_meta_id", ordinal = 1)
        private String preferredMetaId;

        public Config() {
        }

        public Config(String preferredMetaId) {
            this.preferredMetaId = preferredMetaId;
        }

        public String getPreferredMetaId() {
            return preferredMetaId;
        }

        public void setPreferredMetaId(String preferredMetaId) {
            this.preferredMetaId = preferredMetaId;
        }

        @Override
        public String toString() {
            return "Config{" +
                    "preferredMetaId='" + preferredMetaId + '\'' +
                    '}';
        }
    }
}
