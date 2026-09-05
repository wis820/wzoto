package com.wzoto.domain.service;

import com.wzoto.domain.entity.Child;
import com.wzoto.domain.entity.WrongQuestionBank;
import com.wzoto.domain.repository.ChildRepository;
import com.wzoto.domain.repository.WrongQuestionBankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * 错题领域服务 - 错题汇总、复习计划、打印数据准备
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WrongQuestionDomainService {

    private final WrongQuestionBankRepository wrongQuestionBankRepository;
    private final ChildRepository childRepository;

    /**
     * 查询子女错题列表
     */
    public List<WrongQuestionBank> findByChildId(Long parentId, Long childId, String subject, Boolean inReviewPlan) {
        validateOwnership(parentId, childId);
        List<WrongQuestionBank> list;
        if (subject != null && !subject.isBlank()) {
            list = wrongQuestionBankRepository.findByChildIdAndSubject(childId, subject);
        } else {
            list = wrongQuestionBankRepository.findByChildId(childId);
        }
        if (inReviewPlan != null) {
            list = list.stream().filter(w -> inReviewPlan.equals(w.getInReviewPlan())).toList();
        }
        return list;
    }

    /**
     * 批量加入复习计划
     */
    public int batchAddToReviewPlan(Long parentId, Long childId, List<Long> ids) {
        validateOwnership(parentId, childId);
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        String reviewPlanTag = "PLAN_" + LocalDate.now().toString();
        int count = wrongQuestionBankRepository.batchUpdateReviewPlan(ids, reviewPlanTag);
        log.info("批量加入复习计划, parentId={}, childId={}, count={}", parentId, childId, count);
        return count;
    }

    /**
     * 标记错题已掌握
     */
    public WrongQuestionBank markMastered(Long parentId, Long wrongQuestionId) {
        WrongQuestionBank wrongQuestion = wrongQuestionBankRepository.findById(wrongQuestionId);
        if (wrongQuestion == null) {
            throw new IllegalArgumentException("错题记录不存在");
        }
        validateOwnership(parentId, wrongQuestion.getChildId());
        wrongQuestion.markMastered();
        return wrongQuestionBankRepository.update(wrongQuestion);
    }

    /**
     * 准备打印数据
     */
    public List<WrongQuestionBank> preparePrintData(Long parentId, Long childId, String subject) {
        List<WrongQuestionBank> list = findByChildId(parentId, childId, subject, null);
        return list.stream()
                .filter(w -> !w.isMastered())
                .toList();
    }

    /**
     * 记录一次错题（从作答记录同步）
     */
    public WrongQuestionBank recordMistake(Long childId, Long resourceId, String subject, String knowledgePoint) {
        WrongQuestionBank wrongQuestion = wrongQuestionBankRepository.findByChildIdAndResourceId(childId, resourceId);
        if (wrongQuestion == null) {
            wrongQuestion = WrongQuestionBank.create(childId, resourceId, subject, knowledgePoint);
            wrongQuestion = wrongQuestionBankRepository.save(wrongQuestion);
        } else {
            wrongQuestion.addMistake();
            wrongQuestion = wrongQuestionBankRepository.update(wrongQuestion);
        }
        return wrongQuestion;
    }

    private void validateOwnership(Long parentId, Long childId) {
        Child child = childRepository.findById(childId);
        if (child == null || !child.belongsTo(parentId)) {
            throw new IllegalArgumentException("无权操作该子女档案");
        }
    }
}
