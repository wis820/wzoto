package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.LearningResource;
import com.wzoto.domain.repository.LearningResourceRepository;
import com.wzoto.domain.valobj.GradeType;
import com.wzoto.domain.valobj.LearningResourceType;
import com.wzoto.domain.valobj.TextbookVersion;
import com.wzoto.infrastructure.mapper.LearningResourceMapper;
import com.wzoto.infrastructure.pojo.LearningResourcePO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 学习资源仓储实现 - 基础设施层
 */
@Repository
@RequiredArgsConstructor
public class LearningResourceRepositoryImpl implements LearningResourceRepository {

    private final LearningResourceMapper learningResourceMapper;

    @Override
    public LearningResource save(LearningResource resource) {
        LearningResourcePO po = toPO(resource);
        learningResourceMapper.insert(po);
        resource.setId(po.getId());
        return resource;
    }

    @Override
    public LearningResource findById(Long id) {
        LearningResourcePO po = learningResourceMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public List<LearningResource> findByGradeAndSubject(GradeType grade, String subject) {
        LambdaQueryWrapper<LearningResourcePO> wrapper = buildWrapper(grade, subject);
        return learningResourceMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<LearningResource> findByGradeAndSubjectAndTextbookVersion(GradeType grade, String subject, TextbookVersion textbookVersion) {
        LambdaQueryWrapper<LearningResourcePO> wrapper = buildWrapper(grade, subject);
        wrapper.eq(textbookVersion != null, LearningResourcePO::getTextbookVersion, textbookVersion != null ? textbookVersion.getCode() : null);
        return learningResourceMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<LearningResource> findByGradeAndSubjectAndType(GradeType grade, String subject, LearningResourceType resourceType) {
        LambdaQueryWrapper<LearningResourcePO> wrapper = buildWrapper(grade, subject);
        wrapper.eq(resourceType != null, LearningResourcePO::getResourceType, resourceType != null ? resourceType.getCode() : null);
        return learningResourceMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<LearningResource> findByGradeAndSubjectAndVipOnly(GradeType grade, String subject, Boolean vipOnly) {
        LambdaQueryWrapper<LearningResourcePO> wrapper = buildWrapper(grade, subject);
        wrapper.eq(vipOnly != null, LearningResourcePO::getVipOnly, vipOnly);
        return learningResourceMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public LearningResource update(LearningResource resource) {
        LearningResourcePO po = toPO(resource);
        learningResourceMapper.updateById(po);
        return resource;
    }

    private LambdaQueryWrapper<LearningResourcePO> buildWrapper(GradeType grade, String subject) {
        LambdaQueryWrapper<LearningResourcePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(grade != null, LearningResourcePO::getGrade, grade != null ? grade.getCode() : null)
                .eq(subject != null, LearningResourcePO::getSubject, subject)
                .orderByAsc(LearningResourcePO::getSortOrder)
                .orderByDesc(LearningResourcePO::getCreatedAt);
        return wrapper;
    }

    private LearningResource toEntity(LearningResourcePO po) {
        LearningResource entity = new LearningResource();
        entity.setId(po.getId());
        entity.setGrade(po.getGrade() != null ? GradeType.fromCode(po.getGrade()) : null);
        entity.setSubject(po.getSubject());
        entity.setTextbookVersion(po.getTextbookVersion() != null ? TextbookVersion.fromCode(po.getTextbookVersion()) : null);
        entity.setResourceType(po.getResourceType() != null ? LearningResourceType.fromCode(po.getResourceType()) : null);
        entity.setTitle(po.getTitle());
        entity.setCoverUrl(po.getCoverUrl());
        entity.setContentUrl(po.getContentUrl());
        entity.setDurationSeconds(po.getDurationSeconds());
        entity.setKnowledgePoint(po.getKnowledgePoint());
        entity.setTags(po.getTags());
        entity.setSourceType(po.getSourceType());
        entity.setSubtitleUrl(po.getSubtitleUrl());
        entity.setQualityLevels(po.getQualityLevels());
        entity.setKnowledgeMarkers(po.getKnowledgeMarkers());
        entity.setStatus(po.getStatus());
        entity.setDescription(po.getDescription());
        entity.setVipOnly(po.getVipOnly());
        entity.setSortOrder(po.getSortOrder());
        entity.setDeleted(po.getDeleted());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private LearningResourcePO toPO(LearningResource entity) {
        LearningResourcePO po = new LearningResourcePO();
        po.setId(entity.getId());
        po.setGrade(entity.getGrade() != null ? entity.getGrade().getCode() : null);
        po.setSubject(entity.getSubject());
        po.setTextbookVersion(entity.getTextbookVersion() != null ? entity.getTextbookVersion().getCode() : null);
        po.setResourceType(entity.getResourceType() != null ? entity.getResourceType().getCode() : null);
        po.setTitle(entity.getTitle());
        po.setCoverUrl(entity.getCoverUrl());
        po.setContentUrl(entity.getContentUrl());
        po.setDurationSeconds(entity.getDurationSeconds());
        po.setKnowledgePoint(entity.getKnowledgePoint());
        po.setTags(entity.getTags());
        po.setSourceType(entity.getSourceType());
        po.setSubtitleUrl(entity.getSubtitleUrl());
        po.setQualityLevels(entity.getQualityLevels());
        po.setKnowledgeMarkers(entity.getKnowledgeMarkers());
        po.setStatus(entity.getStatus());
        po.setDescription(entity.getDescription());
        po.setVipOnly(entity.getVipOnly());
        po.setSortOrder(entity.getSortOrder());
        po.setDeleted(entity.getDeleted());
        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        return po;
    }
}
