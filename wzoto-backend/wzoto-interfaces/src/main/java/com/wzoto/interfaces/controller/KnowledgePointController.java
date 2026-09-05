package com.wzoto.interfaces.controller;

import com.wzoto.application.service.KnowledgePointApplicationService;
import com.wzoto.domain.entity.KnowledgePoint;
import com.wzoto.interfaces.common.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 知识点控制器 */
@Slf4j
@RestController
@RequestMapping("/api/knowledge")
@RequiredArgsConstructor
public class KnowledgePointController {

    private final KnowledgePointApplicationService knowledgePointApplicationService;

    @GetMapping("/tree")
    public R<List<KnowledgePoint>> getTree(@RequestParam(required = false) String grade,
                                            @RequestParam(required = false) String subject,
                                            @RequestParam(required = false) String textbookVersion) {
        return R.ok(knowledgePointApplicationService.getTree(grade, subject, textbookVersion));
    }

    @GetMapping("/children/{parentId}")
    public R<List<KnowledgePoint>> getChildren(@PathVariable Long parentId) {
        return R.ok(knowledgePointApplicationService.getChildren(parentId));
    }

    @GetMapping("/{id}")
    public R<KnowledgePoint> getById(@PathVariable Long id) {
        return R.ok(knowledgePointApplicationService.getById(id));
    }
}
