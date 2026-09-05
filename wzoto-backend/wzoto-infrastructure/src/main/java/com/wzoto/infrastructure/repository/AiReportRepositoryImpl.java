package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.AiReport;
import com.wzoto.domain.repository.AiReportRepository;
import com.wzoto.infrastructure.mapper.AiReportMapper;
import com.wzoto.infrastructure.pojo.AiReportPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AiReportRepositoryImpl implements AiReportRepository {

    private final AiReportMapper aiReportMapper;

    @Override
    public AiReport save(AiReport report) {
        AiReportPO po = toPO(report);
        aiReportMapper.insert(po);
        report.setId(po.getId());
        return report;
    }

    @Override
    public AiReport findById(Long id) {
        AiReportPO po = aiReportMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public List<AiReport> findByUserId(Long userId) {
        LambdaQueryWrapper<AiReportPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AiReportPO::getUserId, userId).orderByDesc(AiReportPO::getCreatedAt);
        return aiReportMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public int countFreeReportsThisMonth(Long userId) {
        return aiReportMapper.countFreeReportsThisMonth(userId);
    }

    @Override
    public AiReport update(AiReport report) {
        AiReportPO po = toPO(report);
        aiReportMapper.updateById(po);
        return report;
    }

    private AiReport toEntity(AiReportPO po) {
        AiReport entity = new AiReport();
        entity.setId(po.getId());
        entity.setUserId(po.getUserId());
        entity.setChildName(po.getChildName());
        entity.setWeakSubjects(po.getWeakSubjects());
        entity.setRecentScores(po.getRecentScores());
        entity.setWeakPointDesc(po.getWeakPointDesc());
        entity.setPhotoUrls(po.getPhotoUrls());
        entity.setReportContent(po.getReportContent());
        entity.setPreviewContent(po.getPreviewContent());
        entity.setReportType(po.getReportType());
        entity.setIsMemberReport(po.getIsMemberReport());
        entity.setPrice(po.getPrice());
        entity.setPaymentStatus(po.getPaymentStatus());
        entity.setDeleted(po.getDeleted());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private AiReportPO toPO(AiReport entity) {
        AiReportPO po = new AiReportPO();
        po.setId(entity.getId());
        po.setUserId(entity.getUserId());
        po.setChildName(entity.getChildName());
        po.setWeakSubjects(entity.getWeakSubjects());
        po.setRecentScores(entity.getRecentScores());
        po.setWeakPointDesc(entity.getWeakPointDesc());
        po.setPhotoUrls(entity.getPhotoUrls());
        po.setReportContent(entity.getReportContent());
        po.setPreviewContent(entity.getPreviewContent());
        po.setReportType(entity.getReportType());
        po.setIsMemberReport(entity.getIsMemberReport());
        po.setPrice(entity.getPrice());
        po.setPaymentStatus(entity.getPaymentStatus());
        return po;
    }
}
