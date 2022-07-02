package com.dwarfeng.familyhelper.plugin.notify.sender;

import com.dwarfeng.notify.impl.handler.router.IdentityRouterRegistry;
import com.dwarfeng.notify.stack.bean.dto.NotifyInfo;
import com.dwarfeng.notify.stack.bean.dto.Routing;
import com.dwarfeng.notify.stack.bean.entity.*;
import com.dwarfeng.notify.stack.bean.entity.key.SenderRelationKey;
import com.dwarfeng.notify.stack.service.*;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/application-context*.xml")
public class EmbeddedTextSenderRegistryTest {

    private static final LongIdKey NOTIFY_SETTING_KEY = new LongIdKey(10000);
    private static final StringIdKey TOPIC_KEY = new StringIdKey("test_embedded_text_sender_topic");
    private static final LongIdKey ROUTER_INFO_KEY = new LongIdKey(10000);
    private static final LongIdKey SENDER_INFO_KEY = new LongIdKey(10000);

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    @Qualifier("notifyNotifySettingMaintainService")
    private NotifySettingMaintainService notifySettingMaintainService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    @Qualifier("notifyTopicMaintainService")
    private TopicMaintainService topicMaintainService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    @Qualifier("notifyRouterInfoMaintainService")
    private RouterInfoMaintainService routerInfoMaintainService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    @Qualifier("notifySenderInfoMaintainService")
    private SenderInfoMaintainService senderInfoMaintainService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    @Qualifier("notifySenderRelationMaintainService")
    private SenderRelationMaintainService senderRelationMaintainService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    @Qualifier("notifyNotifyService")
    private NotifyService notifyService;

    @Value("#{'${notify.user_list}'.split(',')}")
    private List<String> userIdList;

    @Before
    public void setUp() throws Exception {
        NotifySetting notifySetting = new NotifySetting(
                NOTIFY_SETTING_KEY, "测试通知设置", "仅供测试使用"
        );
        notifySettingMaintainService.insertOrUpdate(notifySetting);
        Topic topic = new Topic(
                TOPIC_KEY, "测试内置文本发送器主题", "仅供测试使用"
        );
        topicMaintainService.insertOrUpdate(topic);
        RouterInfo routerInfo = new RouterInfo(
                ROUTER_INFO_KEY, NOTIFY_SETTING_KEY, "测试路由器", IdentityRouterRegistry.ROUTER_TYPE,
                "", "仅供测试使用"
        );
        routerInfoMaintainService.insertOrUpdate(routerInfo);
        SenderInfo senderInfo = new SenderInfo(
                SENDER_INFO_KEY, "测试内置文本发送器", EmbeddedTextSenderRegistry.SENDER_TYPE, "", "仅供测试使用"
        );
        senderInfoMaintainService.insertOrUpdate(senderInfo);
        SenderRelation senderRelation = new SenderRelation(
                new SenderRelationKey(NOTIFY_SETTING_KEY.getLongId(), TOPIC_KEY.getStringId()),
                SENDER_INFO_KEY, "仅供测试使用"
        );
        senderRelationMaintainService.insertOrUpdate(senderRelation);
    }

    @Test
    public void test() throws Exception {
        List<Routing> routingList = userIdList.stream().map(
                id -> new Routing(TOPIC_KEY, new StringIdKey(id))
        ).collect(Collectors.toList());
        notifyService.notify(new NotifyInfo(NOTIFY_SETTING_KEY, routingList, "测试信息"));
    }
}
