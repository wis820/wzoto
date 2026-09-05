package com.wzoto.interfaces.controller;

import com.wzoto.application.service.AiQaApplicationService;
import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.AiQaRecord;
import com.wzoto.interfaces.common.R;
import com.wzoto.interfaces.dto.AiQaAskDTO;
import com.wzoto.interfaces.dto.AiQaFollowUpDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** AI答疑控制器 */
@Slf4j
@RestController
@RequestMapping("/api/ai/qa")
@RequiredArgsConstructor
public class AiQaController {

    private final AiQaApplicationService aiQaApplicationService;

    @PostMapping("/ask")
    public R<AiQaRecord> ask(@Valid @RequestBody AiQaAskDTO dto) {
        log.info("[BI] ai_qa_ask|userId={}, childId={}", UserContext.getCurrentUserId(), dto.getChildId());
        AiQaRecord record = aiQaApplicationService.ask(dto.getChildId(), dto.getSubject(), dto.getQaType(),
                dto.getQuestionText(), dto.getQuestionImageUrl(), null, false);
        return R.ok(record);
    }

    @PostMapping("/follow-up")
    public R<AiQaRecord> followUp(@Valid @RequestBody AiQaFollowUpDTO dto) {
        log.info("[BI] ai_qa_followup|userId={}, childId={}", UserContext.getCurrentUserId(), dto.getChildId());
        AiQaRecord record = aiQaApplicationService.followUp(dto.getChildId(), dto.getConversationId(),
                dto.getQuestionText(), false);
        return R.ok(record);
    }

    @GetMapping("/history/{childId}")
    public R<List<AiQaRecord>> getHistory(@PathVariable Long childId) {
        return R.ok(aiQaApplicationService.getHistory(childId));
    }

    @GetMapping("/session/{conversationId}")
    public R<List<AiQaRecord>> getSession(@PathVariable String conversationId) {
        return R.ok(aiQaApplicationService.getConversation(conversationId));
    }
}
