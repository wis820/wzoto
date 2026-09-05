package com.wzoto.application.service;

import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.AiGradingRecord;
import com.wzoto.domain.service.AiGradingDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/** AI批改应用服务 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiGradingApplicationService {

    private final AiGradingDomainService aiGradingDomainService;

    public AiGradingRecord gradeComposition(Long childId, String subject, String title, String content) {
        Long parentId = UserContext.getCurrentUserId();
        return aiGradingDomainService.gradeComposition(parentId, childId, subject, title, content);
    }

    public AiGradingRecord evaluatePronunciation(Long childId, String title, String audioUrl, String audioTranscript) {
        Long parentId = UserContext.getCurrentUserId();
        return aiGradingDomainService.evaluatePronunciation(parentId, childId, title, audioUrl, audioTranscript);
    }

    public List<AiGradingRecord> getHistory(Long childId) {
        Long parentId = UserContext.getCurrentUserId();
        return aiGradingDomainService.getHistory(parentId, childId);
    }

    public List<AiGradingRecord> getHistoryByType(Long childId, String gradingType) {
        Long parentId = UserContext.getCurrentUserId();
        return aiGradingDomainService.getHistoryByType(parentId, childId, gradingType);
    }
}
