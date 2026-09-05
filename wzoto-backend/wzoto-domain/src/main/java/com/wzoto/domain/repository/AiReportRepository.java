package com.wzoto.domain.repository;

import com.wzoto.domain.entity.AiReport;

import java.util.List;

/**
 * AI学情诊断报告仓储接口
 */
public interface AiReportRepository {

    AiReport save(AiReport report);

    AiReport findById(Long id);

    /** 查询用户的所有报告 */
    List<AiReport> findByUserId(Long userId);

    /** 查询用户当月免费报告数量（会员每月2份免费） */
    int countFreeReportsThisMonth(Long userId);

    AiReport update(AiReport report);
}
