package com.wzoto.domain.service;

import com.wzoto.domain.entity.AiReport;
import com.wzoto.domain.entity.Membership;
import com.wzoto.domain.repository.AiGenerator;
import com.wzoto.domain.repository.AiReportRepository;
import com.wzoto.domain.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI学情诊断 - 领域服务
 * 核心业务规则：
 * - 家长会员每月2份免费完整报告
 * - 非会员需付费¥19.9/次，仅可看预览
 * - 付费后解锁完整报告
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiReportDomainService {

    private final AiReportRepository aiReportRepository;
    private final MembershipRepository membershipRepository;
    private final AiGenerator aiGenerator;

    /** 会员每月免费报告配额 */
    private static final int MEMBER_MONTHLY_FREE_QUOTA = 2;

    /**
     * 创建AI学情诊断报告
     *
     * @return 报告实体
     */
    public AiReport createReport(Long userId, String childName, String weakSubjects,
                                 String recentScores, String weakPointDesc, String photoUrls) {
        // 1. 判断是否为生效会员
        boolean isMember = isParentMember(userId);

        // 2. 调用AI生成报告内容
        String fullContent = aiGenerator.generateLearningAnalysis(childName, weakSubjects, recentScores, weakPointDesc);
        String previewContent = aiGenerator.extractPreview(fullContent);

        // 3. 根据会员状态创建不同类型报告
        AiReport report;
        if (isMember) {
            // 会员：检查本月免费配额
            int usedCount = aiReportRepository.countFreeReportsThisMonth(userId);
            if (usedCount < MEMBER_MONTHLY_FREE_QUOTA) {
                report = AiReport.createFreeReport(userId, childName, weakSubjects,
                        recentScores, weakPointDesc, photoUrls, fullContent, previewContent);
                log.info("会员免费报告, userId={}, 本月已用{}/{}", userId, usedCount + 1, MEMBER_MONTHLY_FREE_QUOTA);
            } else {
                // 会员但配额用完，需付费
                report = AiReport.createPaidReport(userId, childName, weakSubjects,
                        recentScores, weakPointDesc, photoUrls, fullContent, previewContent);
                log.info("会员配额已用完, userId={}, 需付费", userId);
            }
        } else {
            // 非会员：付费报告
            report = AiReport.createPaidReport(userId, childName, weakSubjects,
                    recentScores, weakPointDesc, photoUrls, fullContent, previewContent);
            log.info("非会员付费报告, userId={}", userId);
        }

        return aiReportRepository.save(report);
    }

    /**
     * 获取用户报告列表
     */
    public List<AiReport> getUserReports(Long userId) {
        return aiReportRepository.findByUserId(userId);
    }

    /**
     * 获取报告详情
     * 会员/已付费 → 返回完整报告；非会员未付费 → 返回预览
     */
    public AiReport getReportDetail(Long reportId, Long userId) {
        AiReport report = aiReportRepository.findById(reportId);
        if (report == null) {
            throw new IllegalArgumentException("报告不存在");
        }
        if (!report.getUserId().equals(userId)) {
            throw new IllegalStateException("无权查看此报告");
        }
        // 如果是预览报告且用户现在是会员，自动升级为完整报告
        if (!report.isFullReport() && isParentMember(userId)) {
            report.unlockAfterPayment();
            aiReportRepository.update(report);
        }
        return report;
    }

    /**
     * 付费解锁报告（模拟支付成功）
     */
    public AiReport payToUnlock(Long reportId, Long userId) {
        AiReport report = aiReportRepository.findById(reportId);
        if (report == null) {
            throw new IllegalArgumentException("报告不存在");
        }
        if (!report.getUserId().equals(userId)) {
            throw new IllegalStateException("无权操作此报告");
        }
        if (report.isFullReport()) {
            throw new IllegalStateException("报告已是完整版本");
        }
        report.unlockAfterPayment();
        return aiReportRepository.update(report);
    }

    /**
     * 查询用户会员状态
     */
    public boolean isParentMember(Long userId) {
        Membership membership = membershipRepository.findActiveByUserId(userId);
        return membership != null && membership.isActive()
                && membership.getMemberType() != null && membership.getMemberType().isParentType();
    }

    /**
     * 获取用户会员信息
     */
    public Membership getActiveMembership(Long userId) {
        return membershipRepository.findActiveByUserId(userId);
    }

    /**
     * 获取当月免费报告使用情况
     */
    public int getFreeReportUsedCount(Long userId) {
        return aiReportRepository.countFreeReportsThisMonth(userId);
    }
}
