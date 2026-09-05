package com.wzoto.interfaces.controller;

import com.wzoto.application.service.AiGradingApplicationService;
import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.AiGradingRecord;
import com.wzoto.interfaces.common.R;
import com.wzoto.interfaces.dto.CompositionGradeDTO;
import com.wzoto.interfaces.dto.PronunciationEvalDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** AI批改控制器 */
@Slf4j
@RestController
@RequestMapping("/api/ai/grading")
@RequiredArgsConstructor
public class AiGradingController {

    private final AiGradingApplicationService aiGradingApplicationService;

    @PostMapping("/composition")
    public R<AiGradingRecord> gradeComposition(@Valid @RequestBody CompositionGradeDTO dto) {
        log.info("[BI] ai_grading_composition|userId={}, childId={}", UserContext.getCurrentUserId(), dto.getChildId());
        return R.ok(aiGradingApplicationService.gradeComposition(dto.getChildId(), dto.getSubject(), dto.getTitle(), dto.getContent()));
    }

    @PostMapping("/pronunciation")
    public R<AiGradingRecord> evaluatePronunciation(@Valid @RequestBody PronunciationEvalDTO dto) {
        log.info("[BI] ai_grading_pronunciation|userId={}, childId={}", UserContext.getCurrentUserId(), dto.getChildId());
        return R.ok(aiGradingApplicationService.evaluatePronunciation(dto.getChildId(), dto.getTitle(), dto.getAudioUrl(), dto.getAudioTranscript()));
    }

    @GetMapping("/history/{childId}")
    public R<List<AiGradingRecord>> getHistory(@PathVariable Long childId) {
        return R.ok(aiGradingApplicationService.getHistory(childId));
    }
}
