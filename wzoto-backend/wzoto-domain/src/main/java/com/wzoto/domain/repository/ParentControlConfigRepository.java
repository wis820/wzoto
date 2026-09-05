package com.wzoto.domain.repository;

import com.wzoto.domain.entity.ParentControlConfig;

/**
 * 家长管控配置仓储接口 - 领域层定义，基础设施层实现
 */
public interface ParentControlConfigRepository {

    ParentControlConfig save(ParentControlConfig config);

    ParentControlConfig findById(Long id);

    ParentControlConfig findByChildId(Long childId);

    ParentControlConfig update(ParentControlConfig config);
}
