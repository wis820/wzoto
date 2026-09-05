package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.ParentControlConfig;
import com.wzoto.domain.repository.ParentControlConfigRepository;
import com.wzoto.infrastructure.mapper.ParentControlConfigMapper;
import com.wzoto.infrastructure.pojo.ParentControlConfigPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 家长管控配置仓储实现 - 基础设施层
 */
@Repository
@RequiredArgsConstructor
public class ParentControlConfigRepositoryImpl implements ParentControlConfigRepository {

    private final ParentControlConfigMapper parentControlConfigMapper;

    @Override
    public ParentControlConfig save(ParentControlConfig config) {
        ParentControlConfigPO po = toPO(config);
        parentControlConfigMapper.insert(po);
        config.setId(po.getId());
        return config;
    }

    @Override
    public ParentControlConfig findById(Long id) {
        ParentControlConfigPO po = parentControlConfigMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public ParentControlConfig findByChildId(Long childId) {
        LambdaQueryWrapper<ParentControlConfigPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ParentControlConfigPO::getChildId, childId).last("LIMIT 1");
        ParentControlConfigPO po = parentControlConfigMapper.selectOne(wrapper);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public ParentControlConfig update(ParentControlConfig config) {
        ParentControlConfigPO po = toPO(config);
        parentControlConfigMapper.updateById(po);
        return config;
    }

    private ParentControlConfig toEntity(ParentControlConfigPO po) {
        ParentControlConfig entity = new ParentControlConfig();
        entity.setId(po.getId());
        entity.setParentId(po.getParentId());
        entity.setChildId(po.getChildId());
        entity.setDailyLimitMinutes(po.getDailyLimitMinutes());
        entity.setRestIntervalMinutes(po.getRestIntervalMinutes());
        entity.setForbiddenStartTime(po.getForbiddenStartTime());
        entity.setForbiddenEndTime(po.getForbiddenEndTime());
        entity.setLocked(po.getLocked());
        entity.setEyeProtectionMode(po.getEyeProtectionMode());
        entity.setBlueLightFilter(po.getBlueLightFilter());
        entity.setPostureReminder(po.getPostureReminder());
        entity.setDeleted(po.getDeleted());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private ParentControlConfigPO toPO(ParentControlConfig entity) {
        ParentControlConfigPO po = new ParentControlConfigPO();
        po.setId(entity.getId());
        po.setParentId(entity.getParentId());
        po.setChildId(entity.getChildId());
        po.setDailyLimitMinutes(entity.getDailyLimitMinutes());
        po.setRestIntervalMinutes(entity.getRestIntervalMinutes());
        po.setForbiddenStartTime(entity.getForbiddenStartTime());
        po.setForbiddenEndTime(entity.getForbiddenEndTime());
        po.setLocked(entity.getLocked());
        po.setEyeProtectionMode(entity.getEyeProtectionMode());
        po.setBlueLightFilter(entity.getBlueLightFilter());
        po.setPostureReminder(entity.getPostureReminder());
        po.setDeleted(entity.getDeleted());
        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        return po;
    }
}
