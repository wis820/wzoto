package com.wzoto.domain.service;

import com.wzoto.domain.entity.AiQaRecord;
import com.wzoto.domain.entity.Child;
import com.wzoto.domain.repository.AiQaRecordRepository;
import com.wzoto.domain.repository.AiGenerator;
import com.wzoto.domain.repository.ChildRepository;
import com.wzoto.domain.valobj.AiQaType;
import com.wzoto.domain.valobj.GradeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * AI答疑领域服务 - 创建提问记录、分步对话管理、会话续问、调用次数限制
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiQaDomainService {

    private final AiQaRecordRepository aiQaRecordRepository;
    private final AiGenerator aiGenerator;
    private final ChildRepository childRepository;

    private static final int FREE_DAILY_LIMIT = 3;

    /**
     * 创建新提问（拍照/文字）
     */
    public AiQaRecord ask(Long parentId, Long childId, String subject,
                          AiQaType qaType, String questionText, String questionImageUrl,
                          String ocrText, boolean isVip) {
        validateOwnership(parentId, childId);
        if (!isVip) {
            int todayCount = aiQaRecordRepository.countTodayByChildId(childId);
            if (todayCount >= FREE_DAILY_LIMIT) {
                throw new IllegalArgumentException("免费用户每日AI答疑次数已用完（" + FREE_DAILY_LIMIT + "次），开通会员不限次数");
            }
        }
        Child child = childRepository.findById(childId);
        GradeType grade = child != null ? child.getGrade() : null;
        String conversationId = UUID.randomUUID().toString().replace("-", "");
        String aiResponse = aiGenerator.generateStepByStepAnswer(subject, questionText != null ? questionText : ocrText,
                grade != null ? grade.getDesc() : "未知");

        AiQaRecord record = AiQaRecord.create(childId, parentId, conversationId, subject, grade, qaType, questionText, questionImageUrl);
        record.setOcrText(ocrText);
        record.setAiResponseJson(aiResponse);
        record.setStepCount(4);
        AiQaRecord saved = aiQaRecordRepository.save(record);
        log.info("AI答疑记录创建成功, childId={}, conversationId={}", childId, conversationId);
        return saved;
    }

    /**
     * 追问（同一会话内续问）
     */
    public AiQaRecord followUp(Long parentId, Long childId, String conversationId,
                                String questionText, boolean isVip) {
        validateOwnership(parentId, childId);
        List<AiQaRecord> conversation = aiQaRecordRepository.findByConversationId(conversationId);
        if (conversation.isEmpty()) {
            throw new IllegalArgumentException("会话不存在");
        }
        AiQaRecord firstRecord = conversation.get(0);
        Child child = childRepository.findById(childId);
        GradeType grade = child != null ? child.getGrade() : null;

        String aiResponse = aiGenerator.generateStepByStepAnswer(firstRecord.getSubject(), questionText,
                grade != null ? grade.getDesc() : "未知");

        AiQaRecord record = AiQaRecord.createFollowUp(childId, parentId, conversationId,
                firstRecord.getSubject(), grade, questionText);
        record.setAiResponseJson(aiResponse);
        AiQaRecord saved = aiQaRecordRepository.save(record);
        log.info("AI追问记录创建成功, childId={}, conversationId={}", childId, conversationId);
        return saved;
    }

    /**
     * 查询提问历史
     */
    public List<AiQaRecord> getHistory(Long parentId, Long childId) {
        validateOwnership(parentId, childId);
        return aiQaRecordRepository.findByChildId(childId);
    }

    /**
     * 查询会话详情
     */
    public List<AiQaRecord> getConversation(String conversationId) {
        return aiQaRecordRepository.findByConversationId(conversationId);
    }

    private void validateOwnership(Long parentId, Long childId) {
        Child child = childRepository.findById(childId);
        if (child == null || !child.belongsTo(parentId)) {
            throw new IllegalArgumentException("无权操作该子女档案");
        }
    }
}
