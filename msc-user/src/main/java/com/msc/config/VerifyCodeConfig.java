package com.msc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "verify-code")
public class VerifyCodeConfig {

    /**
     * 短信验证码配置
     */
    private Sms sms = new Sms();

    /**
     * 邮箱验证码配置
     */
    private Email email = new Email();

    @Data
    public static class Sms {
        /**
         * 过期时间（秒）
         */
        private Integer expireTime = 300; // 5分钟

        /**
         * 每日发送限制
         */
        private Integer sendLimit = 5;
    }

    @Data
    public static class Email {
        /**
         * 过期时间（秒）
         */
        private Integer expireTime = 600; // 10分钟

        /**
         * 每日发送限制
         */
        private Integer sendLimit = 10;
    }
}