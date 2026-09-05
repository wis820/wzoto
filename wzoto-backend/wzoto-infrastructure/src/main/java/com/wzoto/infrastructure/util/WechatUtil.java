package com.wzoto.infrastructure.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wzoto.infrastructure.config.WechatProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 微信小程序API工具类
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WechatUtil {

    private final WechatProperties wechatProperties;

    /**
     * 通过code换取openid和session_key
     * @param code 微信登录凭证
     * @return JSONObject containing openid, session_key, unionid
     */
    public JSONObject code2Session(String code) {
        String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                wechatProperties.getLoginUrl(),
                wechatProperties.getAppId(),
                wechatProperties.getAppSecret(),
                code);

        String response = HttpUtil.get(url);
        log.info("微信code2Session响应: {}", response);

        JSONObject json = JSONUtil.parseObj(response);

        // 检查错误码
        if (json.containsKey("errcode") && json.getInt("errcode") != 0) {
            log.error("微信登录失败, errcode={}, errmsg={}", json.getInt("errcode"), json.getStr("errmsg"));
            throw new RuntimeException("微信登录失败: " + json.getStr("errmsg"));
        }

        return json;
    }

    /**
     * 解密微信手机号（需要session_key）
     * 注意：实际生产中应使用微信提供的解密库，此处为简化示例
     */
    public String decryptPhone(String sessionKey, String encryptedData, String iv) {
        // TODO: 使用微信官方解密算法解密手机号
        // 实际项目中使用 wechat-applet-sdk 或手动AES解密
        log.warn("手机号解密功能待接入微信官方解密SDK");
        return null;
    }
}