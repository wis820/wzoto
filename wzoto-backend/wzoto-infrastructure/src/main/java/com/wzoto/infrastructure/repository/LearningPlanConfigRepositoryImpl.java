package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.LearningPlanConfig;
import com.wzoto.domain.repository.LearningPlanConfigRepository;
import com.wzoto.infrastructure.mapper.LearningPlanConfigMapper;
import com.wzoto.infrastructure.pojo.LearningPlanConfigPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 学习计划配置仓储实现 - 基础设施层
 */
@Repository
@RequiredArgsConstructor
public class LearningPlanConfigRepositoryImpl implements LearningPlanConfigRepository {

    private final LearningPlanConfigMapper learningPlanConfigMapper;

    @Override
    public LearningPlanConfig save(LearningPlanConfig config) {
        LearningPlanConfigPO po = toPO(config);
        learningPlanConfigMapper.insert(po);
        config.setId(po.getId());
        return config;
    }

    @Override
    public LearningPlanConfig findById(Long id) {
        LearningPlanConfigPO po = learningPlanConfigMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public LearningPlanConfig findByChildId(Long childId) {
        LambdaQueryWrapper<LearningPlanConfigPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LearningPlanConfigPO::getChildId, childId).last("LIMIT 1");
        LearningPlanConfigPO po = learningPlanConfigMapper.selectOne(wrapper);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public LearningPlanConfig update(LearningPlanConfig config) {
        LearningPlanConfigPO po = toPO(config);
        learningPlanConfigMapper.updateById(po);
        return config;
    }

    private LearningPlanConfig toEntity(LearningPlanConfigPO po) {
        LearningPlanConfig entity = new LearningPlanConfig();
        entity.setId(po.getId());
        entity.setChildId(po.getChildId());
        entity.setDailyDurationMinutes(po.getDailyDurationMinutes());
        entity.setChineseWeight(po.getChineseWeight());
        entity.setMathWeight(po.getMathWeight());
        entity.setEnglishWeight(po.getEnglishWeight());
        entity.setSpecialCalculationEnabled(po.getSpecialCalculationEnabled());
        entity.setSpecialApplicationEnabled(po.getSpecialApplicationEnabled());
        entity.setSpecialLiteracyEnabled(po.getSpecialLiteracyEnabled());
        entity.setSpecialWordsEnabled(po.getSpecialWordsEnabled());
        entity.setDeleted(po.getDeleted());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private LearningPlanConfigPO toPO(LearningPlanConfig entity) {
        LearningPlanConfigPO po = new LearningPlanConfigPO();
        po.setId(entity.getId());
        po.setChildId(entity.getChildId());
        po.setDailyDurationMinutes(entity.getDailyDurationMinutes());
        po.setChineseWeight(entity.getChineseWeight());
        po.setMathWeight(entity.getMathWeight());
        po.setEnglishWeight(entity.getEnglishWeight());
        po.setSpecialCalculationEnabled(entity.getSpecialCalculationEnabled());
        po.setSpecialApplicationEnabled(entity.getSpecialApplicationEnabled());
        po.setSpecialLiteracyEnabled(entity.getSpecialLiteracyEnabled());
        po.setSpecialWordsEnabled(entity.getSpecialWordsEnabled());
        po.setDeleted(entity.getDeleted());
        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        return po;
    }
}
