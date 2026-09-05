package com.wzoto.application.service;

import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.AiReport;
import com.wzoto.domain.entity.Membership;
import com.wzoto.domain.service.AiReportDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI学情诊断 - 应用服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiReportApplicationService {

    private final AiReportDomainService aiReportDomainService;

    /**
     * 创建AI学情诊断报告
     */
    public AiReport createReport(String childName, String weakSubjects,
                                  String recentScores, String weakPointDesc, String photoUrls) {
        Long userId = UserContext.getCurrentUserId();
        return aiReportDomainService.createReport(userId, childName, weakSubjects,
                recentScores, weakPointDesc, photoUrls);
    }

    /**
     * 获取当前用户的报告列表
     */
    public List<AiReport> getMyReports() {
        Long userId = UserContext.getCurrentUserId();
        return aiReportDomainService.getUserReports(userId);
    }

    /**
     * 获取报告详情
     */
    public AiReport getReportDetail(Long reportId) {
        Long userId = UserContext.getCurrentUserId();
        return aiReportDomainService.getReportDetail(reportId, userId);
    }

    /**
     * 付费解锁报告（模拟支付成功）
     */
    public AiReport payToUnlock(Long reportId) {
        Long userId = UserContext.getCurrentUserId();
        return aiReportDomainService.payToUnlock(reportId, userId);
    }

    /**
     * 获取当前用户会员状态
     */
    public Membership getMyMembership() {
        Long userId = UserContext.getCurrentUserId();
        return aiReportDomainService.getActiveMembership(userId);
    }

    /**
     * 是否为家长会员
     */
    public boolean isParentMember() {
        Long userId = UserContext.getCurrentUserId();
        return aiReportDomainService.isParentMember(userId);
    }

    /**
     * 获取当月免费报告已用次数
     */
    public int getFreeReportUsedCount() {
        Long userId = UserContext.getCurrentUserId();
        return aiReportDomainService.getFreeReportUsedCount(userId);
    }
}
