package com.wzoto.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzoto.infrastructure.pojo.MembershipPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MembershipMapper extends BaseMapper<MembershipPO> {
}
