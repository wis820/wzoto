---
kind: frontend_style
name: 学霸到家前端样式体系：Element Plus + SCSS 设计令牌与移动端优先
category: frontend_style
scope:
    - '**'
source_files:
    - wzoto-frontend/src/main.js
    - wzoto-frontend/src/assets/styles/global.scss
    - wzoto-frontend/src/style.css
    - wzoto-frontend/vite.config.js
    - wzoto-frontend/package.json
---

## 1. 使用的系统与工具

- **框架**：Vue 3（Composition API）+ Vite 构建。
- **UI 组件库**：Element Plus 2.x，通过 `main.js` 全局注册并启用中文语言包 `zhCn`，同时引入完整样式文件 `element-plus/dist/index.css`。
- **样式预处理**：SCSS（依赖 `sass` 包），所有业务样式集中在 `src/assets/styles/global.scss`。
- **主题变量**：使用 CSS Custom Properties（`:root` 变量）集中定义颜色、圆角、字号等设计令牌；Element Plus 通过覆盖其 CSS 变量（如 `--el-button-bg-color`）实现主题定制。
- **响应式策略**：以移动端优先（注释明确“设计规范：375×812移动端适配，微信小程序风格”），并通过 `max-width: 750px` 限制 `html/body` 最大宽度，配合 Element Plus 的栅格/按钮在移动端全宽化。
- **暗色模式**：`style.css`（Vite 默认模板）中通过 `@media (prefers-color-scheme: dark)` 切换一套浅色/深色变量，但业务主样式 `global.scss` 未包含暗色分支。

## 2. 关键文件

| 文件 | 作用 |
|---|---|
| `wzoto-frontend/src/main.js` | 全局注册 Element Plus（含中文 locale）、Pinia、Router，并引入全局样式 |
| `wzoto-frontend/src/assets/styles/global.scss` | 业务全局样式：设计令牌、重置、通用工具类、Element Plus 主题覆盖 |
| `wzoto-frontend/src/style.css` | Vite 模板默认样式（CSS 变量、暗色模式、基础排版），与业务样式并存 |
| `wzoto-frontend/vite.config.js` | 配置 `@` 路径别名指向 `src`，开发服务器代理 `/api` 到后端 `localhost:8080` |
| `prototype/index.html` | 单文件 HTML 移动端交互原型（非生产代码，仅用于产品演示） |

## 3. 架构与约定

### 3.1 设计令牌（Design Tokens）
`global.scss` 的 `:root` 集中声明了完整的视觉令牌：
- **色彩**：`--color-primary` / `--color-primary-light` / `--color-primary-dark`（主色蓝 #4F6EF7）、`--color-success`、`--color-warning`、`--color-danger`、文本三级灰度、背景白/浅灰、边框色。
- **圆角**：`--radius-sm/md/lg/round`，统一卡片、按钮、输入框的圆角规范。
- **字号**：从 `--font-size-xs`（11px）到 `--font-size-xxl`（24px）的阶梯式字号系统。
- **阴影**：`--color-card-shadow` 提供统一的卡片投影。

这些令牌被页面级组件通过 `var(--xxx)` 引用，保证全站视觉一致性。

### 3.2 Element Plus 主题定制
通过 `.el-button--primary` 选择器覆盖 Element Plus 的 CSS 变量（`--el-button-bg-color`、`--el-button-hover-*`、`--el-button-active-*`），将组件默认蓝色替换为项目主色。`.el-button--primary.is-round` 进一步应用 `--radius-round` 实现圆角按钮。

### 3.3 全局工具类
`global.scss` 提供可直接复用的语义化类名：
- `.page-container`：页面容器，带内边距和背景。
- `.card`：卡片容器，带圆角、内边距、阴影。
- `.section-title`：章节标题。
- `.text-primary/secondary/danger/success`：语义化文字颜色。
- `.btn-full`：移动端全宽按钮（48px 高度，圆角）。

### 3.4 布局与响应式
- `html, body` 设置 `max-width: 750px` 并 `margin: 0 auto`，模拟手机屏幕宽度。
- 字体栈使用 `-apple-system, BlinkMacSystemFont, 'PingFang SC', 'Helvetica Neue', 'Microsoft YaHei'`，优先苹方/微软雅黑，确保中文显示质量。
- `body` 设置 `min-height: 100vh`，保证内容撑满视口。

### 3.5 构建与资源
- 使用 Vite 插件 `@vitejs/plugin-vue` 处理 `.vue` 单文件组件。
- 通过 `resolve.alias` 配置 `@` 指向 `src`，组件间引用统一使用绝对路径。
- 开发时通过 proxy 将 `/api` 请求转发到 `http://localhost:8080`，避免跨域问题。

## 4. 约定与约束

- **样式组织**：全局样式统一放在 `src/assets/styles/global.scss`，不分散在各组件内部；业务组件应优先复用 `.card`、`.page-container`、`.text-*` 等工具类，而非重复书写样式。
- **主题色唯一入口**：所有品牌色必须通过 `--color-primary*` 变量引用，禁止在组件中硬编码十六进制颜色值。
- **Element Plus 覆盖方式**：只能通过覆盖 CSS 变量的方式定制主题，不得直接修改 `node_modules` 中的样式。
- **移动端优先**：新增页面需遵循 375×812 设计稿尺寸，并使用 `max-width: 750px` 居中显示；按钮推荐使用 `.btn-full` 获得一致的全宽体验。
- **字体规范**：全局字体栈固定，新增页面不得覆盖 `html/body` 的 `font-family`。
- **暗色模式范围**：当前暗色模式仅在 Vite 模板的 `style.css` 中生效，业务样式尚未接入暗色分支；如需扩展，应在 `global.scss` 中补充对应的 `@media (prefers-color-scheme: dark)` 变量覆盖。
- **原型与生产分离**：`prototype/index.html` 是独立的产品原型文件，不参与构建流程，不应将其样式逻辑带入生产代码。