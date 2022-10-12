package com.dwarfeng.familyhelper.plugin.notify.handler.router;

import com.dwarfeng.familyhelper.clannad.stack.bean.entity.NotifyMeta;
import com.dwarfeng.familyhelper.clannad.stack.bean.entity.NotifyPreference;
import com.dwarfeng.familyhelper.clannad.stack.bean.entity.NotifySetting;
import com.dwarfeng.familyhelper.clannad.stack.bean.entity.NotifyTopic;
import com.dwarfeng.familyhelper.clannad.stack.bean.key.NotifyNodeKey;
import com.dwarfeng.familyhelper.clannad.stack.service.NotifyMetaMaintainService;
import com.dwarfeng.familyhelper.clannad.stack.service.NotifyPreferenceMaintainService;
import com.dwarfeng.familyhelper.clannad.stack.service.NotifySettingMaintainService;
import com.dwarfeng.familyhelper.clannad.stack.service.NotifyTopicMaintainService;
import com.dwarfeng.notify.impl.handler.router.AbstractRouterRegistry;
import com.dwarfeng.notify.stack.bean.dto.Routing;
import com.dwarfeng.notify.stack.exception.RouterException;
import com.dwarfeng.notify.stack.exception.RouterExecutionException;
import com.dwarfeng.notify.stack.exception.RouterMakeException;
import com.dwarfeng.notify.stack.handler.Router;
import com.dwarfeng.rbacds.stack.bean.entity.User;
import com.dwarfeng.rbacds.stack.service.UserLookupService;
import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 家庭助手路由器注册。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class FamilyhelperRouterRegistry extends AbstractRouterRegistry {

    public static final String ROUTER_TYPE = "familyhelper_router";

    private final ApplicationContext ctx;

    private final NotifySettingMaintainService notifySettingMaintainService;
    private final UserLookupService userLookupService;
    private final NotifyTopicMaintainService notifyTopicMaintainService;
    private final NotifyPreferenceMaintainService notifyPreferenceMaintainService;
    private final NotifyMetaMaintainService notifyMetaMaintainService;

    public FamilyhelperRouterRegistry(
            ApplicationContext ctx,
            @Qualifier("notifySettingMaintainService") NotifySettingMaintainService notifySettingMaintainService,
            @Qualifier("userLookupService") UserLookupService userLookupService,
            @Qualifier("notifyTopicMaintainService") NotifyTopicMaintainService notifyTopicMaintainService,
            @Qualifier("notifyPreferenceMaintainService")
            NotifyPreferenceMaintainService notifyPreferenceMaintainService,
            @Qualifier("notifyMetaMaintainService") NotifyMetaMaintainService notifyMetaMaintainService
    ) {
        super(ROUTER_TYPE);
        this.ctx = ctx;
        this.notifySettingMaintainService = notifySettingMaintainService;
        this.userLookupService = userLookupService;
        this.notifyTopicMaintainService = notifyTopicMaintainService;
        this.notifyPreferenceMaintainService = notifyPreferenceMaintainService;
        this.notifyMetaMaintainService = notifyMetaMaintainService;
    }

    @Override
    public String provideLabel() {
        return "家庭助手路由器";
    }

    @Override
    public String provideDescription() {
        return "家庭助手工程使用的唯一路由器。";
    }

    @Override
    public String provideExampleParam() {
        return "1027169665828253696";
    }

    @Override
    public Router makeRouter(String type, String param) throws RouterException {
        try {
            LongIdKey notifySettingKey = new LongIdKey(Long.parseLong(param));
            return ctx.getBean(
                    FamilyhelperRouter.class,
                    notifySettingKey, notifySettingMaintainService, userLookupService, notifyTopicMaintainService,
                    notifyPreferenceMaintainService, notifyMetaMaintainService
            );
        } catch (Exception e) {
            throw new RouterMakeException(e, type, param);
        }
    }

    @Override
    public String toString() {
        return "FamilyhelperRouterRegistry{" +
                "ctx=" + ctx +
                ", routerType='" + routerType + '\'' +
                '}';
    }

    public static class FamilyhelperRouter implements Router {

        private final LongIdKey notifySettingKey;

        private final NotifySettingMaintainService notifySettingMaintainService;
        private final UserLookupService userLookupService;
        private final NotifyTopicMaintainService notifyTopicMaintainService;
        private final NotifyPreferenceMaintainService notifyPreferenceMaintainService;
        private final NotifyMetaMaintainService notifyMetaMaintainService;

        public FamilyhelperRouter(
                LongIdKey notifySettingKey,
                NotifySettingMaintainService notifySettingMaintainService, UserLookupService userLookupService,
                NotifyTopicMaintainService notifyTopicMaintainService,
                NotifyPreferenceMaintainService notifyPreferenceMaintainService,
                NotifyMetaMaintainService notifyMetaMaintainService
        ) {
            this.notifySettingKey = notifySettingKey;
            this.notifySettingMaintainService = notifySettingMaintainService;
            this.userLookupService = userLookupService;
            this.notifyTopicMaintainService = notifyTopicMaintainService;
            this.notifyPreferenceMaintainService = notifyPreferenceMaintainService;
            this.notifyMetaMaintainService = notifyMetaMaintainService;
        }

        @Override
        public List<Routing> parseRouting(Object context) throws RouterException {
            try {
                // 解析上下文，获取用户的白名单和黑名单。
                List<StringIdKey> userWhiteList = null;
                List<StringIdKey> userBlackList = null;

                if (Objects.nonNull(context) && context instanceof FamilyhelperRouterContext) {
                    userWhiteList = ((FamilyhelperRouterContext) context).getUserWhiteList();
                    userBlackList = ((FamilyhelperRouterContext) context).getUserBlackList();
                }

                // 获取路由器对应的通知设置的权限，并查询有权限的用户。
                NotifySetting notifySetting = notifySettingMaintainService.getIfExists(notifySettingKey);
                List<StringIdKey> userValidList = userLookupService.lookupForPermission(
                        new StringIdKey(notifySetting.getRequiredPermission())
                ).stream().map(User::getKey).collect(Collectors.toList());
                // 白名单与黑名单过滤。
                if (Objects.nonNull(userWhiteList)) {
                    userValidList.retainAll(userWhiteList);
                }
                if (Objects.nonNull(userBlackList)) {
                    userValidList.removeAll(userBlackList);
                }

                // 获取所有可能发送的主题。
                Map<StringIdKey, NotifyTopic> notifyTopicMap = notifyTopicMaintainService.lookupAsList().stream()
                        .collect(Collectors.toMap(NotifyTopic::getKey, Function.identity()));

                // 定义结果变量。
                List<Routing> result = new ArrayList<>();

                // 对每个用户和每个主题进行双重遍历。
                for (StringIdKey userKey : userValidList) {
                    for (StringIdKey topicKey : notifyTopicMap.keySet()) {
                        // 构造通知节点的主键。
                        NotifyNodeKey notifyNodeKey = new NotifyNodeKey(
                                notifySettingKey.getLongId(), topicKey.getStringId(), userKey.getStringId()
                        );

                        // 获取默认值。
                        boolean preferred = notifyTopicMap.get(topicKey).isPreferred();
                        long coolDownDuration = notifyTopicMap.get(topicKey).getCoolDownDuration();
                        // 如果存在偏好设置，则用个人偏好设置覆盖默认值。
                        NotifyPreference notifyPreference = notifyPreferenceMaintainService.getIfExists(notifyNodeKey);
                        if (Objects.nonNull(notifyPreference)) {
                            preferred = notifyPreference.isPreferred();
                            coolDownDuration = notifyPreference.getCoolDownDuration();
                        }

                        // 如果 preferred 是 false，则不用进行后续逻辑了，该主题和用户不会添加到结果中。
                        if (!preferred) {
                            continue;
                        }

                        // 获取通知元数据。
                        NotifyMeta notifyMeta = notifyMetaMaintainService.getIfExists(notifyNodeKey);
                        // 进行非空判断。
                        if (Objects.nonNull(notifyMeta)) {
                            // 比较上一次发送时间距离当前时间是否超过了冷却时间。
                            // 如果上一次发送时间距离当前时间小于冷却时长，则不用进行后续逻辑了，
                            // 该主题和用户不会添加到结果中。
                            long coolDownSpent
                                    = System.currentTimeMillis() - notifyMeta.getLastReceivedDate().getTime();
                            if (coolDownSpent < coolDownDuration) {
                                continue;
                            }
                        }

                        // 该主题和用户添加到结果中。
                        result.add(new Routing(topicKey, userKey));
                    }
                }

                // 返回结果。
                return result;
            } catch (Exception e) {
                throw new RouterExecutionException(e);
            }
        }
    }

    public static class FamilyhelperRouterContext implements Dto {

        private static final long serialVersionUID = -4891534949962704756L;

        private List<StringIdKey> userWhiteList;
        private List<StringIdKey> userBlackList;

        public FamilyhelperRouterContext() {
        }

        public FamilyhelperRouterContext(List<StringIdKey> userWhiteList, List<StringIdKey> userBlackList) {
            this.userWhiteList = userWhiteList;
            this.userBlackList = userBlackList;
        }

        public List<StringIdKey> getUserWhiteList() {
            return userWhiteList;
        }

        public void setUserWhiteList(List<StringIdKey> userWhiteList) {
            this.userWhiteList = userWhiteList;
        }

        public List<StringIdKey> getUserBlackList() {
            return userBlackList;
        }

        public void setUserBlackList(List<StringIdKey> userBlackList) {
            this.userBlackList = userBlackList;
        }

        @Override
        public String toString() {
            return "FamilyhelperRouterContext{" +
                    "userWhiteList=" + userWhiteList +
                    ", userBlackList=" + userBlackList +
                    '}';
        }
    }
}
