package com.wzoto.application.service;

import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.VerifyRecord;
import com.wzoto.domain.service.VerifyDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 认证应用服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VerifyApplicationService {

    private final VerifyDomainService verifyDomainService;

    /**
     * 家长提交实名认证
     */
    public VerifyRecord submitParentVerify(String realName, String idCardNo) {
        Long userId = UserContext.getCurrentUserId();
        return verifyDomainService.submitParentVerify(userId, realName, idCardNo);
    }

    /**
     * 大学生提交实名认证
     */
    public VerifyRecord submitStudentVerify(String realName, String idCardNo, String studentCardImage) {
        Long userId = UserContext.getCurrentUserId();
        return verifyDomainService.submitStudentVerify(userId, realName, idCardNo, studentCardImage);
    }

    /**
     * 获取当前用户认证状态
     */
    public VerifyRecord getVerifyStatus() {
        Long userId = UserContext.getCurrentUserId();
        return verifyDomainService.getVerifyStatus(userId);
    }
}