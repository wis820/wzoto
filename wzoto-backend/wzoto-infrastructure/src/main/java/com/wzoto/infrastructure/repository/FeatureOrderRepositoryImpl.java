package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.FeatureOrder;
import com.wzoto.domain.repository.FeatureOrderRepository;
import com.wzoto.domain.valobj.FeatureType;
import com.wzoto.domain.valobj.PaymentStatus;
import com.wzoto.infrastructure.mapper.FeatureOrderMapper;
import com.wzoto.infrastructure.pojo.FeatureOrderPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能订单仓储实现
 */
@Repository
@RequiredArgsConstructor
public class FeatureOrderRepositoryImpl implements FeatureOrderRepository {

    private final FeatureOrderMapper featureOrderMapper;

    @Override
    public FeatureOrder save(FeatureOrder order) {
        FeatureOrderPO po = toPO(order);
        featureOrderMapper.insert(po);
        order.setId(po.getId());
        return order;
    }

    @Override
    public FeatureOrder findById(Long id) {
        FeatureOrderPO po = featureOrderMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public List<FeatureOrder> findByUserId(Long userId) {
        LambdaQueryWrapper<FeatureOrderPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FeatureOrderPO::getUserId, userId)
                .orderByDesc(FeatureOrderPO::getCreatedAt);
        return featureOrderMapper.selectList(wrapper).stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<FeatureOrder> findPaidByUserIdAndType(Long userId, FeatureType featureType) {
        LambdaQueryWrapper<FeatureOrderPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FeatureOrderPO::getUserId, userId)
                .eq(FeatureOrderPO::getFeatureType, featureType.getCode())
                .eq(FeatureOrderPO::getPaymentStatus, PaymentStatus.PAID.getCode());
        return featureOrderMapper.selectList(wrapper).stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public FeatureOrder update(FeatureOrder order) {
        FeatureOrderPO po = toPO(order);
        featureOrderMapper.updateById(po);
        return order;
    }

    // ========== 转换方法 ==========

    private FeatureOrder toEntity(FeatureOrderPO po) {
        return FeatureOrder.builder()
                .id(po.getId())
                .userId(po.getUserId())
                .featureType(po.getFeatureType() != null ? FeatureType.fromCode(po.getFeatureType()) : null)
                .targetId(po.getTargetId())
                .amount(po.getAmount())
                .paymentStatus(po.getPaymentStatus() != null ? PaymentStatus.fromCode(po.getPaymentStatus()) : null)
                .paymentTime(po.getPaymentTime())
                .transactionId(po.getTransactionId())
                .expireTime(po.getExpireTime())
                .deleted(po.getDeleted() != null && po.getDeleted() == 1)
                .createdAt(po.getCreatedAt())
                .updatedAt(po.getUpdatedAt())
                .build();
    }

    private FeatureOrderPO toPO(FeatureOrder order) {
        FeatureOrderPO po = new FeatureOrderPO();
        po.setId(order.getId());
        po.setUserId(order.getUserId());
        po.setFeatureType(order.getFeatureType() != null ? order.getFeatureType().getCode() : null);
        po.setTargetId(order.getTargetId());
        po.setAmount(order.getAmount());
        po.setPaymentStatus(order.getPaymentStatus() != null ? order.getPaymentStatus().getCode() : null);
        po.setPaymentTime(order.getPaymentTime());
        po.setTransactionId(order.getTransactionId());
        po.setExpireTime(order.getExpireTime());
        po.setDeleted(order.getDeleted() != null && order.getDeleted() ? 1 : 0);
        return po;
    }
}
