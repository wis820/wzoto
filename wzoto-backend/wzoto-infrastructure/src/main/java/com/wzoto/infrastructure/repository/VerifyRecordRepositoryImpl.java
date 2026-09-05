package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.VerifyRecord;
import com.wzoto.domain.repository.VerifyRecordRepository;
import com.wzoto.domain.valobj.VerifyStatus;
import com.wzoto.domain.valobj.VerifyType;
import com.wzoto.infrastructure.mapper.VerifyRecordMapper;
import com.wzoto.infrastructure.pojo.VerifyRecordPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class VerifyRecordRepositoryImpl implements VerifyRecordRepository {

    private final VerifyRecordMapper verifyRecordMapper;

    @Override
    public VerifyRecord save(VerifyRecord record) {
        VerifyRecordPO po = toPO(record);
        verifyRecordMapper.insert(po);
        record.setId(po.getId());
        return record;
    }

    @Override
    public VerifyRecord findById(Long id) {
        VerifyRecordPO po = verifyRecordMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public VerifyRecord findLatestByUserId(Long userId) {
        LambdaQueryWrapper<VerifyRecordPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VerifyRecordPO::getUserId, userId)
                .orderByDesc(VerifyRecordPO::getCreatedAt)
                .last("LIMIT 1");
        VerifyRecordPO po = verifyRecordMapper.selectOne(wrapper);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public List<VerifyRecord> findByUserId(Long userId) {
        LambdaQueryWrapper<VerifyRecordPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VerifyRecordPO::getUserId, userId)
                .orderByDesc(VerifyRecordPO::getCreatedAt);
        return verifyRecordMapper.selectList(wrapper).stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public VerifyRecord update(VerifyRecord record) {
        VerifyRecordPO po = toPO(record);
        verifyRecordMapper.updateById(po);
        return record;
    }

    @Override
    public List<VerifyRecord> findPendingRecords() {
        LambdaQueryWrapper<VerifyRecordPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VerifyRecordPO::getVerifyStatus, VerifyStatus.PENDING.getCode())
                // P1: 加急记录优先排序
                .orderByDesc(VerifyRecordPO::getExpedited)
                .orderByAsc(VerifyRecordPO::getCreatedAt);
        return verifyRecordMapper.selectList(wrapper).stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    // ========== 转换方法 ==========

    private VerifyRecord toEntity(VerifyRecordPO po) {
        VerifyRecord entity = new VerifyRecord();
        entity.setId(po.getId());
        entity.setUserId(po.getUserId());
        entity.setVerifyType(po.getVerifyType() != null ? VerifyType.fromCode(po.getVerifyType()) : null);
        entity.setRealName(po.getRealName());
        entity.setIdCardNo(po.getIdCardNo());
        entity.setStudentCardImage(po.getStudentCardImage());
        entity.setVerifyStatus(po.getVerifyStatus() != null ? VerifyStatus.fromCode(po.getVerifyStatus()) : null);
        entity.setRemark(po.getRemark());
        entity.setExpedited(po.getExpedited() != null && po.getExpedited() == 1);
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private VerifyRecordPO toPO(VerifyRecord entity) {
        VerifyRecordPO po = new VerifyRecordPO();
        po.setId(entity.getId());
        po.setUserId(entity.getUserId());
        po.setVerifyType(entity.getVerifyType() != null ? entity.getVerifyType().getCode() : null);
        po.setRealName(entity.getRealName());
        po.setIdCardNo(entity.getIdCardNo());
        po.setStudentCardImage(entity.getStudentCardImage());
        po.setVerifyStatus(entity.getVerifyStatus() != null ? entity.getVerifyStatus().getCode() : null);
        po.setRemark(entity.getRemark());
        po.setExpedited(entity.getExpedited() != null && entity.getExpedited() ? 1 : 0);
        return po;
    }
}