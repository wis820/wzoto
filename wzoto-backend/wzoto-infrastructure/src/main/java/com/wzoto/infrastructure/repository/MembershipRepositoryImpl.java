package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.Membership;
import com.wzoto.domain.repository.MembershipRepository;
import com.wzoto.domain.valobj.MemberStatus;
import com.wzoto.domain.valobj.MemberType;
import com.wzoto.infrastructure.mapper.MembershipMapper;
import com.wzoto.infrastructure.pojo.MembershipPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MembershipRepositoryImpl implements MembershipRepository {

    private final MembershipMapper membershipMapper;

    @Override
    public Membership save(Membership membership) {
        MembershipPO po = toPO(membership);
        membershipMapper.insert(po);
        membership.setId(po.getId());
        return membership;
    }

    @Override
    public Membership findById(Long id) {
        MembershipPO po = membershipMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public Membership findActiveByUserId(Long userId) {
        LambdaQueryWrapper<MembershipPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MembershipPO::getUserId, userId)
                .eq(MembershipPO::getStatus, "ACTIVE")
                .orderByDesc(MembershipPO::getExpireTime)
                .last("LIMIT 1");
        MembershipPO po = membershipMapper.selectOne(wrapper);
        if (po == null) return null;
        Membership entity = toEntity(po);
        entity.checkExpired();
        if (entity.getStatus() != MemberStatus.ACTIVE) {
            membershipMapper.updateById(toPO(entity));
        }
        return entity.isActive() ? entity : null;
    }

    @Override
    public List<Membership> findByUserId(Long userId) {
        LambdaQueryWrapper<MembershipPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MembershipPO::getUserId, userId).orderByDesc(MembershipPO::getCreatedAt);
        return membershipMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public Membership update(Membership membership) {
        MembershipPO po = toPO(membership);
        membershipMapper.updateById(po);
        return membership;
    }

    private Membership toEntity(MembershipPO po) {
        Membership entity = new Membership();
        entity.setId(po.getId());
        entity.setUserId(po.getUserId());
        entity.setMemberType(po.getMemberType() != null ? MemberType.fromCode(po.getMemberType()) : null);
        entity.setStatus(po.getStatus() != null ? MemberStatus.fromCode(po.getStatus()) : null);
        entity.setStartTime(po.getStartTime());
        entity.setExpireTime(po.getExpireTime());
        entity.setAutoRenew(po.getAutoRenew());
        entity.setTransactionId(po.getTransactionId());
        entity.setDeleted(po.getDeleted());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private MembershipPO toPO(Membership entity) {
        MembershipPO po = new MembershipPO();
        po.setId(entity.getId());
        po.setUserId(entity.getUserId());
        po.setMemberType(entity.getMemberType() != null ? entity.getMemberType().getCode() : null);
        po.setStatus(entity.getStatus() != null ? entity.getStatus().getCode() : null);
        po.setStartTime(entity.getStartTime());
        po.setExpireTime(entity.getExpireTime());
        po.setAutoRenew(entity.getAutoRenew());
        po.setTransactionId(entity.getTransactionId());
        return po;
    }
}
