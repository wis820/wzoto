package com.wzoto.application.service;

import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.LearningPlanConfig;
import com.wzoto.domain.entity.LearningTask;
import com.wzoto.domain.service.ChildDomainService;
import com.wzoto.domain.service.LearningPlanDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 学习计划应用服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LearningPlanApplicationService {

    private final LearningPlanDomainService learningPlanDomainService;
    private final ChildDomainService childDomainService;
    private final com.wzoto.domain.repository.LearningTaskRepository learningTaskRepository;

    public LearningPlanConfig getPlanConfig(Long childId) {
        Long parentId = UserContext.getCurrentUserId();
        childDomainService.findAndValidate(parentId, childId);
        return learningPlanDomainService.getOrCreateDefault(childId);
    }

    public List<LearningTask> listTasks(Long childId, LocalDate taskDate) {
        Long parentId = UserContext.getCurrentUserId();
        childDomainService.findAndValidate(parentId, childId);
        if (taskDate != null) {
            return learningTaskRepository.findByChildIdAndTaskDate(childId, taskDate);
        }
        return learningTaskRepository.findByChildId(childId);
    }

    @Transactional(rollbackFor = Exception.class)
    public LearningPlanConfig savePlanConfig(Long childId, LearningPlanConfig config) {
        Long parentId = UserContext.getCurrentUserId();
        childDomainService.findAndValidate(parentId, childId);
        return learningPlanDomainService.saveConfig(childId, config);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<LearningTask> assignSpecialTask(Long childId, String specialType, Integer count) {
        Long parentId = UserContext.getCurrentUserId();
        return learningPlanDomainService.assignSpecialTasks(parentId, childId, specialType, count);
    }

    @Transactional(rollbackFor = Exception.class)
    public LearningTask completeTask(Long taskId) {
        LearningTask task = learningTaskRepository.findById(taskId);
        if (task == null) {
            throw new IllegalArgumentException("任务不存在");
        }
        Long parentId = UserContext.getCurrentUserId();
        childDomainService.findAndValidate(parentId, task.getChildId());
        task.complete(null);
        return learningTaskRepository.update(task);
    }
}
