package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.ChildAchievement;
import com.wzoto.domain.repository.ChildAchievementRepository;
import com.wzoto.infrastructure.mapper.ChildAchievementMapper;
import com.wzoto.infrastructure.pojo.ChildAchievementPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 成长激励仓储实现 - 基础设施层
 */
@Repository
@RequiredArgsConstructor
public class ChildAchievementRepositoryImpl implements ChildAchievementRepository {

    private final ChildAchievementMapper childAchievementMapper;

    @Override
    public ChildAchievement save(ChildAchievement achievement) {
        ChildAchievementPO po = toPO(achievement);
        childAchievementMapper.insert(po);
        achievement.setId(po.getId());
        return achievement;
    }

    @Override
    public ChildAchievement findById(Long id) {
        ChildAchievementPO po = childAchievementMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public List<ChildAchievement> findByChildId(Long childId) {
        LambdaQueryWrapper<ChildAchievementPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChildAchievementPO::getChildId, childId).orderByDesc(ChildAchievementPO::getObtainedAt);
        return childAchievementMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<ChildAchievement> findByChildIdAndType(Long childId, String achievementType) {
        LambdaQueryWrapper<ChildAchievementPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChildAchievementPO::getChildId, childId)
                .eq(ChildAchievementPO::getAchievementType, achievementType)
                .orderByDesc(ChildAchievementPO::getObtainedAt);
        return childAchievementMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public ChildAchievement update(ChildAchievement achievement) {
        ChildAchievementPO po = toPO(achievement);
        childAchievementMapper.updateById(po);
        return achievement;
    }

    private ChildAchievement toEntity(ChildAchievementPO po) {
        ChildAchievement entity = new ChildAchievement();
        entity.setId(po.getId());
        entity.setChildId(po.getChildId());
        entity.setAchievementType(po.getAchievementType());
        entity.setAchievementCode(po.getAchievementCode());
        entity.setAchievementName(po.getAchievementName());
        entity.setIconUrl(po.getIconUrl());
        entity.setPoints(po.getPoints());
        entity.setSkinCode(po.getSkinCode());
        entity.setTaskDescription(po.getTaskDescription());
        entity.setTaskStatus(po.getTaskStatus());
        entity.setObtainedAt(po.getObtainedAt());
        entity.setDeleted(po.getDeleted());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private ChildAchievementPO toPO(ChildAchievement entity) {
        ChildAchievementPO po = new ChildAchievementPO();
        po.setId(entity.getId());
        po.setChildId(entity.getChildId());
        po.setAchievementType(entity.getAchievementType());
        po.setAchievementCode(entity.getAchievementCode());
        po.setAchievementName(entity.getAchievementName());
        po.setIconUrl(entity.getIconUrl());
        po.setPoints(entity.getPoints());
        po.setSkinCode(entity.getSkinCode());
        po.setTaskDescription(entity.getTaskDescription());
        po.setTaskStatus(entity.getTaskStatus());
        po.setObtainedAt(entity.getObtainedAt());
        po.setDeleted(entity.getDeleted());
        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        return po;
    }
}
