package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.AiGradingRecord;
import com.wzoto.domain.repository.AiGradingRecordRepository;
import com.wzoto.domain.valobj.AiGradingType;
import com.wzoto.domain.valobj.GradeType;
import com.wzoto.infrastructure.mapper.AiGradingRecordMapper;
import com.wzoto.infrastructure.pojo.AiGradingRecordPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/** AI批改记录仓储实现 */
@Repository
@RequiredArgsConstructor
public class AiGradingRecordRepositoryImpl implements AiGradingRecordRepository {

    private final AiGradingRecordMapper aiGradingRecordMapper;

    @Override
    public AiGradingRecord save(AiGradingRecord record) {
        AiGradingRecordPO po = toPO(record);
        aiGradingRecordMapper.insert(po);
        record.setId(po.getId());
        return record;
    }

    @Override
    public AiGradingRecord findById(Long id) {
        AiGradingRecordPO po = aiGradingRecordMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public List<AiGradingRecord> findByChildId(Long childId) {
        LambdaQueryWrapper<AiGradingRecordPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AiGradingRecordPO::getChildId, childId)
                .orderByDesc(AiGradingRecordPO::getCreatedAt);
        return aiGradingRecordMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<AiGradingRecord> findByChildIdAndType(Long childId, String gradingType) {
        LambdaQueryWrapper<AiGradingRecordPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AiGradingRecordPO::getChildId, childId)
                .eq(gradingType != null, AiGradingRecordPO::getGradingType, gradingType)
                .orderByDesc(AiGradingRecordPO::getCreatedAt);
        return aiGradingRecordMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    private AiGradingRecord toEntity(AiGradingRecordPO po) {
        AiGradingRecord entity = new AiGradingRecord();
        entity.setId(po.getId());
        entity.setChildId(po.getChildId());
        entity.setParentId(po.getParentId());
        entity.setGradingType(po.getGradingType() != null ? AiGradingType.fromCode(po.getGradingType()) : null);
        entity.setSubject(po.getSubject());
        entity.setGrade(po.getGrade() != null ? GradeType.fromCode(po.getGrade()) : null);
        entity.setTitle(po.getTitle());
        entity.setContentText(po.getContentText());
        entity.setContentAudioUrl(po.getContentAudioUrl());
        entity.setAiResultJson(po.getAiResultJson());
        entity.setScore(po.getScore());
        entity.setErrorCount(po.getErrorCount());
        entity.setSuggestion(po.getSuggestion());
        entity.setDeleted(po.getDeleted());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private AiGradingRecordPO toPO(AiGradingRecord entity) {
        AiGradingRecordPO po = new AiGradingRecordPO();
        po.setId(entity.getId());
        po.setChildId(entity.getChildId());
        po.setParentId(entity.getParentId());
        po.setGradingType(entity.getGradingType() != null ? entity.getGradingType().getCode() : null);
        po.setSubject(entity.getSubject());
        po.setGrade(entity.getGrade() != null ? entity.getGrade().getCode() : null);
        po.setTitle(entity.getTitle());
        po.setContentText(entity.getContentText());
        po.setContentAudioUrl(entity.getContentAudioUrl());
        po.setAiResultJson(entity.getAiResultJson());
        po.setScore(entity.getScore());
        po.setErrorCount(entity.getErrorCount());
        po.setSuggestion(entity.getSuggestion());
        po.setDeleted(entity.getDeleted());
        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        return po;
    }
}
