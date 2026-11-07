---
name: ic2-patcher-build
description: 编译本项目并生成可在 Minecraft 1.12.2 环境使用的 IC2-Patcher 发布 JAR；仅处理构建产物，不负责运行、测试或发布。
---

# IC2-Patcher 构建

只在用户需要把本项目编译成 JAR 时使用。工作目录必须是仓库根目录。

## 构建流程

项目使用 Java 8、Gradle Wrapper 7.6 和 IC2 版本参数。使用以下命令；代理变量仅在当前网络需要代理时保留或替换：

```bash
export JAVA_HOME=/opt/minecraft-tools/jdk8
export PATH="$JAVA_HOME/bin:$PATH"
export GRADLE_USER_HOME=/opt/minecraft-tools/gradle-home
export http_proxy=http://192.168.30.118:7890
export https_proxy=http://192.168.30.118:7890
export HTTP_PROXY=http://192.168.30.118:7890
export HTTPS_PROXY=http://192.168.30.118:7890
export all_proxy=http://192.168.30.118:7890
export ALL_PROXY=http://192.168.30.118:7890
export GRADLE_OPTS='-Dhttp.proxyHost=192.168.30.118 -Dhttp.proxyPort=7890 -Dhttps.proxyHost=192.168.30.118 -Dhttps.proxyPort=7890 -Dhttp.nonProxyHosts=localhost\|127.*'

./gradlew --no-daemon "Setup IC2 Source" \
  -PversionIC2=2.8.222 -PskipSetupConfirmation

./gradlew --no-daemon ":IC2-Patched:Build ~ Patched" \
  -PversionIC2=2.8.222 -PskipSetupConfirmation

patched_jar=$(find src/ic2/patched/build/libs -maxdepth 1 -type f \
  -name 'IC2-Patched-*.jar' | sort | tail -n 1)
test -n "$patched_jar"
cp "$patched_jar" src/ic2/patched/build/libs/IC2-Patched.jar

./gradlew --no-daemon ":IC2-Patched:Generate Binary Patches ~ Patched" \
  -PversionIC2=2.8.222 -PskipSetupConfirmation

./gradlew --no-daemon "Release Jar ~ Patcher" \
  -PversionIC2=2.8.222 -PskipSetupConfirmation
```

最终发布 JAR 位于 `build/libs/IC2-Patcher-*.jar`。构建时不要使用普通 `jar` 任务；项目已禁用该任务，且其产物不包含可运行环境所需的发布内容。
