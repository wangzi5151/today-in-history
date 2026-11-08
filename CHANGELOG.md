# Changelog

本文件记录 today-in-history 的主要变更。

## [Unreleased]

### Added
- 接入联网增量刷新：60s API 优先、Wikimedia 兜底，联网结果按日期写入本地缓存。
- GitHub Actions 工作流 `Android CI`：运行单元测试、构建 debug APK 并上传 artifact。
- `LICENSE`（MIT）与 `app/proguard-rules.pro`。
- Gradle Wrapper（`gradlew`、`gradlew.bat`、`gradle-wrapper.jar`），可直接执行 README 中的构建命令。
- README 中的 API、缓存策略、构建环境与 CI 说明。

### Changed
- 首页改为“本地优先”：先展示离线资源/上次缓存，再在后台联网更新；超时或失败时自动回退并提示。
- 移除仓库中写死 Termux 路径的 `local.properties`，构建改用 `ANDROID_HOME`/CI 环境；`gradle.properties` 不再包含本机绝对路径。

### Fixed
- 修复联网数据从未被 ViewModel 读取的问题。
- 修复切换日期时旧请求覆盖新日期结果的问题。
