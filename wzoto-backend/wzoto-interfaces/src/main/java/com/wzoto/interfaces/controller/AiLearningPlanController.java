package com.wzoto.interfaces.controller;

import com.wzoto.application.service.AiLearningPlanApplicationService;
import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.AiLearningPlan;
import com.wzoto.interfaces.common.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** AI学习规划控制器 */
@Slf4j
@RestController
@RequestMapping("/api/ai/plan")
@RequiredArgsConstructor
public class AiLearningPlanController {

    private final AiLearningPlanApplicationService aiLearningPlanApplicationService;

    @PostMapping("/generate/{childId}")
    public R<AiLearningPlan> generate(@PathVariable Long childId,
                                       @RequestParam(required = false) String weakPoints,
                                       @RequestParam(required = false) String studyTime) {
        log.info("[BI] ai_plan_generate|userId={}, childId={}", UserContext.getCurrentUserId(), childId);
        return R.ok(aiLearningPlanApplicationService.generatePlan(childId, weakPoints, studyTime));
    }

    @GetMapping("/today/{childId}")
    public R<AiLearningPlan> getToday(@PathVariable Long childId) {
        AiLearningPlan plan = aiLearningPlanApplicationService.getTodayPlan(childId);
        return plan != null ? R.ok(plan) : R.ok();
    }

    @PutMapping("/apply/{planId}")
    public R<AiLearningPlan> apply(@PathVariable Long planId) {
        log.info("[BI] ai_plan_apply|userId={}, planId={}", UserContext.getCurrentUserId(), planId);
        return R.ok(aiLearningPlanApplicationService.applyPlan(planId));
    }

    @GetMapping("/history/{childId}")
    public R<List<AiLearningPlan>> getHistory(@PathVariable Long childId) {
        return R.ok(aiLearningPlanApplicationService.getPlanHistory(childId));
    }
}
