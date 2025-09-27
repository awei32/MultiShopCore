package com.msc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JWT配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    /**
     * JWT密钥
     */
    private String secret = "MultiShopCoreUserModuleSecretKey2024";

    /**
     * 访问令牌过期时间（毫秒）
     */
    private Long accessTokenExpiration = 7200000L; // 2小时

    /**
     * 刷新令牌过期时间（毫秒）
     */
    private Long refreshTokenExpiration = 604800000L; // 7天

    /**
     * 发行者
     */
    private String issuer = "MultiShopCore";
}