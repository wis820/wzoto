---
kind: build_system
name: Maven多模块后端 + Vite前端构建体系
category: build_system
scope:
    - '**'
source_files:
    - wzoto-backend/pom.xml
    - wzoto-backend/wzoto-interfaces/pom.xml
    - wzoto-backend/wzoto-application/pom.xml
    - wzoto-backend/wzoto-domain/pom.xml
    - wzoto-backend/wzoto-infrastructure/pom.xml
    - wzoto-frontend/package.json
    - wzoto-frontend/vite.config.js
---

## 1. 使用的构建系统与工具

- **后端**：基于 Maven 的 Spring Boot 聚合工程，继承 `spring-boot-starter-parent:3.2.5`，Java 版本锁定为 21（`maven.compiler.source/target=21`），通过 `maven-compiler-plugin` 配置 Lombok 注解处理器。
- **前端**：Vue 3 + Vite 单页应用，使用 `vite build` 进行生产构建、`vite` 启动开发服务器，并通过 `vite.config.js` 中的 `server.proxy` 将 `/api` 请求代理到 `http://localhost:8080` 的后端服务。
- **打包产物**：后端由 `wzoto-interfaces` 模块通过 `spring-boot-maven-plugin` 打成可执行 JAR；前端构建输出到 `wzoto-frontend/dist`。
- **测试**：前端集成 Playwright（`playwright` 作为 devDependency）用于自动化测试脚本（见 `wzoto-frontend/tests/auto-test.mjs`）。

## 2. 关键文件与位置

- `wzoto-backend/pom.xml`：聚合根 POM，声明 `modules`（domain / infrastructure / application / interfaces）、统一 `properties`（版本号集中管理）、`dependencyManagement`（内部模块与第三方依赖版本收敛）。
- `wzoto-backend/wzoto-interfaces/pom.xml`：唯一包含 `spring-boot-maven-plugin` 的模块，负责最终可执行 JAR 打包，并排除 Lombok 从最终包中。
- `wzoto-backend/wzoto-domain/pom.xml`、`wzoto-backend/wzoto-application/pom.xml`、`wzoto-backend/wzoto-infrastructure/pom.xml`：各层子模块 POM，仅声明自身依赖，版本由父 POM 统一管理。
- `wzoto-frontend/package.json`：定义 `dev`/`build`/`preview` 三个 npm scripts。
- `wzoto-frontend/vite.config.js`：Vite 插件、路径别名 `@` → `./src`、开发代理配置。
- 根目录存在 `build_log.txt`、`backend.log`、`backend-err.log` 等运行时日志文件，表明本地直接运行而非 CI 流水线。

## 3. 架构与约定

- **分层依赖方向严格单向**：`interfaces` → `application` → `domain`，`infrastructure` 实现 domain 的 repository 接口。每个子模块 POM 只引入其上层依赖，不反向引用，保证编译期依赖边界。
- **版本集中管理**：所有内部模块共享 `${wzoto.version}`（当前 `1.0.0-SNAPSHOT`），外部依赖如 MyBatis-Plus (`3.5.6`)、Hutool (`5.8.26`)、JWT (`0.12.5`) 均在父 POM 的 `<properties>` 与 `<dependencyManagement>` 中统一定义，子模块无需再写版本号。
- **Spring Boot 入口**：`WzotoApplication` 位于 `wzoto-interfaces` 模块下，因此只有该模块参与最终打包。
- **前端开发约定**：通过 Vite 的 proxy 在开发阶段绕过跨域，生产构建后静态资源需部署到后端或独立静态站点。

## 4. 约定与约束

- **构建命令**：后端在 `wzoto-backend` 目录下执行 `mvn clean package -DskipTests`（标准 Maven 生命周期）；前端在 `wzoto-frontend` 目录下执行 `npm run build`。
- **无容器化/CI**：仓库中未发现 `Dockerfile`、`docker-compose.yml`、`.github/workflows`、`Jenkinsfile`、`Makefile`、`build.sh` 等 CI/CD 或容器化配置文件，发布流程目前依赖本地手动构建与部署。
- **数据库初始化**：SQL 脚本集中在 `wzoto-backend/sql/` 下（如 `init_all_learning_tables.sql`、`seed_data_*.sql`），属于部署前数据准备步骤，非构建阶段产物。
- **编码与语言**：强制 Java 21、UTF-8 编码（`project.build.sourceEncoding=UTF-8`），前端使用 ES Module（`"type": "module"`）。