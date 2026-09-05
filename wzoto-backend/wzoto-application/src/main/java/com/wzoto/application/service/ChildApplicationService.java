package com.wzoto.application.service;

import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.Child;
import com.wzoto.domain.service.ChildDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 子女档案应用服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChildApplicationService {

    private final ChildDomainService childDomainService;

    public Child createChild(String name, String gradeCode, String textbookVersionCode) {
        Long parentId = UserContext.getCurrentUserId();
        return childDomainService.createChild(parentId, name, gradeCode, textbookVersionCode);
    }

    public Child updateChild(Long childId, String name, String gradeCode,
                             String textbookVersionCode, String school, String avatar) {
        Long parentId = UserContext.getCurrentUserId();
        return childDomainService.updateChild(parentId, childId, name, gradeCode, textbookVersionCode, school, avatar);
    }

    public boolean deleteChild(Long childId) {
        Long parentId = UserContext.getCurrentUserId();
        return childDomainService.deleteChild(parentId, childId);
    }

    public List<Child> getMyChildren() {
        Long parentId = UserContext.getCurrentUserId();
        return childDomainService.findByParentId(parentId);
    }

    public Child getChildDetail(Long childId) {
        Long parentId = UserContext.getCurrentUserId();
        return childDomainService.findAndValidate(parentId, childId);
    }
}
