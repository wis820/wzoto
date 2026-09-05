package com.wzoto.domain.service;

import com.wzoto.domain.entity.Child;
import com.wzoto.domain.entity.User;
import com.wzoto.domain.repository.ChildRepository;
import com.wzoto.domain.repository.UserRepository;
import com.wzoto.domain.valobj.GradeType;
import com.wzoto.domain.valobj.IdentityType;
import com.wzoto.domain.valobj.TextbookVersion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 子女领域服务 - 封装子女增删改查与家长归属权校验
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChildDomainService {

    private final ChildRepository childRepository;
    private final UserRepository userRepository;

    /**
     * 新增子女
     */
    public Child createChild(Long parentId, String name, String gradeCode, String textbookVersionCode) {
        User parent = userRepository.findById(parentId);
        if (parent == null || parent.getIdentityType() != IdentityType.PARENT) {
            throw new IllegalArgumentException("仅家长可创建子女档案");
        }
        GradeType grade = GradeType.fromCode(gradeCode);
        TextbookVersion textbookVersion = TextbookVersion.fromCode(textbookVersionCode);
        Child child = Child.create(parentId, name, grade, textbookVersion);
        Child saved = childRepository.save(child);
        log.info("子女档案创建成功, parentId={}, childId={}", parentId, saved.getId());
        return saved;
    }

    /**
     * 更新子女信息
     */
    public Child updateChild(Long parentId, Long childId, String name, String gradeCode,
                             String textbookVersionCode, String school, String avatar) {
        Child child = findAndValidate(parentId, childId);
        GradeType grade = gradeCode != null ? GradeType.fromCode(gradeCode) : null;
        TextbookVersion textbookVersion = textbookVersionCode != null ? TextbookVersion.fromCode(textbookVersionCode) : null;
        child.update(name, grade, textbookVersion, school, avatar);
        return childRepository.update(child);
    }

    /**
     * 删除子女
     */
    public boolean deleteChild(Long parentId, Long childId) {
        findAndValidate(parentId, childId);
        boolean result = childRepository.delete(childId);
        log.info("子女档案删除成功, parentId={}, childId={}", parentId, childId);
        return result;
    }

    /**
     * 查询家长下子女列表
     */
    public List<Child> findByParentId(Long parentId) {
        return childRepository.findByParentId(parentId);
    }

    /**
     * 查询子女详情并校验归属权
     */
    public Child findAndValidate(Long parentId, Long childId) {
        Child child = childRepository.findById(childId);
        if (child == null) {
            throw new IllegalArgumentException("子女档案不存在");
        }
        if (!child.belongsTo(parentId)) {
            throw new IllegalArgumentException("无权操作该子女档案");
        }
        return child;
    }
}
