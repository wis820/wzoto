package com.wzoto.application.service;

import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.ParentControlConfig;
import com.wzoto.domain.service.ParentControlDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 家长管控应用服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ParentControlApplicationService {

    private final ParentControlDomainService parentControlDomainService;

    public ParentControlConfig getConfig(Long childId) {
        Long parentId = UserContext.getCurrentUserId();
        return parentControlDomainService.getOrCreateDefault(parentId, childId);
    }

    @Transactional(rollbackFor = Exception.class)
    public ParentControlConfig saveConfig(Long childId, ParentControlConfig config) {
        Long parentId = UserContext.getCurrentUserId();
        return parentControlDomainService.saveConfig(parentId, childId, config);
    }

    @Transactional(rollbackFor = Exception.class)
    public ParentControlConfig lock(Long childId) {
        Long parentId = UserContext.getCurrentUserId();
        return parentControlDomainService.lock(parentId, childId);
    }

    @Transactional(rollbackFor = Exception.class)
    public ParentControlConfig unlock(Long childId) {
        Long parentId = UserContext.getCurrentUserId();
        return parentControlDomainService.unlock(parentId, childId);
    }
}
