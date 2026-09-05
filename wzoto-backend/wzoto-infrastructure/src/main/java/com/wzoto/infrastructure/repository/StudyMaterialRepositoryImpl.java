package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.StudyMaterial;
import com.wzoto.domain.repository.StudyMaterialRepository;
import com.wzoto.domain.valobj.GradeType;
import com.wzoto.domain.valobj.TextbookVersion;
import com.wzoto.infrastructure.mapper.StudyMaterialMapper;
import com.wzoto.infrastructure.pojo.StudyMaterialPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/** 学习资料仓储实现 */
@Repository
@RequiredArgsConstructor
public class StudyMaterialRepositoryImpl implements StudyMaterialRepository {

    private final StudyMaterialMapper studyMaterialMapper;

    @Override
    public StudyMaterial findById(Long id) {
        StudyMaterialPO po = studyMaterialMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public List<StudyMaterial> findByGradeAndSubject(GradeType grade, String subject) {
        LambdaQueryWrapper<StudyMaterialPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(grade != null, StudyMaterialPO::getGrade, grade != null ? grade.getCode() : null)
                .eq(subject != null, StudyMaterialPO::getSubject, subject)
                .orderByAsc(StudyMaterialPO::getSortOrder);
        return studyMaterialMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<StudyMaterial> findByGradeAndSubjectAndType(GradeType grade, String subject, String materialType) {
        LambdaQueryWrapper<StudyMaterialPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(grade != null, StudyMaterialPO::getGrade, grade != null ? grade.getCode() : null)
                .eq(subject != null, StudyMaterialPO::getSubject, subject)
                .eq(materialType != null, StudyMaterialPO::getMaterialType, materialType)
                .orderByAsc(StudyMaterialPO::getSortOrder);
        return studyMaterialMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    private StudyMaterial toEntity(StudyMaterialPO po) {
        StudyMaterial entity = new StudyMaterial();
        entity.setId(po.getId());
        entity.setGrade(po.getGrade() != null ? GradeType.fromCode(po.getGrade()) : null);
        entity.setSubject(po.getSubject());
        entity.setTextbookVersion(po.getTextbookVersion() != null ? TextbookVersion.fromCode(po.getTextbookVersion()) : null);
        entity.setTitle(po.getTitle());
        entity.setMaterialType(po.getMaterialType());
        entity.setContentUrl(po.getContentUrl());
        entity.setPreviewUrl(po.getPreviewUrl());
        entity.setDescription(po.getDescription());
        entity.setKnowledgePointId(po.getKnowledgePointId());
        entity.setVipOnly(po.getVipOnly());
        entity.setDownloadCount(po.getDownloadCount());
        entity.setSortOrder(po.getSortOrder());
        entity.setDeleted(po.getDeleted());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }
}
