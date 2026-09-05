package com.wzoto.interfaces.controller;

import com.wzoto.application.service.PlayProgressApplicationService;
import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.CourseWatchRecord;
import com.wzoto.interfaces.common.R;
import com.wzoto.interfaces.vo.learning.PlayProgressVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 播放进度控制器 - 断点续播 + 进度上报
 */
@Slf4j
@RestController
@RequestMapping("/api/learning/play-progress")
@RequiredArgsConstructor
public class PlayProgressController {

    private final PlayProgressApplicationService playProgressApplicationService;

    /**
     * 上报播放进度（每5秒调用一次）
     */
    @PostMapping
    public R<Boolean> reportProgress(@RequestBody Map<String, Object> body) {
        Long userId = UserContext.getCurrentUserId();
        Long childId = Long.valueOf(body.get("childId").toString());
        Long resourceId = Long.valueOf(body.get("resourceId").toString());
        Integer currentTime = Integer.valueOf(body.getOrDefault("currentTime", 0).toString());
        Integer duration = Integer.valueOf(body.getOrDefault("duration", 0).toString());
        Integer percent = Integer.valueOf(body.getOrDefault("percent", 0).toString());
        String eventType = (String) body.getOrDefault("eventType", "HEARTBEAT");

        log.info("[BI] play_progress|userId={}, childId={}, resourceId={}, percent={}, event={}",
                userId, childId, resourceId, percent, eventType);

        playProgressApplicationService.reportProgress(userId, childId, resourceId, currentTime, duration, percent, eventType);
        return R.ok(true);
    }

    /**
     * 获取断点位置（用于续播）
     */
    @GetMapping("/{resourceId}")
    public R<PlayProgressVO> getProgress(@RequestParam Long childId, @PathVariable Long resourceId) {
        CourseWatchRecord record = playProgressApplicationService.getProgress(childId, resourceId);
        if (record == null) {
            return R.ok(PlayProgressVO.builder()
                    .resourceId(resourceId)
                    .lastPositionSeconds(0)
                    .progressPercent(0)
                    .completed(false)
                    .watchDurationSeconds(0)
                    .build());
        }
        return R.ok(PlayProgressVO.builder()
                .resourceId(resourceId)
                .lastPositionSeconds(record.getLastPositionSeconds())
                .progressPercent(record.getProgressPercent())
                .completed(record.isCompleted())
                .watchDurationSeconds(record.getWatchDurationSeconds())
                .build());
    }
}
