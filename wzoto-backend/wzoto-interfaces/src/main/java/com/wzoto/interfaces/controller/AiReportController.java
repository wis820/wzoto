package com.wzoto.interfaces.controller;

import com.wzoto.application.service.AiReportApplicationService;
import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.AiReport;
import com.wzoto.domain.entity.Membership;
import com.wzoto.interfaces.common.R;
import com.wzoto.interfaces.dto.ai.CreateAiReportDTO;
import com.wzoto.interfaces.vo.ai.AiReportVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AI学情诊断控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiReportController {

    private final AiReportApplicationService aiReportApplicationService;

    /**
     * 创建AI学情诊断报告
     * POST /api/ai/report
     */
    @PostMapping("/report")
    public R<AiReportVO> createReport(@Valid @RequestBody CreateAiReportDTO dto) {
        log.info("创建AI学情诊断报告, userId={}", UserContext.getCurrentUserId());
        AiReport report = aiReportApplicationService.createReport(
                dto.getChildName(), dto.getWeakSubjects(),
                dto.getRecentScores(), dto.getWeakPointDesc(), dto.getPhotoUrls());
        return R.ok(toVO(report));
    }

    /**
     * 获取我的AI报告列表
     * GET /api/ai/reports
     */
    @GetMapping("/reports")
    public R<List<AiReportVO>> getMyReports() {
        List<AiReport> reports = aiReportApplicationService.getMyReports();
        List<AiReportVO> voList = reports.stream().map(this::toVO).collect(Collectors.toList());
        return R.ok(voList);
    }

    /**
     * 获取报告详情
     * GET /api/ai/report/{id}
     */
    @GetMapping("/report/{id}")
    public R<AiReportVO> getReportDetail(@PathVariable Long id) {
        AiReport report = aiReportApplicationService.getReportDetail(id);
        return R.ok(toVO(report));
    }

    /**
     * 付费解锁报告（模拟支付成功）
     * POST /api/ai/report/{id}/pay
     */
    @PostMapping("/report/{id}/pay")
    public R<AiReportVO> payToUnlock(@PathVariable Long id) {
        log.info("付费解锁报告, userId={}, reportId={}", UserContext.getCurrentUserId(), id);
        AiReport report = aiReportApplicationService.payToUnlock(id);
        return R.ok(toVO(report));
    }

    /**
     * 获取当前用户会员状态和AI报告使用情况
     * GET /api/ai/membership-status
     */
    @GetMapping("/membership-status")
    public R<Map<String, Object>> getMembershipStatus() {
        Map<String, Object> result = new HashMap<>();
        boolean isMember = aiReportApplicationService.isParentMember();
        result.put("isMember", isMember);
        result.put("freeReportUsed", aiReportApplicationService.getFreeReportUsedCount());
        result.put("freeReportQuota", 2);
        Membership membership = aiReportApplicationService.getMyMembership();
        if (membership != null) {
            result.put("memberType", membership.getMemberType() != null ? membership.getMemberType().getCode() : null);
            result.put("memberTypeDesc", membership.getMemberType() != null ? membership.getMemberType().getDesc() : null);
            result.put("expireTime", membership.getExpireTime());
            result.put("isActive", membership.isActive());
        }
        return R.ok(result);
    }

    // ========== 转换方法 ==========

    private AiReportVO toVO(AiReport report) {
        // 非会员未付费报告，不返回完整内容
        String visibleContent = report.getReportContent();
        if (!report.isFullReport()) {
            visibleContent = null; // 预览模式不返回完整内容
        }
        return AiReportVO.builder()
                .id(report.getId())
                .childName(report.getChildName())
                .weakSubjects(report.getWeakSubjects())
                .recentScores(report.getRecentScores())
                .weakPointDesc(report.getWeakPointDesc())
                .photoUrls(report.getPhotoUrls())
                .reportContent(visibleContent)
                .previewContent(report.getPreviewContent())
                .reportType(report.getReportType())
                .isMemberReport(report.getIsMemberReport())
                .price(report.getPrice())
                .paymentStatus(report.getPaymentStatus())
                .createdAt(report.getCreatedAt())
                .build();
    }
}
