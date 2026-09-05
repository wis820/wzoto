package com.wzoto.domain.service;

import com.wzoto.domain.entity.AiTutorOptimization;
import com.wzoto.domain.entity.Membership;
import com.wzoto.domain.repository.AiGenerator;
import com.wzoto.domain.repository.AiTutorOptimizationRepository;
import com.wzoto.domain.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * AI教员优化 - 领域服务
 * 处理简历优化和定价分析的业务逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiTutorDomainService {

    private final AiGenerator aiGenerator;
    private final AiTutorOptimizationRepository optimizationRepository;
    private final MembershipRepository membershipRepository;

    /** 非会员单次价格 */
    private static final BigDecimal NON_MEMBER_PRICE = new BigDecimal("9.90");

    /**
     * 创建简历优化
     */
    public AiTutorOptimization createResumeOptimization(Long userId, String university,
            String major, String grade, String subjects, String bio, String experience) {

        boolean isMember = isStudentMember(userId);

        String optimizedContent = aiGenerator.generateResumeOptimization(
                university, major, grade, subjects, bio, experience);
        String previewContent = aiGenerator.extractTutorPreview(optimizedContent);

        AiTutorOptimization optimization = new AiTutorOptimization();
        optimization.setUserId(userId);
        optimization.setOptimizationType("RESUME");
        optimization.setUniversity(university);
        optimization.setMajor(major);
        optimization.setGrade(grade);
        optimization.setSubjects(subjects);
        optimization.setCurrentBio(bio);
        optimization.setCurrentExperience(experience);
        optimization.setOptimizedContent(optimizedContent);
        optimization.setPreviewContent(previewContent);
        optimization.setIsMemberReport(isMember);
        optimization.setPrice(isMember ? BigDecimal.ZERO : NON_MEMBER_PRICE);
        optimization.setPaymentStatus(isMember ? "FREE" : "UNPAID");

        optimizationRepository.save(optimization);
        log.info("创建简历优化记录, userId={}, isMember={}", userId, isMember);

        return optimization;
    }

    /**
     * 创建定价分析
     */
    public AiTutorOptimization createPricingAnalysis(Long userId, String university,
            String major, String grade, String subjects, String experience, Integer currentRate) {

        // 定价分析对所有用户免费（引流功能）
        String optimizedContent = aiGenerator.generatePricingAnalysis(
                university, major, grade, subjects, experience, currentRate);
        String previewContent = aiGenerator.extractTutorPreview(optimizedContent);

        AiTutorOptimization optimization = new AiTutorOptimization();
        optimization.setUserId(userId);
        optimization.setOptimizationType("PRICING");
        optimization.setUniversity(university);
        optimization.setMajor(major);
        optimization.setGrade(grade);
        optimization.setSubjects(subjects);
        optimization.setCurrentExperience(experience);
        optimization.setCurrentRate(currentRate);
        optimization.setOptimizedContent(optimizedContent);
        optimization.setPreviewContent(previewContent);
        optimization.setIsMemberReport(true);
        optimization.setPrice(BigDecimal.ZERO);
        optimization.setPaymentStatus("FREE");

        optimizationRepository.save(optimization);
        log.info("创建定价分析记录, userId={}", userId);

        return optimization;
    }

    /**
     * 获取用户优化记录列表
     */
    public List<AiTutorOptimization> getMyOptimizations(Long userId) {
        return optimizationRepository.findByUserId(userId);
    }

    /**
     * 获取优化详情
     */
    public AiTutorOptimization getOptimizationDetail(Long id, Long userId) {
        AiTutorOptimization optimization = optimizationRepository.findById(id);
        if (optimization == null || !optimization.getUserId().equals(userId)) {
            throw new RuntimeException("优化记录不存在");
        }
        return optimization;
    }

    /**
     * 付费解锁
     */
    public AiTutorOptimization payToUnlock(Long id, Long userId) {
        AiTutorOptimization optimization = getOptimizationDetail(id, userId);
        optimization.setPaymentStatus("PAID");
        optimizationRepository.update(optimization);
        return optimization;
    }

    /**
     * 判断用户是否为学生会员
     */
    private boolean isStudentMember(Long userId) {
        Membership membership = membershipRepository.findActiveByUserId(userId);
        return membership != null && membership.isActive();
    }
}
