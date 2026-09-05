package com.wzoto.domain.entity;

import com.wzoto.domain.valobj.MemberStatus;
import com.wzoto.domain.valobj.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 会员 - 领域实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Membership {

    private Long id;
    private Long userId;
    private MemberType memberType;
    private MemberStatus status;
    private LocalDateTime startTime;
    private LocalDateTime expireTime;
    private Boolean autoRenew;
    private String transactionId;
    private Boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ========== 领域行为 ==========

    /**
     * 开通会员
     */
    public static Membership create(Long userId, MemberType memberType, boolean autoRenew) {
        LocalDateTime now = LocalDateTime.now();
        return Membership.builder()
                .userId(userId)
                .memberType(memberType)
                .status(MemberStatus.ACTIVE)
                .startTime(now)
                .expireTime(now.plusDays(memberType.getDays()))
                .autoRenew(autoRenew)
                .deleted(false)
                .build();
    }

    /**
     * 是否生效中
     */
    public boolean isActive() {
        return MemberStatus.ACTIVE.equals(this.status) && this.expireTime.isAfter(LocalDateTime.now());
    }

    /**
     * 检查并更新过期状态
     */
    public void checkExpired() {
        if (MemberStatus.ACTIVE.equals(this.status) && this.expireTime.isBefore(LocalDateTime.now())) {
            this.status = MemberStatus.EXPIRED;
        }
    }

    /**
     * 续费
     */
    public void renew(MemberType memberType, boolean autoRenew) {
        LocalDateTime baseTime = this.isActive() ? this.expireTime : LocalDateTime.now();
        this.memberType = memberType;
        this.status = MemberStatus.ACTIVE;
        this.startTime = LocalDateTime.now();
        this.expireTime = baseTime.plusDays(memberType.getDays());
        this.autoRenew = autoRenew;
    }
}
