package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.AudioMaterial;
import com.wzoto.domain.repository.AudioMaterialRepository;
import com.wzoto.domain.valobj.GradeType;
import com.wzoto.domain.valobj.TextbookVersion;
import com.wzoto.infrastructure.mapper.AudioMaterialMapper;
import com.wzoto.infrastructure.pojo.AudioMaterialPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/** 音频素材仓储实现 */
@Repository
@RequiredArgsConstructor
public class AudioMaterialRepositoryImpl implements AudioMaterialRepository {

    private final AudioMaterialMapper audioMaterialMapper;

    @Override
    public AudioMaterial findById(Long id) {
        AudioMaterialPO po = audioMaterialMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public List<AudioMaterial> findByGradeAndSubject(GradeType grade, String subject) {
        LambdaQueryWrapper<AudioMaterialPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(grade != null, AudioMaterialPO::getGrade, grade != null ? grade.getCode() : null)
                .eq(subject != null, AudioMaterialPO::getSubject, subject)
                .orderByAsc(AudioMaterialPO::getSortOrder);
        return audioMaterialMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<AudioMaterial> findByGradeAndSubjectAndType(GradeType grade, String subject, String audioType) {
        LambdaQueryWrapper<AudioMaterialPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(grade != null, AudioMaterialPO::getGrade, grade != null ? grade.getCode() : null)
                .eq(subject != null, AudioMaterialPO::getSubject, subject)
                .eq(audioType != null, AudioMaterialPO::getAudioType, audioType)
                .orderByAsc(AudioMaterialPO::getSortOrder);
        return audioMaterialMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    private AudioMaterial toEntity(AudioMaterialPO po) {
        AudioMaterial entity = new AudioMaterial();
        entity.setId(po.getId());
        entity.setGrade(po.getGrade() != null ? GradeType.fromCode(po.getGrade()) : null);
        entity.setSubject(po.getSubject());
        entity.setTextbookVersion(po.getTextbookVersion() != null ? TextbookVersion.fromCode(po.getTextbookVersion()) : null);
        entity.setTitle(po.getTitle());
        entity.setAudioType(po.getAudioType());
        entity.setAudioUrl(po.getAudioUrl());
        entity.setDurationSeconds(po.getDurationSeconds());
        entity.setTextContent(po.getTextContent());
        entity.setReferenceText(po.getReferenceText());
        entity.setKnowledgePointId(po.getKnowledgePointId());
        entity.setVipOnly(po.getVipOnly());
        entity.setSortOrder(po.getSortOrder());
        entity.setDeleted(po.getDeleted());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }
}
