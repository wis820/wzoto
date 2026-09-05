package com.wzoto.domain.repository;

import com.wzoto.domain.entity.AudioMaterial;
import com.wzoto.domain.valobj.GradeType;
import java.util.List;

/** 音频素材仓储接口 */
public interface AudioMaterialRepository {
    AudioMaterial findById(Long id);
    List<AudioMaterial> findByGradeAndSubject(GradeType grade, String subject);
    List<AudioMaterial> findByGradeAndSubjectAndType(GradeType grade, String subject, String audioType);
}
