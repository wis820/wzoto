package com.wzoto.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "wzoto.jwt")
public class JwtProperties {

    /** 密钥 */
    private String secret = "wzoto-default-secret-key-change-in-production";

    /** 过期时间（小时） */
    private Long expirationHours = 72L;

    /** Token前缀 */
    private String tokenPrefix = "Bearer ";

    /** Header名称 */
    private String headerName = "Authorization";
}