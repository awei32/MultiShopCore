package com.msc.domain.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户第三方授权实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOauth {
    
    /**
     * 授权ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 第三方平台:wechat,qq,weibo,alipay等
     */
    private String platform;
    
    /**
     * 第三方用户ID
     */
    private String openId;
    
    /**
     * 第三方统一用户ID
     */
    private String unionId;
    
    /**
     * 第三方用户昵称
     */
    private String nickname;
    
    /**
     * 第三方用户头像
     */
    private String avatar;
    
    /**
     * 第三方用户性别:0-未知,1-男,2-女
     */
    private Integer gender;
    
    /**
     * 第三方用户城市
     */
    private String city;
    
    /**
     * 第三方用户省份
     */
    private String province;
    
    /**
     * 第三方用户国家
     */
    private String country;
    
    /**
     * 访问令牌
     */
    private String accessToken;
    
    /**
     * 刷新令牌
     */
    private String refreshToken;
    
    /**
     * 令牌过期时间
     */
    private LocalDateTime tokenExpireTime;
    
    /**
     * 是否绑定:0-否,1-是
     */
    private Integer bound;
    
    /**
     * 绑定时间
     */
    private LocalDateTime bindTime;
    
    /**
     * 解绑时间
     */
    private LocalDateTime unbindTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    // 自定义构造方法
    public UserOauth(Long userId, String platform, String openId) {
        this.userId = userId;
        this.platform = platform;
        this.openId = openId;
        this.bound = 1;
        this.bindTime = LocalDateTime.now();
    }
    
    /**
     * 第三方平台枚举
     */
    public enum Platform {
        WECHAT("wechat", "微信"),
        QQ("qq", "QQ"),
        WEIBO("weibo", "微博"),
        ALIPAY("alipay", "支付宝"),
        APPLE("apple", "苹果");
        
        private final String code;
        private final String desc;
        
        Platform(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        
        public String getCode() {
            return code;
        }
        
        public String getDesc() {
            return desc;
        }
        
        public static Platform getByCode(String code) {
            for (Platform platform : values()) {
                if (platform.getCode().equals(code)) {
                    return platform;
                }
            }
            return null;
        }
    }
    
    /**
     * 绑定账号
     */
    public void bind() {
        this.bound = 1;
        this.bindTime = LocalDateTime.now();
        this.unbindTime = null;
    }
    
    /**
     * 解绑账号
     */
    public void unbind() {
        this.bound = 0;
        this.unbindTime = LocalDateTime.now();
    }
    
    /**
     * 更新令牌
     */
    public void updateToken(String accessToken, String refreshToken, LocalDateTime expireTime) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenExpireTime = expireTime;
    }
    
    /**
     * 检查令牌是否过期
     */
    public boolean isTokenExpired() {
        return tokenExpireTime != null && LocalDateTime.now().isAfter(tokenExpireTime);
    }
}