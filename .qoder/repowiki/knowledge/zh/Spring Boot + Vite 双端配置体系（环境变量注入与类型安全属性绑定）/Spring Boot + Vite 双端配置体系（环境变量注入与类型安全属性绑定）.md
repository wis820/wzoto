---
kind: configuration_system
name: Spring Boot + Vite 双端配置体系（环境变量注入与类型安全属性绑定）
category: configuration_system
scope:
    - '**'
source_files:
    - wzoto-backend/wzoto-interfaces/src/main/resources/application.yml
    - wzoto-backend/wzoto-infrastructure/src/main/java/com/wzoto/infrastructure/config/JwtProperties.java
    - wzoto-backend/wzoto-infrastructure/src/main/java/com/wzoto/infrastructure/config/WechatProperties.java
    - wzoto-backend/wzoto-infrastructure/src/main/java/com/wzoto/infrastructure/config/WebMvcConfig.java
    - wzoto-frontend/vite.config.js
    - wzoto-frontend/src/utils/request.js
---

## 1. 整体方案

后端基于 Spring Boot 3.2.5，使用 `application.yml` 作为唯一运行时配置文件，通过 `${ENV:default}` 占位符从环境变量注入敏感信息；业务相关配置通过 `@ConfigurationProperties` 绑定到 Java Bean。前端基于 Vite + Vue3，构建期配置集中在 `vite.config.js`，运行期 API 地址通过相对路径 `/api` 配合开发代理解决跨域。

## 2. 核心文件

- `wzoto-backend/wzoto-interfaces/src/main/resources/application.yml`：全局运行时配置入口
- `wzoto-backend/wzoto-infrastructure/src/main/java/com/wzoto/infrastructure/config/JwtProperties.java`：JWT 配置类
- `wzoto-backend/wzoto-infrastructure/src/main/java/com/wzoto/infrastructure/config/WechatProperties.java`：微信小程序配置类
- `wzoto-backend/wzoto-infrastructure/src/main/java/com/wzoto/infrastructure/config/WebMvcConfig.java`：MVC 拦截器与 CORS 配置
- `wzoto-frontend/vite.config.js`：Vite 构建与开发服务器代理配置
- `wzoto-frontend/src/utils/request.js`：Axios 实例、统一请求/响应拦截器

## 3. 架构与约定

### 3.1 后端配置加载顺序

`application.yml` 中所有外部化值均使用 `${VAR:default}` 语法，遵循 Spring Boot 的 Environment 解析规则：
- 数据库连接：`spring.datasource.password` 默认 `root`
- Redis：`spring.data.redis.host/port/password`，host 默认 `localhost`，port 默认 `6379`
- MyBatis-Plus：逻辑删除字段 `deleted`，自动映射下划线到驼峰
- 日志级别：`com.wzoto` 和 `com.baomidou.mybatisplus` 设为 `debug`

业务配置以 `wzoto.*` 为前缀分组：
- `wzoto.jwt.secret`：JWT 密钥，默认值为长随机串
- `wzoto.jwt.expiration-hours`：过期时间（小时），默认 72
- `wzoto.wechat.app-id / app-secret`：小程序登录凭据
- `wzoto.ai.enabled/base-url/api-key/model/temperature`：AI 能力开关及 OpenAI 兼容 API 参数，默认关闭并指向 `https://api.openai.com`

### 3.2 类型安全属性绑定

新增配置项必须通过 `@ConfigurationProperties(prefix = "wzoto.xxx")` + `@Component` 声明为 Spring Bean，禁止在业务代码中直接使用 `@Value` 或 `System.getenv()`。当前已实现：
- `JwtProperties`：包含 `secret`、`expirationHours`、`tokenPrefix`、`headerName`
- `WechatProperties`：包含 `appId`、`appSecret`、`loginUrl`

### 3.3 环境隔离策略

通过环境变量覆盖默认值实现多环境部署：生产环境需设置 `MYSQL_PASSWORD`、`REDIS_HOST`、`REDIS_PORT`、`REDIS_PASSWORD`、`JWT_SECRET`、`WECHAT_APP_ID`、`WECHAT_APP_SECRET`、`AI_ENABLED`、`AI_BASE_URL`、`AI_API_KEY`、`AI_MODEL`、`AI_TEMPERATURE`。未设置时回退到 `application.yml` 中的安全默认值。

### 3.4 前端配置

- 构建期：`vite.config.js` 定义 `@` 路径别名指向 `src/`，开发服务器将 `/api` 请求代理到 `http://localhost:8080`，启用 `changeOrigin`
- 运行期：`request.js` 创建 Axios 实例，`baseURL: '/api'`，超时 15s，统一注入 `Authorization: Bearer <token>` 请求头
- 错误处理：响应码非 200 时弹出 toast；401 时清除用户状态并重定向到 `/login`

### 3.5 Web 层配置

`WebMvcConfig` 注册 JWT 拦截器，仅对 `/api/**` 生效，排除 `/api/auth/wx-login`、`/api/auth/select-identity`、`/api/auth/bind-phone`、`/api/auth/dev-login` 等公开接口；CORS 允许所有来源、携带凭证，预检缓存 3600 秒。

## 4. 约定与约束

- **新增外部化配置**：必须在 `application.yml` 中以 `wzoto.*` 前缀声明，并在 `wzoto-infrastructure/config` 下新建对应 `*Properties` 类，使用 `@ConfigurationProperties` 绑定
- **敏感信息不得硬编码**：`application.yml` 中的密码、密钥一律通过 `${ENV:default}` 形式引用，默认值仅供本地开发
- **AI 功能默认关闭**：`wzoto.ai.enabled` 默认 `false`，生产环境需显式开启并配置 `AI_API_KEY`、`AI_BASE_URL`、`AI_MODEL`
- **前端不直接访问后端域名**：通过 Vite dev proxy 转发 `/api`，避免开发期跨域问题；生产部署由反向代理统一处理
- **认证拦截白名单固定**：新增无需鉴权的接口需在 `WebMvcConfig.addInterceptors` 中显式 `excludePathPatterns` 加入
- **统一响应格式**：前端依赖后端返回 `{ code, msg, data }` 结构，code 非 200 视为失败，401 触发登出跳转