package com.wzoto.domain.service;

import com.wzoto.domain.entity.AiLearningPlan;
import com.wzoto.domain.entity.Child;
import com.wzoto.domain.repository.AiLearningPlanRepository;
import com.wzoto.domain.repository.AiGenerator;
import com.wzoto.domain.repository.ChildRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * AI学习规划领域服务 - 根据学情生成每日计划、自动关联任务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiLearningPlanDomainService {

    private final AiLearningPlanRepository aiLearningPlanRepository;
    private final AiGenerator aiGenerator;
    private final ChildRepository childRepository;

    /**
     * 生成AI学习计划
     */
    public AiLearningPlan generatePlan(Long parentId, Long childId, String weakPoints, String studyTime) {
        validateOwnership(parentId, childId);
        Child child = childRepository.findById(childId);
        String childName = child != null ? child.getName() : "学生";
        String grade = child != null && child.getGrade() != null ? child.getGrade().getDesc() : "未知";

        LocalDate today = LocalDate.now();
        AiLearningPlan existing = aiLearningPlanRepository.findByChildIdAndDate(childId, today);
        if (existing != null) {
            log.info("今日已有AI学习计划, childId={}", childId);
            return existing;
        }

        String planJson = aiGenerator.generateDailyPlan(childName, grade, weakPoints, studyTime);
        String weakPointsJson = weakPoints != null ? weakPoints : "";

        AiLearningPlan plan = AiLearningPlan.create(childId, parentId, today, planJson, weakPointsJson,
                "AI根据学情自动生成的每日学习计划");
        AiLearningPlan saved = aiLearningPlanRepository.save(plan);
        log.info("AI学习计划生成成功, childId={}, planId={}", childId, saved.getId());
        return saved;
    }

    /**
     * 获取今日AI计划
     */
    public AiLearningPlan getTodayPlan(Long parentId, Long childId) {
        validateOwnership(parentId, childId);
        return aiLearningPlanRepository.findByChildIdAndDate(childId, LocalDate.now());
    }

    /**
     * 应用计划到学习任务
     */
    public AiLearningPlan applyPlan(Long parentId, Long planId) {
        AiLearningPlan plan = aiLearningPlanRepository.findById(planId);
        if (plan == null) {
            throw new IllegalArgumentException("学习计划不存在");
        }
        validateOwnership(parentId, plan.getChildId());
        plan.markApplied();
        AiLearningPlan updated = aiLearningPlanRepository.update(plan);
        log.info("AI学习计划已应用, planId={}, childId={}", planId, plan.getChildId());
        return updated;
    }

    /**
     * 查询计划历史
     */
    public List<AiLearningPlan> getPlanHistory(Long parentId, Long childId) {
        validateOwnership(parentId, childId);
        return aiLearningPlanRepository.findByChildId(childId);
    }

    private void validateOwnership(Long parentId, Long childId) {
        Child child = childRepository.findById(childId);
        if (child == null || !child.belongsTo(parentId)) {
            throw new IllegalArgumentException("无权操作该子女档案");
        }
    }
}
