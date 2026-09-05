package com.wzoto.domain.service;

import com.wzoto.domain.entity.User;
import com.wzoto.domain.repository.UserRepository;
import com.wzoto.domain.valobj.IdentityType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户领域服务 - 封装核心业务规则
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDomainService {

    private final UserRepository userRepository;

    /**
     * 微信登录/注册 - 领域规则：
     * 1. 根据openid查找用户
     * 2. 不存在则自动注册
     * 3. 更新session_key
     */
    public User loginOrRegister(String openid, String sessionKey, String unionid) {
        User user = userRepository.findByOpenid(openid);
        if (user == null) {
            user = User.builder()
                    .openid(openid)
                    .unionid(unionid)
                    .sessionKey(sessionKey)
                    .nickname("微信用户")
                    .verifyStatus(com.wzoto.domain.valobj.VerifyStatus.NONE)
                    .deleted(false)
                    .createdAt(java.time.LocalDateTime.now())
                    .updatedAt(java.time.LocalDateTime.now())
                    .build();
            user = userRepository.save(user);
            log.info("新用户注册成功, openid={}", openid);
        } else {
            user.setSessionKey(sessionKey);
            log.info("用户登录成功, openid={}", openid);
        }
        return user;
    }

    /**
     * 选择身份 - 领域规则：一经选择不可随意切换
     */
    public User selectIdentity(Long userId, String identityTypeCode) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        IdentityType identityType = IdentityType.fromCode(identityTypeCode);
        user.selectIdentity(identityType);
        userRepository.updateIdentity(userId, identityTypeCode);
        log.info("用户选择身份, userId={}, identity={}", userId, identityType.getDesc());
        return user;
    }

    /**
     * 绑定手机号
     */
    public User bindPhone(Long userId, String phone) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        user.bindPhone(phone);
        userRepository.updatePhone(userId, phone);
        log.info("用户绑定手机号, userId={}", userId);
        return user;
    }
}