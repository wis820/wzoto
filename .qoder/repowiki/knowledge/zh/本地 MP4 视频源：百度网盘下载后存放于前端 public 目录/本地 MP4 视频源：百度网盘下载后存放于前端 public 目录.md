---
kind: external_dependency
name: 本地 MP4 视频源：百度网盘下载后存放于前端 public 目录
slug: baidu-pan
category: external_dependency
category_hints:
    - client_constraint
    - framework_behavior
scope:
    - '**'
source_files:
    - wzoto-frontend/public/videos/grade4-math/
---

### 角色与集成点
- 百度网盘分享链接**不能直接作为视频流嵌入播放**（无公开直链、不支持 iframe 嵌入），因此本项目的做法是：从百度网盘下载 MP4 文件，重命名后放入 `wzoto-frontend/public/videos/grade4-math/`，由前端静态资源服务直接提供播放。
- 四年级数学 29 个 MP4 已按知识点（大数的认识、角的度量、三位数乘两位数、除数是两位数的除法、条形统计图、数学广角—优化、复习）映射到对应章节。

### 稳定用法要点
- 新增网盘视频的流程：下载 → 重命名为 `NN-标题.mp4` → 放入 `public/videos/grade4-math/` → 后端种子数据中的 URL 指向该相对路径。
- 该方案绕过百度网盘直链限制，但要求部署时将 `public/videos` 目录一并发布到 Web 服务器。

### 注意事项
- 不要尝试用分享链接直链抓取视频流，既不可靠也违反服务条款；应始终走“下载→本地托管”流程。