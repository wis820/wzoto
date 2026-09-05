package com.wzoto.interfaces.controller;

import com.wzoto.application.service.WrongQuestionApplicationService;
import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.WrongQuestionBank;
import com.wzoto.interfaces.common.R;
import com.wzoto.interfaces.vo.learning.WrongQuestionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 错题管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/learning")
@RequiredArgsConstructor
public class WrongQuestionController {

    private final WrongQuestionApplicationService wrongQuestionApplicationService;

    @GetMapping("/wrong-questions/{childId}")
    public R<List<WrongQuestionVO>> listWrongQuestions(@PathVariable Long childId,
                                                       @RequestParam(required = false) String subject,
                                                       @RequestParam(required = false) Boolean inReviewPlan) {
        log.info("[BI] wrong_question_list|userId={}, childId={}", UserContext.getCurrentUserId(), childId);
        List<WrongQuestionBank> list = wrongQuestionApplicationService.getWrongQuestions(childId, subject, inReviewPlan);
        return R.ok(list.stream().map(this::toVO).collect(Collectors.toList()));
    }

    @PostMapping("/wrong-questions/batch-review")
    public R<Integer> batchAddToReviewPlan(@RequestParam Long childId,
                                           @RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("请选择错题");
        }
        Long parentId = UserContext.getCurrentUserId();
        log.info("[BI] wrong_question_batch_review|userId={}, childId={}, count={}", parentId, childId, ids.size());
        int updated = wrongQuestionApplicationService.batchAddToReviewPlan(childId, ids);
        return R.ok(updated);
    }

    @PostMapping("/wrong-question/{id}/mastered")
    public R<WrongQuestionVO> markMastered(@PathVariable Long id) {
        log.info("[BI] wrong_question_mastered|userId={}, wrongQuestionId={}", UserContext.getCurrentUserId(), id);
        WrongQuestionBank question = wrongQuestionApplicationService.markMastered(id);
        return R.ok(toVO(question));
    }

    private WrongQuestionVO toVO(WrongQuestionBank question) {
        return WrongQuestionVO.builder()
                .id(question.getId())
                .childId(question.getChildId())
                .resourceId(question.getResourceId())
                .subject(question.getSubject())
                .knowledgePoint(question.getKnowledgePoint())
                .mistakeCount(question.getMistakeCount())
                .masteryLevel(question.getMasteryLevel() != null ? question.getMasteryLevel().getCode() : null)
                .masteryLevelDesc(question.getMasteryLevel() != null ? question.getMasteryLevel().getDesc() : null)
                .masteryColor(question.getMasteryLevel() != null ? question.getMasteryLevel().getColor() : null)
                .inReviewPlan(question.getInReviewPlan())
                .reviewPlanTag(question.getReviewPlanTag())
                .lastMistakeTime(question.getLastMistakeTime())
                .build();
    }
}
