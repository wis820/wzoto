package com.wzoto.application.service;

import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.AiLearningPlan;
import com.wzoto.domain.service.AiLearningPlanDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/** AI学习规划应用服务 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiLearningPlanApplicationService {

    private final AiLearningPlanDomainService aiLearningPlanDomainService;

    public AiLearningPlan generatePlan(Long childId, String weakPoints, String studyTime) {
        Long parentId = UserContext.getCurrentUserId();
        return aiLearningPlanDomainService.generatePlan(parentId, childId, weakPoints, studyTime);
    }

    public AiLearningPlan getTodayPlan(Long childId) {
        Long parentId = UserContext.getCurrentUserId();
        return aiLearningPlanDomainService.getTodayPlan(parentId, childId);
    }

    public AiLearningPlan applyPlan(Long planId) {
        Long parentId = UserContext.getCurrentUserId();
        return aiLearningPlanDomainService.applyPlan(parentId, planId);
    }

    public List<AiLearningPlan> getPlanHistory(Long childId) {
        Long parentId = UserContext.getCurrentUserId();
        return aiLearningPlanDomainService.getPlanHistory(parentId, childId);
    }
}
