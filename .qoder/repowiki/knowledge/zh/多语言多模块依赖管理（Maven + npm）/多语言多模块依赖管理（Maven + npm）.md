---
kind: dependency_management
name: 多语言多模块依赖管理（Maven + npm）
category: dependency_management
scope:
    - '**'
source_files:
    - wzoto-backend/pom.xml
    - wzoto-backend/wzoto-domain/pom.xml
    - wzoto-backend/wzoto-application/pom.xml
    - wzoto-backend/wzoto-infrastructure/pom.xml
    - wzoto-backend/wzoto-interfaces/pom.xml
    - wzoto-frontend/package.json
    - wzoto-frontend/package-lock.json
    - f/wanglianyida/wzoto/wzoto-frontend/package.json
    - fwanglianyidawzotowzoto-frontend/package.json
---

## 1. 使用的系统/工具

仓库包含两个独立的前端工程与一个后端工程，分别采用不同的包管理器：
- **后端**：基于 Maven 的多模块 Spring Boot 3.2.5 项目，继承 `spring-boot-starter-parent`。
- **前端**：Vue 3 + Vite 单页应用，使用 npm（`package.json` + `package-lock.json`）管理依赖。

仓库中未发现 Go、Python、Ruby 等其他语言的依赖清单文件。

## 2. 关键文件

| 层级 | 文件 | 作用 |
|---|---|---|
| 后端聚合根 | `wzoto-backend/pom.xml` | 定义 Java 21、统一版本属性、`dependencyManagement`、子模块列表 |
| 领域层 | `wzoto-backend/wzoto-domain/pom.xml` | 仅依赖 `spring-context`、`jakarta.validation-api` |
| 应用层 | `wzoto-backend/wzoto-application/pom.xml` | 依赖 domain + infrastructure + spring-retry + hutool |
| 基础设施层 | `wzoto-backend/wzoto-infrastructure/pom.xml` | MyBatis-Plus、Redis、MySQL、OKHttp、JSON、JWT 实现 |
| 接口层 | `wzoto-backend/wzoto-interfaces/pom.xml` | Web、Validation、AOP Starter，并排除 Lombok 打包 |
| 主前端 | `wzoto-frontend/package.json` | Vue 3、Element Plus、Pinia、ECharts、Axios、Vite、Playwright |
| 其他前端 | `f/wanglianyida/wzoto/wzoto-frontend/package.json`、`fwanglianyidawzotowzoto-frontend/package.json` | 轻量 Vue 原型，仅依赖 Vue + Vite |
| 锁文件 | 各 `package-lock.json` | 锁定 npm 依赖树 |

## 3. 架构与约定

### 后端（Maven 多模块）
- **父 POM 集中治理版本**：所有第三方库的版本在根 `pom.xml` 的 `<properties>` 中以变量声明（如 `mybatis-plus.version=3.5.6`、`hutool.version=5.8.26`、`jjwt.version=0.12.5`），并通过 `<dependencyManagement>` 统一管理；子模块引用时不写版本号，避免版本漂移。
- **内部模块通过 `${wzoto.version}` 互相引用**：domain → application → interfaces → infrastructure，形成单向依赖链，符合 DDD 分层约束。
- **Spring Boot 依赖通过 starter 引入**：Web、Redis、Validation、AOP、Data Redis 等均以 `spring-boot-starter-*` 形式声明，由父 POM 管理版本。
- **运行时依赖按 scope 划分**：MySQL 驱动标记 `runtime`，Lombok 标记 `optional`，JWT 的 impl/jackson 也标记 `runtime`，减少打包体积。
- **构建插件**：`maven-compiler-plugin` 显式配置 annotation processor path 以支持 Lombok；`spring-boot-maven-plugin` 在 `wzoto-interfaces` 中排除 Lombok 再打包。

### 前端（npm）
- 每个前端目录独立维护 `package.json` 与 `package-lock.json`，互不共享依赖。
- 主前端 `wzoto-frontend` 是完整业务应用，依赖 Element Plus、Pinia、ECharts、Axios、Sass、Vue Router、Vite、Playwright。
- 另外两个前端目录（`f/wanglianyida/wzoto/wzoto-frontend`、`fwanglianyidawzotowzoto-frontend`）仅为轻量原型，仅保留 Vue 与 Vite。
- 所有依赖均使用 `^` 语义化版本范围，未使用固定精确版本。

## 4. 约定与约束

- **Java 版本统一为 21**：根 POM 的 `java.version`、`maven.compiler.source/target` 强制全项目使用 JDK 21。
- **第三方库版本集中管控**：新增或升级第三方库应在根 `pom.xml` 的 `<properties>` 中修改版本号，子模块不得自行指定版本。
- **内部模块版本通过 `${wzoto.version}` 同步**：四个子模块共享同一 SNAPSHOT 版本，保证内部依赖一致。
- **依赖作用域遵循最小化原则**：仅在需要编译的模块引入依赖，运行期才需要的库（如 MySQL、JWT impl）使用 `runtime` scope。
- **前端无私有仓库/镜像配置**：未发现 `.npmrc`、`pnpm-workspace.yaml`、`yarn.lock` 等文件，依赖全部从默认 npm registry 拉取。
- **无 vendoring**：后端未使用 `mvn dependency:go-offline` 生成离线仓库，前端也未将 `node_modules` 纳入版本控制（`.gitignore` 忽略）。