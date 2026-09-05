package com.wzoto.interfaces.controller;

import com.wzoto.domain.context.UserContext;
import com.wzoto.interfaces.common.R;
import com.wzoto.interfaces.vo.learning.CourseTreeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 课程目录树控制器 - 对标洋葱学院左侧目录导航
 * 返回结构：年级 > 学科 > 章节 > 课时（资源列表）
 */
@Slf4j
@RestController
@RequestMapping("/api/learning/courses")
@RequiredArgsConstructor
public class CourseTreeController {

    private final JdbcTemplate jdbcTemplate;

    /**
     * 获取课程目录树
     * GET /api/learning/courses?childId=2&grade=GRADE_4&subject=MATH
     */
    @GetMapping
    public R<List<CourseTreeVO>> getCourseTree(
            @RequestParam Long childId,
            @RequestParam(required = false) String grade,
            @RequestParam(required = false) String subject) {

        log.info("[BI] course_tree|userId={}, childId={}, grade={}, subject={}",
                UserContext.getCurrentUserId(), childId, grade, subject);

        // 查询章节树（2级：顶级章节 + 子章节）
        StringBuilder chapterSql = new StringBuilder(
                "SELECT c.id, c.parent_id, c.grade, c.subject, c.title, c.depth, c.sort_order " +
                "FROM t_resource_chapter c WHERE c.deleted = 0");
        List<Object> params = new ArrayList<>();
        if (grade != null) {
            chapterSql.append(" AND c.grade = ?");
            params.add(grade);
        }
        if (subject != null) {
            chapterSql.append(" AND c.subject = ?");
            params.add(subject);
        }
        chapterSql.append(" ORDER BY c.sort_order ASC");

        List<Map<String, Object>> chapters = jdbcTemplate.queryForList(chapterSql.toString(), params.toArray());

        // 查询所有 VIDEO 类型资源（作为课时）
        StringBuilder resSql = new StringBuilder(
                "SELECT r.id, r.grade, r.subject, r.title, r.cover_url, r.content_url, " +
                "r.duration_seconds, r.knowledge_point, r.source_type, r.vip_only, r.status, " +
                "r.description, r.resource_type " +
                "FROM t_learning_resource r WHERE r.deleted = 0 AND r.resource_type = 'VIDEO' " +
                "AND (r.status IS NULL OR r.status = 'PUBLISHED')");
        List<Object> resParams = new ArrayList<>();
        if (grade != null) {
            resSql.append(" AND r.grade = ?");
            resParams.add(grade);
        }
        if (subject != null) {
            resSql.append(" AND r.subject = ?");
            resParams.add(subject);
        }
        resSql.append(" ORDER BY r.sort_order ASC");

        List<Map<String, Object>> resources = jdbcTemplate.queryForList(resSql.toString(), resParams.toArray());

        // 查询观看进度（按 childId）
        String progressSql = "SELECT resource_id, last_position_seconds, progress_percent, completed " +
                "FROM t_course_watch_record WHERE child_id = ? AND deleted = 0";
        List<Map<String, Object>> progressList = jdbcTemplate.queryForList(progressSql, childId);
        Map<Long, Map<String, Object>> progressMap = new HashMap<>();
        for (Map<String, Object> p : progressList) {
            progressMap.put(((Number) p.get("resource_id")).longValue(), p);
        }

        // 组装树形结构
        Map<Long, CourseTreeVO> nodeMap = new LinkedHashMap<>();
        List<CourseTreeVO> roots = new ArrayList<>();

        // 先创建顶级节点
        for (Map<String, Object> ch : chapters) {
            long id = ((Number) ch.get("id")).longValue();
            int depth = ((Number) ch.get("depth")).intValue();
            if (depth <= 1) {
                CourseTreeVO node = CourseTreeVO.builder()
                        .id(id)
                        .parentId(null)
                        .title((String) ch.get("title"))
                        .grade((String) ch.get("grade"))
                        .subject((String) ch.get("subject"))
                        .isChapter(true)
                        .sortOrder(((Number) ch.get("sort_order")).intValue())
                        .children(new ArrayList<>())
                        .resources(new ArrayList<>())
                        .build();
                nodeMap.put(id, node);
                roots.add(node);
            }
        }

        // 子章节挂载到父节点
        for (Map<String, Object> ch : chapters) {
            long id = ((Number) ch.get("id")).longValue();
            int depth = ((Number) ch.get("depth")).intValue();
            Long parentId = ch.get("parent_id") != null ? ((Number) ch.get("parent_id")).longValue() : null;
            if (depth > 1 && parentId != null) {
                CourseTreeVO node = CourseTreeVO.builder()
                        .id(id)
                        .parentId(parentId)
                        .title((String) ch.get("title"))
                        .grade((String) ch.get("grade"))
                        .subject((String) ch.get("subject"))
                        .isChapter(true)
                        .sortOrder(((Number) ch.get("sort_order")).intValue())
                        .children(new ArrayList<>())
                        .resources(new ArrayList<>())
                        .build();
                nodeMap.put(id, node);
                CourseTreeVO parent = nodeMap.get(parentId);
                if (parent != null) {
                    parent.getChildren().add(node);
                }
            }
        }

        // 资源按知识点匹配到最近的章节节点（简化：按 grade+subject 分组后平均分配到子章节）
        // 实际项目中应通过 knowledge_point_id 关联
        Map<String, List<CourseTreeVO.ResourceItem>> resByGradeSubject = new HashMap<>();
        for (Map<String, Object> r : resources) {
            String key = r.get("grade") + "_" + r.get("subject");
            CourseTreeVO.ResourceItem item = buildResourceItem(r, progressMap);
            resByGradeSubject.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
        }

        // 将资源分配到各顶级节点
        for (CourseTreeVO root : roots) {
            String key = root.getGrade() + "_" + root.getSubject();
            List<CourseTreeVO.ResourceItem> items = resByGradeSubject.getOrDefault(key, Collections.emptyList());
            // 如果有子章节，均匀分配；否则全放当前节点
            if (!root.getChildren().isEmpty()) {
                List<CourseTreeVO> subs = root.getChildren();
                for (int i = 0; i < items.size(); i++) {
                    subs.get(i % subs.size()).getResources().add(items.get(i));
                }
            } else {
                root.setResources(items);
            }
        }

        return R.ok(roots);
    }

    private CourseTreeVO.ResourceItem buildResourceItem(Map<String, Object> r, Map<Long, Map<String, Object>> progressMap) {
        long id = ((Number) r.get("id")).longValue();
        Map<String, Object> progress = progressMap.get(id);
        return CourseTreeVO.ResourceItem.builder()
                .id(id)
                .title((String) r.get("title"))
                .coverUrl((String) r.get("cover_url"))
                .contentUrl((String) r.get("content_url"))
                .durationSeconds(r.get("duration_seconds") != null ? ((Number) r.get("duration_seconds")).intValue() : null)
                .knowledgePoint((String) r.get("knowledge_point"))
                .sourceType((String) r.get("source_type"))
                .vipOnly(toBool(r.get("vip_only")))
                .description((String) r.get("description"))
                .lastPositionSeconds(progress != null && progress.get("last_position_seconds") != null ? ((Number) progress.get("last_position_seconds")).intValue() : 0)
                .progressPercent(progress != null && progress.get("progress_percent") != null ? ((Number) progress.get("progress_percent")).intValue() : 0)
                .completed(progress != null && toBool(progress.get("completed")))
                .build();
    }

    private boolean toBool(Object val) {
        if (val == null) return false;
        if (val instanceof Boolean) return (Boolean) val;
        if (val instanceof Number) return ((Number) val).intValue() == 1;
        return "true".equalsIgnoreCase(val.toString()) || "1".equals(val.toString());
    }
}
