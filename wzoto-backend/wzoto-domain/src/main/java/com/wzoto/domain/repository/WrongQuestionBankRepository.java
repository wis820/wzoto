package com.wzoto.domain.repository;

import com.wzoto.domain.entity.WrongQuestionBank;

import java.util.List;

/**
 * 错题库仓储接口 - 领域层定义，基础设施层实现
 */
public interface WrongQuestionBankRepository {

    WrongQuestionBank save(WrongQuestionBank wrongQuestion);

    WrongQuestionBank findById(Long id);

    WrongQuestionBank findByChildIdAndResourceId(Long childId, Long resourceId);

    List<WrongQuestionBank> findByChildId(Long childId);

    List<WrongQuestionBank> findByChildIdAndSubject(Long childId, String subject);

    List<WrongQuestionBank> findByChildIdAndInReviewPlan(Long childId, Boolean inReviewPlan);

    WrongQuestionBank update(WrongQuestionBank wrongQuestion);

    int batchUpdateReviewPlan(List<Long> ids, String reviewPlanTag);
}
