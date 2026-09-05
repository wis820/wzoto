package com.wzoto.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzoto.infrastructure.pojo.LearningTaskPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 学习任务记录Mapper - MyBatis-Plus
 */
@Mapper
public interface LearningTaskMapper extends BaseMapper<LearningTaskPO> {
}
