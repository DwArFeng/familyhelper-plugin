package com.dwarfeng.familyhelper.plugin.notify.handler.dispatcher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.notify.impl.handler.dispatcher.AbstractDispatcherRegistry;
import com.dwarfeng.notify.stack.exception.DispatcherException;
import com.dwarfeng.notify.stack.exception.DispatcherExecutionException;
import com.dwarfeng.notify.stack.exception.DispatcherMakeException;
import com.dwarfeng.notify.stack.handler.Dispatcher;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
     * 将指定的调度参数转换为参数。
     *
     * @param config 指定的调度参数。
     * @return 指定的参数转换成的参数。
     */
    public static String toParam(Config config) {
        return JSON.toJSONString(config, false);
    }

    /**
     * 解析参数并获取调度参数。
     *
     * @param param 指定的参数。
     * @return 解析参数获取到的调度参数。
     */
    public static Config parseParam(String param) {
        return JSON.parseObject(param, Config.class);
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
        return toParam(config);
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
    public static class GeneralDispatcher implements Dispatcher {

        private final Config config;

        public GeneralDispatcher(Config config) {
            this.config = config;
        }

        @Override
        public List<StringIdKey> dispatch(String dispatchInfo, List<StringIdKey> userKeys, Context context) throws DispatcherException {
            try {
                List<StringIdKey> result = new ArrayList<>();
                for (StringIdKey userKey : userKeys) {
                    if (acceptUser(userKey, dispatchInfo, context)) {
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
        private boolean acceptUser(StringIdKey userKey, String dispatchInfo, Context context)
                throws Exception {
            // 判断用户是否偏好此主题。
            String preferredString = context.getMetaOrDefault(userKey, config.getPreferredMetaId());
            boolean preferred = Boolean.parseBoolean(preferredString);
            if (!preferred) {
                return false;
            }

            // 所有判断都通过后，接收该用户。
            return true;
        }
    }

    public static class Config implements Bean {

        private static final long serialVersionUID = 189049246403824391L;

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
