package com.wzoto.domain.repository;

import com.wzoto.domain.entity.ChildAchievement;

import java.util.List;

/**
 * 成长激励仓储接口 - 领域层定义，基础设施层实现
 */
public interface ChildAchievementRepository {

    ChildAchievement save(ChildAchievement achievement);

    ChildAchievement findById(Long id);

    List<ChildAchievement> findByChildId(Long childId);

    List<ChildAchievement> findByChildIdAndType(Long childId, String achievementType);

    ChildAchievement update(ChildAchievement achievement);
}
