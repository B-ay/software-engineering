# Software Engineering · Android 课程项目骨架

Android/Java 课程团队工程，应用命名空间为 `com.example.adventure`。当前提交主要包含项目初始化结构，尚无完整业务功能或服务端实现证据。

## 当前内容

- `MainActivity.java`：加载 `activity_main` 布局。
- Gradle Kotlin DSL 构建配置：compile/target SDK 33、min SDK 21、Java 8。
- AppCompat、Material 和 ConstraintLayout 依赖。
- 示例单元与设备测试；模板测试不等于业务测试覆盖。

## 打开与构建

使用 Android Studio 打开仓库根目录，准备与 Gradle 插件兼容的 JDK、Android SDK 33 和依赖环境。可通过 IDE 构建，或在 Windows 使用：

```powershell
.\gradlew.bat assembleDebug
```

本次仅核验入口与构建文件，未准备 Android 工具链或执行设备测试。

## 展示定位

当前适合作为课程工程归档。简历不宜描述为已完成答题应用、后端服务或成熟 Android 产品；待补全业务实现和本人职责后再升级为主项目。仓库内许可证文件名为 `LISENCE`，本次保留原文件。

## 原有团队记录

SoftwareEngineering
介绍
队伍成员：林光明，孟想文昕，刘凯欣，周备