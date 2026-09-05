package com.wzoto.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信小程序配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "wzoto.wechat")
public class WechatProperties {

    /** 小程序appid */
    private String appId;

    /** 小程序secret */
    private String appSecret;

    /** 微信登录URL */
    private String loginUrl = "https://api.weixin.qq.com/sns/jscode2session";
}