package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.MicroCourse;
import com.wzoto.domain.repository.MicroCourseRepository;
import com.wzoto.domain.valobj.ExerciseDifficulty;
import com.wzoto.domain.valobj.GradeType;
import com.wzoto.domain.valobj.TextbookVersion;
import com.wzoto.infrastructure.mapper.MicroCourseMapper;
import com.wzoto.infrastructure.pojo.MicroCoursePO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/** 微课视频仓储实现 */
@Repository
@RequiredArgsConstructor
public class MicroCourseRepositoryImpl implements MicroCourseRepository {

    private final MicroCourseMapper microCourseMapper;

    @Override
    public MicroCourse save(MicroCourse course) {
        MicroCoursePO po = toPO(course);
        microCourseMapper.insert(po);
        course.setId(po.getId());
        return course;
    }

    @Override
    public MicroCourse findById(Long id) {
        MicroCoursePO po = microCourseMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public MicroCourse update(MicroCourse course) {
        MicroCoursePO po = toPO(course);
        microCourseMapper.updateById(po);
        return course;
    }

    @Override
    public List<MicroCourse> findByGradeAndSubject(GradeType grade, String subject) {
        LambdaQueryWrapper<MicroCoursePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(grade != null, MicroCoursePO::getGrade, grade != null ? grade.getCode() : null)
                .eq(subject != null, MicroCoursePO::getSubject, subject)
                .orderByAsc(MicroCoursePO::getSortOrder)
                .orderByDesc(MicroCoursePO::getCreatedAt);
        return microCourseMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<MicroCourse> findByGradeAndSubjectAndVip(GradeType grade, String subject, Boolean vipOnly) {
        LambdaQueryWrapper<MicroCoursePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(grade != null, MicroCoursePO::getGrade, grade != null ? grade.getCode() : null)
                .eq(subject != null, MicroCoursePO::getSubject, subject)
                .eq(vipOnly != null, MicroCoursePO::getVipOnly, vipOnly)
                .orderByAsc(MicroCoursePO::getSortOrder);
        return microCourseMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<MicroCourse> findRecommended(GradeType grade, String subject, int limit) {
        LambdaQueryWrapper<MicroCoursePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(grade != null, MicroCoursePO::getGrade, grade != null ? grade.getCode() : null)
                .eq(subject != null, MicroCoursePO::getSubject, subject)
                .orderByDesc(MicroCoursePO::getPlayCount)
                .last("LIMIT " + limit);
        return microCourseMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    private MicroCourse toEntity(MicroCoursePO po) {
        MicroCourse entity = new MicroCourse();
        entity.setId(po.getId());
        entity.setGrade(po.getGrade() != null ? GradeType.fromCode(po.getGrade()) : null);
        entity.setSubject(po.getSubject());
        entity.setTextbookVersion(po.getTextbookVersion() != null ? TextbookVersion.fromCode(po.getTextbookVersion()) : null);
        entity.setChapterId(po.getChapterId());
        entity.setKnowledgePointId(po.getKnowledgePointId());
        entity.setTitle(po.getTitle());
        entity.setDescription(po.getDescription());
        entity.setCoverUrl(po.getCoverUrl());
        entity.setVideoUrl(po.getVideoUrl());
        entity.setDurationSeconds(po.getDurationSeconds());
        entity.setResolution(po.getResolution());
        entity.setFileSizeMb(po.getFileSizeMb());
        entity.setDifficulty(po.getDifficulty() != null ? ExerciseDifficulty.fromCode(po.getDifficulty()) : null);
        entity.setVipOnly(po.getVipOnly());
        entity.setPlayCount(po.getPlayCount());
        entity.setSortOrder(po.getSortOrder());
        entity.setDeleted(po.getDeleted());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private MicroCoursePO toPO(MicroCourse entity) {
        MicroCoursePO po = new MicroCoursePO();
        po.setId(entity.getId());
        po.setGrade(entity.getGrade() != null ? entity.getGrade().getCode() : null);
        po.setSubject(entity.getSubject());
        po.setTextbookVersion(entity.getTextbookVersion() != null ? entity.getTextbookVersion().getCode() : null);
        po.setChapterId(entity.getChapterId());
        po.setKnowledgePointId(entity.getKnowledgePointId());
        po.setTitle(entity.getTitle());
        po.setDescription(entity.getDescription());
        po.setCoverUrl(entity.getCoverUrl());
        po.setVideoUrl(entity.getVideoUrl());
        po.setDurationSeconds(entity.getDurationSeconds());
        po.setResolution(entity.getResolution());
        po.setFileSizeMb(entity.getFileSizeMb());
        po.setDifficulty(entity.getDifficulty() != null ? entity.getDifficulty().getCode() : null);
        po.setVipOnly(entity.getVipOnly());
        po.setPlayCount(entity.getPlayCount());
        po.setSortOrder(entity.getSortOrder());
        po.setDeleted(entity.getDeleted());
        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        return po;
    }
}
