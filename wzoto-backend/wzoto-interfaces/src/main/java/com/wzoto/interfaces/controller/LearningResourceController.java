package com.wzoto.interfaces.controller;

import com.wzoto.application.service.LearningResourceApplicationService;
import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.FeatureOrder;
import com.wzoto.domain.entity.LearningResource;
import com.wzoto.interfaces.common.R;
import com.wzoto.interfaces.vo.learning.LearningResourceVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 学习资源控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/learning")
@RequiredArgsConstructor
public class LearningResourceController {

    private final LearningResourceApplicationService learningResourceApplicationService;

    @GetMapping("/resources")
    public R<List<LearningResourceVO>> listResources(@RequestParam Long childId,
                                                     @RequestParam(required = false) String subject,
                                                     @RequestParam(required = false) String resourceType,
                                                     @RequestParam(required = false) Boolean includeVip) {
        log.info("[BI] resource_list|userId={}, childId={}", UserContext.getCurrentUserId(), childId);
        List<LearningResource> resources = learningResourceApplicationService.listResources(childId, subject, resourceType, includeVip);
        return R.ok(resources.stream().map(this::toVO).collect(Collectors.toList()));
    }

    @GetMapping("/resource/{id}")
    public R<LearningResourceVO> getResourceDetail(@PathVariable Long id) {
        LearningResource resource = learningResourceApplicationService.getResourceDetail(id);
        return R.ok(toVO(resource));
    }

    @PostMapping("/resource/{id}/unlock")
    public R<FeatureOrder> unlockResource(@PathVariable Long id) {
        log.info("[BI] resource_unlock|userId={}, resourceId={}", UserContext.getCurrentUserId(), id);
        FeatureOrder order = learningResourceApplicationService.unlockResource(id);
        if (order == null) {
            return R.ok();
        }
        return R.ok(order);
    }

    private LearningResourceVO toVO(LearningResource resource) {
        return LearningResourceVO.builder()
                .id(resource.getId())
                .grade(resource.getGrade() != null ? resource.getGrade().getCode() : null)
                .gradeDesc(resource.getGrade() != null ? resource.getGrade().getDesc() : null)
                .subject(resource.getSubject())
                .textbookVersion(resource.getTextbookVersion() != null ? resource.getTextbookVersion().getCode() : null)
                .resourceType(resource.getResourceType() != null ? resource.getResourceType().getCode() : null)
                .resourceTypeDesc(resource.getResourceType() != null ? resource.getResourceType().getDesc() : null)
                .title(resource.getTitle())
                .coverUrl(resource.getCoverUrl())
                .contentUrl(resource.getContentUrl())
                .durationSeconds(resource.getDurationSeconds())
                .knowledgePoint(resource.getKnowledgePoint())
                .tags(resource.getTags())
                .sourceType(resource.getSourceType())
                .subtitleUrl(resource.getSubtitleUrl())
                .qualityLevels(resource.getQualityLevels())
                .knowledgeMarkers(resource.getKnowledgeMarkers())
                .status(resource.getStatus())
                .description(resource.getDescription())
                .vipOnly(resource.isVipOnly())
                .createdAt(resource.getCreatedAt())
                .build();
    }
}
