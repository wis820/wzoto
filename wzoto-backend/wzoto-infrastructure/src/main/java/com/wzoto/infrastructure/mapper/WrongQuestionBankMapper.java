package com.wzoto.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzoto.infrastructure.pojo.WrongQuestionBankPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 错题库Mapper - MyBatis-Plus
 */
@Mapper
public interface WrongQuestionBankMapper extends BaseMapper<WrongQuestionBankPO> {

    @Update("UPDATE t_wrong_question_bank SET in_review_plan = 1, review_plan_tag = #{reviewPlanTag}, updated_at = NOW() " +
            "WHERE id IN (${ids}) AND deleted = 0")
    int batchUpdateReviewPlan(@Param("ids") String ids, @Param("reviewPlanTag") String reviewPlanTag);
}
