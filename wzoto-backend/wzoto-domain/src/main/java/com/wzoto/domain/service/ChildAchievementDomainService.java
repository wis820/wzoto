package com.wzoto.domain.service;

import com.wzoto.domain.entity.Child;
import com.wzoto.domain.entity.ChildAchievement;
import com.wzoto.domain.repository.ChildAchievementRepository;
import com.wzoto.domain.repository.ChildRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 成长激励领域服务 - 勋章、积分、皮肤记录与家长下发任务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChildAchievementDomainService {

    private final ChildAchievementRepository achievementRepository;
    private final ChildRepository childRepository;

    /**
     * 查询子女成长激励记录
     */
    public List<ChildAchievement> findByChildId(Long parentId, Long childId, String achievementType) {
        validateOwnership(parentId, childId);
        if (achievementType != null && !achievementType.isBlank()) {
            return achievementRepository.findByChildIdAndType(childId, achievementType);
        }
        return achievementRepository.findByChildId(childId);
    }

    /**
     * 授予勋章
     */
    public ChildAchievement grantMedal(Long parentId, Long childId, String achievementCode,
                                       String achievementName, String iconUrl) {
        validateOwnership(parentId, childId);
        ChildAchievement achievement = ChildAchievement.createMedal(childId, achievementCode, achievementName, iconUrl);
        ChildAchievement saved = achievementRepository.save(achievement);
        log.info("授予勋章, parentId={}, childId={}, medal={}", parentId, childId, achievementCode);
        return saved;
    }

    /**
     * 发放积分
     */
    public ChildAchievement grantPoints(Long parentId, Long childId, Integer points, String achievementName) {
        validateOwnership(parentId, childId);
        if (points == null || points <= 0) {
            throw new IllegalArgumentException("积分必须大于 0");
        }
        ChildAchievement achievement = ChildAchievement.createPoints(childId, points, achievementName);
        return achievementRepository.save(achievement);
    }

    /**
     * 解锁皮肤
     */
    public ChildAchievement unlockSkin(Long parentId, Long childId, String skinCode,
                                       String achievementName, String iconUrl) {
        validateOwnership(parentId, childId);
        ChildAchievement achievement = ChildAchievement.createSkin(childId, skinCode, achievementName, iconUrl);
        return achievementRepository.save(achievement);
    }

    /**
     * 家长下发激励任务
     */
    public ChildAchievement assignTask(Long parentId, Long childId, String achievementName,
                                       String taskDescription, Integer points) {
        validateOwnership(parentId, childId);
        ChildAchievement achievement = ChildAchievement.createTask(childId, achievementName, taskDescription, points);
        ChildAchievement saved = achievementRepository.save(achievement);
        log.info("家长下发激励任务, parentId={}, childId={}, task={}", parentId, childId, achievementName);
        return saved;
    }

    /**
     * 完成激励任务
     */
    public ChildAchievement completeTask(Long parentId, Long achievementId) {
        ChildAchievement achievement = achievementRepository.findById(achievementId);
        if (achievement == null) {
            throw new IllegalArgumentException("激励记录不存在");
        }
        validateOwnership(parentId, achievement.getChildId());
        achievement.completeTask();
        return achievementRepository.update(achievement);
    }

    private void validateOwnership(Long parentId, Long childId) {
        Child child = childRepository.findById(childId);
        if (child == null || !child.belongsTo(parentId)) {
            throw new IllegalArgumentException("无权操作该子女档案");
        }
    }
}
