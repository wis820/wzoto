---
kind: logging_system
name: 基于 SLF4J + Lombok @Slf4j 的日志系统
category: logging_system
scope:
    - '**'
source_files:
    - wzoto-backend/pom.xml
    - wzoto-backend/wzoto-interfaces/src/main/resources/application.yml
    - wzoto-backend/wzoto-application/src/main/java/com/wzoto/application/service/AuthApplicationService.java
    - wzoto-backend/wzoto-domain/src/main/java/com/wzoto/domain/service/AiGradingDomainService.java
    - wzoto-backend/wzoto-domain/src/main/java/com/wzoto/domain/service/AiLearningPlanDomainService.java
---

## 1. 使用的系统与框架

- **日志门面**：SLF4J（`org.slf4j:slf4j-api`，在根 `pom.xml` 中声明为全局依赖）。
- **日志实现**：Spring Boot 3.2.5 默认引入 Logback，项目未显式替换实现或添加自定义 `logback-spring.xml`，因此使用 Spring Boot 内置的 Logback 配置。
- **注解注入**：通过 Lombok 的 `@Slf4j` 在每个类上生成 `private final Logger log` 字段，所有业务代码统一通过 `log.info(...)`、`log.error(...)` 等 API 输出日志。
- **SQL 日志**：MyBatis-Plus 的 SQL 日志通过 `application.yml` 中的 `mybatis-plus.configuration.log-impl: org.apache.ibatis.logging.stdout.StdOutImpl` 启用，直接打印到标准输出。

## 2. 关键文件与位置

- `wzoto-backend/pom.xml`：声明 `slf4j-api` 全局依赖，并继承 `spring-boot-starter-parent` 以获取默认 Logback 实现。
- `wzoto-backend/wzoto-interfaces/src/main/resources/application.yml`：
  - 第 48–50 行：`logging.level.com.wzoto: debug`，将业务包日志级别设为 `debug`；同时设置 MyBatis-Plus 包为 `debug`。
  - 第 23 行：`mybatis-plus.configuration.log-impl: org.apache.ibatis.logging.stdout.StdOutImpl`，开启 SQL 语句输出。
- 各模块 Service/Domain 类：通过 `import lombok.extern.slf4j.Slf4j;` 和 `@Slf4j` 注解获得 logger，例如 `wzoto-application/service/AuthApplicationService.java`、`wzoto-domain/service/AiGradingDomainService.java` 等。

## 3. 架构与约定

- **分层无独立日志组件**：domain、application、infrastructure、interfaces 四层没有统一的日志封装类或 AOP 切面，每个类自行通过 `@Slf4j` 注入 logger。
- **日志级别使用**：当前代码主要使用 `log.info(...)` 记录业务流程关键点（如“用户登录成功”“AI学习计划生成成功”“预约创建成功”等），未见 `log.debug` / `log.warn` / `log.error` 的系统性使用模式。
- **结构化字段**：日志消息采用占位符形式（如 `log.info("用户登录成功, userId={}, identityType={}", ...)`），属于键值对形式的轻量级结构化日志，但未使用 JSON 格式输出。
- **请求链路追踪**：未发现 MDC、TraceId、RequestId 等跨请求上下文机制，日志中不包含统一的请求标识。
- **异常日志**：业务方法抛出异常时由上层处理，当前未见统一的异常拦截器将异常统一记录为 error 日志。

## 4. 约定与约束

- **统一入口**：所有日志通过 SLF4J 门面 + Lombok `@Slf4j` 生成，禁止直接使用 `System.out.println` 或 `java.util.logging`（仓库中未发现此类用法）。
- **包级日志级别**：通过 `application.yml` 的 `logging.level.com.wzoto` 集中控制业务日志级别，便于调试与生产切换。
- **SQL 日志开关**：MyBatis-Plus 的 SQL 日志通过 `log-impl` 属性启用，开发环境可看到完整 SQL 输出，生产可通过修改该配置关闭。
- **无自定义日志配置**：仓库中没有 `logback-spring.xml`、`logback.xml` 或 `logging.config` 指向自定义配置文件，全部依赖 Spring Boot 默认 Logback 行为。
- **日志内容约定**：现有日志均以中文描述事件 + 关键业务 ID 作为参数，便于人工排查；尚未形成强制的字段命名规范或 JSON 结构规范。