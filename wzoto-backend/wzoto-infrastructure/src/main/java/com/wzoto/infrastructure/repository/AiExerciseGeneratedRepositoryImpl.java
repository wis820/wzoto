package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.AiExerciseGenerated;
import com.wzoto.domain.repository.AiExerciseGeneratedRepository;
import com.wzoto.domain.valobj.ExerciseDifficulty;
import com.wzoto.domain.valobj.GradeType;
import com.wzoto.infrastructure.mapper.AiExerciseGeneratedMapper;
import com.wzoto.infrastructure.pojo.AiExerciseGeneratedPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/** AI生成练习题仓储实现 */
@Repository
@RequiredArgsConstructor
public class AiExerciseGeneratedRepositoryImpl implements AiExerciseGeneratedRepository {

    private final AiExerciseGeneratedMapper aiExerciseGeneratedMapper;

    @Override
    public AiExerciseGenerated save(AiExerciseGenerated exercise) {
        AiExerciseGeneratedPO po = toPO(exercise);
        aiExerciseGeneratedMapper.insert(po);
        exercise.setId(po.getId());
        return exercise;
    }

    @Override
    public AiExerciseGenerated findById(Long id) {
        AiExerciseGeneratedPO po = aiExerciseGeneratedMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public AiExerciseGenerated update(AiExerciseGenerated exercise) {
        AiExerciseGeneratedPO po = toPO(exercise);
        aiExerciseGeneratedMapper.updateById(po);
        return exercise;
    }

    @Override
    public List<AiExerciseGenerated> findByChildId(Long childId) {
        LambdaQueryWrapper<AiExerciseGeneratedPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AiExerciseGeneratedPO::getChildId, childId)
                .orderByDesc(AiExerciseGeneratedPO::getCreatedAt);
        return aiExerciseGeneratedMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<AiExerciseGenerated> findByChildIdAndSubject(Long childId, String subject) {
        LambdaQueryWrapper<AiExerciseGeneratedPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AiExerciseGeneratedPO::getChildId, childId)
                .eq(AiExerciseGeneratedPO::getSubject, subject)
                .orderByDesc(AiExerciseGeneratedPO::getCreatedAt);
        return aiExerciseGeneratedMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<AiExerciseGenerated> findUnanswered(Long childId) {
        LambdaQueryWrapper<AiExerciseGeneratedPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AiExerciseGeneratedPO::getChildId, childId)
                .eq(AiExerciseGeneratedPO::getAnswered, false)
                .orderByDesc(AiExerciseGeneratedPO::getCreatedAt);
        return aiExerciseGeneratedMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    private AiExerciseGenerated toEntity(AiExerciseGeneratedPO po) {
        AiExerciseGenerated entity = new AiExerciseGenerated();
        entity.setId(po.getId());
        entity.setChildId(po.getChildId());
        entity.setParentId(po.getParentId());
        entity.setSource(po.getSource());
        entity.setSourceId(po.getSourceId());
        entity.setSubject(po.getSubject());
        entity.setGrade(po.getGrade() != null ? GradeType.fromCode(po.getGrade()) : null);
        entity.setKnowledgePoint(po.getKnowledgePoint());
        entity.setQuestionContent(po.getQuestionContent());
        entity.setCorrectAnswer(po.getCorrectAnswer());
        entity.setAiExplanation(po.getAiExplanation());
        entity.setDifficulty(po.getDifficulty() != null ? ExerciseDifficulty.fromCode(po.getDifficulty()) : null);
        entity.setAnswered(po.getAnswered());
        entity.setIsCorrect(po.getIsCorrect());
        entity.setAnsweredAt(po.getAnsweredAt());
        entity.setDeleted(po.getDeleted());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private AiExerciseGeneratedPO toPO(AiExerciseGenerated entity) {
        AiExerciseGeneratedPO po = new AiExerciseGeneratedPO();
        po.setId(entity.getId());
        po.setChildId(entity.getChildId());
        po.setParentId(entity.getParentId());
        po.setSource(entity.getSource());
        po.setSourceId(entity.getSourceId());
        po.setSubject(entity.getSubject());
        po.setGrade(entity.getGrade() != null ? entity.getGrade().getCode() : null);
        po.setKnowledgePoint(entity.getKnowledgePoint());
        po.setQuestionContent(entity.getQuestionContent());
        po.setCorrectAnswer(entity.getCorrectAnswer());
        po.setAiExplanation(entity.getAiExplanation());
        po.setDifficulty(entity.getDifficulty() != null ? entity.getDifficulty().getCode() : null);
        po.setAnswered(entity.getAnswered());
        po.setIsCorrect(entity.getIsCorrect());
        po.setAnsweredAt(entity.getAnsweredAt());
        po.setDeleted(entity.getDeleted());
        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        return po;
    }
}
