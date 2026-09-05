package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.AiTutorOptimization;
import com.wzoto.domain.repository.AiTutorOptimizationRepository;
import com.wzoto.infrastructure.mapper.AiTutorOptimizationMapper;
import com.wzoto.infrastructure.pojo.AiTutorOptimizationPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AiTutorOptimizationRepositoryImpl implements AiTutorOptimizationRepository {

    private final AiTutorOptimizationMapper mapper;

    @Override
    public void save(AiTutorOptimization optimization) {
        AiTutorOptimizationPO po = toPO(optimization);
        mapper.insert(po);
        optimization.setId(po.getId());
    }

    @Override
    public AiTutorOptimization findById(Long id) {
        AiTutorOptimizationPO po = mapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public List<AiTutorOptimization> findByUserId(Long userId) {
        LambdaQueryWrapper<AiTutorOptimizationPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AiTutorOptimizationPO::getUserId, userId)
               .orderByDesc(AiTutorOptimizationPO::getCreatedAt);
        return mapper.selectList(wrapper).stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void update(AiTutorOptimization optimization) {
        AiTutorOptimizationPO po = toPO(optimization);
        mapper.updateById(po);
    }

    private AiTutorOptimizationPO toPO(AiTutorOptimization e) {
        AiTutorOptimizationPO po = new AiTutorOptimizationPO();
        po.setId(e.getId());
        po.setUserId(e.getUserId());
        po.setOptimizationType(e.getOptimizationType());
        po.setUniversity(e.getUniversity());
        po.setMajor(e.getMajor());
        po.setGrade(e.getGrade());
        po.setSubjects(e.getSubjects());
        po.setCurrentBio(e.getCurrentBio());
        po.setCurrentExperience(e.getCurrentExperience());
        po.setCurrentRate(e.getCurrentRate());
        po.setOptimizedContent(e.getOptimizedContent());
        po.setPreviewContent(e.getPreviewContent());
        po.setIsMemberReport(e.getIsMemberReport());
        po.setPrice(e.getPrice());
        po.setPaymentStatus(e.getPaymentStatus());
        po.setCreatedAt(e.getCreatedAt());
        po.setUpdatedAt(e.getUpdatedAt());
        return po;
    }

    private AiTutorOptimization toEntity(AiTutorOptimizationPO po) {
        AiTutorOptimization e = new AiTutorOptimization();
        e.setId(po.getId());
        e.setUserId(po.getUserId());
        e.setOptimizationType(po.getOptimizationType());
        e.setUniversity(po.getUniversity());
        e.setMajor(po.getMajor());
        e.setGrade(po.getGrade());
        e.setSubjects(po.getSubjects());
        e.setCurrentBio(po.getCurrentBio());
        e.setCurrentExperience(po.getCurrentExperience());
        e.setCurrentRate(po.getCurrentRate());
        e.setOptimizedContent(po.getOptimizedContent());
        e.setPreviewContent(po.getPreviewContent());
        e.setIsMemberReport(po.getIsMemberReport());
        e.setPrice(po.getPrice());
        e.setPaymentStatus(po.getPaymentStatus());
        e.setCreatedAt(po.getCreatedAt());
        e.setUpdatedAt(po.getUpdatedAt());
        return e;
    }
}
