package com.wzoto.domain.repository;

import com.wzoto.domain.entity.Membership;

import java.util.List;

/**
 * 会员仓储接口
 */
public interface MembershipRepository {

    Membership save(Membership membership);

    Membership findById(Long id);

    /** 查询用户当前生效的会员 */
    Membership findActiveByUserId(Long userId);

    /** 查询用户所有会员记录 */
    List<Membership> findByUserId(Long userId);

    Membership update(Membership membership);
}
