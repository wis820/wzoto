package com.wzoto.domain.repository;

import com.wzoto.domain.entity.AiGradingRecord;
import java.util.List;

/** AI批改记录仓储接口 */
public interface AiGradingRecordRepository {
    AiGradingRecord save(AiGradingRecord record);
    AiGradingRecord findById(Long id);
    List<AiGradingRecord> findByChildId(Long childId);
    List<AiGradingRecord> findByChildIdAndType(Long childId, String gradingType);
}
