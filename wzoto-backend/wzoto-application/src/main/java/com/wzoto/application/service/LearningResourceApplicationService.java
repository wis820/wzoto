package com.wzoto.application.service;

import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.FeatureOrder;
import com.wzoto.domain.entity.LearningResource;
import com.wzoto.domain.service.LearningResourceDomainService;
import com.wzoto.domain.valobj.LearningResourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 学习资源应用服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LearningResourceApplicationService {

    private final LearningResourceDomainService learningResourceDomainService;

    public List<LearningResource> listResources(Long childId, String subject,
                                                String resourceTypeCode, Boolean includeVip) {
        Long parentId = UserContext.getCurrentUserId();
        LearningResourceType resourceType = resourceTypeCode != null ? LearningResourceType.fromCode(resourceTypeCode) : null;
        return learningResourceDomainService.listResources(parentId, childId, subject, resourceType, includeVip);
    }

    public LearningResource getResourceDetail(Long resourceId) {
        return learningResourceDomainService.getResourceDetail(resourceId);
    }

    public FeatureOrder unlockResource(Long resourceId) {
        Long userId = UserContext.getCurrentUserId();
        return learningResourceDomainService.unlockResource(userId, resourceId);
    }
}
