package com.wzoto.application.service;

import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.AiQaRecord;
import com.wzoto.domain.service.AiQaDomainService;
import com.wzoto.domain.valobj.AiQaType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/** AI答疑应用服务 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiQaApplicationService {

    private final AiQaDomainService aiQaDomainService;

    public AiQaRecord ask(Long childId, String subject, String qaTypeCode,
                           String questionText, String questionImageUrl,
                           String ocrText, boolean isVip) {
        Long parentId = UserContext.getCurrentUserId();
        AiQaType qaType = qaTypeCode != null ? AiQaType.fromCode(qaTypeCode) : AiQaType.TEXT;
        return aiQaDomainService.ask(parentId, childId, subject, qaType, questionText, questionImageUrl, ocrText, isVip);
    }

    public AiQaRecord followUp(Long childId, String conversationId, String questionText, boolean isVip) {
        Long parentId = UserContext.getCurrentUserId();
        return aiQaDomainService.followUp(parentId, childId, conversationId, questionText, isVip);
    }

    public List<AiQaRecord> getHistory(Long childId) {
        Long parentId = UserContext.getCurrentUserId();
        return aiQaDomainService.getHistory(parentId, childId);
    }

    public List<AiQaRecord> getConversation(String conversationId) {
        return aiQaDomainService.getConversation(conversationId);
    }
}
