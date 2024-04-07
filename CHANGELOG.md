# ChangeLog

### Release_1.4.0_20240407_build_A

#### 功能构建

- 优化打包输出的项目结构。
  - 子模块打包时，在输出的项目结构的根目录中增加 `LICENSE` 文件。

- 实现 `familyhelper-assets` 模块的服务。
  - com.dwarfeng.familyhelper.plugin.assets.service.DubboRestItemFileOperateService。

- 依赖升级。
  - 升级 `spring` 依赖版本为 `5.3.31` 以规避漏洞。

- 优化 pom.xml 文件格式。

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Release_1.3.0_20230320_build_A

#### 功能构建

- 依赖升级。
  - 升级 `notify` 依赖版本为 `1.4.0` 并解决兼容性问题，以应用其新功能。

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Release_1.2.0_20230303_build_A

#### 功能构建

- 改进 `familyhelper-project` 模块的推送器。
  - com.dwarfeng.familyhelper.plugin.project.handler.pusher.FamilyhelperPusher。

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Release_1.1.0_20230227_build_A

#### 功能构建

- 实现 `familyhelper-project` 模块的推送器。
  - com.dwarfeng.familyhelper.plugin.project.handler.pusher.FamilyhelperPusher。

- 依赖升级。
  - 升级 `notify` 依赖版本为 `1.3.0.a` 并解决兼容性问题，以应用其新功能。
  - 升级 `familyhelper-clannad` 依赖版本为 `1.3.0.a` 并解决兼容性问题，以应用其新功能。

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Release_1.0.1_20230108_build_A

#### 功能构建

- 实现 `familyhelper-clannad` 模块的推送器。
  - com.dwarfeng.familyhelper.plugin.clannad.handler.pusher.FamilyhelperPusher。

- 实现 `notify` 模块的发送器。
  - com.dwarfeng.familyhelper.plugin.notify.handler.sender.EmailSenderRegistry。

- 更改推送器，使其支持电子邮件发送。
  - com.dwarfeng.familyhelper.plugin.finance.handler.pusher.FamilyhelperPusher。
  - com.dwarfeng.familyhelper.plugin.notify.handler.pusher.FamilyhelperPusher。

- 将推送器的推送方法优化为非阻塞方法。
  - com.dwarfeng.familyhelper.plugin.notify.handler.pusher.FamilyhelperPusher。
  - com.dwarfeng.familyhelper.plugin.finance.handler.pusher.FamilyhelperPusher。

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Release_1.0.0_20230101_build_A

#### 功能构建

- 实现 `familyhelper-finance` 模块的推送器。
  - com.dwarfeng.familyhelper.plugin.finance.handler.pusher.FamilyhelperPusher。

- 实现 `notify` 模块的路由器、调度器、发送器、推送器。
  - com.dwarfeng.familyhelper.plugin.notify.handler.dispatcher.GeneralDispatcherRegistry。
  - com.dwarfeng.familyhelper.plugin.notify.handler.router.PermissionRouterRegistry。
  - com.dwarfeng.familyhelper.plugin.notify.handler.sender.BuiltinSenderRegistry。
  - com.dwarfeng.familyhelper.plugin.notify.handler.pusher.FamilyhelperPusher。

- 项目建立，清除测试通过。

#### Bug修复

- (无)

#### 功能移除

- (无)
