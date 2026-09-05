package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.AiLearningPlan;
import com.wzoto.domain.repository.AiLearningPlanRepository;
import com.wzoto.infrastructure.mapper.AiLearningPlanMapper;
import com.wzoto.infrastructure.pojo.AiLearningPlanPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/** AI学习规划仓储实现 */
@Repository
@RequiredArgsConstructor
public class AiLearningPlanRepositoryImpl implements AiLearningPlanRepository {

    private final AiLearningPlanMapper aiLearningPlanMapper;

    @Override
    public AiLearningPlan save(AiLearningPlan plan) {
        AiLearningPlanPO po = toPO(plan);
        aiLearningPlanMapper.insert(po);
        plan.setId(po.getId());
        return plan;
    }

    @Override
    public AiLearningPlan findById(Long id) {
        AiLearningPlanPO po = aiLearningPlanMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public AiLearningPlan update(AiLearningPlan plan) {
        AiLearningPlanPO po = toPO(plan);
        aiLearningPlanMapper.updateById(po);
        return plan;
    }

    @Override
    public List<AiLearningPlan> findByChildId(Long childId) {
        LambdaQueryWrapper<AiLearningPlanPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AiLearningPlanPO::getChildId, childId)
                .orderByDesc(AiLearningPlanPO::getPlanDate);
        return aiLearningPlanMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public AiLearningPlan findByChildIdAndDate(Long childId, LocalDate planDate) {
        LambdaQueryWrapper<AiLearningPlanPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AiLearningPlanPO::getChildId, childId)
                .eq(AiLearningPlanPO::getPlanDate, planDate);
        AiLearningPlanPO po = aiLearningPlanMapper.selectOne(wrapper);
        return po != null ? toEntity(po) : null;
    }

    private AiLearningPlan toEntity(AiLearningPlanPO po) {
        AiLearningPlan entity = new AiLearningPlan();
        entity.setId(po.getId());
        entity.setChildId(po.getChildId());
        entity.setParentId(po.getParentId());
        entity.setPlanDate(po.getPlanDate());
        entity.setPlanContentJson(po.getPlanContentJson());
        entity.setWeakPointsJson(po.getWeakPointsJson());
        entity.setAiSuggestion(po.getAiSuggestion());
        entity.setApplied(po.getApplied());
        entity.setAppliedAt(po.getAppliedAt());
        entity.setGeneratedAt(po.getGeneratedAt());
        entity.setDeleted(po.getDeleted());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private AiLearningPlanPO toPO(AiLearningPlan entity) {
        AiLearningPlanPO po = new AiLearningPlanPO();
        po.setId(entity.getId());
        po.setChildId(entity.getChildId());
        po.setParentId(entity.getParentId());
        po.setPlanDate(entity.getPlanDate());
        po.setPlanContentJson(entity.getPlanContentJson());
        po.setWeakPointsJson(entity.getWeakPointsJson());
        po.setAiSuggestion(entity.getAiSuggestion());
        po.setApplied(entity.getApplied());
        po.setAppliedAt(entity.getAppliedAt());
        po.setGeneratedAt(entity.getGeneratedAt());
        po.setDeleted(entity.getDeleted());
        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        return po;
    }
}
