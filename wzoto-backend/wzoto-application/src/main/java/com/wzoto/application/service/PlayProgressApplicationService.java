package com.wzoto.application.service;

import com.wzoto.domain.entity.CourseWatchRecord;
import com.wzoto.domain.repository.CourseWatchRecordRepository;
import com.wzoto.infrastructure.mapper.CoursePlayLogMapper;
import com.wzoto.infrastructure.pojo.CoursePlayLogPO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 播放进度应用服务
 * 负责断点续播 + 进度上报 + 播放日志
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PlayProgressApplicationService {

    private final CourseWatchRecordRepository courseWatchRecordRepository;
    private final CoursePlayLogMapper coursePlayLogMapper;

    /**
     * 上报播放进度
     * 1. 创建或更新 t_course_watch_record（汇总）
     * 2. 插入 t_course_play_log（明细）
     */
    public void reportProgress(Long userId, Long childId, Long resourceId,
                               Integer currentTime, Integer duration, Integer percent,
                               String eventType) {
        // 1. 更新汇总记录
        CourseWatchRecord record = courseWatchRecordRepository.findByChildIdAndResourceId(childId, resourceId);
        if (record == null) {
            record = CourseWatchRecord.create(childId, resourceId);
            record.recordProgress(currentTime, currentTime);
            record.updateProgressPercent(percent);
            courseWatchRecordRepository.save(record);
        } else {
            int delta = Math.max(0, currentTime - record.getLastPositionSeconds());
            record.recordProgress(
                    record.getWatchDurationSeconds() + delta,
                    currentTime
            );
            record.updateProgressPercent(Math.max(record.getProgressPercent(), percent));
            courseWatchRecordRepository.update(record);
        }

        // 2. 插入播放日志明细
        CoursePlayLogPO playLog = new CoursePlayLogPO();
        playLog.setUserId(userId);
        playLog.setChildId(childId);
        playLog.setResourceId(resourceId);
        playLog.setSessionId(UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        playLog.setCurrentTime(currentTime);
        playLog.setDuration(duration);
        playLog.setPercent(percent);
        playLog.setPlaybackRate(BigDecimal.ONE);
        playLog.setEventType(eventType != null ? eventType : "HEARTBEAT");
        playLog.setCreatedAt(LocalDateTime.now());
        playLog.setDeleted(false);
        coursePlayLogMapper.insert(playLog);
    }

    /**
     * 获取断点位置（用于续播）
     * @return CourseWatchRecord 或 null
     */
    public CourseWatchRecord getProgress(Long childId, Long resourceId) {
        return courseWatchRecordRepository.findByChildIdAndResourceId(childId, resourceId);
    }
}
