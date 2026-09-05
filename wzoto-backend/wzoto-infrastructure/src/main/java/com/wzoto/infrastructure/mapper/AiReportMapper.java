package com.wzoto.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzoto.infrastructure.pojo.AiReportPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AiReportMapper extends BaseMapper<AiReportPO> {

    /**
     * 统计用户当月免费报告数量
     */
    @Select("SELECT COUNT(*) FROM t_ai_report WHERE user_id = #{userId} AND is_member_report = 1 AND payment_status = 'FREE' AND YEAR(created_at) = YEAR(NOW()) AND MONTH(created_at) = MONTH(NOW()) AND deleted = 0")
    int countFreeReportsThisMonth(Long userId);
}
