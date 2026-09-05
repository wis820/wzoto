package com.wzoto.domain.repository;

import com.wzoto.domain.entity.CourseWatchRecord;

import java.util.List;

/**
 * 课程观看记录仓储接口 - 领域层定义，基础设施层实现
 */
public interface CourseWatchRecordRepository {

    CourseWatchRecord save(CourseWatchRecord record);

    CourseWatchRecord findById(Long id);

    CourseWatchRecord findByChildIdAndResourceId(Long childId, Long resourceId);

    List<CourseWatchRecord> findByChildId(Long childId);

    CourseWatchRecord update(CourseWatchRecord record);
}
