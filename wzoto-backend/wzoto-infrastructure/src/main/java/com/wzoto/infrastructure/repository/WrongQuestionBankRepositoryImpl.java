package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.WrongQuestionBank;
import com.wzoto.domain.repository.WrongQuestionBankRepository;
import com.wzoto.domain.valobj.MasteryLevel;
import com.wzoto.infrastructure.mapper.WrongQuestionBankMapper;
import com.wzoto.infrastructure.pojo.WrongQuestionBankPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 错题库仓储实现 - 基础设施层
 */
@Repository
@RequiredArgsConstructor
public class WrongQuestionBankRepositoryImpl implements WrongQuestionBankRepository {

    private final WrongQuestionBankMapper wrongQuestionBankMapper;

    @Override
    public WrongQuestionBank save(WrongQuestionBank wrongQuestion) {
        WrongQuestionBankPO po = toPO(wrongQuestion);
        wrongQuestionBankMapper.insert(po);
        wrongQuestion.setId(po.getId());
        return wrongQuestion;
    }

    @Override
    public WrongQuestionBank findById(Long id) {
        WrongQuestionBankPO po = wrongQuestionBankMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public WrongQuestionBank findByChildIdAndResourceId(Long childId, Long resourceId) {
        LambdaQueryWrapper<WrongQuestionBankPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WrongQuestionBankPO::getChildId, childId)
                .eq(WrongQuestionBankPO::getResourceId, resourceId)
                .last("LIMIT 1");
        WrongQuestionBankPO po = wrongQuestionBankMapper.selectOne(wrapper);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public List<WrongQuestionBank> findByChildId(Long childId) {
        LambdaQueryWrapper<WrongQuestionBankPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WrongQuestionBankPO::getChildId, childId).orderByDesc(WrongQuestionBankPO::getLastMistakeTime);
        return wrongQuestionBankMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<WrongQuestionBank> findByChildIdAndSubject(Long childId, String subject) {
        LambdaQueryWrapper<WrongQuestionBankPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WrongQuestionBankPO::getChildId, childId)
                .eq(WrongQuestionBankPO::getSubject, subject)
                .orderByDesc(WrongQuestionBankPO::getLastMistakeTime);
        return wrongQuestionBankMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<WrongQuestionBank> findByChildIdAndInReviewPlan(Long childId, Boolean inReviewPlan) {
        LambdaQueryWrapper<WrongQuestionBankPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WrongQuestionBankPO::getChildId, childId)
                .eq(WrongQuestionBankPO::getInReviewPlan, inReviewPlan)
                .orderByDesc(WrongQuestionBankPO::getLastMistakeTime);
        return wrongQuestionBankMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public WrongQuestionBank update(WrongQuestionBank wrongQuestion) {
        WrongQuestionBankPO po = toPO(wrongQuestion);
        wrongQuestionBankMapper.updateById(po);
        return wrongQuestion;
    }

    @Override
    public int batchUpdateReviewPlan(List<Long> ids, String reviewPlanTag) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        String idStr = ids.stream()
                .filter(id -> id != null)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        if (!StringUtils.hasText(idStr)) {
            return 0;
        }
        return wrongQuestionBankMapper.batchUpdateReviewPlan(idStr, reviewPlanTag);
    }

    private WrongQuestionBank toEntity(WrongQuestionBankPO po) {
        WrongQuestionBank entity = new WrongQuestionBank();
        entity.setId(po.getId());
        entity.setChildId(po.getChildId());
        entity.setResourceId(po.getResourceId());
        entity.setSubject(po.getSubject());
        entity.setKnowledgePoint(po.getKnowledgePoint());
        entity.setMistakeCount(po.getMistakeCount());
        entity.setMasteryLevel(po.getMasteryLevel() != null ? MasteryLevel.fromCode(po.getMasteryLevel()) : null);
        entity.setInReviewPlan(po.getInReviewPlan());
        entity.setReviewPlanTag(po.getReviewPlanTag());
        entity.setLastMistakeTime(po.getLastMistakeTime());
        entity.setDeleted(po.getDeleted());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private WrongQuestionBankPO toPO(WrongQuestionBank entity) {
        WrongQuestionBankPO po = new WrongQuestionBankPO();
        po.setId(entity.getId());
        po.setChildId(entity.getChildId());
        po.setResourceId(entity.getResourceId());
        po.setSubject(entity.getSubject());
        po.setKnowledgePoint(entity.getKnowledgePoint());
        po.setMistakeCount(entity.getMistakeCount());
        po.setMasteryLevel(entity.getMasteryLevel() != null ? entity.getMasteryLevel().getCode() : null);
        po.setInReviewPlan(entity.getInReviewPlan());
        po.setReviewPlanTag(entity.getReviewPlanTag());
        po.setLastMistakeTime(entity.getLastMistakeTime());
        po.setDeleted(entity.getDeleted());
        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        return po;
    }
}
