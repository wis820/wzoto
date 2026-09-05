package com.wzoto.application.service;

import com.wzoto.domain.entity.KnowledgePoint;
import com.wzoto.domain.service.KnowledgePointDomainService;
import com.wzoto.domain.valobj.GradeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/** 知识点应用服务 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KnowledgePointApplicationService {

    private final KnowledgePointDomainService knowledgePointDomainService;

    public List<KnowledgePoint> getTree(String gradeCode, String subject, String textbookVersion) {
        GradeType grade = gradeCode != null ? GradeType.fromCode(gradeCode) : null;
        return knowledgePointDomainService.getTree(grade, subject, textbookVersion);
    }

    public List<KnowledgePoint> getChildren(Long parentId) {
        return knowledgePointDomainService.getChildren(parentId);
    }

    public KnowledgePoint getById(Long id) {
        return knowledgePointDomainService.getById(id);
    }
}
