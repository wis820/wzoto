package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.CourseWatchRecord;
import com.wzoto.domain.repository.CourseWatchRecordRepository;
import com.wzoto.infrastructure.mapper.CourseWatchRecordMapper;
import com.wzoto.infrastructure.pojo.CourseWatchRecordPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 课程观看记录仓储实现 - 基础设施层
 */
@Repository
@RequiredArgsConstructor
public class CourseWatchRecordRepositoryImpl implements CourseWatchRecordRepository {

    private final CourseWatchRecordMapper courseWatchRecordMapper;

    @Override
    public CourseWatchRecord save(CourseWatchRecord record) {
        CourseWatchRecordPO po = toPO(record);
        courseWatchRecordMapper.insert(po);
        record.setId(po.getId());
        return record;
    }

    @Override
    public CourseWatchRecord findById(Long id) {
        CourseWatchRecordPO po = courseWatchRecordMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public CourseWatchRecord findByChildIdAndResourceId(Long childId, Long resourceId) {
        LambdaQueryWrapper<CourseWatchRecordPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseWatchRecordPO::getChildId, childId)
                .eq(CourseWatchRecordPO::getResourceId, resourceId)
                .last("LIMIT 1");
        CourseWatchRecordPO po = courseWatchRecordMapper.selectOne(wrapper);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public List<CourseWatchRecord> findByChildId(Long childId) {
        LambdaQueryWrapper<CourseWatchRecordPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseWatchRecordPO::getChildId, childId).orderByDesc(CourseWatchRecordPO::getLastWatchTime);
        return courseWatchRecordMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public CourseWatchRecord update(CourseWatchRecord record) {
        CourseWatchRecordPO po = toPO(record);
        courseWatchRecordMapper.updateById(po);
        return record;
    }

    private CourseWatchRecord toEntity(CourseWatchRecordPO po) {
        CourseWatchRecord entity = new CourseWatchRecord();
        entity.setId(po.getId());
        entity.setChildId(po.getChildId());
        entity.setResourceId(po.getResourceId());
        entity.setWatchDurationSeconds(po.getWatchDurationSeconds());
        entity.setProgressPercent(po.getProgressPercent());
        entity.setLastPositionSeconds(po.getLastPositionSeconds());
        entity.setCompleted(po.getCompleted());
        entity.setFirstWatchTime(po.getFirstWatchTime());
        entity.setLastWatchTime(po.getLastWatchTime());
        entity.setDeleted(po.getDeleted());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private CourseWatchRecordPO toPO(CourseWatchRecord entity) {
        CourseWatchRecordPO po = new CourseWatchRecordPO();
        po.setId(entity.getId());
        po.setChildId(entity.getChildId());
        po.setResourceId(entity.getResourceId());
        po.setWatchDurationSeconds(entity.getWatchDurationSeconds());
        po.setProgressPercent(entity.getProgressPercent());
        po.setLastPositionSeconds(entity.getLastPositionSeconds());
        po.setCompleted(entity.getCompleted());
        po.setFirstWatchTime(entity.getFirstWatchTime());
        po.setLastWatchTime(entity.getLastWatchTime());
        po.setDeleted(entity.getDeleted());
        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        return po;
    }
}
