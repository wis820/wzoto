package com.wzoto.application.service;

import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.ChildAchievement;
import com.wzoto.domain.service.ChildAchievementDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 成长激励应用服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChildAchievementApplicationService {

    private final ChildAchievementDomainService childAchievementDomainService;

    public List<ChildAchievement> getAchievements(Long childId, String achievementType) {
        Long parentId = UserContext.getCurrentUserId();
        return childAchievementDomainService.findByChildId(parentId, childId, achievementType);
    }

    @Transactional(rollbackFor = Exception.class)
    public ChildAchievement grantMedal(Long childId, String achievementCode, String achievementName, String iconUrl) {
        Long parentId = UserContext.getCurrentUserId();
        return childAchievementDomainService.grantMedal(parentId, childId, achievementCode, achievementName, iconUrl);
    }

    @Transactional(rollbackFor = Exception.class)
    public ChildAchievement grantPoints(Long childId, Integer points, String achievementName) {
        Long parentId = UserContext.getCurrentUserId();
        return childAchievementDomainService.grantPoints(parentId, childId, points, achievementName);
    }

    @Transactional(rollbackFor = Exception.class)
    public ChildAchievement assignTask(Long childId, String achievementName, String taskDescription, Integer points) {
        Long parentId = UserContext.getCurrentUserId();
        return childAchievementDomainService.assignTask(parentId, childId, achievementName, taskDescription, points);
    }

    @Transactional(rollbackFor = Exception.class)
    public ChildAchievement completeTask(Long achievementId) {
        Long parentId = UserContext.getCurrentUserId();
        return childAchievementDomainService.completeTask(parentId, achievementId);
    }
}
