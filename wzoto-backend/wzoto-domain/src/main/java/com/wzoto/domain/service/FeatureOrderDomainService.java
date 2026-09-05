package com.wzoto.domain.service;

import com.wzoto.domain.entity.FeatureOrder;
import com.wzoto.domain.entity.TutorProfile;
import com.wzoto.domain.entity.VerifyRecord;
import com.wzoto.domain.repository.FeatureOrderRepository;
import com.wzoto.domain.repository.TutorProfileRepository;
import com.wzoto.domain.repository.VerifyRecordRepository;
import com.wzoto.domain.valobj.FeatureType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 功能订单领域服务 (P1: 简历置顶+加急审核+单次付费)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FeatureOrderDomainService {

    private final FeatureOrderRepository featureOrderRepository;
    private final TutorProfileRepository tutorProfileRepository;
    private final VerifyRecordRepository verifyRecordRepository;

    /**
     * 创建简历置顶订单
     * 业务规则：大学生用户，已有教员档案
     */
    public FeatureOrder createResumePinOrder(Long userId) {
        // 检查教员档案是否存在
        TutorProfile profile = tutorProfileRepository.findByUserId(userId);
        if (profile == null) {
            throw new IllegalStateException("请先完善教员档案");
        }

        // 检查是否已有有效置顶
        List<FeatureOrder> existingPins = featureOrderRepository.findPaidByUserIdAndType(userId, FeatureType.RESUME_PIN);
        for (FeatureOrder pin : existingPins) {
            if (pin.getExpireTime() != null && pin.getExpireTime().isAfter(LocalDateTime.now())) {
                throw new IllegalStateException("您当前已有有效置顶，到期时间：" + pin.getExpireTime());
            }
        }

        FeatureOrder order = FeatureOrder.create(userId, FeatureType.RESUME_PIN, profile.getId());
        return featureOrderRepository.save(order);
    }

    /**
     * 创建加急审核订单
     * 业务规则：大学生用户，有待审核的认证记录
     */
    public FeatureOrder createExpediteVerifyOrder(Long userId) {
        // 检查是否有待审核的认证记录
        VerifyRecord record = verifyRecordRepository.findLatestByUserId(userId);
        if (record == null) {
            throw new IllegalStateException("未找到认证记录");
        }
        if (record.getExpedited() != null && record.getExpedited()) {
            throw new IllegalStateException("您的认证已加急，请勿重复购买");
        }

        FeatureOrder order = FeatureOrder.create(userId, FeatureType.EXPEDITE_VERIFY, record.getId());
        return featureOrderRepository.save(order);
    }

    /**
     * 支付订单（模拟微信支付回调）
     */
    public FeatureOrder payOrder(Long orderId, Long userId) {
        FeatureOrder order = featureOrderRepository.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new IllegalStateException("无权操作此订单");
        }

        // 模拟支付成功
        String transactionId = "MOCK_TX_" + System.currentTimeMillis();
        order.pay(transactionId);
        featureOrderRepository.update(order);

        // 支付成功后执行功能效果
        applyFeatureEffect(order);

        return order;
    }

    /**
     * 支付成功后执行功能效果
     */
    private void applyFeatureEffect(FeatureOrder order) {
        switch (order.getFeatureType()) {
            case RESUME_PIN:
                // 设置教员档案置顶到期时间
                TutorProfile profile = tutorProfileRepository.findById(order.getTargetId());
                if (profile != null) {
                    profile.setPinnedUntil(order.getExpireTime());
                    tutorProfileRepository.update(profile);
                    log.info("教员档案置顶成功, profileId={}, until={}", profile.getId(), order.getExpireTime());
                }
                break;
            case EXPEDITE_VERIFY:
                // 标记认证记录为加急
                VerifyRecord record = verifyRecordRepository.findById(order.getTargetId());
                if (record != null) {
                    record.setExpedited(true);
                    verifyRecordRepository.update(record);
                    log.info("认证记录加急标记成功, recordId={}", record.getId());
                }
                break;
        }
    }

    /**
     * 查询用户功能订单列表
     */
    public List<FeatureOrder> getMyOrders(Long userId) {
        return featureOrderRepository.findByUserId(userId);
    }

    /**
     * 获取订单详情
     */
    public FeatureOrder getOrderById(Long orderId, Long userId) {
        FeatureOrder order = featureOrderRepository.findById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new IllegalArgumentException("订单不存在或无权查看");
        }
        return order;
    }
}
