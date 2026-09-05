package com.wzoto.domain.repository;

import com.wzoto.domain.entity.Child;

import java.util.List;

/**
 * 子女档案仓储接口 - 领域层定义，基础设施层实现
 */
public interface ChildRepository {

    Child save(Child child);

    Child findById(Long id);

    List<Child> findByParentId(Long parentId);

    Child update(Child child);

    boolean delete(Long id);
}
