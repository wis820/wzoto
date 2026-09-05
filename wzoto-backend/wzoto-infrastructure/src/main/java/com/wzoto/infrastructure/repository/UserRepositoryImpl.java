package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.User;
import com.wzoto.domain.repository.UserRepository;
import com.wzoto.domain.valobj.IdentityType;
import com.wzoto.domain.valobj.VerifyStatus;
import com.wzoto.infrastructure.mapper.UserMapper;
import com.wzoto.infrastructure.pojo.UserPO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * 用户仓储实现 - 基础设施层
 * 负责领域实体User与持久化对象UserPO之间的转换
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;

    @Override
    public User findByOpenid(String openid) {
        LambdaQueryWrapper<UserPO> wrapper = new LambdaQueryWrapper<UserPO>()
                .eq(UserPO::getOpenid, openid)
                .eq(UserPO::getDeleted, false);
        UserPO po = userMapper.selectOne(wrapper);
        return toEntity(po);
    }

    @Override
    public User findById(Long id) {
        UserPO po = userMapper.selectById(id);
        return toEntity(po);
    }

    @Override
    public User save(User user) {
        UserPO po = toPO(user);
        if (po.getId() == null) {
            po.setCreatedAt(LocalDateTime.now());
            po.setUpdatedAt(LocalDateTime.now());
            userMapper.insert(po);
        } else {
            po.setUpdatedAt(LocalDateTime.now());
            userMapper.updateById(po);
        }
        user.setId(po.getId());
        return user;
    }

    @Override
    public boolean updateIdentity(Long userId, String identityType) {
        UserPO po = new UserPO();
        po.setId(userId);
        po.setIdentityType(identityType);
        po.setUpdatedAt(LocalDateTime.now());
        return userMapper.updateById(po) > 0;
    }

    @Override
    public boolean updatePhone(Long userId, String phone) {
        UserPO po = new UserPO();
        po.setId(userId);
        po.setPhone(phone);
        po.setUpdatedAt(LocalDateTime.now());
        return userMapper.updateById(po) > 0;
    }

    @Override
    public User findByNickname(String nickname) {
        LambdaQueryWrapper<UserPO> wrapper = new LambdaQueryWrapper<UserPO>()
                .eq(UserPO::getNickname, nickname)
                .eq(UserPO::getDeleted, false)
                .last("LIMIT 1");
        UserPO po = userMapper.selectOne(wrapper);
        return toEntity(po);
    }

    // ========== 转换方法 ==========

    private User toEntity(UserPO po) {
        if (po == null) {
            return null;
        }
        return User.builder()
                .id(po.getId())
                .openid(po.getOpenid())
                .unionid(po.getUnionid())
                .phone(po.getPhone())
                .nickname(po.getNickname())
                .avatar(po.getAvatar())
                .identityType(po.getIdentityType() != null ? IdentityType.fromCode(po.getIdentityType()) : null)
                .verifyStatus(po.getVerifyStatus() != null ? VerifyStatus.valueOf(po.getVerifyStatus()) : VerifyStatus.NONE)
                .realName(po.getRealName())
                .deleted(po.getDeleted())
                .createdAt(po.getCreatedAt())
                .updatedAt(po.getUpdatedAt())
                .build();
    }

    private UserPO toPO(User entity) {
        return UserPO.builder()
                .id(entity.getId())
                .openid(entity.getOpenid())
                .unionid(entity.getUnionid())
                .phone(entity.getPhone())
                .nickname(entity.getNickname())
                .avatar(entity.getAvatar())
                .identityType(entity.getIdentityType() != null ? entity.getIdentityType().getCode() : null)
                .verifyStatus(entity.getVerifyStatus() != null ? entity.getVerifyStatus().getCode() : VerifyStatus.NONE.getCode())
                .realName(entity.getRealName())
                .deleted(entity.getDeleted() != null ? entity.getDeleted() : false)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}