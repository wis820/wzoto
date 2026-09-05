package com.wzoto.domain.repository;

import com.wzoto.domain.entity.FeatureOrder;
import com.wzoto.domain.valobj.FeatureType;

import java.util.List;

/**
 * 功能订单仓储接口
 */
public interface FeatureOrderRepository {

    FeatureOrder save(FeatureOrder order);

    FeatureOrder findById(Long id);

    /** 查询用户的功能订单列表 */
    List<FeatureOrder> findByUserId(Long userId);

    /** 查询用户某类型的有效（已支付）订单 */
    List<FeatureOrder> findPaidByUserIdAndType(Long userId, FeatureType featureType);

    /** 更新订单 */
    FeatureOrder update(FeatureOrder order);
}
