package com.wzoto.interfaces.controller;

import com.wzoto.application.service.LearningPlanApplicationService;
import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.LearningTask;
import com.wzoto.domain.valobj.LearningTaskStatus;
import com.wzoto.domain.valobj.LearningTaskType;
import com.wzoto.interfaces.common.R;
import com.wzoto.interfaces.vo.learning.LearningTaskVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 学习任务控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/learning")
@RequiredArgsConstructor
public class LearningTaskController {

    private final LearningPlanApplicationService learningPlanApplicationService;

    @GetMapping("/tasks/{childId}")
    public R<List<LearningTaskVO>> listTasks(@PathVariable Long childId,
                                             @RequestParam(required = false) String date) {
        LocalDate taskDate = date != null && !date.isBlank() ? LocalDate.parse(date) : null;
        List<LearningTask> tasks = learningPlanApplicationService.listTasks(childId, taskDate);
        return R.ok(tasks.stream().map(this::toVO).collect(Collectors.toList()));
    }

    @PostMapping("/task/{taskId}/complete")
    public R<LearningTaskVO> completeTask(@PathVariable Long taskId) {
        log.info("[BI] task_complete|userId={}, taskId={}", UserContext.getCurrentUserId(), taskId);
        LearningTask task = learningPlanApplicationService.completeTask(taskId);
        return R.ok(toVO(task));
    }

    private LearningTaskVO toVO(LearningTask task) {
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
