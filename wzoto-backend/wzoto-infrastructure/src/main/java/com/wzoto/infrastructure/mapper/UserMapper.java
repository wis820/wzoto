package com.wzoto.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzoto.infrastructure.pojo.UserPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper - MyBatis-Plus
 */
@Mapper
public interface UserMapper extends BaseMapper<UserPO> {
}