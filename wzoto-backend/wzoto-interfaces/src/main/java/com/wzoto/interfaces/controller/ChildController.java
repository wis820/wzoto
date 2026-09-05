package com.wzoto.interfaces.controller;

import com.wzoto.application.service.ChildApplicationService;
import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.Child;
import com.wzoto.domain.valobj.GradeType;
import com.wzoto.domain.valobj.TextbookVersion;
import com.wzoto.interfaces.common.R;
import com.wzoto.interfaces.dto.child.CreateChildDTO;
import com.wzoto.interfaces.dto.child.UpdateChildDTO;
import com.wzoto.interfaces.vo.child.ChildVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 子女档案控制器
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChildController {

    private final ChildApplicationService childApplicationService;

    @PostMapping("/child")
    public R<ChildVO> createChild(@Valid @RequestBody CreateChildDTO dto) {
        log.info("[BI] child_create|userId={}", UserContext.getCurrentUserId());
        Child child = childApplicationService.createChild(dto.getName(), dto.getGrade(), dto.getTextbookVersion());
        return R.ok(toVO(child));
    }

    @PutMapping("/child/{id}")
    public R<ChildVO> updateChild(@PathVariable Long id, @RequestBody UpdateChildDTO dto) {
        Child child = childApplicationService.updateChild(id, dto.getName(), dto.getGrade(),
                dto.getTextbookVersion(), dto.getSchool(), dto.getAvatar());
        return R.ok(toVO(child));
    }

    @DeleteMapping("/child/{id}")
    public R<Boolean> deleteChild(@PathVariable Long id) {
        return R.ok(childApplicationService.deleteChild(id));
    }

    @GetMapping("/children")
    public R<List<ChildVO>> getMyChildren() {
        List<Child> children = childApplicationService.getMyChildren();
        return R.ok(children.stream().map(this::toVO).collect(Collectors.toList()));
    }

    @GetMapping("/child/{id}")
    public R<ChildVO> getChildDetail(@PathVariable Long id) {
        Child child = childApplicationService.getChildDetail(id);
        return R.ok(toVO(child));
    }

    private ChildVO toVO(Child child) {
        return ChildVO.builder()
                .id(child.getId())
                .parentId(child.getParentId())
                .name(child.getName())
                .grade(child.getGrade() != null ? child.getGrade().getCode() : null)
                .gradeDesc(child.getGrade() != null ? child.getGrade().getDesc() : null)
                .textbookVersion(child.getTextbookVersion() != null ? child.getTextbookVersion().getCode() : null)
                .textbookVersionDesc(child.getTextbookVersion() != null ? child.getTextbookVersion().getDesc() : null)
                .school(child.getSchool())
                .avatar(child.getAvatar())
                .createdAt(child.getCreatedAt())
                .build();
    }
}
