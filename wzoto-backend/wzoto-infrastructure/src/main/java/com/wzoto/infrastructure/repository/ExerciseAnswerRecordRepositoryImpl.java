package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.ExerciseAnswerRecord;
import com.wzoto.domain.repository.ExerciseAnswerRecordRepository;
import com.wzoto.infrastructure.mapper.ExerciseAnswerRecordMapper;
import com.wzoto.infrastructure.pojo.ExerciseAnswerRecordPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 习题作答记录仓储实现 - 基础设施层
 */
@Repository
@RequiredArgsConstructor
public class ExerciseAnswerRecordRepositoryImpl implements ExerciseAnswerRecordRepository {

    private final ExerciseAnswerRecordMapper exerciseAnswerRecordMapper;

    @Override
    public ExerciseAnswerRecord save(ExerciseAnswerRecord record) {
        ExerciseAnswerRecordPO po = toPO(record);
        exerciseAnswerRecordMapper.insert(po);
        record.setId(po.getId());
        return record;
    }

    @Override
    public ExerciseAnswerRecord findById(Long id) {
        ExerciseAnswerRecordPO po = exerciseAnswerRecordMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public List<ExerciseAnswerRecord> findByChildId(Long childId) {
        LambdaQueryWrapper<ExerciseAnswerRecordPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExerciseAnswerRecordPO::getChildId, childId).orderByDesc(ExerciseAnswerRecordPO::getCreatedAt);
        return exerciseAnswerRecordMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<ExerciseAnswerRecord> findByChildIdAndSubject(Long childId, String subject) {
        LambdaQueryWrapper<ExerciseAnswerRecordPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExerciseAnswerRecordPO::getChildId, childId)
                .eq(ExerciseAnswerRecordPO::getSubject, subject)
                .orderByDesc(ExerciseAnswerRecordPO::getCreatedAt);
        return exerciseAnswerRecordMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<ExerciseAnswerRecord> findByChildIdAndTaskId(Long childId, Long taskId) {
        LambdaQueryWrapper<ExerciseAnswerRecordPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExerciseAnswerRecordPO::getChildId, childId)
                .eq(ExerciseAnswerRecordPO::getTaskId, taskId)
                .orderByDesc(ExerciseAnswerRecordPO::getCreatedAt);
        return exerciseAnswerRecordMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    private ExerciseAnswerRecord toEntity(ExerciseAnswerRecordPO po) {
        ExerciseAnswerRecord entity = new ExerciseAnswerRecord();
        entity.setId(po.getId());
        entity.setChildId(po.getChildId());
        entity.setResourceId(po.getResourceId());
        entity.setTaskId(po.getTaskId());
        entity.setSubject(po.getSubject());
        entity.setKnowledgePoint(po.getKnowledgePoint());
        entity.setUserAnswer(po.getUserAnswer());
        entity.setCorrectAnswer(po.getCorrectAnswer());
        entity.setCorrect(po.getCorrect());
        entity.setSpentSeconds(po.getSpentSeconds());
        entity.setDeleted(po.getDeleted());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private ExerciseAnswerRecordPO toPO(ExerciseAnswerRecord entity) {
        ExerciseAnswerRecordPO po = new ExerciseAnswerRecordPO();
        po.setId(entity.getId());
        po.setChildId(entity.getChildId());
        po.setResourceId(entity.getResourceId());
        po.setTaskId(entity.getTaskId());
        po.setSubject(entity.getSubject());
        po.setKnowledgePoint(entity.getKnowledgePoint());
        po.setUserAnswer(entity.getUserAnswer());
        po.setCorrectAnswer(entity.getCorrectAnswer());
        po.setCorrect(entity.getCorrect());
        po.setSpentSeconds(entity.getSpentSeconds());
        po.setDeleted(entity.getDeleted());
        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        return po;
    }
}
