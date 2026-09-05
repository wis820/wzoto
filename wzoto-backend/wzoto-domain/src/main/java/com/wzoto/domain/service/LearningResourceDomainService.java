package com.wzoto.domain.service;

import com.wzoto.domain.entity.Child;
import com.wzoto.domain.entity.FeatureOrder;
import com.wzoto.domain.entity.LearningResource;
import com.wzoto.domain.entity.Membership;
import com.wzoto.domain.repository.ChildRepository;
import com.wzoto.domain.repository.FeatureOrderRepository;
import com.wzoto.domain.repository.LearningResourceRepository;
import com.wzoto.domain.repository.MembershipRepository;
import com.wzoto.domain.valobj.FeatureType;
import com.wzoto.domain.valobj.GradeType;
import com.wzoto.domain.valobj.LearningResourceType;
import com.wzoto.domain.valobj.TextbookVersion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 学习资源领域服务 - 资源列表过滤、解锁校验
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LearningResourceDomainService {

    private final LearningResourceRepository learningResourceRepository;
    private final ChildRepository childRepository;
    private final MembershipRepository membershipRepository;
    private final FeatureOrderRepository featureOrderRepository;

    /**
     * 查询资源列表（按会员过滤）
     */
    public List<LearningResource> listResources(Long parentId, Long childId, String subject,
                                                LearningResourceType resourceType, Boolean includeVip) {
        Child child = childRepository.findById(childId);
        if (child == null || !child.belongsTo(parentId)) {
            throw new IllegalArgumentException("无权操作该子女档案");
        }
        GradeType grade = child.getGrade();
        TextbookVersion textbookVersion = child.getTextbookVersion();
        boolean hasMembership = hasLearningMembership(parentId);

        List<LearningResource> resources;
        if (resourceType != null) {
            resources = learningResourceRepository.findByGradeAndSubjectAndType(grade, subject, resourceType);
        } else {
            resources = learningResourceRepository.findByGradeAndSubjectAndTextbookVersion(grade, subject, textbookVersion);
        }
        boolean finalIncludeVip = Boolean.TRUE.equals(includeVip) || hasMembership;
        return resources.stream()
                .filter(r -> finalIncludeVip || !r.isVipOnly())
                .toList();
    }

    /**
     * 查询资源详情
     */
    public LearningResource getResourceDetail(Long resourceId) {
        LearningResource resource = learningResourceRepository.findById(resourceId);
        if (resource == null) {
            throw new IllegalArgumentException("资源不存在");
        }
        return resource;
    }

    /**
     * 解锁付费资源
     */
    public FeatureOrder unlockResource(Long userId, Long resourceId) {
        LearningResource resource = learningResourceRepository.findById(resourceId);
        if (resource == null) {
            throw new IllegalArgumentException("资源不存在");
        }
        if (hasLearningMembership(userId)) {
            log.info("用户已持有学习会员，无需再次解锁, userId={}, resourceId={}", userId, resourceId);
            return null;
        }
        FeatureOrder order = FeatureOrder.create(userId, FeatureType.FULL_COURSE_UNLOCK, resourceId);
        FeatureOrder saved = featureOrderRepository.save(order);
        log.info("创建资源解锁订单, userId={}, resourceId={}, orderId={}", userId, resourceId, saved.getId());
        return saved;
    }

    /**
     * 是否已购买 AI 答疑
     */
    public boolean hasAiAnswerUnlock(Long userId) {
        return hasActiveFeatureOrder(userId, FeatureType.AI_ANSWER_UNLOCK);
    }

    /**
     * 是否已购买错题打印
     */
    public boolean hasWrongBookPrint(Long userId) {
        return hasActiveFeatureOrder(userId, FeatureType.WRONG_BOOK_PRINT);
    }

    private boolean hasLearningMembership(Long userId) {
        List<Membership> memberships = membershipRepository.findByUserId(userId);
        return memberships.stream().anyMatch(m -> m.getMemberType().isLearningType() && m.isActive());
    }

    private boolean hasActiveFeatureOrder(Long userId, FeatureType featureType) {
        List<FeatureOrder> orders = featureOrderRepository.findByUserId(userId);
        return orders.stream().anyMatch(o -> o.getFeatureType() == featureType && o.isPaid()
                && (!featureType.isTimeLimited() || o.getExpireTime().isAfter(java.time.LocalDateTime.now())));
    }
}
