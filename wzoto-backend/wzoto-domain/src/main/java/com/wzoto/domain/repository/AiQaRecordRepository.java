package com.wzoto.domain.repository;

import com.wzoto.domain.entity.AiQaRecord;
import java.util.List;

/** AI提问记录仓储接口 */
public interface AiQaRecordRepository {
    AiQaRecord save(AiQaRecord record);
    AiQaRecord findById(Long id);
    List<AiQaRecord> findByChildId(Long childId);
    List<AiQaRecord> findByChildIdAndSubject(Long childId, String subject);
    List<AiQaRecord> findByConversationId(String conversationId);
    int countTodayByChildId(Long childId);
}
