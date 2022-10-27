package com.dwarfeng.familyhelper.plugin.notify.handler.router;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.notify.impl.handler.router.AbstractRouterRegistry;
import com.dwarfeng.notify.stack.exception.RouterException;
import com.dwarfeng.notify.stack.exception.RouterExecutionException;
import com.dwarfeng.notify.stack.exception.RouterMakeException;
import com.dwarfeng.notify.stack.handler.Router;
import com.dwarfeng.rbacds.stack.bean.entity.User;
import com.dwarfeng.rbacds.stack.service.UserLookupService;
import com.dwarfeng.subgrade.sdk.bean.key.FastJsonStringIdKey;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限路由器注册。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
@Component
public class PermissionRouterRegistry extends AbstractRouterRegistry {

    public static final String ROUTER_TYPE = "permission_router";

    /**
     * 将指定的路由参数转换为路由信息文本。
     *
     * @param extraUserInfo 指定的路由参数。
     * @return 指定的参数转换成的路由信息文本。
     */
    public static String toRouteInfo(ExtraUserInfo extraUserInfo) {
        FastJsonExtraUserInfo fastJsonExtraUserInfo = new FastJsonExtraUserInfo(
                extraUserInfo.isUseBlackList(),
                extraUserInfo.getBlackList().stream().map(FastJsonStringIdKey::of).collect(Collectors.toList()),
                extraUserInfo.isUseWhiteList(),
                extraUserInfo.getWhiteList().stream().map(FastJsonStringIdKey::of).collect(Collectors.toList())
        );
        return JSON.toJSONString(fastJsonExtraUserInfo, false);
    }

    /**
     * 解析路由信息文本并获取路由参数。
     *
     * @param routeInfo 指定的路由信息文本。
     * @return 解析路由信息文本获取到的路由参数。
     */
    public static ExtraUserInfo parseRouteInfo(String routeInfo) {
        FastJsonExtraUserInfo fastJsonExtraUserInfo = JSON.parseObject(routeInfo, FastJsonExtraUserInfo.class);
        return new ExtraUserInfo(
                fastJsonExtraUserInfo.isUseBlackList(),
                fastJsonExtraUserInfo.getBlackList().stream().map(FastJsonStringIdKey::toStackBean)
                        .collect(Collectors.toList()),
                fastJsonExtraUserInfo.isUseWhiteList(),
                fastJsonExtraUserInfo.getWhiteList().stream().map(FastJsonStringIdKey::toStackBean)
                        .collect(Collectors.toList())
        );
    }

    private final ApplicationContext ctx;

    private final UserLookupService userLookupService;

    public PermissionRouterRegistry(
            ApplicationContext ctx,
            @Qualifier("userLookupService") UserLookupService userLookupService
    ) {
        super(ROUTER_TYPE);
        this.ctx = ctx;
        this.userLookupService = userLookupService;
    }

    @Override
    public String provideLabel() {
        return "权限路由器";
    }

    @Override
    public String provideDescription() {
        return "使用权限确定需要发送通知的用户，可在 routeInfo 配置用户的黑白名单。";
    }

    @Override
    public String provideExampleParam() {
        return "notify.your_permission_node_here";
    }

    @Override
    public Router makeRouter(String type, String param) throws RouterException {
        try {
            // 通过 param 生成路由器的参数。
            StringIdKey permissionKey = new StringIdKey(param);

            // 通过 ctx 生成路由器。
            return ctx.getBean(PermissionRouter.class, userLookupService, permissionKey);
        } catch (Exception e) {
            throw new RouterMakeException(e, type, param);
        }
    }

    @Override
    public String toString() {
        return "PermissionRouterRegistry{" +
                "routerType='" + routerType + '\'' +
                '}';
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class PermissionRouter implements Router {

        private final UserLookupService userLookupService;

        private final StringIdKey permissionKey;

        public PermissionRouter(UserLookupService userLookupService, StringIdKey permissionKey) {
            this.userLookupService = userLookupService;
            this.permissionKey = permissionKey;
        }

        @Override
        public List<StringIdKey> route(String routeInfo, Context context) throws RouterException {
            try {
                // 初步获取具有权限的所有用户，该步骤可以保证返回的所有用户均符合路由器的返回要求。
                List<StringIdKey> userKeys = userLookupService.lookupForPermission(permissionKey).stream()
                        .map(User::getKey).collect(Collectors.toList());

                // 对额外的用户黑白名单进行判断。
                if (!StringUtils.isEmpty(routeInfo)) {
                    ExtraUserInfo extraUserInfo = parseRouteInfo(routeInfo);
                    if (extraUserInfo.isUseBlackList()) {
                        userKeys.removeAll(extraUserInfo.getBlackList());
                    }
                    if (extraUserInfo.useWhiteList) {
                        userKeys.retainAll(extraUserInfo.getWhiteList());
                    }
                }

                // 返回结果。
                return userKeys;
            } catch (Exception e) {
                throw new RouterExecutionException(e);
            }
        }

        @Override
        public String toString() {
            return "PermissionRouter{" +
                    "permissionKey=" + permissionKey +
                    '}';
        }
    }

    public static class ExtraUserInfo implements Bean {

        private static final long serialVersionUID = -8193736981579847675L;

        private boolean useBlackList;
        private List<StringIdKey> blackList;
        private boolean useWhiteList;
        private List<StringIdKey> whiteList;

        public ExtraUserInfo() {
        }

        public ExtraUserInfo(
                boolean useBlackList, List<StringIdKey> blackList, boolean useWhiteList,
                List<StringIdKey> whiteList
        ) {
            this.useBlackList = useBlackList;
            this.blackList = blackList;
            this.useWhiteList = useWhiteList;
            this.whiteList = whiteList;
        }

        public boolean isUseBlackList() {
            return useBlackList;
        }

        public void setUseBlackList(boolean useBlackList) {
            this.useBlackList = useBlackList;
        }

        public List<StringIdKey> getBlackList() {
            return blackList;
        }

        public void setBlackList(List<StringIdKey> blackList) {
            this.blackList = blackList;
        }

        public boolean isUseWhiteList() {
            return useWhiteList;
        }

        public void setUseWhiteList(boolean useWhiteList) {
            this.useWhiteList = useWhiteList;
        }

        public List<StringIdKey> getWhiteList() {
            return whiteList;
        }

        public void setWhiteList(List<StringIdKey> whiteList) {
            this.whiteList = whiteList;
        }

        @Override
        public String toString() {
            return "ExtraUserInfo{" +
                    "useBlackList=" + useBlackList +
                    ", blackList=" + blackList +
                    ", useWhiteList=" + useWhiteList +
                    ", whiteList=" + whiteList +
                    '}';
        }
    }

    private static class FastJsonExtraUserInfo implements Bean {

        private static final long serialVersionUID = -7137044154285869268L;

        @JSONField(name = "use_black_list", ordinal = 1)
        private boolean useBlackList;

        @JSONField(name = "black_list", ordinal = 2)
        private List<FastJsonStringIdKey> blackList;

        @JSONField(name = "use_white_list", ordinal = 3)
        private boolean useWhiteList;

        @JSONField(name = "white_list", ordinal = 4)
        private List<FastJsonStringIdKey> whiteList;

        // Bean 规范要求实体拥有无参数构造方法。
        @SuppressWarnings("unused")
        public FastJsonExtraUserInfo() {
        }

        public FastJsonExtraUserInfo(
                boolean useBlackList, List<FastJsonStringIdKey> blackList, boolean useWhiteList,
                List<FastJsonStringIdKey> whiteList
        ) {
            this.useBlackList = useBlackList;
            this.blackList = blackList;
            this.useWhiteList = useWhiteList;
            this.whiteList = whiteList;
        }

        public boolean isUseBlackList() {
            return useBlackList;
        }

        public void setUseBlackList(boolean useBlackList) {
            this.useBlackList = useBlackList;
        }

        public List<FastJsonStringIdKey> getBlackList() {
            return blackList;
        }

        public void setBlackList(List<FastJsonStringIdKey> blackList) {
            this.blackList = blackList;
        }

        public boolean isUseWhiteList() {
            return useWhiteList;
        }

        public void setUseWhiteList(boolean useWhiteList) {
            this.useWhiteList = useWhiteList;
        }

        public List<FastJsonStringIdKey> getWhiteList() {
            return whiteList;
        }

        public void setWhiteList(List<FastJsonStringIdKey> whiteList) {
            this.whiteList = whiteList;
        }

        @Override
        public String toString() {
            return "FastJsonExtraUserInfo{" +
                    "useBlackList=" + useBlackList +
                    ", blackList=" + blackList +
                    ", useWhiteList=" + useWhiteList +
                    ", whiteList=" + whiteList +
                    '}';
        }
    }
}
