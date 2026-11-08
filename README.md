# 历史上的今天

一个 Android 应用，展示今天历史上发生的大事。

## 特性
- 查看今日历史事件
- 任意日期浏览
- 收藏功能
- 本地优先 + 联网增量更新
- 离线缓存兜底数据

## 数据来源与缓存策略
- **离线数据**：`app/src/main/assets/fallback_01.json` ~ `fallback_12.json`，按月分包，共 3 万余条。
- **在线数据**：优先请求 [60s API](https://60s.viki.moe/) 的 `today_in_history` 接口，失败后回退到 [Wikimedia On This Day API](https://api.wikimedia.org/feed/v1/wikipedia/zh/onthisday/events/)。
- **本地优先**：打开某个日期时先展示离线数据与上次联网缓存，再在后台发起网络请求。
- **增量更新**：联网成功后按事件去重合并，并把结果按日期写入 `SharedPreferences`（`network_cache`）；下次离线也能看到。
- **超时/失败回退**：连接与读取超时均为 10 秒；超时、无网络或接口无数据时保留本地数据，并在首页提示“网络不可用，已显示本地数据”。
- 需要 `INTERNET` 权限；应用已声明该权限。

## 技术栈
- Kotlin + Jetpack Compose
- MinSdk 24, TargetSdk 34
- Gradle 8.2.1 + AGP 8.2.1
- 60s API + Wikimedia API 数据源

## 构建
需要 JDK 17 与 Android SDK（设置 `ANDROID_HOME` 或 `ANDROID_SDK_ROOT`）：

```bash
export ANDROID_HOME="$HOME/android-sdk"
export JAVA_HOME="/path/to/jdk-17"
./gradlew assembleDebug
```

运行单元测试：

```bash
./gradlew testDebugUnitTest
```

> Termux 等自带 `aapt2` 的环境可在本地（不提交）`local.properties` 中设置
> `sdk.dir` 与 `aapt2FromMavenOverride`，或通过命令行传入
> `-Pandroid.aapt2FromMavenOverride=/path/to/aapt2`。仓库不再跟踪 `local.properties`，
> 也不会提交任何本机绝对路径。

## 安装
构建产物位于 `app/build/outputs/apk/debug/app-debug.apk`，安装到 Android 设备即可。

## CI
`.github/workflows/android.yml` 会在 push/PR 时运行 `testDebugUnitTest` 与 `assembleDebug`，并上传 debug APK artifact。

## 许可证
[MIT](LICENSE)

## GitHub
https://github.com/wangzi5151/today-in-history
