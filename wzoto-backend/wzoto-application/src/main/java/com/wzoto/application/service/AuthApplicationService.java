package com.wzoto.application.service;

import cn.hutool.json.JSONObject;
import com.wzoto.domain.entity.User;
import com.wzoto.domain.service.UserDomainService;
import com.wzoto.infrastructure.util.JwtUtil;
import com.wzoto.infrastructure.util.WechatUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户授权登录 - 应用服务
 * 编排微信API调用、领域服务、JWT生成的流程
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthApplicationService {

    private final WechatUtil wechatUtil;
    private final UserDomainService userDomainService;
    private final JwtUtil jwtUtil;

    /**
     * 微信登录
     * 流程：1.调用微信code2Session → 2.登录/注册 → 3.生成JWT
     */
    @Transactional(rollbackFor = Exception.class)
    public LoginResult login(String code) {
        // 1. 调用微信接口获取openid和session_key
        JSONObject wxResult = wechatUtil.code2Session(code);
        String openid = wxResult.getStr("openid");
        String sessionKey = wxResult.getStr("session_key");
        String unionid = wxResult.getStr("unionid");

        // 2. 登录或注册
        User user = userDomainService.loginOrRegister(openid, sessionKey, unionid);

        // 3. 生成JWT
        String identityType = user.getIdentityType() != null ? user.getIdentityType().getCode() : "";
        String token = jwtUtil.generateToken(user.getId(), user.getOpenid(), identityType);

        log.info("用户登录成功, userId={}, identityType={}", user.getId(), identityType);

        return LoginResult.builder()
                .token(token)
                .userId(user.getId())
                .openid(user.getOpenid())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .identityType(identityType)
                .hasIdentity(user.hasIdentity())
                .verifyStatus(user.getVerifyStatus() != null ? user.getVerifyStatus().getCode() : "NONE")
                .build();
    }

    /**
     * 选择身份
     */
    @Transactional(rollbackFor = Exception.class)
    public LoginResult selectIdentity(Long userId, String identityType) {
        User user = userDomainService.selectIdentity(userId, identityType);

        // 重新生成Token（含新身份）
        String token = jwtUtil.generateToken(user.getId(), user.getOpenid(), user.getIdentityType().getCode());

        return LoginResult.builder()
                .token(token)
                .userId(user.getId())
                .openid(user.getOpenid())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .identityType(user.getIdentityType().getCode())
                .hasIdentity(true)
                .verifyStatus(user.getVerifyStatus() != null ? user.getVerifyStatus().getCode() : "NONE")
                .build();
    }

    /**
     * 绑定手机号
     */
    @Transactional(rollbackFor = Exception.class)
    public LoginResult bindPhone(Long userId, String phone) {
        User user = userDomainService.bindPhone(userId, phone);

        String identityType = user.getIdentityType() != null ? user.getIdentityType().getCode() : "";

        return LoginResult.builder()
                .token(null) // 绑定手机号不需要重新生成token
                .userId(user.getId())
                .openid(user.getOpenid())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .identityType(identityType)
                .hasIdentity(user.hasIdentity())
                .verifyStatus(user.getVerifyStatus() != null ? user.getVerifyStatus().getCode() : "NONE")
                .build();
    }

    /**
     * 登录结果
     */
    @lombok.Builder
    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class LoginResult {
        private String token;
        private Long userId;
        private String openid;
        private String nickname;
        private String avatar;
        private String identityType;
        private Boolean hasIdentity;
        private String verifyStatus;
    }
}