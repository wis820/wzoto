---
kind: external_dependency
name: 学习资源视频源：B站教育视频嵌入播放
slug: bilibili
category: external_dependency
category_hints:
    - sdk_real_api
    - framework_behavior
scope:
    - '**'
source_files:
    - wzoto-backend/scripts/generate-learning-resources.mjs
    - wzoto-frontend/src/components/OnionPlayer.vue
---

### 角色与集成点
- 本项目将 Bilibili 作为四年级语数英学习资源的**主力视频源**，通过生成器脚本把真实 BV 号写入 `t_learning_resource` 表，前端 `OnionPlayer.vue` 检测 `sourceType=BILIBILI` 后以 `<iframe>` 嵌入 `https://player.bilibili.com/player.html?bvid=...&p=...&autoplay=0` 播放。
- 当前四年级数学、语文、英语各约 28 条资源，其中约 60% 来自 B站公开教学合集。

### 稳定用法要点
- 视频以 **BV 号 + p 分P** 形式构造播放器 URL，不是调用 B站开放 API；新增资源时需在生成器脚本中追加 BV 号并映射到对应知识点章节。
- B站视频为公开可嵌入的 iframe 模式，无需鉴权即可在 Web 端直接播放。
- 验证方式：后端 `/api/learning/resources?childId=3` 返回的 `contentUrl` 应以 `player.bilibili.com` 开头。

### 注意事项
- 若未来改用私有课程或需要后台拉取元数据，需另行接入 B站开放平台 SDK；当前实现仅依赖静态 BV 号。