package com.wzoto.application.service;

import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.WrongQuestionBank;
import com.wzoto.domain.service.WrongQuestionDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 错题管理应用服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WrongQuestionApplicationService {

    private final WrongQuestionDomainService wrongQuestionDomainService;

    public List<WrongQuestionBank> getWrongQuestions(Long childId, String subject, Boolean inReviewPlan) {
        Long parentId = UserContext.getCurrentUserId();
        return wrongQuestionDomainService.findByChildId(parentId, childId, subject, inReviewPlan);
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchAddToReviewPlan(Long childId, List<Long> ids) {
        Long parentId = UserContext.getCurrentUserId();
        return wrongQuestionDomainService.batchAddToReviewPlan(parentId, childId, ids);
    }

    @Transactional(rollbackFor = Exception.class)
    public WrongQuestionBank markMastered(Long wrongQuestionId) {
        Long parentId = UserContext.getCurrentUserId();
        return wrongQuestionDomainService.markMastered(parentId, wrongQuestionId);
    }

    public List<WrongQuestionBank> preparePrintData(Long childId, String subject) {
        Long parentId = UserContext.getCurrentUserId();
        return wrongQuestionDomainService.preparePrintData(parentId, childId, subject);
    }
}
