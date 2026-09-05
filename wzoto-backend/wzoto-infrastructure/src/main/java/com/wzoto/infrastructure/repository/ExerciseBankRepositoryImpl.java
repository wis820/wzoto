package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.ExerciseBank;
import com.wzoto.domain.repository.ExerciseBankRepository;
import com.wzoto.domain.valobj.ExerciseDifficulty;
import com.wzoto.domain.valobj.GradeType;
import com.wzoto.domain.valobj.TextbookVersion;
import com.wzoto.infrastructure.mapper.ExerciseBankMapper;
import com.wzoto.infrastructure.pojo.ExerciseBankPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/** 习题库仓储实现 */
@Repository
@RequiredArgsConstructor
public class ExerciseBankRepositoryImpl implements ExerciseBankRepository {

    private final ExerciseBankMapper exerciseBankMapper;

    @Override
    public ExerciseBank save(ExerciseBank exercise) {
        ExerciseBankPO po = toPO(exercise);
        exerciseBankMapper.insert(po);
        exercise.setId(po.getId());
        return exercise;
    }

    @Override
    public ExerciseBank findById(Long id) {
        ExerciseBankPO po = exerciseBankMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public ExerciseBank update(ExerciseBank exercise) {
        ExerciseBankPO po = toPO(exercise);
        exerciseBankMapper.updateById(po);
        return exercise;
    }

    @Override
    public List<ExerciseBank> findByGradeAndSubject(GradeType grade, String subject) {
        LambdaQueryWrapper<ExerciseBankPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(grade != null, ExerciseBankPO::getGrade, grade != null ? grade.getCode() : null)
                .eq(subject != null, ExerciseBankPO::getSubject, subject)
                .orderByAsc(ExerciseBankPO::getSortOrder);
        return exerciseBankMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<ExerciseBank> findByKnowledgePoint(Long knowledgePointId) {
        LambdaQueryWrapper<ExerciseBankPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExerciseBankPO::getKnowledgePointId, knowledgePointId)
                .orderByAsc(ExerciseBankPO::getSortOrder);
        return exerciseBankMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<ExerciseBank> findByConditions(GradeType grade, String subject, String difficulty, String questionType, int limit) {
        LambdaQueryWrapper<ExerciseBankPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(grade != null, ExerciseBankPO::getGrade, grade != null ? grade.getCode() : null)
                .eq(subject != null, ExerciseBankPO::getSubject, subject)
                .eq(difficulty != null, ExerciseBankPO::getDifficulty, difficulty)
                .eq(questionType != null, ExerciseBankPO::getQuestionType, questionType)
                .orderByAsc(ExerciseBankPO::getSortOrder)
                .last(limit > 0 ? "LIMIT " + limit : "");
        return exerciseBankMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    private ExerciseBank toEntity(ExerciseBankPO po) {
        ExerciseBank entity = new ExerciseBank();
        entity.setId(po.getId());
        entity.setGrade(po.getGrade() != null ? GradeType.fromCode(po.getGrade()) : null);
        entity.setSubject(po.getSubject());
        entity.setTextbookVersion(po.getTextbookVersion() != null ? TextbookVersion.fromCode(po.getTextbookVersion()) : null);
        entity.setKnowledgePointId(po.getKnowledgePointId());
        entity.setQuestionType(po.getQuestionType());
        entity.setQuestionContent(po.getQuestionContent());
        entity.setOptions(po.getOptions());
        entity.setCorrectAnswer(po.getCorrectAnswer());
        entity.setExplanation(po.getExplanation());
        entity.setDifficulty(po.getDifficulty() != null ? ExerciseDifficulty.fromCode(po.getDifficulty()) : null);
        entity.setSourceType(po.getSourceType());
        entity.setVipOnly(po.getVipOnly());
        entity.setUseCount(po.getUseCount());
        entity.setCorrectRate(po.getCorrectRate());
        entity.setSortOrder(po.getSortOrder());
        entity.setDeleted(po.getDeleted());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private ExerciseBankPO toPO(ExerciseBank entity) {
        ExerciseBankPO po = new ExerciseBankPO();
        po.setId(entity.getId());
        po.setGrade(entity.getGrade() != null ? entity.getGrade().getCode() : null);
        po.setSubject(entity.getSubject());
        po.setTextbookVersion(entity.getTextbookVersion() != null ? entity.getTextbookVersion().getCode() : null);
        po.setKnowledgePointId(entity.getKnowledgePointId());
        po.setQuestionType(entity.getQuestionType());
        po.setQuestionContent(entity.getQuestionContent());
        po.setOptions(entity.getOptions());
        po.setCorrectAnswer(entity.getCorrectAnswer());
        po.setExplanation(entity.getExplanation());
        po.setDifficulty(entity.getDifficulty() != null ? entity.getDifficulty().getCode() : null);
        po.setSourceType(entity.getSourceType());
        po.setVipOnly(entity.getVipOnly());
        po.setUseCount(entity.getUseCount());
        po.setCorrectRate(entity.getCorrectRate());
        po.setSortOrder(entity.getSortOrder());
        po.setDeleted(entity.getDeleted());
        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        return po;
    }
}
