package com.wzoto.domain.repository;

import com.wzoto.domain.entity.Subject;
import java.util.List;

/** 学科仓储接口 */
public interface SubjectRepository {
    List<Subject> findAll();
    Subject findByCode(String code);
}
