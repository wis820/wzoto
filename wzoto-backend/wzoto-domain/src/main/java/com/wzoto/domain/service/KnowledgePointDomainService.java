package com.wzoto.domain.service;

import com.wzoto.domain.entity.KnowledgePoint;
import com.wzoto.domain.repository.KnowledgePointRepository;
import com.wzoto.domain.valobj.GradeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/** 知识点领域服务 - 知识点树查询、掌握度统计、图谱构建 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KnowledgePointDomainService {

    private final KnowledgePointRepository knowledgePointRepository;

    public List<KnowledgePoint> getTree(GradeType grade, String subject, String textbookVersion) {
        return knowledgePointRepository.findTree(grade, subject, textbookVersion);
    }

    public List<KnowledgePoint> getChildren(Long parentId) {
        return knowledgePointRepository.findByParentId(parentId);
    }

    public KnowledgePoint getById(Long id) {
        KnowledgePoint kp = knowledgePointRepository.findById(id);
        if (kp == null) {
            throw new IllegalArgumentException("知识点不存在");
        }
        return kp;
    }

    public List<KnowledgePoint> findByGradeAndSubject(GradeType grade, String subject) {
        return knowledgePointRepository.findByGradeAndSubject(grade, subject);
    }
}
