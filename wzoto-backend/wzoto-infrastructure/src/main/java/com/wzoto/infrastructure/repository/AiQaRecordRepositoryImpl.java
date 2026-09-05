package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.AiQaRecord;
import com.wzoto.domain.repository.AiQaRecordRepository;
import com.wzoto.domain.valobj.AiQaType;
import com.wzoto.domain.valobj.GradeType;
import com.wzoto.infrastructure.mapper.AiQaRecordMapper;
import com.wzoto.infrastructure.pojo.AiQaRecordPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/** AI提问记录仓储实现 */
@Repository
@RequiredArgsConstructor
public class AiQaRecordRepositoryImpl implements AiQaRecordRepository {

    private final AiQaRecordMapper aiQaRecordMapper;

    @Override
    public AiQaRecord save(AiQaRecord record) {
        AiQaRecordPO po = toPO(record);
        aiQaRecordMapper.insert(po);
        record.setId(po.getId());
        return record;
    }

    @Override
    public AiQaRecord findById(Long id) {
        AiQaRecordPO po = aiQaRecordMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public List<AiQaRecord> findByChildId(Long childId) {
        LambdaQueryWrapper<AiQaRecordPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AiQaRecordPO::getChildId, childId)
                .orderByDesc(AiQaRecordPO::getCreatedAt);
        return aiQaRecordMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<AiQaRecord> findByChildIdAndSubject(Long childId, String subject) {
        LambdaQueryWrapper<AiQaRecordPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AiQaRecordPO::getChildId, childId)
                .eq(AiQaRecordPO::getSubject, subject)
                .orderByDesc(AiQaRecordPO::getCreatedAt);
        return aiQaRecordMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<AiQaRecord> findByConversationId(String conversationId) {
        LambdaQueryWrapper<AiQaRecordPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AiQaRecordPO::getConversationId, conversationId)
                .orderByAsc(AiQaRecordPO::getCreatedAt);
        return aiQaRecordMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public int countTodayByChildId(Long childId) {
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LambdaQueryWrapper<AiQaRecordPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AiQaRecordPO::getChildId, childId)
                .ge(AiQaRecordPO::getCreatedAt, todayStart);
        return Math.toIntExact(aiQaRecordMapper.selectCount(wrapper));
    }

    private AiQaRecord toEntity(AiQaRecordPO po) {
        AiQaRecord entity = new AiQaRecord();
        entity.setId(po.getId());
        entity.setChildId(po.getChildId());
        entity.setParentId(po.getParentId());
        entity.setConversationId(po.getConversationId());
        entity.setSubject(po.getSubject());
        entity.setGrade(po.getGrade() != null ? GradeType.fromCode(po.getGrade()) : null);
        entity.setQaType(po.getQaType() != null ? AiQaType.fromCode(po.getQaType()) : null);
        entity.setQuestionText(po.getQuestionText());
        entity.setQuestionImageUrl(po.getQuestionImageUrl());
        entity.setOcrText(po.getOcrText());
        entity.setAiResponseJson(po.getAiResponseJson());
        entity.setKnowledgeTags(po.getKnowledgeTags());
        entity.setIsFollowUp(po.getIsFollowUp());
        entity.setStepCount(po.getStepCount());
        entity.setDeleted(po.getDeleted());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private AiQaRecordPO toPO(AiQaRecord entity) {
        AiQaRecordPO po = new AiQaRecordPO();
        po.setId(entity.getId());
        po.setChildId(entity.getChildId());
        po.setParentId(entity.getParentId());
        po.setConversationId(entity.getConversationId());
        po.setSubject(entity.getSubject());
        po.setGrade(entity.getGrade() != null ? entity.getGrade().getCode() : null);
        po.setQaType(entity.getQaType() != null ? entity.getQaType().getCode() : null);
        po.setQuestionText(entity.getQuestionText());
        po.setQuestionImageUrl(entity.getQuestionImageUrl());
        po.setOcrText(entity.getOcrText());
        po.setAiResponseJson(entity.getAiResponseJson());
        po.setKnowledgeTags(entity.getKnowledgeTags());
        po.setIsFollowUp(entity.getIsFollowUp());
        po.setStepCount(entity.getStepCount());
        po.setDeleted(entity.getDeleted());
        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        return po;
    }
}
