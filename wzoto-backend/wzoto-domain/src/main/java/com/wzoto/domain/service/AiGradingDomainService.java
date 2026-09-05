package com.wzoto.domain.service;

import com.wzoto.domain.entity.AiGradingRecord;
import com.wzoto.domain.entity.Child;
import com.wzoto.domain.repository.AiGradingRecordRepository;
import com.wzoto.domain.repository.AiGenerator;
import com.wzoto.domain.repository.ChildRepository;
import com.wzoto.domain.valobj.GradeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI批改领域服务 - 作文批改、口语评测、评分记录、改进建议
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiGradingDomainService {

    private final AiGradingRecordRepository aiGradingRecordRepository;
    private final AiGenerator aiGenerator;
    private final ChildRepository childRepository;

    /**
     * 作文批改
     */
    public AiGradingRecord gradeComposition(Long parentId, Long childId, String subject,
                                             String title, String content) {
        validateOwnership(parentId, childId);
        Child child = childRepository.findById(childId);
        GradeType grade = child != null ? child.getGrade() : null;

        String aiResult = aiGenerator.gradeComposition(
                grade != null ? grade.getDesc() : "未知", title, content);

        AiGradingRecord record = AiGradingRecord.createComposition(childId, parentId, subject, grade, title, content);
        record.setAiResultJson(aiResult);
        record.setScore(80);
        AiGradingRecord saved = aiGradingRecordRepository.save(record);
        log.info("作文批改记录创建成功, childId={}, score={}", childId, record.getScore());
        return saved;
    }

    /**
     * 口语评测
     */
    public AiGradingRecord evaluatePronunciation(Long parentId, Long childId,
                                                  String title, String audioUrl, String audioTranscript) {
        validateOwnership(parentId, childId);
        Child child = childRepository.findById(childId);
        GradeType grade = child != null ? child.getGrade() : null;

        String aiResult = aiGenerator.evaluatePronunciation(title, audioTranscript);

        AiGradingRecord record = AiGradingRecord.createPronunciation(childId, parentId, grade, title, audioUrl);
        record.setAiResultJson(aiResult);
        record.setScore(85);
        AiGradingRecord saved = aiGradingRecordRepository.save(record);
        log.info("口语评测记录创建成功, childId={}, score={}", childId, record.getScore());
        return saved;
    }

    /**
     * 查询批改历史
     */
    public List<AiGradingRecord> getHistory(Long parentId, Long childId) {
        validateOwnership(parentId, childId);
        return aiGradingRecordRepository.findByChildId(childId);
    }

    /**
     * 按类型查询批改历史
     */
    public List<AiGradingRecord> getHistoryByType(Long parentId, Long childId, String gradingType) {
        validateOwnership(parentId, childId);
        return aiGradingRecordRepository.findByChildIdAndType(childId, gradingType);
    }

    private void validateOwnership(Long parentId, Long childId) {
        Child child = childRepository.findById(childId);
        if (child == null || !child.belongsTo(parentId)) {
            throw new IllegalArgumentException("无权操作该子女档案");
        }
    }
}
