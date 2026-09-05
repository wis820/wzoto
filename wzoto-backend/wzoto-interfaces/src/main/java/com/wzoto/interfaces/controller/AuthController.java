package com.wzoto.interfaces.controller;

import com.wzoto.application.service.AuthApplicationService;
import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.User;
import com.wzoto.domain.repository.UserRepository;
import com.wzoto.domain.valobj.IdentityType;
import com.wzoto.domain.valobj.VerifyStatus;
import com.wzoto.infrastructure.util.JwtUtil;
import com.wzoto.interfaces.common.R;
import com.wzoto.interfaces.dto.BindPhoneDTO;
import com.wzoto.interfaces.dto.SelectIdentityDTO;
import com.wzoto.interfaces.dto.WxLoginDTO;
import com.wzoto.interfaces.vo.LoginVO;
import com.wzoto.interfaces.vo.UserInfoVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器 - 微信登录、身份选择、手机号绑定
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthApplicationService authApplicationService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    /**
     * 获取当前登录用户信息
     * GET /api/auth/me
     */
    @GetMapping("/me")
    public R<UserInfoVO> getCurrentUser() {
        Long userId = UserContext.getCurrentUserId();
        User user = userRepository.findById(userId);
        if (user == null) {
            return R.unauthorized("用户不存在");
        }

        IdentityType identityType = user.getIdentityType();
        VerifyStatus verifyStatus = user.getVerifyStatus();

        UserInfoVO vo = UserInfoVO.builder()
                .userId(user.getId())
                .openid(user.getOpenid())
                .phone(user.getPhone())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .identityType(identityType != null ? identityType.getCode() : null)
                .identityTypeDesc(identityType != null ? identityType.getDesc() : "未选择")
                .hasIdentity(identityType != null)
                .verifyStatus(verifyStatus != null ? verifyStatus.getCode() : VerifyStatus.NONE.getCode())
                .verifyStatusDesc(verifyStatus != null ? verifyStatus.getDesc() : VerifyStatus.NONE.getDesc())
                .realName(VerifyStatus.APPROVED.equals(verifyStatus) ? user.getRealName() : null)
                .build();
        return R.ok(vo);
    }

    /**
     * 微信登录
     * POST /api/auth/wx-login
     */
    @PostMapping("/wx-login")
    public R<LoginVO> wxLogin(@Valid @RequestBody WxLoginDTO dto) {
        log.info("微信登录请求, code={}", dto.getCode());
        AuthApplicationService.LoginResult result = authApplicationService.login(dto.getCode());
        LoginVO vo = toLoginVO(result);
        return R.ok(vo);
    }

    /**
     * 选择身份
     * POST /api/auth/select-identity
     */
    @PostMapping("/select-identity")
    public R<LoginVO> selectIdentity(@Valid @RequestBody SelectIdentityDTO dto) {
        log.info("选择身份, userId={}, identityType={}", dto.getUserId(), dto.getIdentityType());
        AuthApplicationService.LoginResult result = authApplicationService.selectIdentity(dto.getUserId(), dto.getIdentityType());
        LoginVO vo = toLoginVO(result);
        return R.ok(vo);
    }

    /**
     * 绑定手机号
     * POST /api/auth/bind-phone
     */
    @PostMapping("/bind-phone")
    public R<LoginVO> bindPhone(@Valid @RequestBody BindPhoneDTO dto) {
        log.info("绑定手机号, userId={}", dto.getUserId());
        AuthApplicationService.LoginResult result = authApplicationService.bindPhone(dto.getUserId(), dto.getPhone());
        LoginVO vo = toLoginVO(result);
        vo.setPhone(dto.getPhone());
        return R.ok(vo);
    }

    /**
     * 开发环境登录 - 通过昵称直接登录（仅开发环境可用）
     * POST /api/auth/dev-login
     */
    @PostMapping("/dev-login")
    public R<LoginVO> devLogin(@RequestBody DevLoginDTO dto) {
        log.info("开发环境登录, nickname={}", dto.getNickname());
        // 根据昵称查找用户
        User user = userRepository.findByNickname(dto.getNickname());
        if (user == null) {
            return R.fail("用户不存在，请先插入测试数据");
        }
        // 生成JWT Token
        String token = jwtUtil.generateToken(user.getId(), user.getOpenid(),
                user.getIdentityType() != null ? user.getIdentityType().getCode() : null);
        LoginVO vo = LoginVO.builder()
                .token(token)
                .userId(user.getId())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .identityType(user.getIdentityType() != null ? user.getIdentityType().getCode() : null)
                .hasIdentity(user.getIdentityType() != null)
                .verifyStatus(user.getVerifyStatus() != null ? user.getVerifyStatus().getCode() : "NONE")
                .build();
        return R.ok(vo);
    }

    // ========== 内部DTO ==========

    @lombok.Data
    public static class DevLoginDTO {
        private String nickname;
    }

    // ========== 转换方法 ==========

    private LoginVO toLoginVO(AuthApplicationService.LoginResult result) {
        return LoginVO.builder()
                .token(result.getToken())
                .userId(result.getUserId())
                .nickname(result.getNickname())
                .avatar(result.getAvatar())
                .identityType(result.getIdentityType())
                .hasIdentity(result.getHasIdentity())
                .verifyStatus(result.getVerifyStatus())
                .build();
    }
}