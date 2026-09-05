package com.wzoto.domain.service;

import com.wzoto.domain.entity.User;
import com.wzoto.domain.entity.VerifyRecord;
import com.wzoto.domain.repository.UserRepository;
import com.wzoto.domain.repository.VerifyRecordRepository;
import com.wzoto.domain.valobj.IdentityType;
import com.wzoto.domain.valobj.VerifyStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 认证领域服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VerifyDomainService {

    private final VerifyRecordRepository verifyRecordRepository;
    private final UserRepository userRepository;

    /**
     * 家长提交实名认证
     * 业务规则：家长认证自动通过（可后续接入第三方实名API）
     */
    public VerifyRecord submitParentVerify(Long userId, String realName, String idCardNo) {
        // 1. 校验用户身份
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        if (user.getIdentityType() != IdentityType.PARENT) {
            throw new IllegalArgumentException("仅家长身份可进行此认证");
        }
        if (user.getVerifyStatus() == VerifyStatus.APPROVED) {
            throw new IllegalStateException("您已完成实名认证，无需重复认证");
        }

        // 2. 创建认证记录
        VerifyRecord record = new VerifyRecord();
        record.setUserId(userId);
        record.submitParentVerify(realName, idCardNo);

        // 3. 保存认证记录
        record = verifyRecordRepository.save(record);

        // 4. 同步更新用户认证状态
        user.setVerifyStatus(VerifyStatus.APPROVED);
        user.setRealName(realName);
        userRepository.save(user);

        log.info("家长实名认证通过, userId={}, realName={}", userId, realName);
        return record;
    }

    /**
     * 大学生提交实名认证
     * 业务规则：大学生认证需人工审核（学生证+身份证）
     */
    public VerifyRecord submitStudentVerify(Long userId, String realName, String idCardNo, String studentCardImage) {
        // 1. 校验用户身份
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        if (user.getIdentityType() != IdentityType.STUDENT) {
            throw new IllegalArgumentException("仅大学生身份可进行此认证");
        }
        if (user.getVerifyStatus() == VerifyStatus.APPROVED) {
            throw new IllegalStateException("您已完成实名认证，无需重复认证");
        }
        if (user.getVerifyStatus() == VerifyStatus.PENDING) {
            throw new IllegalStateException("您已提交认证，请等待审核");
        }

        // 2. 创建认证记录
        VerifyRecord record = new VerifyRecord();
        record.setUserId(userId);
        record.submitStudentVerify(realName, idCardNo, studentCardImage);

        // 3. 保存认证记录
        record = verifyRecordRepository.save(record);

        // 4. 更新用户状态为审核中
        user.setVerifyStatus(VerifyStatus.PENDING);
        user.setRealName(realName);
        userRepository.save(user);

        log.info("大学生实名认证提交, userId={}, 待人工审核", userId);
        return record;
    }

    /**
     * 获取用户认证状态
     */
    public VerifyRecord getVerifyStatus(Long userId) {
        return verifyRecordRepository.findLatestByUserId(userId);
    }

    /**
     * 管理员审核（通过/拒绝）
     */
    public VerifyRecord auditVerify(Long recordId, boolean approved, String remark) {
        VerifyRecord record = verifyRecordRepository.findById(recordId);
        if (record == null) {
            throw new IllegalArgumentException("认证记录不存在");
        }

        if (approved) {
            record.approve(remark);
        } else {
            record.reject(remark);
        }

        record = verifyRecordRepository.update(record);

        // 同步更新用户认证状态
        User user = userRepository.findById(record.getUserId());
        if (user != null) {
            user.setVerifyStatus(record.getVerifyStatus());
            if (approved) {
                user.setRealName(record.getRealName());
            }
            userRepository.save(user);
        }

        log.info("认证审核完成, recordId={}, result={}", recordId, approved ? "通过" : "拒绝");
        return record;
    }
}