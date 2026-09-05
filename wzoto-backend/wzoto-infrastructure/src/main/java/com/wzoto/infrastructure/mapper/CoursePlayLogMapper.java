package com.wzoto.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzoto.infrastructure.pojo.CoursePlayLogPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 播放明细日志Mapper
 */
@Mapper
public interface CoursePlayLogMapper extends BaseMapper<CoursePlayLogPO> {
}
