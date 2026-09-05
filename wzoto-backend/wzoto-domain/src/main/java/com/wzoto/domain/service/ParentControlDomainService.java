package com.wzoto.domain.service;

import com.wzoto.domain.entity.Child;
import com.wzoto.domain.entity.ParentControlConfig;
import com.wzoto.domain.repository.ChildRepository;
import com.wzoto.domain.repository.ParentControlConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

/**
 * 家长管控领域服务 - 时长管控、锁定、护眼模式规则
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ParentControlDomainService {

    private final ParentControlConfigRepository controlConfigRepository;
    private final ChildRepository childRepository;

    /**
     * 获取或创建默认管控配置
     */
    public ParentControlConfig getOrCreateDefault(Long parentId, Long childId) {
        validateOwnership(parentId, childId);
        ParentControlConfig config = controlConfigRepository.findByChildId(childId);
        if (config == null) {
            config = ParentControlConfig.createDefault(parentId, childId);
            config = controlConfigRepository.save(config);
            log.info("创建默认管控配置, parentId={}, childId={}", parentId, childId);
        }
        return config;
    }

    /**
     * 保存管控配置
     */
    public ParentControlConfig saveConfig(Long parentId, Long childId, ParentControlConfig config) {
        validateOwnership(parentId, childId);
        ParentControlConfig existing = controlConfigRepository.findByChildId(childId);
        if (existing == null) {
            config.setParentId(parentId);
            config.setChildId(childId);
            return controlConfigRepository.save(config);
        }
        if (config.getDailyLimitMinutes() != null) {
            existing.updateTimeLimit(config.getDailyLimitMinutes());
        }
        if (config.getRestIntervalMinutes() != null) {
            existing.updateRestInterval(config.getRestIntervalMinutes());
        }
        existing.updateForbiddenTime(config.getForbiddenStartTime(), config.getForbiddenEndTime());
        existing.setEyeProtectionMode(config.getEyeProtectionMode());
        existing.setBlueLightFilter(config.getBlueLightFilter());
        existing.setPostureReminder(config.getPostureReminder());
        return controlConfigRepository.update(existing);
    }

    /**
     * 一键锁定
     */
    public ParentControlConfig lock(Long parentId, Long childId) {
        validateOwnership(parentId, childId);
        ParentControlConfig config = getOrCreateDefault(parentId, childId);
        config.lock();
        return controlConfigRepository.update(config);
    }

    /**
     * 解除锁定
     */
    public ParentControlConfig unlock(Long parentId, Long childId) {
        validateOwnership(parentId, childId);
        ParentControlConfig config = getOrCreateDefault(parentId, childId);
        config.unlock();
        return controlConfigRepository.update(config);
    }

    /**
     * 切换护眼模式
     */
    public ParentControlConfig toggleEyeProtection(Long parentId, Long childId) {
        validateOwnership(parentId, childId);
        ParentControlConfig config = getOrCreateDefault(parentId, childId);
        config.toggleEyeProtection();
        return controlConfigRepository.update(config);
    }

    /**
     * 检查当前是否在禁用时段
     */
    public boolean isInForbiddenTime(Long childId) {
        ParentControlConfig config = controlConfigRepository.findByChildId(childId);
        if (config == null || config.getForbiddenStartTime() == null || config.getForbiddenEndTime() == null) {
            return false;
        }
        LocalTime now = LocalTime.now();
        LocalTime start = LocalTime.parse(config.getForbiddenStartTime());
        LocalTime end = LocalTime.parse(config.getForbiddenEndTime());
        if (start.isBefore(end)) {
            return !now.isBefore(start) && now.isBefore(end);
        } else {
            return !now.isBefore(start) || now.isBefore(end);
        }
    }

    /**
     * 检查是否超出每日可用时长
     */
    public boolean isExceedDailyLimit(Long childId, int usedMinutes) {
        ParentControlConfig config = controlConfigRepository.findByChildId(childId);
        if (config == null || config.getDailyLimitMinutes() == null) {
            return false;
        }
        return usedMinutes >= config.getDailyLimitMinutes();
    }

    private void validateOwnership(Long parentId, Long childId) {
        Child child = childRepository.findById(childId);
        if (child == null || !child.belongsTo(parentId)) {
            throw new IllegalArgumentException("无权操作该子女档案");
        }
    }
}
