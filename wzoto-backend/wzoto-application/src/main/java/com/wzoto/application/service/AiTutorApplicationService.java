package com.wzoto.application.service;

import com.wzoto.domain.entity.AiTutorOptimization;
import com.wzoto.domain.service.AiTutorDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI教员优化 - 应用服务
 */
@Service
@RequiredArgsConstructor
public class AiTutorApplicationService {

    private final AiTutorDomainService aiTutorDomainService;

    public AiTutorOptimization createResumeOptimization(Long userId, String university,
            String major, String grade, String subjects, String bio, String experience) {
        return aiTutorDomainService.createResumeOptimization(
                userId, university, major, grade, subjects, bio, experience);
    }

    public AiTutorOptimization createPricingAnalysis(Long userId, String university,
            String major, String grade, String subjects, String experience, Integer currentRate) {
        return aiTutorDomainService.createPricingAnalysis(
                userId, university, major, grade, subjects, experience, currentRate);
    }

    public List<AiTutorOptimization> getMyOptimizations(Long userId) {
        return aiTutorDomainService.getMyOptimizations(userId);
    }

    public AiTutorOptimization getOptimizationDetail(Long id, Long userId) {
        return aiTutorDomainService.getOptimizationDetail(id, userId);
    }

    public AiTutorOptimization payToUnlock(Long id, Long userId) {
        return aiTutorDomainService.payToUnlock(id, userId);
    }
}
