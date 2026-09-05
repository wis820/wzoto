package com.wzoto.interfaces.controller;

import com.wzoto.application.service.AiTutorApplicationService;
import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.AiTutorOptimization;
import com.wzoto.interfaces.common.R;
import com.wzoto.interfaces.dto.ai.CreatePricingAnalysisDTO;
import com.wzoto.interfaces.dto.ai.CreateResumeOptimizationDTO;
import com.wzoto.interfaces.vo.ai.AiTutorOptimizationVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AI教员优化 - 接口控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/ai/tutor")
@RequiredArgsConstructor
public class AiTutorController {

    private final AiTutorApplicationService aiTutorApplicationService;

    /**
     * 创建简历优化
     * POST /api/ai/tutor/resume
     */
    @PostMapping("/resume")
    public R<AiTutorOptimizationVO> createResumeOptimization(@Valid @RequestBody CreateResumeOptimizationDTO dto) {
        Long userId = UserContext.getCurrentUserId();
        log.info("创建AI简历优化, userId={}", userId);
        AiTutorOptimization result = aiTutorApplicationService.createResumeOptimization(
                userId, dto.getUniversity(), dto.getMajor(), dto.getGrade(),
                dto.getSubjects(), dto.getBio(), dto.getExperience());
        return R.ok(toVO(result));
    }

    /**
     * 创建定价分析
     * POST /api/ai/tutor/pricing
     */
    @PostMapping("/pricing")
    public R<AiTutorOptimizationVO> createPricingAnalysis(@Valid @RequestBody CreatePricingAnalysisDTO dto) {
        Long userId = UserContext.getCurrentUserId();
        log.info("创建AI定价分析, userId={}", userId);
        AiTutorOptimization result = aiTutorApplicationService.createPricingAnalysis(
                userId, dto.getUniversity(), dto.getMajor(), dto.getGrade(),
                dto.getSubjects(), dto.getExperience(), dto.getCurrentRate());
        return R.ok(toVO(result));
    }

    /**
     * 获取我的优化记录列表
     * GET /api/ai/tutor/optimizations
     */
    @GetMapping("/optimizations")
    public R<List<AiTutorOptimizationVO>> getMyOptimizations() {
        Long userId = UserContext.getCurrentUserId();
        List<AiTutorOptimization> list = aiTutorApplicationService.getMyOptimizations(userId);
        return R.ok(list.stream().map(this::toVO).collect(Collectors.toList()));
    }

    /**
     * 获取优化详情
     * GET /api/ai/tutor/{id}
     */
    @GetMapping("/{id}")
    public R<AiTutorOptimizationVO> getOptimizationDetail(@PathVariable Long id) {
        Long userId = UserContext.getCurrentUserId();
        AiTutorOptimization opt = aiTutorApplicationService.getOptimizationDetail(id, userId);
        return R.ok(toVO(opt));
    }

    /**
     * 付费解锁
     * POST /api/ai/tutor/{id}/pay
     */
    @PostMapping("/{id}/pay")
    public R<AiTutorOptimizationVO> payToUnlock(@PathVariable Long id) {
        Long userId = UserContext.getCurrentUserId();
        AiTutorOptimization opt = aiTutorApplicationService.payToUnlock(id, userId);
        return R.ok(toVO(opt));
    }

    private AiTutorOptimizationVO toVO(AiTutorOptimization e) {
        // 非会员未付费时，隐藏完整内容
        String content = e.getOptimizedContent();
        if (!e.isFullReport()) {
            content = null;
        }
        return AiTutorOptimizationVO.builder()
                .id(e.getId())
                .optimizationType(e.getOptimizationType())
                .university(e.getUniversity())
                .major(e.getMajor())
                .grade(e.getGrade())
                .subjects(e.getSubjects())
                .currentBio(e.getCurrentBio())
                .currentExperience(e.getCurrentExperience())
                .currentRate(e.getCurrentRate())
                .optimizedContent(content)
                .previewContent(e.getPreviewContent())
                .isMemberReport(e.getIsMemberReport())
                .price(e.getPrice())
                .paymentStatus(e.getPaymentStatus())
                .createdAt(e.getCreatedAt())
                .build();
    }
}
