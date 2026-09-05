package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.TestPaper;
import com.wzoto.domain.repository.TestPaperRepository;
import com.wzoto.domain.valobj.GradeType;
import com.wzoto.domain.valobj.TestPaperType;
import com.wzoto.domain.valobj.TextbookVersion;
import com.wzoto.infrastructure.mapper.TestPaperMapper;
import com.wzoto.infrastructure.pojo.TestPaperPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/** 试卷仓储实现 */
@Repository
@RequiredArgsConstructor
public class TestPaperRepositoryImpl implements TestPaperRepository {

    private final TestPaperMapper testPaperMapper;

    @Override
    public TestPaper findById(Long id) {
        TestPaperPO po = testPaperMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public List<TestPaper> findByGradeAndSubject(GradeType grade, String subject) {
        LambdaQueryWrapper<TestPaperPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(grade != null, TestPaperPO::getGrade, grade != null ? grade.getCode() : null)
                .eq(subject != null, TestPaperPO::getSubject, subject)
                .orderByAsc(TestPaperPO::getSortOrder);
        return testPaperMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<TestPaper> findByGradeAndSubjectAndType(GradeType grade, String subject, String paperType) {
        LambdaQueryWrapper<TestPaperPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(grade != null, TestPaperPO::getGrade, grade != null ? grade.getCode() : null)
                .eq(subject != null, TestPaperPO::getSubject, subject)
                .eq(paperType != null, TestPaperPO::getPaperType, paperType)
                .orderByAsc(TestPaperPO::getSortOrder);
        return testPaperMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    private TestPaper toEntity(TestPaperPO po) {
        TestPaper entity = new TestPaper();
        entity.setId(po.getId());
        entity.setGrade(po.getGrade() != null ? GradeType.fromCode(po.getGrade()) : null);
        entity.setSubject(po.getSubject());
        entity.setTextbookVersion(po.getTextbookVersion() != null ? TextbookVersion.fromCode(po.getTextbookVersion()) : null);
        entity.setTitle(po.getTitle());
        entity.setPaperType(po.getPaperType() != null ? TestPaperType.fromCode(po.getPaperType()) : null);
        entity.setDescription(po.getDescription());
        entity.setTotalScore(po.getTotalScore());
        entity.setDurationMinutes(po.getDurationMinutes());
        entity.setQuestionsJson(po.getQuestionsJson());
        entity.setCoverUrl(po.getCoverUrl());
        entity.setVipOnly(po.getVipOnly());
        entity.setUseCount(po.getUseCount());
        entity.setSortOrder(po.getSortOrder());
        entity.setDeleted(po.getDeleted());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }
}
