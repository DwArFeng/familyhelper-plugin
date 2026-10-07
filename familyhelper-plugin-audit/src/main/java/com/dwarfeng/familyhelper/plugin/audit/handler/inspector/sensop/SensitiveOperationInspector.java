package com.dwarfeng.familyhelper.plugin.audit.handler.inspector.sensop;

import com.dwarfeng.audit.sdk.handler.inspector.AbstractInspector;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 敏感操作审计器。
 *
 * <p>
 * 该审计器在每次构造请求中持有独立的参数，并通过 Spring 容器构造对应的执行器，
 * 从而保证同一个审计器类型在不同审计器信息下互不干扰。同一类型被配置为多条审计器信息时，
 * 只要这些配置使用了不同的变量 ID，其审计器变量也彼此独立。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
@Component("sensitiveOperationInspectorRegistry.sensitiveOperationInspector")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SensitiveOperationInspector extends AbstractInspector {

    private final ApplicationContext ctx;

    private final SensitiveOperationInspectorConfig config;

    public SensitiveOperationInspector(ApplicationContext ctx, SensitiveOperationInspectorConfig config) {
        Objects.requireNonNull(ctx, "ctx 不能为 null");
        Objects.requireNonNull(config, "config 不能为 null");
        this.ctx = ctx;
        this.config = config;
    }

    @Override
    protected Executor doNewExecutor() {
        return ctx.getBean(SensitiveOperationExecutor.class, config);
    }

    @Override
    public String toString() {
        return "SensitiveOperationInspector{" +
                "config=" + config +
                '}';
    }
}
