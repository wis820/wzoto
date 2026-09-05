package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.LearningTask;
import com.wzoto.domain.repository.LearningTaskRepository;
import com.wzoto.domain.valobj.LearningTaskStatus;
import com.wzoto.domain.valobj.LearningTaskType;
import com.wzoto.infrastructure.mapper.LearningTaskMapper;
import com.wzoto.infrastructure.pojo.LearningTaskPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 学习任务记录仓储实现 - 基础设施层
 */
@Repository
@RequiredArgsConstructor
public class LearningTaskRepositoryImpl implements LearningTaskRepository {

    private final LearningTaskMapper learningTaskMapper;

    @Override
    public LearningTask save(LearningTask task) {
        LearningTaskPO po = toPO(task);
        learningTaskMapper.insert(po);
        task.setId(po.getId());
        return task;
    }

    @Override
    public LearningTask findById(Long id) {
        LearningTaskPO po = learningTaskMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public List<LearningTask> findByChildId(Long childId) {
        LambdaQueryWrapper<LearningTaskPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LearningTaskPO::getChildId, childId).orderByDesc(LearningTaskPO::getTaskDate);
        return learningTaskMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<LearningTask> findByChildIdAndTaskDate(Long childId, LocalDate taskDate) {
        LambdaQueryWrapper<LearningTaskPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LearningTaskPO::getChildId, childId)
                .eq(LearningTaskPO::getTaskDate, taskDate)
                .orderByAsc(LearningTaskPO::getCreatedAt);
        return learningTaskMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<LearningTask> findByChildIdAndStatus(Long childId, LearningTaskStatus status) {
        LambdaQueryWrapper<LearningTaskPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LearningTaskPO::getChildId, childId)
                .eq(LearningTaskPO::getStatus, status.getCode())
                .orderByDesc(LearningTaskPO::getTaskDate);
        return learningTaskMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public LearningTask update(LearningTask task) {
        LearningTaskPO po = toPO(task);
        learningTaskMapper.updateById(po);
        return task;
    }

    @Override
    public int batchSave(List<LearningTask> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (LearningTask task : tasks) {
            learningTaskMapper.insert(toPO(task));
            count++;
        }
        return count;
    }

    private LearningTask toEntity(LearningTaskPO po) {
        LearningTask entity = new LearningTask();
        entity.setId(po.getId());
        entity.setChildId(po.getChildId());
        entity.setPlanId(po.getPlanId());
        entity.setTaskType(po.getTaskType() != null ? LearningTaskType.fromCode(po.getTaskType()) : null);
        entity.setSubject(po.getSubject());
        entity.setTitle(po.getTitle());
        entity.setResourceId(po.getResourceId());
        entity.setTaskDate(po.getTaskDate());
        entity.setStatus(po.getStatus() != null ? LearningTaskStatus.fromCode(po.getStatus()) : null);
        entity.setStartTime(po.getStartTime());
        entity.setCompleteTime(po.getCompleteTime());
        entity.setSpentSeconds(po.getSpentSeconds());
        entity.setDeleted(po.getDeleted());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private LearningTaskPO toPO(LearningTask entity) {
        LearningTaskPO po = new LearningTaskPO();
        po.setId(entity.getId());
        po.setChildId(entity.getChildId());
        po.setPlanId(entity.getPlanId());
        po.setTaskType(entity.getTaskType() != null ? entity.getTaskType().getCode() : null);
        po.setSubject(entity.getSubject());
        po.setTitle(entity.getTitle());
        po.setResourceId(entity.getResourceId());
        po.setTaskDate(entity.getTaskDate());
        po.setStatus(entity.getStatus() != null ? entity.getStatus().getCode() : null);
        po.setStartTime(entity.getStartTime());
        po.setCompleteTime(entity.getCompleteTime());
        po.setSpentSeconds(entity.getSpentSeconds());
        po.setDeleted(entity.getDeleted());
        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        return po;
    }
}
