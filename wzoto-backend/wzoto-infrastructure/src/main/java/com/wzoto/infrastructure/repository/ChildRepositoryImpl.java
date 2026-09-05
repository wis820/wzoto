package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.Child;
import com.wzoto.domain.repository.ChildRepository;
import com.wzoto.domain.valobj.GradeType;
import com.wzoto.domain.valobj.TextbookVersion;
import com.wzoto.infrastructure.mapper.ChildMapper;
import com.wzoto.infrastructure.pojo.ChildPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 子女档案仓储实现 - 基础设施层
 */
@Repository
@RequiredArgsConstructor
public class ChildRepositoryImpl implements ChildRepository {

    private final ChildMapper childMapper;

    @Override
    public Child save(Child child) {
        ChildPO po = toPO(child);
        childMapper.insert(po);
        child.setId(po.getId());
        return child;
    }

    @Override
    public Child findById(Long id) {
        ChildPO po = childMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public List<Child> findByParentId(Long parentId) {
        LambdaQueryWrapper<ChildPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChildPO::getParentId, parentId).orderByDesc(ChildPO::getCreatedAt);
        return childMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public Child update(Child child) {
        ChildPO po = toPO(child);
        childMapper.updateById(po);
        return child;
    }

    @Override
    public boolean delete(Long id) {
        return childMapper.deleteById(id) > 0;
    }

    private Child toEntity(ChildPO po) {
        Child entity = new Child();
        entity.setId(po.getId());
        entity.setParentId(po.getParentId());
        entity.setName(po.getName());
        entity.setGrade(po.getGrade() != null ? GradeType.fromCode(po.getGrade()) : null);
        entity.setTextbookVersion(po.getTextbookVersion() != null ? TextbookVersion.fromCode(po.getTextbookVersion()) : null);
        entity.setSchool(po.getSchool());
        entity.setAvatar(po.getAvatar());
        entity.setDeleted(po.getDeleted());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private ChildPO toPO(Child entity) {
        ChildPO po = new ChildPO();
        po.setId(entity.getId());
        po.setParentId(entity.getParentId());
        po.setName(entity.getName());
        po.setGrade(entity.getGrade() != null ? entity.getGrade().getCode() : null);
        po.setTextbookVersion(entity.getTextbookVersion() != null ? entity.getTextbookVersion().getCode() : null);
        po.setSchool(entity.getSchool());
        po.setAvatar(entity.getAvatar());
        po.setDeleted(entity.getDeleted());
        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        return po;
    }
}
