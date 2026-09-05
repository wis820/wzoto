package com.wzoto.domain.service;

import com.wzoto.domain.entity.Child;
import com.wzoto.domain.entity.LearningPlanConfig;
import com.wzoto.domain.entity.LearningTask;
import com.wzoto.domain.repository.ChildRepository;
import com.wzoto.domain.repository.LearningPlanConfigRepository;
import com.wzoto.domain.repository.LearningTaskRepository;
import com.wzoto.domain.valobj.LearningTaskType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 学习计划领域服务 - 生成默认计划、调整配置、下发专项练习
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LearningPlanDomainService {

    private final LearningPlanConfigRepository planConfigRepository;
    private final LearningTaskRepository learningTaskRepository;
    private final ChildRepository childRepository;

    /**
     * 获取或创建默认学习计划配置
     */
    public LearningPlanConfig getOrCreateDefault(Long childId) {
        LearningPlanConfig config = planConfigRepository.findByChildId(childId);
        if (config == null) {
            config = LearningPlanConfig.createDefault(childId);
            config = planConfigRepository.save(config);
            log.info("创建默认学习计划配置, childId={}", childId);
        }
        return config;
    }

    /**
     * 保存学习计划配置
     */
    public LearningPlanConfig saveConfig(Long childId, LearningPlanConfig config) {
        LearningPlanConfig existing = planConfigRepository.findByChildId(childId);
        if (existing == null) {
            config.setChildId(childId);
            return planConfigRepository.save(config);
        }
        existing.adjustDuration(config.getDailyDurationMinutes());
        existing.adjustSubjectWeight(config.getChineseWeight(), config.getMathWeight(), config.getEnglishWeight());
        existing.setSpecialCalculationEnabled(config.getSpecialCalculationEnabled());
        existing.setSpecialApplicationEnabled(config.getSpecialApplicationEnabled());
        existing.setSpecialLiteracyEnabled(config.getSpecialLiteracyEnabled());
        existing.setSpecialWordsEnabled(config.getSpecialWordsEnabled());
        return planConfigRepository.update(existing);
    }

    /**
     * 下发专项练习任务
     */
    public List<LearningTask> assignSpecialTasks(Long parentId, Long childId, String specialType, Integer count) {
        Child child = childRepository.findById(childId);
        if (child == null || !child.belongsTo(parentId)) {
            throw new IllegalArgumentException("无权操作该子女档案");
        }
        LearningTaskType taskType = resolveSpecialTaskType(specialType);
        if (taskType == null) {
            throw new IllegalArgumentException("未知的专项练习类型: " + specialType);
        }
        if (count == null || count <= 0) {
            count = 5;
        }
        List<LearningTask> tasks = new ArrayList<>();
        LocalDate today = LocalDate.now();
        String subject = resolveSubject(taskType);
        for (int i = 0; i < count; i++) {
            LearningTask task = LearningTask.create(childId, null, taskType, subject,
                    taskType.getDesc() + " " + (i + 1), null, today);
            tasks.add(task);
        }
        learningTaskRepository.batchSave(tasks);
        log.info("下发专项练习, parentId={}, childId={}, specialType={}, count={}", parentId, childId, specialType, count);
        return tasks;
    }

    private LearningTaskType resolveSpecialTaskType(String specialType) {
        return switch (specialType) {
            case "calculation" -> LearningTaskType.SPECIAL_CALCULATION;
            case "application" -> LearningTaskType.SPECIAL_APPLICATION;
            case "literacy" -> LearningTaskType.SPECIAL_LITERACY;
            case "words" -> LearningTaskType.SPECIAL_WORDS;
            default -> null;
        };
    }

    private String resolveSubject(LearningTaskType taskType) {
        return switch (taskType) {
            case SPECIAL_CALCULATION, SPECIAL_APPLICATION -> "math";
            case SPECIAL_LITERACY -> "chinese";
            case SPECIAL_WORDS -> "english";
            default -> "math";
        };
    }
}
