package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.KnowledgePoint;
import com.wzoto.domain.repository.KnowledgePointRepository;
import com.wzoto.domain.valobj.GradeType;
import com.wzoto.domain.valobj.TextbookVersion;
import com.wzoto.infrastructure.mapper.KnowledgePointMapper;
import com.wzoto.infrastructure.pojo.KnowledgePointPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/** 知识点仓储实现 */
@Repository
@RequiredArgsConstructor
public class KnowledgePointRepositoryImpl implements KnowledgePointRepository {

    private final KnowledgePointMapper knowledgePointMapper;

    @Override
    public List<KnowledgePoint> findByGradeAndSubject(GradeType grade, String subject) {
        LambdaQueryWrapper<KnowledgePointPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(grade != null, KnowledgePointPO::getGrade, grade != null ? grade.getCode() : null)
                .eq(subject != null, KnowledgePointPO::getSubject, subject)
                .orderByAsc(KnowledgePointPO::getDepth)
                .orderByAsc(KnowledgePointPO::getSortOrder);
        return knowledgePointMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<KnowledgePoint> findByParentId(Long parentId) {
        LambdaQueryWrapper<KnowledgePointPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KnowledgePointPO::getParentId, parentId)
                .orderByAsc(KnowledgePointPO::getSortOrder);
        return knowledgePointMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public KnowledgePoint findById(Long id) {
        KnowledgePointPO po = knowledgePointMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public List<KnowledgePoint> findTree(GradeType grade, String subject, String textbookVersion) {
        LambdaQueryWrapper<KnowledgePointPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(grade != null, KnowledgePointPO::getGrade, grade != null ? grade.getCode() : null)
                .eq(subject != null, KnowledgePointPO::getSubject, subject)
                .eq(textbookVersion != null, KnowledgePointPO::getTextbookVersion, textbookVersion)
                .orderByAsc(KnowledgePointPO::getDepth)
                .orderByAsc(KnowledgePointPO::getSortOrder);
        return knowledgePointMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    private KnowledgePoint toEntity(KnowledgePointPO po) {
        KnowledgePoint entity = new KnowledgePoint();
        entity.setId(po.getId());
        entity.setParentId(po.getParentId());
        entity.setGrade(po.getGrade() != null ? GradeType.fromCode(po.getGrade()) : null);
        entity.setSubject(po.getSubject());
        entity.setTextbookVersion(po.getTextbookVersion() != null ? TextbookVersion.fromCode(po.getTextbookVersion()) : null);
        entity.setName(po.getName());
        entity.setDescription(po.getDescription());
        entity.setDepth(po.getDepth());
        entity.setSortOrder(po.getSortOrder());
        entity.setDeleted(po.getDeleted());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }
}
