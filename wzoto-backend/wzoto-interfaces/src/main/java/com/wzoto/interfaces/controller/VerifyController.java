package com.wzoto.interfaces.controller;

import com.wzoto.application.service.VerifyApplicationService;
import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.VerifyRecord;
import com.wzoto.interfaces.common.R;
import com.wzoto.interfaces.dto.verify.ParentVerifyDTO;
import com.wzoto.interfaces.dto.verify.StudentVerifyDTO;
import com.wzoto.interfaces.vo.verify.VerifyRecordVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 实名认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/verify")
@RequiredArgsConstructor
public class VerifyController {

    private final VerifyApplicationService verifyApplicationService;

    /**
     * 家长提交实名认证
     * POST /api/verify/parent
     */
    @PostMapping("/parent")
    public R<VerifyRecordVO> submitParentVerify(@Valid @RequestBody ParentVerifyDTO dto) {
        log.info("家长提交实名认证, userId={}", UserContext.getCurrentUserId());
        VerifyRecord record = verifyApplicationService.submitParentVerify(dto.getRealName(), dto.getIdCardNo());
        return R.ok(toVO(record));
    }

    /**
     * 大学生提交实名认证
     * POST /api/verify/student
     */
    @PostMapping("/student")
    public R<VerifyRecordVO> submitStudentVerify(@Valid @RequestBody StudentVerifyDTO dto) {
        log.info("大学生提交实名认证, userId={}", UserContext.getCurrentUserId());
        VerifyRecord record = verifyApplicationService.submitStudentVerify(
                dto.getRealName(), dto.getIdCardNo(), dto.getStudentCardImage());
        return R.ok(toVO(record));
    }

    /**
     * 获取当前用户认证状态
     * GET /api/verify/status
     */
    @GetMapping("/status")
    public R<VerifyRecordVO> getVerifyStatus() {
        VerifyRecord record = verifyApplicationService.getVerifyStatus();
        return R.ok(record != null ? toVO(record) : null);
    }

    // ========== 转换方法 ==========

    private VerifyRecordVO toVO(VerifyRecord record) {
        return VerifyRecordVO.builder()
                .id(record.getId())
                .verifyType(record.getVerifyType() != null ? record.getVerifyType().getCode() : null)
                .verifyTypeDesc(record.getVerifyType() != null ? record.getVerifyType().getDesc() : null)
                .realNameMasked(maskName(record.getRealName()))
                .idCardNoMasked(maskIdCard(record.getIdCardNo()))
                .studentCardImage(record.getStudentCardImage())
                .verifyStatus(record.getVerifyStatus() != null ? record.getVerifyStatus().getCode() : null)
                .verifyStatusDesc(record.getVerifyStatus() != null ? record.getVerifyStatus().getDesc() : null)
                .remark(record.getRemark())
                .expedited(record.getExpedited())
                .createdAt(record.getCreatedAt())
                .build();
    }

    /** 姓名脱敏：张三 → 张* */
    private String maskName(String name) {
        if (name == null || name.length() <= 1) return name;
        return name.charAt(0) + "*".repeat(name.length() - 1);
    }

    /** 身份证脱敏：110101199001011234 → 110101****1234 */
    private String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 8) return idCard;
        return idCard.substring(0, 6) + "****" + idCard.substring(idCard.length() - 4);
    }
}