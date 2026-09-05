package com.wzoto.interfaces.controller;

import com.wzoto.application.service.ChildAchievementApplicationService;
import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.ChildAchievement;
import com.wzoto.interfaces.common.R;
import com.wzoto.interfaces.vo.learning.AchievementVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 成长激励控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/learning")
@RequiredArgsConstructor
public class ChildAchievementController {

    private final ChildAchievementApplicationService childAchievementApplicationService;

    @GetMapping("/achievements/{childId}")
    public R<List<AchievementVO>> listAchievements(@PathVariable Long childId,
                                                   @RequestParam(required = false) String achievementType) {
        log.info("[BI] achievement_list|userId={}, childId={}", UserContext.getCurrentUserId(), childId);
        List<ChildAchievement> achievements = childAchievementApplicationService.getAchievements(childId, achievementType);
        return R.ok(achievements.stream().map(this::toVO).collect(Collectors.toList()));
    }

    @PostMapping("/achievements/{childId}/task")
    public R<AchievementVO> assignTask(@PathVariable Long childId,
                                       @RequestParam String achievementName,
                                       @RequestParam String taskDescription,
                                       @RequestParam(required = false, defaultValue = "0") Integer points) {
        log.info("[BI] achievement_assign_task|userId={}, childId={}", UserContext.getCurrentUserId(), childId);
        ChildAchievement achievement = childAchievementApplicationService.assignTask(childId, achievementName, taskDescription, points);
        return R.ok(toVO(achievement));
    }

    private AchievementVO toVO(ChildAchievement achievement) {
        return AchievementVO.builder()
                .id(achievement.getId())
                .childId(achievement.getChildId())
                .achievementType(achievement.getAchievementType())
                .achievementCode(achievement.getAchievementCode())
                .achievementName(achievement.getAchievementName())
                .iconUrl(achievement.getIconUrl())
                .points(achievement.getPoints())
                .skinCode(achievement.getSkinCode())
                .taskDescription(achievement.getTaskDescription())
                .taskStatus(achievement.getTaskStatus())
                .obtainedAt(achievement.getObtainedAt())
                .build();
    }
}
