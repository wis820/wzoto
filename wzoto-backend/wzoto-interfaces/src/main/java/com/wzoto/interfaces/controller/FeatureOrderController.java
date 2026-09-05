package com.wzoto.interfaces.controller;

import com.wzoto.application.service.FeatureOrderApplicationService;
import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.FeatureOrder;
import com.wzoto.interfaces.common.R;
import com.wzoto.interfaces.vo.feature.FeatureOrderVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能订单控制器 (P1: 简历置顶+加急审核+单次付费)
 */
@Slf4j
@RestController
@RequestMapping("/api/feature")
@RequiredArgsConstructor
public class FeatureOrderController {

    private final FeatureOrderApplicationService featureOrderApplicationService;

    /**
     * 创建简历置顶订单
     * POST /api/feature/resume-pin
     */
    @PostMapping("/resume-pin")
    public R<FeatureOrderVO> createResumePinOrder() {
        log.info("创建简历置顶订单, userId={}", UserContext.getCurrentUserId());
        FeatureOrder order = featureOrderApplicationService.createResumePinOrder();
        return R.ok(toVO(order));
    }

    /**
     * 创建加急审核订单
     * POST /api/feature/expedite-verify
     */
    @PostMapping("/expedite-verify")
    public R<FeatureOrderVO> createExpediteVerifyOrder() {
        log.info("创建加急审核订单, userId={}", UserContext.getCurrentUserId());
        FeatureOrder order = featureOrderApplicationService.createExpediteVerifyOrder();
        return R.ok(toVO(order));
    }

    /**
     * 支付订单（模拟微信支付回调）
     * POST /api/feature/{id}/pay
     */
    @PostMapping("/{id}/pay")
    public R<FeatureOrderVO> payOrder(@PathVariable Long id) {
        log.info("支付功能订单, orderId={}, userId={}", id, UserContext.getCurrentUserId());
        FeatureOrder order = featureOrderApplicationService.payOrder(id);
        return R.ok(toVO(order));
    }

    /**
     * 获取我的功能订单列表
     * GET /api/feature/orders
     */
    @GetMapping("/orders")
    public R<List<FeatureOrderVO>> getMyOrders() {
        List<FeatureOrder> orders = featureOrderApplicationService.getMyOrders();
        List<FeatureOrderVO> voList = orders.stream().map(this::toVO).collect(Collectors.toList());
        return R.ok(voList);
    }

    /**
     * 获取订单详情
     * GET /api/feature/{id}
     */
    @GetMapping("/{id}")
    public R<FeatureOrderVO> getOrderDetail(@PathVariable Long id) {
        FeatureOrder order = featureOrderApplicationService.getOrderDetail(id);
        return R.ok(toVO(order));
    }

    // ========== 转换方法 ==========

    private FeatureOrderVO toVO(FeatureOrder order) {
        return FeatureOrderVO.builder()
                .id(order.getId())
                .featureType(order.getFeatureType() != null ? order.getFeatureType().getCode() : null)
                .featureTypeDesc(order.getFeatureType() != null ? order.getFeatureType().getDesc() : null)
                .targetId(order.getTargetId())
                .amount(order.getAmount())
                .paymentStatus(order.getPaymentStatus() != null ? order.getPaymentStatus().getCode() : null)
                .paymentStatusDesc(order.getPaymentStatus() != null ? order.getPaymentStatus().getDesc() : null)
                .paymentTime(order.getPaymentTime())
                .expireTime(order.getExpireTime())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
