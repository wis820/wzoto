package com.wzoto.interfaces.controller;

import com.wzoto.application.service.ParentControlApplicationService;
import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.ParentControlConfig;
import com.wzoto.interfaces.common.R;
import com.wzoto.interfaces.dto.learning.ControlConfigDTO;
import com.wzoto.interfaces.vo.learning.ControlConfigVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 家长管控控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/learning/control")
@RequiredArgsConstructor
public class ParentControlController {

    private final ParentControlApplicationService parentControlApplicationService;

    @GetMapping("/{childId}")
    public R<ControlConfigVO> getControlConfig(@PathVariable Long childId) {
        ParentControlConfig config = parentControlApplicationService.getConfig(childId);
        return R.ok(toVO(config));
    }

    @PostMapping("/{childId}")
    public R<ControlConfigVO> saveControlConfig(@PathVariable Long childId, @RequestBody ControlConfigDTO dto) {
        log.info("[BI] control_save|userId={}, childId={}", UserContext.getCurrentUserId(), childId);
        ParentControlConfig config = new ParentControlConfig();
        config.setDailyLimitMinutes(dto.getDailyLimitMinutes());
        config.setRestIntervalMinutes(dto.getRestIntervalMinutes());
        config.setForbiddenStartTime(dto.getForbiddenStartTime());
        config.setForbiddenEndTime(dto.getForbiddenEndTime());
        config.setEyeProtectionMode(dto.getEyeProtectionMode());
        config.setBlueLightFilter(dto.getBlueLightFilter());
        config.setPostureReminder(dto.getPostureReminder());
        ParentControlConfig saved = parentControlApplicationService.saveConfig(childId, config);
        return R.ok(toVO(saved));
    }

    @PostMapping("/{childId}/lock")
    public R<ControlConfigVO> lock(@PathVariable Long childId) {
        log.info("[BI] control_lock|userId={}, childId={}", UserContext.getCurrentUserId(), childId);
        ParentControlConfig config = parentControlApplicationService.lock(childId);
        return R.ok(toVO(config));
    }

    @PostMapping("/{childId}/unlock")
    public R<ControlConfigVO> unlock(@PathVariable Long childId) {
        log.info("[BI] control_unlock|userId={}, childId={}", UserContext.getCurrentUserId(), childId);
        ParentControlConfig config = parentControlApplicationService.unlock(childId);
        return R.ok(toVO(config));
    }

    private ControlConfigVO toVO(ParentControlConfig config) {
        return ControlConfigVO.builder()
                .id(config.getId())
                .parentId(config.getParentId())
                .childId(config.getChildId())
                .dailyLimitMinutes(config.getDailyLimitMinutes())
                .restIntervalMinutes(config.getRestIntervalMinutes())
                .forbiddenStartTime(config.getForbiddenStartTime())
                .forbiddenEndTime(config.getForbiddenEndTime())
                .locked(config.getLocked())
                .eyeProtectionMode(config.getEyeProtectionMode())
                .blueLightFilter(config.getBlueLightFilter())
                .postureReminder(config.getPostureReminder())
                .build();
    }
}
