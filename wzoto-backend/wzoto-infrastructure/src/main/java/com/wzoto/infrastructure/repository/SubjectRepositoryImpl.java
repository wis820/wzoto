package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.Subject;
import com.wzoto.domain.repository.SubjectRepository;
import com.wzoto.infrastructure.mapper.SubjectMapper;
import com.wzoto.infrastructure.pojo.SubjectPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/** 学科仓储实现 */
@Repository
@RequiredArgsConstructor
public class SubjectRepositoryImpl implements SubjectRepository {

    private final SubjectMapper subjectMapper;

    @Override
    public List<Subject> findAll() {
        LambdaQueryWrapper<SubjectPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(SubjectPO::getSortOrder);
        return subjectMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public Subject findByCode(String code) {
        LambdaQueryWrapper<SubjectPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SubjectPO::getCode, code);
        SubjectPO po = subjectMapper.selectOne(wrapper);
        return po != null ? toEntity(po) : null;
    }

    private Subject toEntity(SubjectPO po) {
        Subject entity = new Subject();
        entity.setId(po.getId());
        entity.setCode(po.getCode());
        entity.setName(po.getName());
        entity.setIcon(po.getIcon());
        entity.setColor(po.getColor());
        entity.setSortOrder(po.getSortOrder());
        entity.setDeleted(po.getDeleted());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private SubjectPO toPO(Subject entity) {
        SubjectPO po = new SubjectPO();
        po.setId(entity.getId());
        po.setCode(entity.getCode());
        po.setName(entity.getName());
        po.setIcon(entity.getIcon());
        po.setColor(entity.getColor());
        po.setSortOrder(entity.getSortOrder());
        po.setDeleted(entity.getDeleted());
        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        return po;
    }
}
