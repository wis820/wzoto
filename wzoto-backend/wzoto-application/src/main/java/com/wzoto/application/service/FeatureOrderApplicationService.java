package com.wzoto.application.service;

import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.FeatureOrder;
import com.wzoto.domain.service.FeatureOrderDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 功能订单应用服务 (P1: 简历置顶+加急审核+单次付费)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FeatureOrderApplicationService {

    private final FeatureOrderDomainService featureOrderDomainService;

    /**
     * 创建简历置顶订单
     */
    public FeatureOrder createResumePinOrder() {
        Long userId = UserContext.getCurrentUserId();
        return featureOrderDomainService.createResumePinOrder(userId);
    }

    /**
     * 创建加急审核订单
     */
    public FeatureOrder createExpediteVerifyOrder() {
        Long userId = UserContext.getCurrentUserId();
        return featureOrderDomainService.createExpediteVerifyOrder(userId);
    }

    /**
     * 支付订单（模拟支付）
     */
    public FeatureOrder payOrder(Long orderId) {
        Long userId = UserContext.getCurrentUserId();
        return featureOrderDomainService.payOrder(orderId, userId);
    }

    /**
     * 获取我的订单列表
     */
    public List<FeatureOrder> getMyOrders() {
        Long userId = UserContext.getCurrentUserId();
        return featureOrderDomainService.getMyOrders(userId);
    }

    /**
     * 获取订单详情
     */
    public FeatureOrder getOrderDetail(Long orderId) {
        Long userId = UserContext.getCurrentUserId();
        return featureOrderDomainService.getOrderById(orderId, userId);
    }
}
