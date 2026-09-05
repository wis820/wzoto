package com.wzoto.interfaces.controller;

import com.wzoto.application.service.TutorProfileApplicationService;
import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.TutorProfile;
import com.wzoto.domain.entity.User;
import com.wzoto.domain.repository.UserRepository;
import com.wzoto.interfaces.common.R;
import com.wzoto.interfaces.dto.profile.TutorProfileCreateDTO;
import com.wzoto.interfaces.dto.profile.TutorProfileUpdateDTO;
import com.wzoto.interfaces.vo.profile.TutorProfileVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/tutor-profile")
@RequiredArgsConstructor
public class TutorProfileController {

    private final TutorProfileApplicationService tutorProfileApplicationService;
    private final UserRepository userRepository;

    @PostMapping
    public R<TutorProfileVO> createProfile(@Valid @RequestBody TutorProfileCreateDTO dto) {
        log.info("创建教员档案, userId={}", UserContext.getCurrentUserId());
        TutorProfile profile = new TutorProfile();
        profile.setUniversity(dto.getUniversity());
        profile.setMajor(dto.getMajor());
        profile.setGrade(dto.getGrade());
        profile.setSubjects(dto.getSubjects());
        profile.setHourlyRate(dto.getHourlyRate());
        profile.setBio(dto.getBio());
        profile.setExperience(dto.getExperience());
        profile.setDistricts(dto.getDistricts());
        profile.setAvailableTimes(dto.getAvailableTimes());
        TutorProfile saved = tutorProfileApplicationService.createProfile(profile);
        return R.ok(toVO(saved));
    }

    @PutMapping
    public R<TutorProfileVO> updateProfile(@RequestBody TutorProfileUpdateDTO dto) {
        TutorProfile updateData = new TutorProfile();
        updateData.setUniversity(dto.getUniversity());
        updateData.setMajor(dto.getMajor());
        updateData.setGrade(dto.getGrade());
        updateData.setSubjects(dto.getSubjects());
        updateData.setHourlyRate(dto.getHourlyRate());
        updateData.setBio(dto.getBio());
        updateData.setExperience(dto.getExperience());
        updateData.setDistricts(dto.getDistricts());
        updateData.setAvailableTimes(dto.getAvailableTimes());
        TutorProfile updated = tutorProfileApplicationService.updateProfile(updateData);
        return R.ok(toVO(updated));
    }

    @PostMapping("/toggle-active")
    public R<TutorProfileVO> toggleActive() {
        TutorProfile profile = tutorProfileApplicationService.toggleActive();
        return R.ok(toVO(profile));
    }

    @GetMapping("/mine")
    public R<TutorProfileVO> getMyProfile() {
        TutorProfile profile = tutorProfileApplicationService.getMyProfile();
        return R.ok(profile != null ? toVO(profile) : null);
    }

    @GetMapping("/search")
    public R<List<TutorProfileVO>> searchTutors(
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String district,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<TutorProfile> profiles = tutorProfileApplicationService.searchTutors(subject, district, page, size);
        List<TutorProfileVO> voList = profiles.stream().map(this::toVO).collect(Collectors.toList());
        return R.ok(voList);
    }

    @GetMapping("/{id}")
    public R<TutorProfileVO> getProfileById(@PathVariable Long id) {
        TutorProfile profile = tutorProfileApplicationService.getProfileById(id);
        return R.ok(profile != null ? toVO(profile) : null);
    }

    private TutorProfileVO toVO(TutorProfile profile) {
        User user = userRepository.findById(profile.getUserId());
        return TutorProfileVO.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .nickname(user != null ? user.getNickname() : null)
                .avatar(user != null ? user.getAvatar() : null)
                .university(profile.getUniversity())
                .major(profile.getMajor())
                .grade(profile.getGrade())
                .subjects(profile.getSubjects())
                .hourlyRate(profile.getHourlyRate())
                .bio(profile.getBio())
                .experience(profile.getExperience())
                .districts(profile.getDistricts())
                .availableTimes(profile.getAvailableTimes())
                .rating(profile.getRating())
                .reviewCount(profile.getReviewCount())
                .orderCount(profile.getOrderCount())
                .active(profile.getActive())
                .isPinned(profile.isPinned())
                .pinnedUntil(profile.getPinnedUntil())
                .createdAt(profile.getCreatedAt())
                .build();
    }
}