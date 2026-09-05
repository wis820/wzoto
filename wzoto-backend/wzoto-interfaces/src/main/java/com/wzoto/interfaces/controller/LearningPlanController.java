package com.wzoto.interfaces.controller;

import com.wzoto.application.service.LearningPlanApplicationService;
import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.LearningPlanConfig;
import com.wzoto.domain.entity.LearningTask;
import com.wzoto.domain.valobj.LearningTaskStatus;
import com.wzoto.domain.valobj.LearningTaskType;
import com.wzoto.interfaces.common.R;
import com.wzoto.interfaces.dto.learning.CreateTaskDTO;
import com.wzoto.interfaces.dto.learning.LearningPlanConfigDTO;
import com.wzoto.interfaces.vo.learning.LearningTaskVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 学习计划控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/learning/plan")
@RequiredArgsConstructor
public class LearningPlanController {

    private final LearningPlanApplicationService learningPlanApplicationService;

    @GetMapping("/{childId}")
    public R<LearningPlanConfig> getPlanConfig(@PathVariable Long childId) {
        LearningPlanConfig config = learningPlanApplicationService.getPlanConfig(childId);
        return R.ok(config);
    }

    @PostMapping("/{childId}")
    public R<LearningPlanConfig> savePlanConfig(@PathVariable Long childId, @RequestBody LearningPlanConfigDTO dto) {
        log.info("[BI] plan_save|userId={}, childId={}", UserContext.getCurrentUserId(), childId);
        LearningPlanConfig config = new LearningPlanConfig();
        config.setDailyDurationMinutes(dto.getDailyDurationMinutes());
        config.setChineseWeight(dto.getChineseWeight());
        config.setMathWeight(dto.getMathWeight());
        config.setEnglishWeight(dto.getEnglishWeight());
        config.setSpecialCalculationEnabled(dto.getSpecialCalculationEnabled());
        config.setSpecialApplicationEnabled(dto.getSpecialApplicationEnabled());
        config.setSpecialLiteracyEnabled(dto.getSpecialLiteracyEnabled());
        config.setSpecialWordsEnabled(dto.getSpecialWordsEnabled());
        LearningPlanConfig saved = learningPlanApplicationService.savePlanConfig(childId, config);
        return R.ok(saved);
    }

    @PostMapping("/{childId}/special-task")
    public R<List<LearningTaskVO>> assignSpecialTask(@PathVariable Long childId, @RequestBody CreateTaskDTO dto) {
        log.info("[BI] plan_special_task|userId={}, childId={}", UserContext.getCurrentUserId(), childId);
        List<LearningTask> tasks = learningPlanApplicationService.assignSpecialTask(childId, dto.getSpecialType(), dto.getCount());
        return R.ok(tasks.stream().map(this::toTaskVO).collect(Collectors.toList()));
    }

    private LearningTaskVO toTaskVO(LearningTask task) {
        return LearningTaskVO.builder()
                .id(task.getId())
                .childId(task.getChildId())
                .taskType(task.getTaskType() != null ? task.getTaskType().getCode() : null)
                .taskTypeDesc(task.getTaskType() != null ? task.getTaskType().getDesc() : null)
                .subject(task.getSubject())
                .title(task.getTitle())
                .resourceId(task.getResourceId())
                .taskDate(task.getTaskDate())
                .status(task.getStatus() != null ? task.getStatus().getCode() : null)
                .statusDesc(task.getStatus() != null ? task.getStatus().getDesc() : null)
                .startTime(task.getStartTime())
                .completeTime(task.getCompleteTime())
                .spentSeconds(task.getSpentSeconds())
                .build();
    }
}
