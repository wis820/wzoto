package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.ResourceChapter;
import com.wzoto.domain.repository.ResourceChapterRepository;
import com.wzoto.domain.valobj.GradeType;
import com.wzoto.domain.valobj.TextbookVersion;
import com.wzoto.infrastructure.mapper.ResourceChapterMapper;
import com.wzoto.infrastructure.pojo.ResourceChapterPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/** 教材章节仓储实现 */
@Repository
@RequiredArgsConstructor
public class ResourceChapterRepositoryImpl implements ResourceChapterRepository {

    private final ResourceChapterMapper resourceChapterMapper;

    @Override
    public List<ResourceChapter> findByGradeAndSubject(GradeType grade, String subject) {
        LambdaQueryWrapper<ResourceChapterPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(grade != null, ResourceChapterPO::getGrade, grade != null ? grade.getCode() : null)
                .eq(subject != null, ResourceChapterPO::getSubject, subject)
                .orderByAsc(ResourceChapterPO::getDepth)
                .orderByAsc(ResourceChapterPO::getSortOrder);
        return resourceChapterMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<ResourceChapter> findByParentId(Long parentId) {
        LambdaQueryWrapper<ResourceChapterPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ResourceChapterPO::getParentId, parentId)
                .orderByAsc(ResourceChapterPO::getSortOrder);
        return resourceChapterMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public ResourceChapter findById(Long id) {
        ResourceChapterPO po = resourceChapterMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public List<ResourceChapter> findTree(GradeType grade, String subject, String textbookVersion) {
        LambdaQueryWrapper<ResourceChapterPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(grade != null, ResourceChapterPO::getGrade, grade != null ? grade.getCode() : null)
                .eq(subject != null, ResourceChapterPO::getSubject, subject)
                .eq(textbookVersion != null, ResourceChapterPO::getTextbookVersion, textbookVersion)
                .orderByAsc(ResourceChapterPO::getDepth)
                .orderByAsc(ResourceChapterPO::getSortOrder);
        return resourceChapterMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    private ResourceChapter toEntity(ResourceChapterPO po) {
        ResourceChapter entity = new ResourceChapter();
        entity.setId(po.getId());
        entity.setParentId(po.getParentId());
        entity.setGrade(po.getGrade() != null ? GradeType.fromCode(po.getGrade()) : null);
        entity.setSubject(po.getSubject());
        entity.setTextbookVersion(po.getTextbookVersion() != null ? TextbookVersion.fromCode(po.getTextbookVersion()) : null);
        entity.setTitle(po.getTitle());
        entity.setDepth(po.getDepth());
        entity.setSortOrder(po.getSortOrder());
        entity.setDeleted(po.getDeleted());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }
}
